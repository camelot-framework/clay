package ru.yandex.qatools.clay.maven.settings;

import org.apache.maven.settings.RepositoryPolicy;

/**
 * @author innokenty
 */
public class FluentRepositoryPolicyBuilder {

    private final RepositoryPolicy policy;

    private FluentRepositoryPolicyBuilder(RepositoryPolicy policy) {
        this.policy = policy;
    }

    public static FluentRepositoryPolicyBuilder newRepositoryPolicy() {
        return new FluentRepositoryPolicyBuilder(new RepositoryPolicy());
    }

    public RepositoryPolicy build() {
        return policy;
    }

    /* DELEGATED METHODS */

    public FluentRepositoryPolicyBuilder disabled() {
        policy.setEnabled(false);
        return this;
    }

    public FluentRepositoryPolicyBuilder withChecksumPolicy(String checksumPolicy) {
        policy.setChecksumPolicy(checksumPolicy);
        return this;
    }

    public FluentRepositoryPolicyBuilder withUpdatePolicy(String updatePolicy) {
        policy.setUpdatePolicy(updatePolicy);
        return this;
    }
}
