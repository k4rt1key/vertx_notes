package org.example;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

public class ExecuteBlocking
{
    public static void main(String[] args)
    {
        Vertx vertx = Vertx.vertx();

        System.out.println("1");

        vertx.executeBlocking(ExecuteBlocking::asyncTask1);

        asyncTask1();

        System.out.println("2");
    }

    public static Future<String> asyncTask1()
    {
        Promise promise = Promise.promise();

        for(int i = 0; i < 1000000000; i++)
        {
            for(int j = 0; j < 100000000; j++)
            {
                for(int k = 0; k < 1000000000; k++)
                {
                    //
                }
            }
        }

        System.out.println("Done For Loop");

        promise.complete("Done");

        return promise.future();
    }
}
