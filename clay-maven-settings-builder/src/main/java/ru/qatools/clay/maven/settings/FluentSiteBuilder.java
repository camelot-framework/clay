package ru.qatools.clay.maven.settings;

import org.apache.maven.model.InputLocation;
import org.apache.maven.model.Site;

/**
 * Contains the information needed for deploying websites.
 *
 * @author Innokenty Shuvalov innokenty@yandex-team.ru
 */
@SuppressWarnings("JavaDoc")
public class FluentSiteBuilder {

    private final Site site;

    private FluentSiteBuilder(Site site) {
        this.site = site;
    }

    /**
     * Contains the information needed for deploying websites.
     */
    public static FluentSiteBuilder newSite() {
        return new FluentSiteBuilder(new Site());
    }

    public Site build() {
        return site;
    }

    /* DELEGATED METHODS */

    /**
     * Set a unique identifier for a deployment location. This is
     * used to match the site to configuration in the
     * <code>settings.xml</code> file, for example.
     *
     * @param id
     */
    public FluentSiteBuilder withId(String id) {
        site.setId(id);
        return this;
    }

    /**
     * @param key
     * @param location
     */
    public FluentSiteBuilder withLocation(Object key, InputLocation location) {
        site.setLocation(key, location);
        return this;
    }

    /**
     * Set human readable name of the deployment location.
     *
     * @param name
     */
    public FluentSiteBuilder withName(String name) {
        site.setName(name);
        return this;
    }

    /**
     * Set the url of the location where website is deployed, in
     * the form <code>protocol://hostname/path</code>.
     * <br /><b>Default value is</b>: parent value [+
     * path adjustment] + artifactId.
     *
     * @param url
     */
    public FluentSiteBuilder withUrl(String url) {
        site.setUrl(url);
        return this;
    }
}
