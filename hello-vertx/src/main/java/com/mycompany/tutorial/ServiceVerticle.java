package com.mycompany.tutorial;

import io.vertx.rxcore.java.eventbus.RxEventBus;
import io.vertx.rxcore.java.eventbus.RxMessage;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.platform.Verticle;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public class ServiceVerticle extends Verticle {
    @Override
    public void start() {
        // emulate an busy worker
        vertx.eventBus().registerHandler("service", new Handler<Message<String>>() {
            @Override
            public void handle(final Message<String> event) {
                // Send a message to a worker and wait for it's response
                final RxEventBus rxEventBus = new RxEventBus(vertx.eventBus());
                final long startTime = java.util.Calendar.getInstance().getTimeInMillis();

                final StringBuilder resultBuffer = new StringBuilder();

                Observable.from("busy.worker", "busy.worker").flatMap(new Func1<String, Observable<RxMessage<String>>>() {
                    @Override
                    public Observable<RxMessage<String>> call(String s) {
                        return rxEventBus.send(s, "work!");
                    }
                }).subscribe(new Subscriber<RxMessage<String>>() {
                    @Override
                    public void onCompleted() {
                        long timeElapsed = (java.util.Calendar.getInstance().getTimeInMillis() - startTime) / 1000;
                        container.logger().info("Work done, time elapsed: " + timeElapsed + "ms");
                        event.reply(resultBuffer.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        container.logger().error(e);
                    }

                    @Override
                    public void onNext(RxMessage<String> message) {
                        container.logger().info("Received a reply from worker: " + message.body());
                        resultBuffer.append(message.body());
                    }
                });
            }
        });
    }
}
