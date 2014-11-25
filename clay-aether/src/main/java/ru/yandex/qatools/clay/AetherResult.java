package ru.yandex.qatools.clay;

import org.eclipse.aether.resolution.ArtifactResult;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 */
public class AetherResult {

    private List<ArtifactResult> results;

    AetherResult(List<ArtifactResult> results) {
        this.results = results;
    }

    /**
     * Return {@link #results}
     *
     * @return {@link #results}
     */
    public List<ArtifactResult> get() {
        return results;
    }

    /**
     * Shortcut for {@link #getAsClassLoader(boolean)}
     */
    public URLClassLoader getAsClassLoader() throws AetherException {
        return getAsClassLoader(false);
    }

    /**
     * Shortcut for {@link #getAsClassLoader(ClassLoader, boolean)}
     */
    public URLClassLoader getAsClassLoader(boolean failOnError) throws AetherException {
        return getAsClassLoader(null, failOnError);
    }

    /**
     * Shortcut for {@link #getAsClassLoader(ClassLoader, boolean)}
     */
    public URLClassLoader getAsClassLoader(ClassLoader parent) throws AetherException {
        return getAsClassLoader(parent, false);
    }

    /**
     * Get results as {@link java.net.URLClassLoader}
     *
     * @param parent the parent ClassLoader, may be {@code null}
     * @return created {@link java.net.URLClassLoader}
     * @throws AetherException if can't get artifact URL
     */
    public URLClassLoader getAsClassLoader(ClassLoader parent, boolean failOnError) throws AetherException {
        return new URLClassLoader(getAsUrls(failOnError), parent);
    }

    /**
     * Shortcut for {@link #getAsUrls(boolean)}
     */
    public URL[] getAsUrls() throws AetherException {
        return getAsUrls(false);
    }

    /**
     * Get results as array of {@link java.net.URL}
     *
     * @return array of {@link java.net.URL}
     * @throws AetherException if can't get artifact URL
     */
    public URL[] getAsUrls(boolean failOnError) throws AetherException {
        List<URL> urls = getAs(new Converter<URL>() {
            @Override
            public boolean check(ArtifactResult result) {
                return result != null && result.getArtifact() != null && result.getArtifact().getFile() != null;
            }

            @Override
            public URL convert(ArtifactResult result) throws AetherException {
                try {
                    return result.getArtifact().getFile().toURI().toURL();
                } catch (MalformedURLException e) {
                    throw new AetherException(e);
                }
            }
        }, failOnError);
        return urls.toArray(new URL[urls.size()]);
    }

    /**
     * Shortcut for {@link #getAsClassPath(boolean)}
     */
    public String[] getAsClassPath() throws AetherException {
        return getAsClassPath(false);
    }

    /**
     * Get results as array of {@link java.lang.String}
     *
     * @return array of {@link java.lang.String}
     * @throws AetherException if can't get artifact URL
     */
    public String[] getAsClassPath(boolean failOnError) throws AetherException {
        List<String> strings = getAs(new Converter<String>() {
            @Override
            public boolean check(ArtifactResult result) {
                return result != null && result.getArtifact() != null && result.getArtifact().getFile() != null;
            }

            @Override
            public String convert(ArtifactResult result) throws AetherException {
                try {
                    return result.getArtifact().getFile().toURI().toURL().toString();
                } catch (MalformedURLException e) {
                    throw new AetherException(e);
                }

            }
        }, failOnError);
        return strings.toArray(new String[strings.size()]);
    }

    /**
     * Convert results to list using given {@link ru.yandex.qatools.clay.AetherResult.Converter}
     *
     * @param converter   the converter
     * @param failOnError true if you need to get {@link ru.yandex.qatools.clay.AetherException} on failure,
     *                    false otherwise
     * @return list of converted results
     * @throws AetherException if can't convert some result and {@code failOnError} is true
     */
    public <T> List<T> getAs(Converter<T> converter, boolean failOnError) throws AetherException {
        List<T> converted = new ArrayList<>();
        for (ArtifactResult result : results) {
            if (converter.check(result)) {
                try {
                    converted.add(converter.convert(result));
                } catch (Exception e) {
                    if (failOnError) {
                        throw new AetherException("Can't convert artifact result", e);
                    }
                }
            } else if (failOnError) {
                throw new AetherException("Artifact result " + result + " doesn't contains file");
            }
        }
        return converted;
    }

    /**
     * Converter for {@link org.eclipse.aether.resolution.ArtifactResult}
     *
     * @param <T> type in which results will be converted
     */
    public interface Converter<T> {

        /**
         * Use this method to check {@link org.eclipse.aether.resolution.ArtifactResult}
         *
         * @param result to check
         * @return true if you need to convert this {@link org.eclipse.aether.resolution.ArtifactResult}
         */
        boolean check(ArtifactResult result);

        /**
         * Use this method to convert {@link org.eclipse.aether.resolution.ArtifactResult}
         *
         * @param result to convert
         * @return converted result
         * @throws ru.yandex.qatools.clay.AetherException if something went wrong
         */
        T convert(ArtifactResult result) throws AetherException;
    }
}
