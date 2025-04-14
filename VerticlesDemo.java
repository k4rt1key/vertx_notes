package org.example;

import io.vertx.core.*;

class SomeVerticle extends AbstractVerticle
{
    // By default, single threaded
    // During deployment it has it's own event loop thread

    @Override
    public void start(Promise<Void> promise)
    {
        System.out.println("Some Vertical is running...");

        vertx.setPeriodic(1000, (i)->
        {
            System.out.println("Periodic task");
        });
    }

    @Override
    public void stop()
    {
        System.out.println("Cleanup");
    }
}

public class VerticlesDemo
{
    public static void main(String[] args)
    {

        VertxOptions vertxOptions = new VertxOptions();
        vertxOptions.setEventLoopPoolSize(10);

        Vertx vertx = Vertx.vertx(vertxOptions);

        vertx.deployVerticle(new SomeVerticle(), new DeploymentOptions().setThreadingModel(ThreadingModel.WORKER) ,(res)->
        {
            if(res.succeeded())
            {
                System.out.println("Successfully deployed verticle with id + " + res.result());
                // res.result() returns vertical id

                vertx.setTimer(10000, (i)-> // We can also vertx.cancelTimer(TimerId)
                {
                    System.out.println("Undeploy verticle with id "  + res.result());

                    vertx.undeploy(res.result());
                });
            }
        });

    }
}
