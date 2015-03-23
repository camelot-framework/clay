package ru.qatools.clay.maven.settings;

import org.apache.maven.settings.ActivationOS;

/**
 * This is an activator which will detect an operating
 * system's attributes in order to activate
 * its profile.
 *
 * @author Innokenty Shuvalov innokenty@yandex-team.ru
 */
@SuppressWarnings("JavaDoc")
public class FluentActivationOSBuilder {

    private final ActivationOS activationOS;

    private FluentActivationOSBuilder(ActivationOS activationOS) {
        this.activationOS = activationOS;
    }

    /**
     * This is an activator which will detect an operating
     * system's attributes in order to activate
     * its profile.
     */
    public static FluentActivationOSBuilder newActivationOS() {
        return new FluentActivationOSBuilder(new ActivationOS());
    }

    public ActivationOS build() {
        return activationOS;
    }

    /* DELEGATED METHODS */

    /**
     * Set the architecture of the OS to be used to activate a
     * profile.
     *
     * @param arch
     */
    public FluentActivationOSBuilder withArch(String arch) {
        activationOS.setArch(arch);
        return this;
    }

    /**
     * Set the general family of the OS to be used to activate a
     *             profile (e.g. 'windows').
     *
     * @param family
     */
    public FluentActivationOSBuilder withFamily(String family) {
        activationOS.setFamily(family);
        return this;
    }

    /**
     * Set the name of the OS to be used to activate a profile.
     *
     * @param name
     */
    public FluentActivationOSBuilder withName(String name) {
        activationOS.setName(name);
        return this;
    }

    /**
     * Set the version of the OS to be used to activate a profile.
     *
     * @param version
     */
    public FluentActivationOSBuilder withVersion(String version) {
        activationOS.setVersion(version);
        return this;
    }
}
