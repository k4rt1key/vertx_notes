package org.example;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

public class ExceptionHandler
{
    public static void main(String[] args)
    {
        Vertx vertx = Vertx.vertx();

        vertx.exceptionHandler(ex -> {
            System.out.println("Exception: " + ex.getMessage());
        });

    }
}
