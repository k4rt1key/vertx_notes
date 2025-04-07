package org.example;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

public class FutureComposition {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

        Future<String> future1 = asyncOperation1(vertx);
        Future<String> future2 = asyncOperation2(vertx);


        // Compose futures
        Future<String> composed = future1.compose(res1 -> {
            return future2.map(res2 -> res1 + " " + res2);
        });

        composed.onSuccess(result -> {
            System.out.println("Final result: " + result);
        });
    }

    private static Future<String> asyncOperation1(Vertx vertx) {
        Promise<String> promise = Promise.promise();
        vertx.setTimer(500, id -> promise.complete("Hello"));
        return promise.future();
    }

    private static Future<String> asyncOperation2(Vertx vertx) {
        Promise<String> promise = Promise.promise();
        vertx.setTimer(1000, id -> promise.complete("Vert.x!"));
        return promise.future();
    }
}