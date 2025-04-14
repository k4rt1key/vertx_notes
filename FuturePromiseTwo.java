package org.example;

import io.vertx.core.Future;
import io.vertx.core.Promise;

public class FuturePromiseTwo
{
    public static void main(String[] args)
    {
        System.out.println("1");
        loop1();
        System.out.println("2");

        System.out.println("3");
        Future<Integer> future = loop2();
        future.onSuccess((i)->{
            System.out.println(i);
        });
        System.out.println("4");
    }

    public static Integer loop1()
    {
        for(int i = 0; i < 100000; i++)
        {
            for(int j = 0; j < 100000; j++)
            {
                for(int k = 0; k < 100000; k++)
                {

                }
            }
        }

        return 1;
    }

    public static Future<Integer> loop2()
    {
        Promise promise = Promise.promise();

        for(int i = 0; i < 100000; i++)
        {
            for(int j = 0; j < 100000; j++)
            {
                for(int k = 0; k < 100000; k++)
                {

                }
            }
        }

        promise.complete(1);

        return promise.future();
    }
}
