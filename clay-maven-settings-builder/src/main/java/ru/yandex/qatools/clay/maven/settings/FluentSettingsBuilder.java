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

    private final Settings settings;

    private FluentSettingsBuilder(Settings settings) {
        this.settings = settings;
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
     * {@see http://maven.apache.org/settings.html}
     *
     * @throws SettingsBuildingException {@link org.apache.maven.settings.building.DefaultSettingsBuilder#build(org.apache.maven.settings.building.SettingsBuildingRequest)}
     * @throws MavenNotFoundException if can't find Maven distribution
     */
    public static FluentSettingsBuilder newSystemSettings() throws SettingsBuildingException, MavenNotFoundException {
        String userHome = System.getProperty("user.home");
        String m2home = System.getProperty("M2_HOME");
        String path = null;
        if (userHome != null) {
            return newSettings(String.format("%s/.m2/settings.xml", userHome));
        } else if (m2home != null) {
            return newSettings(String.format("%s/conf/settings.xml", m2home));
        } else {
            throw new MavenNotFoundException("There are no Maven distribution");
        }
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
