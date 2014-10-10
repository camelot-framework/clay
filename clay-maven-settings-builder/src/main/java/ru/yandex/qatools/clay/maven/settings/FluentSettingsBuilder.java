package ru.yandex.qatools.clay.maven.settings;

import org.apache.maven.settings.Mirror;
import org.apache.maven.settings.Profile;
import org.apache.maven.settings.Proxy;
import org.apache.maven.settings.Server;
import org.apache.maven.settings.Settings;
import org.apache.maven.settings.building.DefaultSettingsBuilderFactory;
import org.apache.maven.settings.building.DefaultSettingsBuildingRequest;
import org.apache.maven.settings.building.SettingsBuildingException;

import java.io.File;

/**
 * @author innokenty
 */
@SuppressWarnings("JavaDoc")
public class FluentSettingsBuilder {

    public static final String M2_HOME = "M2_HOME";
    public static final String USER_HOME = "user.home";
    public static final String MAVEN_HOME = "maven.home";
    public static final String TEMP_DIR = "java.io.tmpdir";

    private final Settings settings;

    private FluentSettingsBuilder(Settings settings) {
        this.settings = settings;
        if (settings.getLocalRepository() == null) {
            settings.setLocalRepository(String.format(
                    "%s/.m2/repository",
                    System.getProperty(USER_HOME, System.getProperty(TEMP_DIR, ""))));
        }
    }

    public static FluentSettingsBuilder newSettings() {
        return new FluentSettingsBuilder(new Settings());
    }

    public static FluentSettingsBuilder newSettings(String settingsFilePath)
            throws SettingsBuildingException {
        DefaultSettingsBuildingRequest request = new DefaultSettingsBuildingRequest();
        request.setGlobalSettingsFile(new File(settingsFilePath));
        Settings settings = new DefaultSettingsBuilderFactory()
                .newInstance()
                .build(request)
                .getEffectiveSettings();
        return new FluentSettingsBuilder(settings);
    }

    /**
     * Looks for settings.xml at '${user.home}/.m2/settings.xml' and '$M2_HOME/conf/settings.xml'.
     * {@see http://maven.apache.org/settings.html}. If can't find settings, returns {@link #newSettings()}
     *
     */
    public static FluentSettingsBuilder newSystemSettings() {

        String userHome = System.getProperty(USER_HOME);
        if (userHome != null) {
            String settingsPath = String.format("%s/.m2/settings.xml", userHome);
            try {
                return newSettings(settingsPath);
            } catch (SettingsBuildingException ignored) {
            }
        }

        String m2home = System.getProperty(MAVEN_HOME, System.getProperty(M2_HOME));
        if (m2home != null) {
            String settingsPath = String.format("%s/conf/settings.xml", m2home);
            try {
                return newSettings(settingsPath);
            } catch (SettingsBuildingException ignored) {
            }
        }

        return newSettings();
    }

    public Settings build() {
        return settings;
    }

    /* DELEGATED METHODS */

    /**
     * Set the local repository.<br /><b>Default value is:</b>
     * <tt>${user.home}/.m2/repository</tt>
     *
     * @param localRepository
     */
    public FluentSettingsBuilder withLocalRepository(String localRepository) {
        settings.setLocalRepository(localRepository);
        return this;
    }

    public FluentSettingsBuilder withMirror(FluentMirrorBuilder mirror) {
        return withMirror(mirror.build());
    }

    public FluentSettingsBuilder withMirror(Mirror mirror) {
        settings.addMirror(mirror);
        return this;
    }

    public FluentSettingsBuilder withPluginGroup(String string) {
        settings.addPluginGroup(string);
        return this;
    }

    public FluentSettingsBuilder withProfile(FluentProfileBuilder profile) {
        return withProfile(profile.build());
    }

    public FluentSettingsBuilder withProfile(Profile profile) {
        settings.addProfile(profile);
        return this;
    }

    public FluentSettingsBuilder withActiveProfile(FluentProfileBuilder profile) {
        return withActiveProfile(profile.build());
    }

    protected FluentSettingsBuilder withActiveProfile(Profile profile) {
        settings.addProfile(profile);
        settings.addActiveProfile(profile.getId());
        return this;
    }

    public FluentSettingsBuilder withActiveProfile(String string) {
        settings.addActiveProfile(string);
        return this;
    }

    public FluentSettingsBuilder withProxy(FluentProxyBuilder proxy) {
        return withProxy(proxy.build());
    }

    public FluentSettingsBuilder withProxy(Proxy proxy) {
        settings.addProxy(proxy);
        return this;
    }

    public FluentSettingsBuilder withServer(FluentServerBuilder server) {
        return withServer(server.build());
    }

    public FluentSettingsBuilder withServer(Server server) {
        settings.addServer(server);
        return this;
    }

    public FluentSettingsBuilder withOffline(boolean offline) {
        settings.setOffline(offline);
        return this;
    }
}
