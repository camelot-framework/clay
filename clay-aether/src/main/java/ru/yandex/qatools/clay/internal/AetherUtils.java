package ru.yandex.qatools.clay.internal;

import org.apache.maven.repository.internal.DefaultArtifactDescriptorReader;
import org.apache.maven.repository.internal.DefaultVersionRangeResolver;
import org.apache.maven.repository.internal.DefaultVersionResolver;
import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.apache.maven.settings.Mirror;
import org.apache.maven.settings.Profile;
import org.apache.maven.settings.Proxy;
import org.apache.maven.settings.Repository;
import org.apache.maven.settings.Server;
import org.apache.maven.settings.Settings;
import org.eclipse.aether.DefaultRepositoryCache;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory;
import org.eclipse.aether.impl.ArtifactDescriptorReader;
import org.eclipse.aether.impl.ArtifactResolver;
import org.eclipse.aether.impl.DefaultServiceLocator;
import org.eclipse.aether.impl.DependencyCollector;
import org.eclipse.aether.impl.Deployer;
import org.eclipse.aether.impl.Installer;
import org.eclipse.aether.impl.LocalRepositoryProvider;
import org.eclipse.aether.impl.MetadataResolver;
import org.eclipse.aether.impl.SyncContextFactory;
import org.eclipse.aether.impl.VersionRangeResolver;
import org.eclipse.aether.impl.VersionResolver;
import org.eclipse.aether.internal.impl.DefaultArtifactResolver;
import org.eclipse.aether.internal.impl.DefaultDependencyCollector;
import org.eclipse.aether.internal.impl.DefaultDeployer;
import org.eclipse.aether.internal.impl.DefaultInstaller;
import org.eclipse.aether.internal.impl.DefaultLocalRepositoryProvider;
import org.eclipse.aether.internal.impl.DefaultMetadataResolver;
import org.eclipse.aether.internal.impl.DefaultRepositorySystem;
import org.eclipse.aether.internal.impl.DefaultSyncContextFactory;
import org.eclipse.aether.repository.Authentication;
import org.eclipse.aether.repository.AuthenticationSelector;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.MirrorSelector;
import org.eclipse.aether.repository.ProxySelector;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.repository.RepositoryPolicy;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.spi.log.LoggerFactory;
import org.eclipse.aether.spi.log.NullLoggerFactory;
import org.eclipse.aether.transport.file.FileTransporterFactory;
import org.eclipse.aether.transport.http.HttpTransporterFactory;
import org.eclipse.aether.util.repository.AuthenticationBuilder;
import org.eclipse.aether.util.repository.ConservativeAuthenticationSelector;
import org.eclipse.aether.util.repository.DefaultAuthenticationSelector;
import org.eclipse.aether.util.repository.DefaultMirrorSelector;
import org.eclipse.aether.util.repository.DefaultProxySelector;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 24.07.14
 *         <p/>
 *         Warning, it's not a part of public API.
 */
public final class AetherUtils {

    AetherUtils() {
    }

    /**
     * Create a new {@link org.eclipse.aether.RepositorySystem}
     */
    public static RepositorySystem newRepositorySystem() {
        DefaultRepositorySystem repositorySystem = new DefaultRepositorySystem();
        DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();

        locator.addService(RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class);
        locator.addService(TransporterFactory.class, FileTransporterFactory.class);
        locator.addService(TransporterFactory.class, HttpTransporterFactory.class);
        locator.setService(LoggerFactory.class, NullLoggerFactory.class);
        locator.setService(VersionResolver.class, DefaultVersionResolver.class);
        locator.setService(VersionRangeResolver.class, DefaultVersionRangeResolver.class);
        locator.setService(ArtifactResolver.class, DefaultArtifactResolver.class);
        locator.setService(MetadataResolver.class, DefaultMetadataResolver.class);
        locator.setService(ArtifactDescriptorReader.class, DefaultArtifactDescriptorReader.class);
        locator.setService(DependencyCollector.class, DefaultDependencyCollector.class);
        locator.setService(Installer.class, DefaultInstaller.class);
        locator.setService(Deployer.class, DefaultDeployer.class);
        locator.setService(LocalRepositoryProvider.class, DefaultLocalRepositoryProvider.class);
        locator.setService(SyncContextFactory.class, DefaultSyncContextFactory.class);

        repositorySystem.initService(locator);
        return repositorySystem;
    }

