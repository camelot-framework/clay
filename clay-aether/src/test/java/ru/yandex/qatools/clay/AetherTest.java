package ru.yandex.qatools.clay;

import org.apache.maven.model.DistributionManagement;
import org.apache.maven.settings.Settings;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.resolution.ArtifactResult;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

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
import static ru.yandex.qatools.clay.Aether.MAVEN_CENTRAL_URL;
import static ru.yandex.qatools.clay.Aether.aether;
import static ru.yandex.qatools.clay.DirectoryMatcher.contains;
import static ru.yandex.qatools.clay.maven.settings.FluentDeploymentRepositoryBuilder.newDeploymentRepository;
import static ru.yandex.qatools.clay.maven.settings.FluentDistributionManagementBuilder.newDistributionManagement;
import static ru.yandex.qatools.clay.maven.settings.FluentProfileBuilder.newProfile;
import static ru.yandex.qatools.clay.maven.settings.FluentRepositoryBuilder.newRepository;
import static ru.yandex.qatools.clay.maven.settings.FluentSettingsBuilder.newSettings;
import static ru.yandex.qatools.clay.maven.settings.FluentSettingsBuilder.newSystemSettings;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 24.07.14
 */
public class AetherTest {

    public static final Settings MAVEN_SETTINGS = newSettings()
            .withActiveProfile(newProfile()
                    .withId("profile")
                    .withRepository(newRepository()
                            .withUrl(MAVEN_CENTRAL_URL))).build();

    public static final String ALLURE_MODEL = "ru.yandex.qatools.allure:allure-model:jar:1.3.9";

    public static final String ALLURE_ANNOTATIONS = "ru.yandex.qatools.allure:allure-java-annotations:jar:1.3.9";

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    public File localRepo;

    @Before
    public void setUp() throws Exception {
        localRepo = folder.newFolder();
    }

    @Test
    public void createAetherTest() throws Exception {
        aether(MAVEN_SETTINGS);
    }

    @Test
    public void resolveWithoutTransitivesTest() throws Exception {
        List<ArtifactResult> results = aether(localRepo, MAVEN_SETTINGS)
                .resolve(ALLURE_MODEL, false).get();

        assertThat(results.size(), is(1));
    }

    @Test
    public void resolveWithoutTransitivesAsClassLoaderTest() throws Exception {
        ClassLoader classLoader = aether(localRepo, MAVEN_SETTINGS)
                .resolve(ALLURE_MODEL, false).getAsClassLoader();

        assertNotNull(classLoader.getResourceAsStream("allure.xsd"));
    }

    @Test
    public void resolveWithoutTransitivesAsUrlsTest() throws Exception {
        URL[] urls = aether(localRepo, MAVEN_SETTINGS)
                .resolve(ALLURE_MODEL, false).getAsUrls();

        assertThat(urls.length, is(1));
    }

    @Test
    public void resolveAllTest() throws Exception {
        List<ArtifactResult> results = aether(localRepo, MAVEN_SETTINGS)
                .resolveAll(ALLURE_MODEL, ALLURE_ANNOTATIONS).get();

        assertThat(results.size(), is(2));
    }

    @Test
    public void resolveAllGetAsClassLoaderTest() throws Exception {
        ClassLoader classLoader = aether(localRepo, MAVEN_SETTINGS)
                .resolveAll(ALLURE_MODEL, ALLURE_ANNOTATIONS)
                .getAsClassLoader(Thread.currentThread().getContextClassLoader());

        assertNotNull(classLoader.getResourceAsStream("allure.xsd"));
        assertEquals(classLoader.getParent(), Thread.currentThread().getContextClassLoader());
    }

    @Test
    public void resolveWithTransitivesTest() throws Exception {
        List<ArtifactResult> results = aether(localRepo, MAVEN_SETTINGS)
                .resolve(ALLURE_MODEL).get();

        assertThat(results.size(), is(5));
    }

    @Test
    public void resolveWithTransitivesGetAsClassPathTest() throws Exception {
        String[] strings = aether(localRepo, MAVEN_SETTINGS)
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
        List<Artifact> artifacts = aether(localRepo, MAVEN_SETTINGS).collect(ALLURE_MODEL);
        assertThat(artifacts.size(), is(5));
    }

    @Test
    public void installTest() throws Exception {
        File jar = createJar();
        aether(localRepo, MAVEN_SETTINGS).install(jar, "testGroupId", "testArtifactId", "testVersion");

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

        aether(localRepo, MAVEN_SETTINGS).deploy(distributionManagement, jar, "testGroupId", "testArtifactId", "testVersion");
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
