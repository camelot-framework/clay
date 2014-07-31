package ru.yandex.qatools.clay.utils.archive;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author Artem Eroshenko eroshenkoam@yandex-team.ru
 *         Date: 29.05.14
 *         <p/>
 *         Unpack JAR archive utilities.
 */
public final class ArchiveUtil {

    ArchiveUtil() {
    }

    public static void unpackJar(File source, File destination) throws IOException {
        unpackJar(source, destination, new DefaultJarEntryFilter());
    }

    /**
     * Unpack jar archive to specified directory. Uses {@link JarEntryFilter} to filter some
     * entries uses {@link #unpackJar(java.util.jar.JarFile, java.io.File, JarEntryFilter)}
     *
     * @param source      a jar file to unpack.
     * @param destination a directory to unpack jar archive. Shouldn't be null.
     * @param filter      a JarEntryFilter
     * @throws SecurityException   if access to the file is denied by the SecurityManager
     * @throws java.io.IOException if an I/O error has occurred
     *                             if the <code>destination</code> directory cannot be created or
     *                             the file already exists but is not a directory
     *                             if <code>destination</code> cannot be written
     *                             if an IO error occurs during copying
     */
    public static void unpackJar(File source, File destination, JarEntryFilter filter) throws IOException {
        unpackJar(new JarFile(source), destination, filter);
    }

    /**
     * Unpack {@link java.util.jar.JarFile} to specified directory. Uses {@link JarEntryFilter} to filter some
     * entries.
     *
     * @param source      a {@link java.util.jar.JarFile} to unpack. Shouldn't be null.
     * @param destination a directory to unpack jar archive. Shouldn't be null.
     * @param filter      a JarEntryFilter
     * @throws java.io.IOException if the <code>destination</code> directory cannot be created or the file already exists
     *                             but is not a directory
     *                             if <code>destination</code> cannot be written
     *                             if an IO error occurs during copying
     */
    public static void unpackJar(JarFile source, File destination, JarEntryFilter filter) throws IOException {
        Enumeration<JarEntry> entries = source.entries();
        while (entries.hasMoreElements()) {
            JarEntry entryToCopy = entries.nextElement();
            if (filter.accept(entryToCopy)) {
                File outputFile = new File(destination, filter.getOutputFilePath(entryToCopy));
                if (entryToCopy.isDirectory()) {
                    FileUtils.forceMkdir(outputFile);
                } else {
                    FileUtils.copyInputStreamToFile(source.getInputStream(entryToCopy), outputFile);
                }
            }
        }
    }
}
