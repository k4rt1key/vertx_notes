package org.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBusOptions;
import jdk.jfr.Event;

class SenderVerticle extends AbstractVerticle
{
    @Override
    public void start()
    {
            DeliveryOptions deliveryOptions = new DeliveryOptions().setSendTimeout(1000);
            deliveryOptions.setHeaders(MultiMap.caseInsensitiveMultiMap().add("Type", "Practice"));

            vertx.setTimer(2000,res->{
                vertx.eventBus()
                        .request("news.ipl", "GT WINS",new DeliveryOptions().setSendTimeout(5000), (reply)->{
                            System.out.println(reply.result().body().toString());
                                }
                        );
            });

    }
}

class ReceiverVerticle extends AbstractVerticle
{
    @Override
    public void start()
    {

//        EventBusOptions eventBusOptions = new EventBusOptions();
//        eventBusOptions.setSendBufferSize();
//        eventBusOptions.setAcceptBacklog();
//        eventBusOptions.setSoLinger();


        vertx.eventBus().consumer("news.ipl", message ->
                {
                    System.out.println("Received Message " + message.body().toString() + " With header " + message.headers().get("type"));
                    vertx.setTimer(4000,res->{
                        message.reply("a");
                    });
                });
    }
}

public class EventBusDemo
{
    public static void main(String[] args)
    {
        Vertx vertx = Vertx.vertx();


        vertx.deployVerticle(new ReceiverVerticle());
        vertx.deployVerticle(new SenderVerticle());

    }
}
