package ru.yandex.qatools.clay;

import org.apache.maven.model.DistributionManagement;
import org.apache.maven.settings.Settings;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.resolution.ArtifactResult;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static ru.yandex.qatools.clay.Aether.aether;
import static ru.yandex.qatools.clay.DirectoryMatcher.contains;
import static ru.yandex.qatools.clay.maven.settings.FluentDeploymentRepositoryBuilder.newDeploymentRepository;
import static ru.yandex.qatools.clay.maven.settings.FluentDistributionManagementBuilder.newDistributionManagement;
import static ru.yandex.qatools.clay.maven.settings.FluentSettingsBuilder.newSettings;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 24.07.14
 */
public class AetherTest extends AbstractAetherTest {

    @Test
    public void createAetherTest() throws Exception {
        aether(mavenSettings);
    }

    @Test
    public void resolveWithoutTransitivesTest() throws Exception {
        List<ArtifactResult> results = aether(localRepo, mavenSettings)
                .resolve(ALLURE_MODEL, false).get();

        assertThat(results.size(), is(1));
    }

    @Test
    public void resolveWithoutTransitivesAsClassLoaderTest() throws Exception {
        ClassLoader classLoader = aether(localRepo, mavenSettings)
                .resolve(ALLURE_MODEL, false).getAsClassLoader();

        assertNotNull(classLoader.getResourceAsStream("allure.xsd"));
    }

    @Test
    public void resolveWithoutTransitivesAsUrlsTest() throws Exception {
        URL[] urls = aether(localRepo, mavenSettings)
                .resolve(ALLURE_MODEL, false).getAsUrls();

        assertThat(urls.length, is(1));
    }

    @Test
    public void resolveAllTest() throws Exception {
        List<ArtifactResult> results = aether(localRepo, mavenSettings)
                .resolveAll(ALLURE_MODEL, ALLURE_ANNOTATIONS).get();

        assertThat(results.size(), is(2));
    }

    @Test
    public void resolveAllGetAsClassLoaderTest() throws Exception {
        ClassLoader classLoader = aether(localRepo, mavenSettings)
                .resolveAll(ALLURE_MODEL, ALLURE_ANNOTATIONS)
                .getAsClassLoader(Thread.currentThread().getContextClassLoader());

        assertNotNull(classLoader.getResourceAsStream("allure.xsd"));
        assertEquals(classLoader.getParent(), Thread.currentThread().getContextClassLoader());
    }

    @Test
    public void resolveWithTransitivesTest() throws Exception {
        List<ArtifactResult> results = aether(localRepo, mavenSettings)
                .resolve(ALLURE_MODEL).get();

        assertThat(results.size(), is(5));
    }

    @Test
    public void resolveWithTransitivesGetAsClassPathTest() throws Exception {
        String[] strings = aether(localRepo, mavenSettings)
                .resolve(ALLURE_MODEL).getAsClassPath();

        assertThat(strings.length, is(5));
    }

    @Test(expected = AetherException.class)
    public void collectInOfflineTest() throws Exception {
        Settings settings = newSettings()
                .withOffline(true).build();
        aether(localRepo, settings).collect(ALLURE_MODEL);
    }

    @Test
    public void collectTest() throws Exception {
        List<Artifact> artifacts = aether(localRepo, mavenSettings).collect(ALLURE_MODEL);
        assertThat(artifacts.size(), is(5));
    }

    @Test
    public void installTest() throws Exception {
        File jar = createJar();
        aether(localRepo, mavenSettings).install(jar, "testGroupId", "testArtifactId", "testVersion");

        File artifactDirectory = directoryContains(localRepo, "testGroupId", "testArtifactId", "testVersion");
        assertThat(artifactDirectory, contains("testArtifactId-testVersion.jar", "testArtifactId-testVersion.pom"));
    }

    @Test
    public void deployTest() throws Exception {
        File jar = createJar();
        DistributionManagement distributionManagement = newDistributionManagement()
                .withRepository(newDeploymentRepository()
                        .withId("server")
                        .withUrl(localRepo.toURI().toURL().toString())).build();

        aether(localRepo, mavenSettings).deploy(distributionManagement, jar, "testGroupId", "testArtifactId", "testVersion");
        File artifactDirectory = directoryContains(localRepo, "testGroupId", "testArtifactId", "testVersion");
        assertThat(artifactDirectory, contains("testArtifactId-testVersion.jar", "testArtifactId-testVersion.pom"));
    }

    private File createJar() throws IOException {
        File jar = folder.newFile("test.jar");

        try (JarOutputStream jarOut = new JarOutputStream(new FileOutputStream(jar), createManifest())) {
            jarOut.flush();
        }
        return jar;
    }

    private static Manifest createManifest() {
        Manifest manifest = new Manifest();
        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
        return manifest;
    }

    private static File directoryContains(File directory, String... path) {
        File current = directory;
        for (String name : path) {
            current = new File(current, name);
            assertTrue(current.exists());
        }
        return current;
    }
}
