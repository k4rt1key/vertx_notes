package org.example;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PromisePractical
{
    public static void main(String[] args)
    {

        Vertx vertx = Vertx.vertx();

        List<Promise> promises = new ArrayList<>();

        Promise<String> promise1 = Promise.promise();
        Promise<String> promise2 = Promise.promise();
        Promise<String> promise3 = Promise.promise();
        Promise<String> promise4 = Promise.promise();
        Promise<String> promise5 = Promise.promise();
        Promise<String> promise6 = Promise.promise();

        promises.add(promise1);
        promises.add(promise2);
        promises.add(promise3);
        promises.add(promise4);
        promises.add(promise5);
        promises.add(promise6);

        io.vertx.core.Future<String> future1 =  promise1.future();
        io.vertx.core.Future<String> future2 = promise2.future();
        io.vertx.core.Future<String> future3 = promise3.future();
        io.vertx.core.Future<String> future4 = promise4.future();
        io.vertx.core.Future<String> future5 = promise5.future();
        io.vertx.core.Future<String> future6 = promise6.future();

        CompositeFuture compositeFuture = Future.all(future1, future2, future3, future4, future5, future6);
        CompositeFuture compositeFuture1 = Future.join(future1, future2, future3, future4, future5, future6);

//        compositeFuture1.onSuccess(ar -> {
//            System.out.println("JOIN. All promises completed successfully");
//        }).onFailure(ar -> {
//            System.out.println("JOIN. One or more promises failed");
//        });

        vertx.setTimer(1000, id -> {
            System.out.println("1000ms. Timer completed");
            promise1.complete("Completed");
            promise3.complete("Completed");
            promise5.fail("Failed 5");
        });

        vertx.setTimer(2000, id ->{
            System.out.println("2000ms. Timer completed");
            promise2.complete("Completed");
            promise4.complete("Completed");
            promise6.fail("Failed 6");
        });

        compositeFuture1.onComplete(ar -> {
            if (ar.succeeded()) {
                System.out.println("JOIN All promises completed successfully");
            } else {
                System.out.println("JOIN One or more promises failed");
            }
        });

        compositeFuture.onComplete(ar -> {
            if (ar.succeeded()) {
                System.out.println("ALL All promises completed successfully");
            } else {
                System.out.println("ALL One or more promises failed");
            }
        });

    }
}
