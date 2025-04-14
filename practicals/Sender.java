package org.example.practicals;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.DeliveryOptions;

public class Sender extends AbstractVerticle
{
    @Override
    public void start()
    {
        String id = deploymentID();
        System.out.println("Sender thread " + Thread.currentThread().getName() + " with id " + id + " is started");

        vertx.setPeriodic(5000, (ii)->
        {
            String message = "gt wins";

//            for(int i = 0; i < 10000; i++){
//                for(int j = 0; i < 10000; j++){
//                    for(int k = 0; k < 10000; k++){
//                        // Do nothing
//                    }
//                }
//            }

            DeliveryOptions deliveryOptions = new DeliveryOptions();
            deliveryOptions.setSendTimeout(2400);
            deliveryOptions.addHeader("team", "gt");

            vertx.eventBus().request("ipl.today", message, deliveryOptions, (reply)->
            {
                System.out.println("Sender thread " + Thread.currentThread().getName() + " sends message " + message);

                if(reply.succeeded())
                {
                    System.out.println(reply.result().body().toString());
                }
            });
        });
    }

    @Override
    public void stop()
    {

    }
}