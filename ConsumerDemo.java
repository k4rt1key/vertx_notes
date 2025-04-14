package org.example;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;

public class ConsumerDemo
{
//    When registering a handler on a clustered event bus,
//    it can take some time for the registration to reach all nodes of the cluster.
//    If you want to be notified when this has completed,
//    you can register a completion handler on the MessageConsumer object.

    public static void main(String[] args)
    {
        Vertx vertx = Vertx.vertx();

        EventBus eb = vertx.eventBus();

        MessageConsumer<String> consumer = eb.consumer("news.uk.sport");
        consumer.handler(message ->
        {
            System.out.println("I have received a message: " + message.body());
        });

        consumer.completionHandler(res -> {
            if (res.succeeded()) {
                System.out.println("The handler registration has reached all nodes");
            } else {
                System.out.println("Registration failed!");
            }
        });

    }
}
