package ru.yandex.qatools.clay;

import org.eclipse.aether.RepositoryException;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 24.07.14
 *         <p/>
 *         Thrown in case of a unresolvable artifacts.
 *         Thrown in case of bad artifact descriptors, version ranges or other issues
 *         encountered during calculation of the dependency graph.
 *         Combination of {@link org.eclipse.aether.resolution.ArtifactResolutionException}
 *         {@link org.eclipse.aether.resolution.DependencyResolutionException} and
 *         {@link org.eclipse.aether.collection.DependencyCollectionException}
 */
@SuppressWarnings("unused")
public class AetherException extends RepositoryException {
    public AetherException(String message) {
        super(message);
    }

    public AetherException(String message, Throwable cause) {
        super(message, cause);
    }
}
