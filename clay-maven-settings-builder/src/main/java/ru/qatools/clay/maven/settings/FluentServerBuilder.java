package ru.qatools.clay.maven.settings;

import org.apache.maven.settings.Server;

/**
 *  The <code>&lt;server&gt;</code> element contains
 * information required to a server settings.
 * @author Innokenty Shuvalov innokenty@yandex-team.ru
 */
@SuppressWarnings("JavaDoc")
public class FluentServerBuilder {

    private final Server server;

    private FluentServerBuilder(Server server) {
        this.server = server;
    }

    /**
     * The <code>&lt;server&gt;</code> element contains
     * information required to a server settings.
     */
    public static FluentServerBuilder newServer() {
        return new FluentServerBuilder(new Server());
    }

    public Server build() {
        return server;
    }

    /* DELEGATED METHODS */

    /**
     * Set the id field.
     * @param id
     */
    public FluentServerBuilder withId(String id) {
        server.setId(id);
        return this;
    }

    /**
     * Set the username used to authenticate.
     * @param username
     */
    public FluentServerBuilder withUsername(String username) {
        server.setUsername(username);
        return this;
    }

    /**
     * Set the password used in conjunction with the username to
     * authenticate.
     * @param password
     */
    public FluentServerBuilder withPassword(String password) {
        server.setPassword(password);
        return this;
    }

    /**
     * Set the passphrase used in conjunction with the privateKey
     * to authenticate.
     * @param passphrase
     */
    public FluentServerBuilder withPassphrase(String passphrase) {
        server.setPassphrase(passphrase);
        return this;
    }

    /**
     * Set the private key location used to authenticate.
     * @param privateKey
     */
    public FluentServerBuilder withPrivateKey(String privateKey) {
        server.setPrivateKey(privateKey);
        return this;
    }

    /**
     * Set extra configuration for the transport layer.
     * @param configuration
     */
    public FluentServerBuilder withConfiguration(Object configuration) {
        server.setConfiguration(configuration);
        return this;
    }

    /**
     * Set the permissions for directories when they are created.
     * @param directoryPermissions
     */
    public FluentServerBuilder withDirectoryPermissions(String directoryPermissions) {
        server.setDirectoryPermissions(directoryPermissions);
        return this;
    }

    /**
     * Set the permissions for files when they are created.
     * @param filePermissions
     */
    public FluentServerBuilder withFilePermissions(String filePermissions) {
        server.setFilePermissions(filePermissions);
        return this;
    }
}
