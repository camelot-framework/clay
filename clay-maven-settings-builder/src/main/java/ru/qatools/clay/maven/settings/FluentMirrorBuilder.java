package ru.qatools.clay.maven.settings;

import org.apache.maven.settings.Mirror;

/**
 * Build a download mirror for a given repository.
 * @author Innokenty Shuvalov innokenty@yandex-team.ru
 */
@SuppressWarnings("JavaDoc")
public class FluentMirrorBuilder {

    private final Mirror mirror;

    private FluentMirrorBuilder(Mirror mirror) {
        this.mirror = mirror;
    }

    /**
     * Build a download mirror for a given repository.
     */
    public static FluentMirrorBuilder newMirror() {
        return new FluentMirrorBuilder(new Mirror());
    }

    public Mirror build() {
        return mirror;
    }

    /* DELEGATED METHODS */

    /**
     * Set the id field.
     * @param id
     */
    public FluentMirrorBuilder withId(String id) {
        mirror.setId(id);
        return this;
    }

    /**
     * Set the URL of the mirror repository.
     * @param url
     */
    public FluentMirrorBuilder withUrl(String url) {
        mirror.setUrl(url);
        return this;
    }

    /**
     * Set the optional name that describes the mirror.
     * @param name
     */
    public FluentMirrorBuilder withName(String name) {
        mirror.setName(name);
        return this;
    }

    /**
     * Set the server ID of the repository being mirrored, eg
     *             "central". This MUST NOT match the mirror id.
     * @param mirrorOf
     */
    public FluentMirrorBuilder withMirrorOf(String mirrorOf) {
        mirror.setMirrorOf(mirrorOf);
        return this;
    }

    /**
     * Set the layout of the mirror repository. Since Maven 3.
     * @param layout
     */
    public FluentMirrorBuilder withLayout(String layout) {
        mirror.setLayout(layout);
        return this;
    }

    /**
     * Set the layouts of repositories being mirrored. This value
     * can be used to restrict the usage
     *             of the mirror to repositories with a matching
     * layout (apart from a matching id). Since Maven 3.
     * @param mirrorOfLayouts
     */
    public FluentMirrorBuilder withMirrorOfLayouts(String mirrorOfLayouts) {
        mirror.setMirrorOfLayouts(mirrorOfLayouts);
        return this;
    }
}
