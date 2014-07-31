package ru.yandex.qatools.clay.maven.settings;

import org.apache.maven.model.DeploymentRepository;
import org.apache.maven.model.InputLocation;
import org.apache.maven.model.RepositoryPolicy;

/**
 * Repository contains the information needed for deploying to the
 * remote repository.
 *
 * @author innokenty
 */
@SuppressWarnings("JavaDoc")
public class FluentDeploymentRepositoryBuilder {

    private final DeploymentRepository repository;

    private FluentDeploymentRepositoryBuilder(DeploymentRepository repository) {
        this.repository = repository;
    }

    /**
     * Repository contains the information needed for deploying to the
     * remote repository.
     */
    public static FluentDeploymentRepositoryBuilder newDeploymentRepository() {
        return new FluentDeploymentRepositoryBuilder(new DeploymentRepository());
    }

    public DeploymentRepository build() {
        return repository;
    }

    /* DELEGATED METHODS */

    /**
     * Set a unique identifier for a repository. This is used to
     * match the repository to configuration in the
     * <code>settings.xml</code> file, for example. Furthermore,
     * the identifier is used during POM inheritance and profile
     * injection to detect repositories that should be merged.
     *
     * @param id
     */
    public FluentDeploymentRepositoryBuilder withId(String id) {
        repository.setId(id);
        return this;
    }

    /**
     * Set human readable name of the repository.
     *
     * @param name
     */
    public FluentDeploymentRepositoryBuilder withName(String name) {
        repository.setName(name);
        return this;
    }

    /**
     * Set the url of the repository, in the form
     * <code>protocol://hostname/path</code>.
     *
     * @param url
     */
    public FluentDeploymentRepositoryBuilder withUrl(String url) {
        repository.setUrl(url);
        return this;
    }

    /**
     * Set not to use the same version each time instead of
     * assigning snapshots a unique version comprised of the
     * timestamp and build number (which is the default behaviour).
     */
    public FluentDeploymentRepositoryBuilder withNotUniqueVersion() {
        repository.setUniqueVersion(false);
        return this;
    }

    /**
     * Set how to handle downloading of releases from this
     * repository.
     *
     * @param releases
     */
    public FluentDeploymentRepositoryBuilder withReleases(RepositoryPolicy releases) {
        repository.setReleases(releases);
        return this;
    }

    /**
     * Set how to handle downloading of snapshots from this
     * repository.
     *
     * @param snapshots
     */
    public FluentDeploymentRepositoryBuilder withSnapshots(RepositoryPolicy snapshots) {
        repository.setSnapshots(snapshots);
        return this;
    }

    /**
     * Set the type of layout this repository uses for locating and
     * storing artifacts - can be <code>legacy</code> or
     * <code>default</code>.
     *
     * @param layout
     */
    public FluentDeploymentRepositoryBuilder withLayout(String layout) {
        repository.setLayout(layout);
        return this;
    }

    /**
     * @param key
     * @param location
     */
    public FluentDeploymentRepositoryBuilder withLocation(Object key, InputLocation location) {
        repository.setLocation(key, location);
        return this;
    }
}
