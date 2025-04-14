package org.example.practicals;
import io.vertx.core.*;
import io.vertx.core.eventbus.EventBusOptions;

import java.util.concurrent.TimeUnit;

public class EventBusPracticals
{
    public static void main(String[] args) {



        EventBusOptions eventBusOptions = new EventBusOptions();
        eventBusOptions.setSendBufferSize(1);
        eventBusOptions.setReceiveBufferSize(1);
        Vertx vertx = Vertx.vertx(
                new VertxOptions()
                        .setEventBusOptions(eventBusOptions)
                        .setMaxEventLoopExecuteTime(10)
                        .setMaxEventLoopExecuteTimeUnit(TimeUnit.MILLISECONDS)
        );

        DeploymentOptions deploymentOptions = new DeploymentOptions();
            deploymentOptions.setInstances(8);
        deploymentOptions.setMaxWorkerExecuteTime(10);
        deploymentOptions.setMaxWorkerExecuteTimeUnit(TimeUnit.MILLISECONDS);

        vertx.deployVerticle(Sender.class, deploymentOptions);
        vertx.deployVerticle(Reciever.class, deploymentOptions);

    }
}
