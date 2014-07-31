package ru.yandex.qatools.clay.utils.archive;

import java.util.jar.JarEntry;

/**
 * @author Artem Eroshenko eroshenkoam@yandex-team.ru
 *         Date: 29.05.14
 *         <p/>
 *         Uses to filter some enties in {@link java.util.jar.JarFile}. Also map each entry
 *         to location.
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