package org.example;

import io.vertx.core.*;
import io.vertx.core.eventbus.DeliveryOptions;

class Sending extends AbstractVerticle
{
    @Override
    public void start()
    {

        vertx.setPeriodic(1000, (i)->
        {
            DeliveryOptions deliveryOptions = new DeliveryOptions();

            vertx.eventBus().request("ipl.today", "gt wins", (reply)->
            {
                System.out.println("Sending " + Thread.currentThread().getName());

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

class Recieving extends AbstractVerticle
{
    @Override
    public void start()
    {

        vertx.eventBus().consumer("ipl.today", (message)->{

            System.out.println("recieving " + Thread.currentThread().getName());

            System.out.println(message.body().toString());

            message.reply("rcb looser");
        });

//       vertx.executeBlocking(()->
//       {
//           System.out.println("Recieving " + Thread.currentThread().getName());
//
//           Thread.sleep(10000);
//
//           return null;
//       }, false);

//        vertx.executeBlocking(()->
//        {
//            System.out.println("Recieving " + Thread.currentThread().getName());
//
//            Thread.sleep(10000);
//
//            return null;
//        });
    }

    @Override
    public void stop()
    {

    }
}

public class ExecuteBlockingTwo
{
    public static void main(String[] args)
    {
        VertxOptions vertxOptions = new VertxOptions().setWorkerPoolSize(1);

        Vertx vertx = Vertx.vertx(vertxOptions);


        DeploymentOptions deploymentOptions = new DeploymentOptions().setWorkerPoolSize(1).setThreadingModel(ThreadingModel.WORKER);

        vertx.deployVerticle(new Recieving(), deploymentOptions);

        vertx.deployVerticle(new Sending(), deploymentOptions);

    }
}
