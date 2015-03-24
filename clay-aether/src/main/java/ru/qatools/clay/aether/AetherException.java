package ru.qatools.clay.aether;

/**
 * Thrown in case of a unresolvable artifacts.
 * Thrown in case of bad artifact descriptors, version ranges or other issues
 * encountered during calculation of the dependency graph.
 * <br/><br/>
 * Combination of: <br/>
 * {@link org.eclipse.aether.resolution.ArtifactResolutionException},<br/>
 * {@link org.eclipse.aether.resolution.DependencyResolutionException} and <br/>
 * {@link org.eclipse.aether.collection.DependencyCollectionException}
 *
 * @author Dmitry Baev charlie@yandex-team.ru
 */
@SuppressWarnings("unused")
public class AetherException extends Exception {

    public AetherException(String message) {
        super(message);
    }

    public AetherException(Throwable cause) {
        super(cause);
    }

    public AetherException(String message, Throwable cause) {
        super(message, cause);
    }
}
