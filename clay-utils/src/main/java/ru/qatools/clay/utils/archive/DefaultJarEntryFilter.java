package ru.qatools.clay.utils.archive;

import java.util.jar.JarEntry;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 */
public class DefaultJarEntryFilter implements JarEntryFilter {
    @Override
    public boolean accept(JarEntry entry) {
        return true;
    }

    @Override
    public String getOutputFilePath(JarEntry entry) {
        return entry.getName();
    }
}
