package org.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.http.RequestOptions;

class SenderVerticle extends AbstractVerticle
{
    @Override
    public void start()
    {
        vertx.setPeriodic(1000, (i)->
        {
            DeliveryOptions deliveryOptions = new DeliveryOptions();
            deliveryOptions.setSendTimeout(1000); // For this much time
            MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
            multiMap.add("Type", "Practice");
            deliveryOptions.setHeaders(multiMap);

            vertx.eventBus()
                    .request("news.ipl", "GT WINS", deliveryOptions,(req)->{
                        if(req.succeeded()){
                            System.out.println(req.result().body());
                        }
                    });
        });
    }
}

class ReceiverVerticle extends AbstractVerticle
{
    @Override
    public void start()
    {
        vertx.setPeriodic(1000, (i)->
        {
            vertx.eventBus().consumer("news.ipl", message -> {
                System.out.println("Received Message " + message.body().toString() + " With header " + message.headers().get("type"));

                for(int j = 0; j < 100000000; j++){
                    for (int k = 0; k < 1000000; k++ ){

                    }
                }
                // Exeception
                //                WARNING: Thread Thread[vert.x-eventloop-thread-1,5,main] has been blocked for 5899 ms, time limit is 2000 ms
                //                io.vertx.core.VertxException: Thread blocked
                //                at app//org.example.ReceiverVerticle.lambda$start$0(EventBusDemo.java:43)

                message.reply("reply " + message.body().toString());
            });
        });
    }
}

public class EventBusDemo
{
    public static void main(String[] args)
    {
        Vertx vertx = Vertx.vertx();

        vertx.deployVerticle(new SenderVerticle());
        vertx.deployVerticle(new ReceiverVerticle());
    }
}