    /**
     * Create a new {@link org.eclipse.aether.RepositorySystemSession} from given
     * {@link org.eclipse.aether.RepositorySystem}, {@link org.apache.maven.settings.Settings} and
     * local repository file
     */
    public static RepositorySystemSession newSession(RepositorySystem system, Settings settings, File localRepoDir) {
        DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();
        LocalRepository localRepo = new LocalRepository(localRepoDir.getAbsolutePath());
        session.setLocalRepositoryManager(system.newLocalRepositoryManager(session, localRepo));
        session.setUpdatePolicy(RepositoryPolicy.UPDATE_POLICY_ALWAYS);
        if (settings != null) {
            session.setOffline(settings.isOffline());
            session.setMirrorSelector(newMirrorSelector(settings.getMirrors()));
            session.setAuthenticationSelector(newAuthSelector(settings.getServers()));
            session.setCache(new DefaultRepositoryCache());
            session.setProxySelector(newProxySelector(settings.getProxies()));
        }
        return session;
    }

    /**
     * Create a new {@link org.eclipse.aether.repository.ProxySelector} from given list of
     * {@link org.apache.maven.settings.Proxy}
     */
    public static ProxySelector newProxySelector(List<Proxy> proxies) {
        DefaultProxySelector proxySelector = new DefaultProxySelector();
        for (Proxy proxy : proxies) {
            Authentication auth = new AuthenticationBuilder()
                    .addUsername(proxy.getUsername())
                    .addPassword(proxy.getPassword())
                    .build();
            proxySelector.add(new org.eclipse.aether.repository.Proxy(
                            proxy.getProtocol(),
                            proxy.getHost(),
                            proxy.getPort(),
                            auth),
                    proxy.getNonProxyHosts()
            );
        }
        return proxySelector;
    }

    /**
     * Create a new {@link org.eclipse.aether.repository.MirrorSelector} from given
     * {@link org.apache.maven.settings.Mirror}
     */
    public static MirrorSelector newMirrorSelector(List<Mirror> mirrors) {
        DefaultMirrorSelector selector = new DefaultMirrorSelector();
        for (Mirror mirror : mirrors) {
            selector.add(mirror.getId(), mirror.getUrl(), mirror.getLayout(),
                    false, mirror.getMirrorOf(), mirror.getMirrorOfLayouts()
            );
        }
        return selector;
    }

    /**
     * Create a new {@link org.eclipse.aether.repository.AuthenticationSelector} from given
     * list of {@link org.apache.maven.settings.Server}
     */
    public static AuthenticationSelector newAuthSelector(List<Server> servers) {
        DefaultAuthenticationSelector selector = new DefaultAuthenticationSelector();
        for (Server server : servers) {
            AuthenticationBuilder auth = new AuthenticationBuilder();
            auth.addUsername(server.getUsername()).addPassword(server.getPassword());
            auth.addPrivateKey(server.getPrivateKey(), server.getPassphrase());
            selector.add(server.getId(), auth.build());
        }
        return new ConservativeAuthenticationSelector(selector);
    }

    /**
     * Create a new list of {@link org.eclipse.aether.repository.RemoteRepository} from given
     * {@link org.eclipse.aether.RepositorySystemSession} and {@link org.apache.maven.settings.Settings}
     */
    public static List<RemoteRepository> getRepositoriesAsList(RepositorySystemSession session, Settings settings) {
        if (settings == null) {
            return Collections.emptyList();
        }

        List<RemoteRepository> repositories = new ArrayList<>();
        for (String profileName : settings.getActiveProfiles()) {
            Profile profile = settings.getProfilesAsMap().get(profileName);

            for (Repository repository : profile.getRepositories()) {
                repositories.add(toRemoteRepository(repository, session));
            }
        }
        return repositories;
    }


