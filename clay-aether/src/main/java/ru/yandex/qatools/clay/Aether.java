package ru.yandex.qatools.clay;

import org.apache.commons.io.FileUtils;
import org.apache.maven.model.DistributionManagement;
import org.apache.maven.settings.Settings;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.collection.CollectResult;
import org.eclipse.aether.collection.DependencyCollectionException;
import org.eclipse.aether.deployment.DeployRequest;
import org.eclipse.aether.deployment.DeploymentException;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.installation.InstallRequest;
import org.eclipse.aether.installation.InstallationException;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.eclipse.aether.resolution.DependencyRequest;
import org.eclipse.aether.resolution.DependencyResolutionException;
import org.eclipse.aether.util.artifact.JavaScopes;
import org.eclipse.aether.util.artifact.SubArtifact;
import org.eclipse.aether.util.graph.visitor.PreorderNodeListGenerator;
import ru.yandex.qatools.clay.maven.settings.FluentModelBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.yandex.qatools.clay.internal.AetherUtils.getRepositoriesAsList;
import static ru.yandex.qatools.clay.internal.AetherUtils.newRepositorySystem;
import static ru.yandex.qatools.clay.internal.AetherUtils.newSession;
import static ru.yandex.qatools.clay.internal.AetherUtils.toRemoteRepository;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 24.07.14
 */
public class Aether {

    public static final String MAVEN_CENTRAL_URL = "http://repo1.maven.org/maven2";
    public static final String POM_EXTENSION = "pom";
    public static final String MODEL_VERSION = "4.0.0";
    public static final String JAR = "jar";

    private RepositorySystemSession session;
    private RepositorySystem system;
    private List<RemoteRepository> repositories;

    private String scope = JavaScopes.RUNTIME;

    Aether(File localRepoDir, Settings settings) {
        this.system = newRepositorySystem();
        this.session = newSession(system, settings, localRepoDir);
        this.repositories = getRepositoriesAsList(session, settings);
    }

    Aether(RepositorySystem system, RepositorySystemSession session, List<RemoteRepository> repositories) {
        this.session = session;
        this.system = system;
        this.repositories = repositories;
    }

    public static Aether aether(File localRepoDir) {
        return aether(localRepoDir, null);
    }

    public static Aether aether(File localRepoDir, Settings settings) {
        return new Aether(localRepoDir, settings);
    }

    public static Aether aether(RepositorySystem system, RepositorySystemSession session, List<RemoteRepository> repositories) {
        return new Aether(system, session, repositories);
    }

    /**
     * Using this method you can specify resolve scope, RUNTIME by default
     *
     * @param scope specified scope to resolving artifacts
     */
    public Aether scope(String scope) {
        this.scope = scope;
        return this;
    }

    /**
     * Other way to run {@link #resolve(org.eclipse.aether.artifact.Artifact)}
     *
     * @param artifactCoordinates The artifact coordinates in the format
     *                            {@code <groupId>:<artifactId>[:<extension>[:<classifier>]]:<version>}, must not be {@code null}.
     */
    public AetherResult resolve(String artifactCoordinates) throws AetherException {
        return resolve(new DefaultArtifact(artifactCoordinates));
    }

    /**
     * Shortcut for {@link #resolve(org.eclipse.aether.artifact.Artifact, boolean)}
     */
    public AetherResult resolve(Artifact artifact) throws AetherException {
        return resolve(artifact, true);
    }

    /**
     * Other way to run {@link #resolve(org.eclipse.aether.artifact.Artifact, boolean)}
     *
     * @param artifactCoordinates The artifact coordinates in the format
     *                            {@code <groupId>:<artifactId>[:<extension>[:<classifier>]]:<version>}, must not be {@code null}.
     */
    public AetherResult resolve(String artifactCoordinates, boolean withTransitives) throws AetherException {
        return resolve(new DefaultArtifact(artifactCoordinates), withTransitives);
    }

