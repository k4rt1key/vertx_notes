package org.example;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

/*
- Vert.x creates as many eventloop thread as total CPU cores
- Under the hood it uses Java NIO
*/

public class Basic
{
    public static void main( String[] args )
    {
        Vertx vertx = Vertx.vertx();

        System.out.println(new VertxOptions().getEventLoopPoolSize()); // Default : 16

        // When we call Vertx.vertx() it will initialize an event loop
        // one thread per logical CPU
        // And worker threads

        // vertx object is thread safe and can be shared across threads

        System.out.println("first");

        vertx.setPeriodic(1000, (i) ->
        {
            System.out.println("Running");
        });

        System.out.println("second");
    }
}
