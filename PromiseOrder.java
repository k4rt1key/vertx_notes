package org.example;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Promise;

public class PromiseOrder
{
    public static void main(String[] args) {
        Promise<String> promise1 = Promise.promise();
        Promise<String> promise2 = Promise.promise();
        Promise<String> promise3 = Promise.promise();

        promise1.complete("First");
        promise2.fail("Second");
        promise3.complete("Third");


        CompositeFuture.any(promise1.future(), promise2.future(), promise3.future())
                .onComplete(ar -> {
                    if (ar.succeeded()) {
                        System.out.println("Success");
                    } else {
                        System.out.println("failed");
                    }
                });


        CompositeFuture.all(promise1.future(), promise2.future(), promise3.future())
            .onComplete(ar -> {
                if (ar.succeeded()) {
                    System.out.println("Success");
                } else {
                    System.out.println("failed");
                }
            });

        CompositeFuture.join(promise1.future(), promise2.future(), promise3.future())
            .onComplete(ar -> {
                if (ar.succeeded()) {
                    System.out.println("Success");
                } else {
                    System.out.println("failed");
                }
            });

    }
}
