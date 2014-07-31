package ru.yandex.qatools.clay.maven.settings;

import org.apache.maven.settings.Proxy;

/**
 * The <code>&lt;proxy&gt;</code> element contains
 * information required to a proxy settings.
 *
 * @author innokenty
 */
@SuppressWarnings("JavaDoc")
public class FluentProxyBuilder {

    private final Proxy proxy;

    private FluentProxyBuilder(Proxy proxy) {
        this.proxy = proxy;
    }

    /**
     * The <code>&lt;proxy&gt;</code> element contains
     * informations required to a proxy settings.
     */
    public static FluentProxyBuilder newProxy() {
        return new FluentProxyBuilder(new Proxy());
    }

    public Proxy build() {
        return proxy;
    }

    /* DELEGATED METHODS */

    /**
     * Set the id field.
     * @param id
     */
    public FluentProxyBuilder withId(String id) {
        proxy.setId(id);
        return this;
    }

    /**
     * Set the proxy user.
     * @param username
     */
    public FluentProxyBuilder withUsername(String username) {
        proxy.setUsername(username);
        return this;
    }

    /**
     * Set the proxy password.
     * @param password
     */
    public FluentProxyBuilder withPassword(String password) {
        proxy.setPassword(password);
        return this;
    }

    /**
     * Make this proxy configuration not active.
     * All proxies are active by default.
     */
    public FluentProxyBuilder notActive() {
        proxy.setActive(false);
        return this;
    }

    /**
     * Set the proxy host.
     * @param host
     */
    public FluentProxyBuilder withHost(String host) {
        proxy.setHost(host);
        return this;
    }

    /**
     * Set the list of non-proxied hosts (delimited by |).
     * @param nonProxyHosts
     */
    public FluentProxyBuilder withNonProxyHosts(String nonProxyHosts) {
        proxy.setNonProxyHosts(nonProxyHosts);
        return this;
    }

    /**
     * Set the proxy protocol.
     * @param protocol
     */
    public FluentProxyBuilder withProtocol(String protocol) {
        proxy.setProtocol(protocol);
        return this;
    }

    /**
     * Set the proxy port.
     * @param port
     */
    public FluentProxyBuilder withPort(int port) {
        proxy.setPort(port);
        return this;
    }
}
