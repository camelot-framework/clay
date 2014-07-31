package ru.yandex.qatools.clay.maven.settings;

import org.apache.maven.model.InputLocation;
import org.apache.maven.model.Relocation;

/**
 * @author innokenty
 */
@SuppressWarnings("JavaDoc")
public class FluentRelocationBuilder {

    private final Relocation relocation;

    private FluentRelocationBuilder(Relocation relocation) {
        this.relocation = relocation;
    }

    public static FluentRelocationBuilder newRelocation() {
        return new FluentRelocationBuilder(new Relocation());
    }

    public Relocation build() {
        return relocation;
    }

    /* DELEGATED METHODS */

    /**
     * Set the new artifact ID of the artifact.
     *
     * @param artifactId
     */
    public FluentRelocationBuilder withArtifactId(String artifactId) {
        relocation.setArtifactId(artifactId);
        return this;
    }

    /**
     * Set the group ID the artifact has moved to.
     *
     * @param groupId
     */
    public FluentRelocationBuilder withGroupId(String groupId) {
        relocation.setGroupId(groupId);
        return this;
    }

    /**
     * @param key
     * @param location
     */
    public FluentRelocationBuilder withLocation(Object key, InputLocation location) {
        relocation.setLocation(key, location);
        return this;
    }

    /**
     * Set an additional message to show the user about the move,
     * such as the reason.
     *
     * @param message
     */
    public FluentRelocationBuilder withMessage(String message) {
        relocation.setMessage(message);
        return this;
    }

    /**
     * Set the new version of the artifact.
     *
     * @param version
     */
    public FluentRelocationBuilder withVersion(String version) {
        relocation.setVersion(version);
        return this;
    }
}
