package ru.qatools.clay.aether;

import net.lightbody.bmp.proxy.ProxyServer;
import org.apache.maven.settings.Proxy;
import org.eclipse.aether.artifact.Artifact;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static ru.qatools.clay.aether.Aether.aether;

/**
 * Was unable to make it work on ipv6-only machine
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 24.07.14
 */
@Ignore
public class AetherProxyTest extends AbstractAetherTest {

    public static final String HTTP = "http";
    public ProxyServer proxyServer;

    @Before
    public void setUp() throws Exception {
        proxyServer = createNewProxyServer();
        proxyServer.start();
    }

    @Test
    public void collectWithProxyTest() throws Exception {
        Proxy proxy = createProxy();
        mavenSettings.addProxy(proxy);
        List<Artifact> artifacts = aether(localRepo, mavenSettings).collect(ALLURE_MODEL);
        assertThat(artifacts.size(), is(5));
    }

    private Proxy createProxy() throws UnknownHostException {
        Proxy proxy = new Proxy();
        proxy.setActive(true);
        String proxyAddress = InetAddress.getLocalHost().getCanonicalHostName();
        proxy.setHost(proxyAddress);
        proxy.setProtocol(HTTP);
        proxy.setPort(proxyServer.getPort());
        return proxy;
    }

    @After
    public void tearDown() throws Exception {
        proxyServer.stop();
    }

    private ProxyServer createNewProxyServer() throws IOException {
        //find unused port using server socket constructor
        try (ServerSocket s = new ServerSocket(0)) {
            return new ProxyServer(s.getLocalPort());
        }
    }
}
