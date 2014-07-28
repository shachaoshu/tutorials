package com.mycompany.tutorial;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

public class HttpServerVerticle extends Verticle {
    @Override
    public void start() {
        // deploy service verticle
        container.deployVerticle("com.mycompany.tutorial.ServiceVerticle");
        // deploy 2 instances of worker verticle
        container.deployVerticle("com.mycompany.tutorial.BusyWorkerVerticle", new JsonObject("{\"worker\":true}"), 2);

        vertx.createHttpServer().requestHandler(new Handler<HttpServerRequest>() {
            public void handle(final HttpServerRequest req) {
                vertx.eventBus().send("service", req.path(), new Handler<Message<String>>() {
                    @Override
                    public void handle(Message<String> message) {
                        container.logger().info("Service reply: " + message.body());
                        req.response().headers().add("Content-Length", Integer.toString(message.body().length()));
                        req.response().write(message.body()).end();
                    }
                });
            }
        }).listen(8080);
    }
}