    /**
     * Create a new {@link org.eclipse.aether.repository.RemoteRepository} from given
     * {@link org.apache.maven.settings.Repository} and {@link org.eclipse.aether.RepositorySystemSession}
     */
    public static RemoteRepository toRemoteRepository(Repository repo, RepositorySystemSession session) {
        RemoteRepository.Builder prototypeBuilder =
                new RemoteRepository.Builder(repo.getId(), repo.getLayout(), repo.getUrl());
        prototypeBuilder.setSnapshotPolicy(transformPolicy(repo.getSnapshots()));
        prototypeBuilder.setReleasePolicy(transformPolicy(repo.getReleases()));
        RemoteRepository prototype = prototypeBuilder.build();
        RemoteRepository.Builder builder = new RemoteRepository.Builder(prototype);
        builder.setAuthentication(session.getAuthenticationSelector().getAuthentication(prototype));
        builder.setProxy(session.getProxySelector().getProxy(prototype));
        builder.setMirroredRepositories(Arrays.asList(session.getMirrorSelector().getMirror(prototype)));
        return prototypeBuilder.build();
    }

    /**
     * Create a new {@link org.eclipse.aether.repository.RemoteRepository} from given
     * {@link org.apache.maven.model.Repository} and {@link org.eclipse.aether.RepositorySystemSession}
     */
    public static RemoteRepository toRemoteRepository(org.apache.maven.model.Repository repo, RepositorySystemSession session) {
        RemoteRepository.Builder prototypeBuilder =
                new RemoteRepository.Builder(repo.getId(), repo.getLayout(), repo.getUrl());
        prototypeBuilder.setSnapshotPolicy(transformPolicy(repo.getSnapshots()));
        prototypeBuilder.setReleasePolicy(transformPolicy(repo.getReleases()));
        RemoteRepository prototype = prototypeBuilder.build();
        RemoteRepository.Builder builder = new RemoteRepository.Builder(prototype);
        builder.setAuthentication(session.getAuthenticationSelector().getAuthentication(prototype));
        builder.setProxy(session.getProxySelector().getProxy(prototype));
        builder.setMirroredRepositories(Arrays.asList(session.getMirrorSelector().getMirror(prototype)));
        return prototypeBuilder.build();
    }

    /**
     * Convert {@link org.apache.maven.model.RepositoryPolicy} to
     * {@link org.eclipse.aether.repository.RepositoryPolicy}
     *
     * @param policy the policy to convert, may be {@code null}
     */
    public static RepositoryPolicy transformPolicy(org.apache.maven.model.RepositoryPolicy policy) {
        return policy == null ? null : newRepositoryPolicy(
                policy.isEnabled(),
                policy.getUpdatePolicy(),
                policy.getChecksumPolicy()
        );
    }

    /**
     * Convert {@link org.apache.maven.settings.RepositoryPolicy} to
     * {@link org.eclipse.aether.repository.RepositoryPolicy}
     *
     * @param policy the policy to convert, may be {@code null}
     */
    public static RepositoryPolicy transformPolicy(org.apache.maven.settings.RepositoryPolicy policy) {
        return policy == null ? null : newRepositoryPolicy(
                policy.isEnabled(),
                policy.getUpdatePolicy(),
                policy.getChecksumPolicy()
        );
    }

    /**
     * Creates a new policy with the specified settings.
     *
     * @param enabled        A flag whether the associated repository should be accessed or not.
     * @param updatePolicy   The update interval after which locally cached data from the repository is considered stale
     *                       and should be refetched, may be {@code null}.
     * @param checksumPolicy The way checksum verification should be handled, may be {@code null}.
     */
    public static RepositoryPolicy newRepositoryPolicy(boolean enabled, String updatePolicy, String checksumPolicy) {
        return new RepositoryPolicy(enabled, updatePolicy, checksumPolicy);
    }
}
