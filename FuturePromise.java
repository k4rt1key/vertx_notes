package org.example;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

import java.util.concurrent.TimeUnit;

/**
 * Demonstrates the usage of Vert.x Future and Promise.
 *
 * Future:
 * - Represents the result of an asynchronous operation that may not have completed yet.
 * - It is **read-only** (consumers can attach handlers to it but cannot modify it).
 *
 * Promise:
 * - Used to create and complete a Future.
 * - It is **write-only** (producers complete it successfully or with failure).
 */
public class FuturePromise {
    public static void main(String[] args) {
        // Create a Vert.x instance
        Vertx vertx = Vertx.vertx();

        // Create a Promise (allows setting the result later)
        Promise<String> promise = Promise.promise();

        // Get a Future from the Promise (read-only for consumers)
        Future<String> future = promise.future();

        /*
         * Future methods:
         * - future.isComplete()  -> Checks if the Future is completed (success or failure).
         * - future.failed()      -> Returns true if the Future failed.
         * - future.succeeded()   -> Returns true if the Future succeeded.
         */

        // Example: Composing Futures (Chaining asynchronous operations)
        // future.compose(result -> Future.succeededFuture());

        // Example: Adding a timeout to the Future (fails if not completed in time)
        // future.timeout(2, TimeUnit.SECONDS)
        //       .onSuccess(res -> System.out.println(res))
        //       .onFailure(err -> System.out.println("Timed Out"));

        // Example: Recovering from a failure by providing a fallback value
        // future.recover(err -> Future.succeededFuture("Recovered"))
        //       .onSuccess(System.out::println)
        //       .onFailure(System.out::println);

        /*
         * Creating pre-completed Futures:
         * - Future.succeededFuture("success") -> Creates a Future that is already completed successfully.
         * - Future.failedFuture("error")      -> Creates a Future that is already completed with failure.
         */

        // Combining multiple Futures
        // Future<Void> x = Future.all(future1, future2)  -> Completes when all succeed.
        // Future<String> y = Future.any(future1, future2)  -> Completes when any one succeeds.
        CompositeFuture y = Future.all(future, future);
        y.onComplete(res -> {
            for(int i = 0; i < y.size(); i++) {
                System.out.println("Future.any() & " + i + " " + y.resultAt(i));
            }
        });

        // Handler for successful completion
        future.onSuccess(result -> {
            System.out.println("Success: " + result);
        });

        // Handler for completion (either success or failure)
        future.onComplete(result -> {
            System.out.println("Complete: " + result.result());
            /*
             * Additional result methods:
             * - result.failed()     -> Returns true if the Future failed.
             * - result.succeeded()  -> Returns true if the Future succeeded.
             * - result.cause()      -> Returns the Throwable cause of failure.
             * - result.otherwise(val) -> Returns "val" on failure and actual result on success.
             */
        });

        // Handler that always executes, regardless of success or failure
        future.eventually(() -> {
            System.out.println("Inside eventually block");
            return Future.succeededFuture("Eventual");
        });

        // Handler for failure cases
        future.onFailure(error -> {
            System.out.println("Error: " + error);
        });

        // Executes after the Future completes (regardless of success or failure)
        future.andThen(result -> {
            System.out.println("And then: " + result);
        });

        // Simulate an asynchronous operation using Vert.x Timer
        vertx.setTimer(1000, (i) -> {
            // Completing the Promise after 1 second
            promise.complete("Done");
            promise.complete("done");

            /*
             * Output when promise completes successfully:
             * - Success: Done
             * - Complete: Done
             * - Inside eventually block
             * - And then: Future{result=Done}
             */

            // Using trySuccess() to attempt completion (only works if not already completed)
            boolean success = promise.tryComplete("Try Success Done");
            System.out.println("trySuccess result: " + success);

            // Using tryFail() to attempt failure (only works if not already completed)
            boolean failure = promise.tryFail("Try Fail Error");
            System.out.println("tryFail result: " + failure);

            // Uncomment the line below to test failure case:
            // promise.fail("Error");

            /*
             * Output when promise fails:
             * - Complete: null
             * - Inside eventually block
             * - Error: io.vertx.core.impl.NoStackTraceThrowable: Error
             * - And then: Future{cause=Error}
             */
        });
    }
}
