package ru.yandex.qatools.clay.maven.settings;

import org.apache.maven.settings.Activation;
import org.apache.maven.settings.ActivationFile;
import org.apache.maven.settings.ActivationOS;
import org.apache.maven.settings.ActivationProperty;

/**
 * The conditions within the build runtime environment
 * which will trigger
 * the automatic inclusion of the parent build profile.
 *
 * @author innokenty
 */
@SuppressWarnings("JavaDoc")
public class FluentActivationBuilder {

    private final Activation activation;

    private FluentActivationBuilder(Activation activation) {
        this.activation = activation;
    }

    public Activation build() {
        return activation;
    }

    /* DELEGATED METHODS */

    /**
     * The conditions within the build runtime environment
     * which will trigger
     * the automatic inclusion of the parent build profile.
     */
    public static FluentActivationBuilder newActivation() {
        return new FluentActivationBuilder(new Activation());
    }

    /**
     * Make this profile active as by default.
     */
    public FluentActivationBuilder activeByDefault() {
        activation.setActiveByDefault(true);
        return this;
    }

    /**
     * Specifies that this profile will be activated based on
     * existence of a file.
     *
     * @param file
     */
    public FluentActivationBuilder withFile(ActivationFile file) {
        activation.setFile(file);
        return this;
    }

    /**
     * Specifies that this profile will be activated when a
     * matching JDK is detected.
     *
     * @param jdk
     */
    public FluentActivationBuilder withJdk(String jdk) {
        activation.setJdk(jdk);
        return this;
    }

    /**
     * Specifies that this profile will be activated when
     * matching OS attributes are detected.
     *
     * @param os
     */
    public FluentActivationBuilder withOs(FluentActivationOSBuilder os) {
        return withOs(os.build());
    }

    /**
     * Specifies that this profile will be activated when
     * matching OS attributes are detected.
     *
     * @param os
     */
    public FluentActivationBuilder withOs(ActivationOS os) {
        activation.setOs(os);
        return this;
    }

    /**
     * Specifies that this profile will be activated when this
     * System property is specified.
     *
     * @param name
     * @param value
     */
    public FluentActivationBuilder withProperty(String name, String value) {
        ActivationProperty property = new ActivationProperty();
        property.setName(name);
        property.setValue(value);
        return withProperty(property);
    }

    /**
     * Specifies that this profile will be activated when this
     * System property is specified.
     *
     * @param property
     */
    public FluentActivationBuilder withProperty(ActivationProperty property) {
        activation.setProperty(property);
        return this;
    }
}
