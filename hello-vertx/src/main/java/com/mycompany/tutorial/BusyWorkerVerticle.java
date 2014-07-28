package com.mycompany.tutorial;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.platform.Verticle;

public class BusyWorkerVerticle extends Verticle {
    @Override
    public void start() {
        // emulate an busy worker
        vertx.eventBus().registerHandler("busy.worker", new Handler<Message<String>>() {
            @Override
            public void handle(Message<String> event) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                event.reply("pong!");
            }
        });
    }
}
