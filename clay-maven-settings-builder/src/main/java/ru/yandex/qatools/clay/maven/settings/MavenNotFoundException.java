package ru.yandex.qatools.clay.maven.settings;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 28.07.14
 */
public class MavenNotFoundException extends Exception {
    public MavenNotFoundException(String message) {
        super(message);
    }
}
