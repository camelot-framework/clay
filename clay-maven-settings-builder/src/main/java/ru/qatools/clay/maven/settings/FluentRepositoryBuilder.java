package ru.qatools.clay.maven.settings;

import org.apache.maven.settings.Repository;
import org.apache.maven.settings.RepositoryPolicy;

/**
 * Repository contains the information needed for
 * establishing connections with remote repository.
 *
 * @author Innokenty Shuvalov innokenty@yandex-team.ru
 */
@SuppressWarnings("JavaDoc")
public class FluentRepositoryBuilder {

    private final Repository repository;

    private FluentRepositoryBuilder(Repository repository) {
        this.repository = repository;
    }

    /**
     * Repository contains the information needed for
     * establishing connections with remote repository.
     */
    public static FluentRepositoryBuilder newRepository() {
        return new FluentRepositoryBuilder(new Repository());
    }

    public Repository build() {
        return repository;
    }

    /* DELEGATED METHODS */

    /**
     * Set a unique identifier for a repository.
     *
     * @param id
     */
    public FluentRepositoryBuilder withId(String id) {
        repository.setId(id);
        return this;
    }

    /**
     * Set human readable name of the repository.
     *
     * @param name
     */
    public FluentRepositoryBuilder withName(String name) {
        repository.setName(name);
        return this;
    }

    /**
     * Set the url of the repository.
     *
     * @param url
     */
    public FluentRepositoryBuilder withUrl(String url) {
        repository.setUrl(url);
        return this;
    }

    /**
     * Set the type of layout this repository uses for locating and
     *             storing artifacts - can be "legacy" or
     * "default".
     *
     * @param layout
     */
    public FluentRepositoryBuilder withLayout(String layout) {
        repository.setLayout(layout);
        return this;
    }

    /**
     * Set how to handle downloading of releases from this
     * repository.
     *
     * @param releases
     */
    public FluentRepositoryBuilder withReleases(FluentRepositoryPolicyBuilder releases) {
        return withReleases(releases.build());
    }

    /**
     * Set how to handle downloading of releases from this
     * repository.
     *
     * @param releases
     */
    public FluentRepositoryBuilder withReleases(RepositoryPolicy releases) {
        repository.setReleases(releases);
        return this;
    }

    /**
     * Set how to handle downloading of snapshots from this
     * repository.
     *
     * @param snapshots
     */
    public FluentRepositoryBuilder withSnapshots(FluentRepositoryPolicyBuilder snapshots) {
        return withSnapshots(snapshots.build());
    }

    /**
     * Set how to handle downloading of snapshots from this
     * repository.
     *
     * @param snapshots
     */
    public FluentRepositoryBuilder withSnapshots(RepositoryPolicy snapshots) {
        repository.setSnapshots(snapshots);
        return this;
    }
}
