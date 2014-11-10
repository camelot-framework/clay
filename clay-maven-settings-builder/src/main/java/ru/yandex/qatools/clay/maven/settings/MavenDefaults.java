package ru.yandex.qatools.clay.maven.settings;

import java.io.File;

/**
 * eroshenkoam
 * 24/10/14
 */
public class MavenDefaults {

    public static final String M2_HOME = "M2_HOME";
    public static final String USER_HOME = "user.home";
    public static final String MAVEN_HOME = "maven.home";

    public static final String TEMP_DIR = "java.io.tmpdir";

    public static String getDefaultLocalRepository() {
        if (System.getProperty(USER_HOME) == null) {
            return null;
        }

        String localRepositoryPath = String.format("%s/.m2/repository",
                System.getProperty(USER_HOME));
        return new File(localRepositoryPath).getAbsolutePath();
    }


    /**
     * Looks for settings.xml at '${user.home}/.m2/settings.xml' and '$M2_HOME/conf/settings.xml'.
     * {@see http://maven.apache.org/settings.html}. If can't find settings, then throw Exception}
     */
    public static String getDefaultSystemSettings() {
        String userHome = System.getProperty(USER_HOME);
        if (userHome != null) {
            String settingsPath = String.format("%s/.m2/settings.xml", userHome);
            return new File(settingsPath).getAbsolutePath();
        }

        String m2home = System.getProperty(MAVEN_HOME, System.getProperty(M2_HOME));
        if (m2home != null) {
            String settingsPath = String.format("%s/conf/settings.xml", m2home);
            return new File(settingsPath).getAbsolutePath();
        }

        throw new RuntimeException("Can't find default maven settings file");
    }

}
