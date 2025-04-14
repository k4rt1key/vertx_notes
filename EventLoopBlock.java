package org.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

class Verticless extends AbstractVerticle
{
    @Override
    public void start() throws Exception {
        vertx.eventBus().consumer("abc", (message) -> {
            System.out.println("Recieving thread 1 " + Thread.currentThread().getName() + " recieves message " + message.body().toString());
            message.reply("Receiver thread " + Thread.currentThread().getName() + " replies to message " + message.body().toString());
        });
    }

    @Override
    public void stop() throws Exception {
        System.out.println("Verticle stopped");
    }
}

class Verticless2 extends AbstractVerticle
{
    @Override
    public void start() throws Exception {
        vertx.eventBus().consumer("abc", (message) -> {
            try {
                Thread.sleep(10000);
                System.out.println("Recieving thread 2" + Thread.currentThread().getName() + " recieves message " + message.body().toString());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            message.reply("Receiver thread " + Thread.currentThread().getName() + " replies to message " + message.body().toString());
        });
    }

    @Override
    public void stop() throws Exception {
        System.out.println("Verticle stopped");
    }
}


class Sender extends AbstractVerticle
{
    @Override
    public void start() throws Exception {
        vertx.setPeriodic(1000, (i) -> {
            vertx.eventBus().request("abc", "gt wins", (reply) -> {
                System.out.println("Sending " + Thread.currentThread().getName());
                if (reply.succeeded()) {
                    System.out.println(reply.result().body().toString());
                }
            });
        });
    }


    @Override
    public void stop() throws Exception {
        System.out.println("Sender stopped");
    }
}


public class EventLoopBlock
{
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx(new VertxOptions().setEventLoopPoolSize(1));

        vertx.deployVerticle(new Verticless(), res -> {
            if (res.succeeded()) {
                System.out.println("Verticle deployed successfully");
            } else {
                System.out.println("Failed to deploy verticle: " + res.cause());
            }
        });

        vertx.deployVerticle(new Verticless2(), res -> {
            if (res.succeeded()) {
                System.out.println("Verticle deployed successfully");
            } else {
                System.out.println("Failed to deploy verticle: " + res.cause());
            }
        });


        vertx.deployVerticle(new Sender(), res -> {
            if (res.succeeded()) {
                System.out.println("Sender deployed successfully");
            } else {
                System.out.println("Failed to deploy sender: " + res.cause());
            }
        });

    }
}
