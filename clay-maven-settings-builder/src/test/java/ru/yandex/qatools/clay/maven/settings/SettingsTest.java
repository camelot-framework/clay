package ru.yandex.qatools.clay.maven.settings;

import org.apache.commons.io.FileUtils;
import org.apache.maven.settings.Settings;
import org.apache.maven.settings.building.DefaultSettingsBuilderFactory;
import org.apache.maven.settings.building.DefaultSettingsBuildingRequest;
import org.apache.maven.settings.building.SettingsBuildingException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * eroshenkoam
 * 24/10/14
 */
public class SettingsTest {

    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    @Test
    public void emptySettingHasNullLocalRepository() throws Exception {
        File settingsFile = temp.newFile();
        FileUtils.copyInputStreamToFile(ClassLoader.getSystemResourceAsStream("empty.settings.xml"), settingsFile);
        Settings settings = getSettings(settingsFile);
        assertThat(settings.getLocalRepository(), nullValue());
    }

    @Test
    public void repositorySettingHasLocalRepository() throws Exception {
        File settingsFile = temp.newFile();
        FileUtils.copyInputStreamToFile(ClassLoader.getSystemResourceAsStream("repository.settings.xml"), settingsFile);
        Settings settings = getSettings(settingsFile);
        assertThat(settings.getLocalRepository(), notNullValue());
    }

    private Settings getSettings(File settingsFile) throws SettingsBuildingException {
        DefaultSettingsBuildingRequest request = new DefaultSettingsBuildingRequest();
        request.setGlobalSettingsFile(settingsFile);
        return new DefaultSettingsBuilderFactory()
                .newInstance()
                .build(request)
                .getEffectiveSettings();
    }
}
