package ru.qatools.clay.maven.settings;

import org.apache.maven.settings.Activation;
import org.apache.maven.settings.Profile;
import org.apache.maven.settings.Repository;

import static ru.qatools.clay.maven.settings.FluentActivationBuilder.newActivation;

/**
 * Modifications to the build process which is keyed on
 * some sort of environmental parameter.
 *
 * @author Innokenty Shuvalov innokenty@yandex-team.ru
 */
@SuppressWarnings("JavaDoc")
public class FluentProfileBuilder {

    private final Profile profile;

    private FluentProfileBuilder(Profile profile) {
        this.profile = profile;
    }

    /**
     * Modifications to the build process which is keyed on
     * some sort of environmental parameter.
     */
    public static FluentProfileBuilder newProfile() {
        return new FluentProfileBuilder(new Profile());
    }

    public Profile build() {
        return profile;
    }

    /* DELEGATED METHODS */

    /**
     * Set the id field.
     * @param id
     */
    public FluentProfileBuilder withId(String id) {
        profile.setId(id);
        return this;
    }

    /**
     * Set this profile to be active
     */
    public FluentProfileBuilder activeByDefault() {
        return withActivation(newActivation().activeByDefault());
    }

    /**
     * Set the conditional logic which will automatically
     *             trigger the inclusion of this profile.
     * @param activation
     */
    public FluentProfileBuilder withActivation(FluentActivationBuilder activation) {
        return withActivation(activation.build());
    }

    /**
     * Set the conditional logic which will automatically
     *             trigger the inclusion of this profile.
     * @param activation
     */
    public FluentProfileBuilder withActivation(Activation activation) {
        profile.setActivation(activation);
        return this;
    }

    /**
     * Add extended configuration specific to this profile goes
     * here. Contents take the form of
     * <code>&lt;key&gt;value&lt;/key&gt;</code>
     * @param key
     * @param value
     */
    public FluentProfileBuilder withProperty(String key, String value) {
        profile.addProperty(key, value);
        return this;
    }

    /**
     * Add a remote repository.
     * @param repository
     */
    public FluentProfileBuilder withRepository(FluentRepositoryBuilder repository) {
        return withRepository(repository.build());
    }

    /**
     * Add a remote repository.
     * @param repository
     */
    public FluentProfileBuilder withRepository(Repository repository) {
        profile.addRepository(repository);
        return this;
    }

    /**
     * add a remote repository for discovering plugins.
     * @param repository
     */
    public FluentProfileBuilder withPluginRepository(FluentRepositoryBuilder repository) {
        return withRepository(repository.build());
    }

    /**
     * add a remote repository for discovering plugins.
     * @param repository
     */
    public FluentProfileBuilder withPluginRepository(Repository repository) {
        profile.addPluginRepository(repository);
        return this;
    }
}
