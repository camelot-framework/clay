package ru.yandex.qatools.clay;

import org.apache.maven.settings.Settings;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static ru.yandex.qatools.clay.Aether.MAVEN_CENTRAL_URL;
import static ru.yandex.qatools.clay.maven.settings.FluentProfileBuilder.newProfile;
import static ru.yandex.qatools.clay.maven.settings.FluentRepositoryBuilder.newRepository;
import static ru.yandex.qatools.clay.maven.settings.FluentSettingsBuilder.newSettings;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 14.11.14
 */
public class AbstractAetherTest {

    public static final String ALLURE_MODEL = "ru.yandex.qatools.allure:allure-model:jar:1.3.9";

    public static final String ALLURE_ANNOTATIONS = "ru.yandex.qatools.allure:allure-java-annotations:jar:1.3.9";

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    public Settings mavenSettings;

    public File localRepo;

    @Before
    public void init() throws Exception {
        localRepo = folder.newFolder();
        mavenSettings = newSettings()
                .withActiveProfile(newProfile()
                        .withId("profile")
                        .withRepository(newRepository()
                                .withUrl(MAVEN_CENTRAL_URL))).build();
    }
}