    /**
     * Resolve given artifact with or without transitives
     * {@link #resolveWithTransitives(org.eclipse.aether.artifact.Artifact)}
     * {@link #resolveWithoutTransitives(org.eclipse.aether.artifact.Artifact...)}
     *
     * @param artifact        the given artifacts to resolve
     * @param withTransitives true if you need to resolve artifact with transitives, false otherwise
     */
    public AetherResult resolve(Artifact artifact, boolean withTransitives) throws AetherException {
        return withTransitives ? resolveWithTransitives(artifact) : resolveWithoutTransitives(artifact);
    }

    /**
     * Other way to run {@link #resolveAll(org.eclipse.aether.artifact.Artifact...)}
     *
     * @param artifactsCoordinates The array of artifact coordinates in the format
     *                             {@code <groupId>:<artifactId>[:<extension>[:<classifier>]]:<version>}, must not be {@code null}.
     */
    public AetherResult resolveAll(String... artifactsCoordinates) throws AetherException {
        List<Artifact> artifacts = new ArrayList<>();
        for (String coordinate : artifactsCoordinates) {
            artifacts.add(new DefaultArtifact(coordinate));
        }
        return resolveAll(artifacts.toArray(new Artifact[artifacts.size()]));
    }

    /**
     * Resolve all given artifacts {@link #resolveWithoutTransitives(org.eclipse.aether.artifact.Artifact...)}
     * without transitives
     *
     * @param artifacts the given artifacts to resolve
     * @return The resolution result, never {@code null}.
     * @throws AetherException if can't resolve given artifacts
     */
    public AetherResult resolveAll(Artifact... artifacts) throws AetherException {
        return resolveWithoutTransitives(artifacts);
    }

    /**
     * Collects and resolves the transitive dependencies of an artifact. This operation is essentially a combination of
     * {@link org.eclipse.aether.RepositorySystem#collectDependencies(RepositorySystemSession, CollectRequest)} and
     * {@link org.eclipse.aether.RepositorySystem#resolveArtifacts(RepositorySystemSession, java.util.Collection)}.
     *
     * @param artifact The artifact to resolve, may be {@code null}.
     * @return The resolution result {@link ru.yandex.qatools.clay.AetherResult}, never {@code null}.
     * @throws AetherException if can't resolve given artifact
     */
    protected AetherResult resolveWithTransitives(Artifact artifact) throws AetherException {
        try {
            CollectRequest collectRequest = new CollectRequest(
                    new Dependency(artifact, scope),
                    repositories()
            );

            DependencyRequest request = new DependencyRequest(collectRequest, null);
            return new AetherResult(system.resolveDependencies(session, request).getArtifactResults());
        } catch (DependencyResolutionException e) {
            throw new AetherException("Can't resolve given artifact " + artifact, e);
        }
    }

    /**
     * Resolves the paths for a collection of artifacts. Artifacts will be downloaded to the local repository if
     * necessary. Artifacts that are already resolved will be skipped and are not re-resolved. In general, callers must
     * not assume any relationship between an artifact's filename and its coordinates. Note that this method assumes
     * that any relocations have already been processed.
     *
     * @param artifacts Collection of artifacts to resolve, never {@code null}.
     * @return The resolution result {@link ru.yandex.qatools.clay.AetherResult}, never {@code null}.
     * @throws AetherException if can't resolve given artifact
     */
    protected AetherResult resolveWithoutTransitives(Artifact... artifacts) throws AetherException {
        try {
            return new AetherResult(system.resolveArtifacts(session, buildArtifactRequests(artifacts)));
        } catch (ArtifactResolutionException e) {
            throw new AetherException("Can't resolve one or more given artifacts " + Arrays.toString(artifacts), e);
        }
    }

