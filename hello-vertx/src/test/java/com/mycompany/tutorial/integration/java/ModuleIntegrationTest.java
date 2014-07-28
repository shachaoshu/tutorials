package com.mycompany.tutorial.integration.java;

import org.junit.Test;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpClientResponse;
import org.vertx.testtools.TestVerticle;

import static org.vertx.testtools.VertxAssert.*;

/**
 * Example Java integration test that deploys the module that this project builds.
 * <p/>
 * Quite often in integration tests you want to deploy the same module for all tests and you don't want tests
 * to start before the module has been deployed.
 * <p/>
 * This test demonstrates how to do that.
 */
public class ModuleIntegrationTest extends TestVerticle {

    @Test
    public void testService() {
        container.logger().info("in testService()");
        vertx.createHttpClient().setPort(8080).getNow("/", new Handler<HttpClientResponse>() {
            @Override
            public void handle(HttpClientResponse resp) {
                resp.bodyHandler(new Handler<Buffer>() {
                    @Override
                    public void handle(Buffer event) {
                        container.logger().info("Client received data: " + event);
                        assertEquals("pong!pong!", event.toString());
                        testComplete();
                    }
                });
            }
        });
    }

    @Test
    public void testSomethingElse() {
        // Whatever
        testComplete();
    }


    @Override
    public void start() {
        // Make sure we call initialize() - this sets up the assert stuff so assert functionality works correctly
        initialize();
        // Deploy the module - the System property `vertx.modulename` will contain the name of the module so you
        // don't have to hardecode it in your tests
        container.deployModule(System.getProperty("vertx.modulename"), new AsyncResultHandler<String>() {
            @Override
            public void handle(AsyncResult<String> asyncResult) {
                // Deployment is asynchronous and this this handler will be called when it's complete (or failed)
                assertTrue(asyncResult.succeeded());
                assertNotNull("deploymentID should not be null", asyncResult.result());
                // If deployed correctly then start the tests!
                startTests();
            }
        });
    }

}
