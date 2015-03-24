package ru.qatools.clay.utils.archive;

import java.util.jar.JarEntry;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 */
@SuppressWarnings("unused")
public class PathJarEntryFilter implements JarEntryFilter {

    private final String path;

    public PathJarEntryFilter(String path) {
        this.path = path;
    }

    @Override
    public boolean accept(JarEntry entry) {
        return entry.getName().equals(path);
    }

    @Override
    public String getOutputFilePath(JarEntry entry) {
        return entry.getName();
    }
}