    /**
     * Build {@link org.eclipse.aether.resolution.ArtifactRequest} for each given
     * {@link org.eclipse.aether.artifact.Artifact}
     *
     * @param artifacts the given artifacts
     * @return list of artifact requests
     */
    protected List<ArtifactRequest> buildArtifactRequests(Artifact... artifacts) {
        List<ArtifactRequest> requests = new ArrayList<>();
        for (Artifact artifact : artifacts) {
            requests.add(new ArtifactRequest(artifact, repositories(), null));
        }
        return requests;
    }

    /**
     * Other way to run {@link #collect(org.eclipse.aether.artifact.Artifact)}
     *
     * @param artifactCoordinates The artifact coordinates in the format
     *                            {@code <groupId>:<artifactId>[:<extension>[:<classifier>]]:<version>}, must not be {@code null}.
     */
    public List<Artifact> collect(String artifactCoordinates) throws AetherException {
        return collect(new DefaultArtifact(artifactCoordinates));
    }

    /**
     * Collects the transitive dependencies of an artifact and builds a dependency graph. Note that this operation is
     * only concerned about determining the coordinates of the transitive dependencies. To also resolve the actual
     * artifact files, use {@link #resolve(org.eclipse.aether.artifact.Artifact)}.
     *
     * @param artifact the given artifacts to collect
     * @throws AetherException if can't collect transitive dependencies for given artifact
     */
    public List<Artifact> collect(Artifact artifact) throws AetherException {
        try {
            if (session.isOffline()) {
                throw new AetherException("Can't collect dependencies in offline mode");
            }
            CollectRequest collectRequest = new CollectRequest(
                    new Dependency(artifact, scope),
                    repositories()
            );
            CollectResult result = system.collectDependencies(session, collectRequest);
            PreorderNodeListGenerator visitor = new PreorderNodeListGenerator();
            result.getRoot().accept(visitor);
            return visitor.getArtifacts(true);
        } catch (DependencyCollectionException e) {
            throw new AetherException("Can't collect given artifact " + artifact, e);
        }
    }

    /**
     * Shortcut for {@link #install(java.io.File, String, String, String, String)}
     */
    public void install(File jar, String groupId, String artifactId, String version) throws AetherException {
        install(jar, groupId, artifactId, JAR, version);
    }

    /**
     * Shortcut for {@link #install(java.io.File, String, String, String, String, String)}
     */
    public void install(File jar, String groupId, String artifactId, String extension, String version)
            throws AetherException {
        install(jar, groupId, artifactId, "", extension, version);
    }

    /**
     * Shortcut for {@link #install(java.io.File, java.io.File, String, String, String, String, String)}
     */
    public void install(File jar, String groupId, String artifactId, String classifier, String extension, String version)
            throws AetherException {
        File pom = null;
        try {
            pom = File.createTempFile("install", "pom.xml");
            FluentModelBuilder.newPom()
                    .withGroupId(groupId)
                    .withArtifactId(artifactId)
                    .withVersion(version)
                    .withModelVersion(MODEL_VERSION)
                    .marshalTo(pom);
            install(jar, pom, groupId, artifactId, classifier, extension, version);
        } catch (IOException e) {
            throw new AetherException("Can't create temp file for pom.xml", e);
        } finally {
            FileUtils.deleteQuietly(pom);
        }
    }

    /**
     * Install given jars to local repository using specified artifact coordinates.
     *
     * @param jar archive with given artifact to install
     * @param pom archive with pom.xml
     * @throws AetherException If any artifact/metadata from the request could not be installed.
     */
    public void install(File jar, File pom, String groupId, String artifactId, String classifier, String extension, String version)
            throws AetherException {
        Artifact jarArtifact = new DefaultArtifact(groupId, artifactId, classifier, extension, version, null, jar);
        Artifact pomArtifact = new SubArtifact(jarArtifact, null, POM_EXTENSION, pom);
        install(jarArtifact, pomArtifact);
    }

