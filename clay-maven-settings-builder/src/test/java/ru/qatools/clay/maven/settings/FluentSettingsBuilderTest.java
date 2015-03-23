package ru.qatools.clay.maven.settings;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ProvideSystemProperty;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assume.assumeThat;

/**
 * @author Artem Eroshenko eroshenkoam@yandex-team.ru
 */
public class FluentSettingsBuilderTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Rule
    public ProvideSystemProperty property = new ProvideSystemProperty();

    @Test
    public void testLoadSettingsWithoutDefaultSettingsFile() throws Exception {
        File mavenHome = temporaryFolder.newFolder();
        property.setProperty(MavenDefaults.USER_HOME, null);
        property.setProperty(MavenDefaults.M2_HOME, mavenHome.getAbsolutePath());

        assertThat(
                FluentSettingsBuilder.loadSettings().build().getLocalRepository(),
                nullValue());
    }

    @Test
    public void testLoadSettingsWithDefaultSettingsFile() throws Exception {
        File mavenHome = temporaryFolder.newFolder();
        File confHome = new File(mavenHome, "conf");
        assumeThat(confHome.mkdirs(), equalTo(true));

        File settingsFile = new File(confHome, "settings.xml");
        FileUtils.copyInputStreamToFile(
                getClass().getClassLoader().getResourceAsStream("repository.settings.xml"),
                settingsFile);


        assumeThat(settingsFile.exists(), equalTo(true));

        property.setProperty(MavenDefaults.USER_HOME, null);
        property.setProperty(MavenDefaults.M2_HOME, mavenHome.getAbsolutePath());

        String localRepository = FluentSettingsBuilder.loadSettings().build().getLocalRepository();
        assertThat(localRepository, notNullValue());
    }
}
