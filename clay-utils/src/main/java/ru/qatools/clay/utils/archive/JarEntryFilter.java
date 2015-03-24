package ru.qatools.clay.utils.archive;

import java.util.jar.JarEntry;

/**
 * Uses to filter some entries in {@link java.util.jar.JarFile}.
 * Also map each entry to location.
 * @author Artem Eroshenko eroshenkoam@yandex-team.ru
 */
public interface JarEntryFilter {

    /**
     * Tests if a specified JarEntry should be unpacked.
     *
     * @param entry a entry to test
     * @return true if entry should be included, false otherwise
     */
    boolean accept(JarEntry entry);

    /**
     * Map entry to location.
     *
     * @param entry a entry to map.
     * @return relative file path for specified entry
     */
    String getOutputFilePath(JarEntry entry);
}