    /**
     * Installs a collection of artifacts and their accompanying metadata to the local repository.
     *
     * @param artifacts the given artifacts to install
     * @throws AetherException If any artifact/metadata from the request could not be installed.
     */
    public void install(Artifact... artifacts) throws AetherException {
        try {
            InstallRequest installRequest = new InstallRequest();
            for (Artifact artifact : artifacts) {
                installRequest.addArtifact(artifact);
            }
            system.install(session, installRequest);
        } catch (InstallationException e) {
            throw new AetherException("Can't install one or more given artifacts " + Arrays.toString(artifacts), e);
        }
    }

    /**
     * Shortcut for {@link #deploy(DistributionManagement distribution, java.io.File, String, String, String, String)}
     */
    public void deploy(DistributionManagement distribution, File jar, String groupId, String artifactId, String version)
            throws AetherException {
        deploy(distribution, jar, groupId, artifactId, JAR, version);
    }

    /**
     * Shortcut for {@link #deploy(DistributionManagement distribution, java.io.File, String, String, String, String, String)}
     */
    public void deploy(DistributionManagement distribution, File jar, String groupId, String artifactId,
                       String extension, String version)
            throws AetherException {
        deploy(distribution, jar, groupId, artifactId, "", extension, version);
    }

    /**
     * Other way to run {@link #deploy(org.apache.maven.model.DistributionManagement, java.io.File, java.io.File, String, String, String, String, String)}
     */
    public void deploy(DistributionManagement distribution, File jar, String groupId, String artifactId,
                       String classifier, String extension, String version) throws AetherException {
        File pom = null;
        try {
            pom = File.createTempFile("install", "pom.xml");
            FluentModelBuilder.newPom()
                    .withGroupId(groupId)
                    .withArtifactId(artifactId)
                    .withVersion(version)
                    .withModelVersion(MODEL_VERSION)
                    .marshalTo(pom);
            deploy(distribution, jar, pom, groupId, artifactId, classifier, extension, version);
        } catch (IOException e) {
            throw new AetherException("Can't create temp file for pom.xml", e);
        } finally {
            FileUtils.deleteQuietly(pom);
        }
    }

    /**
     * Other way to run {@link #deploy(org.apache.maven.model.DistributionManagement, org.eclipse.aether.artifact.Artifact...)}
     */
    public void deploy(DistributionManagement distribution, File jar, File pom, String groupId, String artifactId,
                       String classifier, String extension, String version) throws AetherException {
        Artifact jarArtifact = new DefaultArtifact(groupId, artifactId, classifier, extension, version, null, jar);
        Artifact pomArtifact = new SubArtifact(jarArtifact, null, POM_EXTENSION, pom);
        deploy(distribution, jarArtifact, pomArtifact);
    }

    /**
     * Uploads a collection of artifacts and their accompanying metadata to a remote repository.
     *
     * @param distribution {@link org.apache.maven.model.DistributionManagement} which contains information
     *                     about remotes
     * @param artifacts    the given artifacts to deploy
     * @throws AetherException If any artifact/metadata from the request could not be deployd.
     */
    public void deploy(DistributionManagement distribution, Artifact... artifacts) throws AetherException {
        try {
            if (artifacts.length == 0) {
                return;
            }
            DeployRequest deployRequest = new DeployRequest();
            for (Artifact artifact : artifacts) {
                deployRequest.addArtifact(artifact);
            }
            deployRequest.setRepository(toRemoteRepository(
                    artifacts[0].isSnapshot() ?
                            distribution.getSnapshotRepository() :
                            distribution.getRepository(),
                    session
            ));
            system.deploy(session, deployRequest);
        } catch (DeploymentException e) {
            throw new AetherException("Can't deploy one or more given artifacts" + Arrays.toString(artifacts), e);
        }
    }

    /**
     * Get remote repositories if online mode, null otherwise
     *
     * @return list of remotes or null
     */
    protected List<RemoteRepository> repositories() {
        return session.isOffline() ? null : repositories;
    }
}
