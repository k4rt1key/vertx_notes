package org.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;

class MyVericle extends AbstractVerticle {

    private HttpServer server;

    @Override
    public void start(Promise<Void> startPromise) {
        // DB connection
        System.out.println("yo");
        startPromise.complete();
    }

//
//    @Override
//    public void start(Promise<Void> startPromise) {
//        server = vertx.createHttpServer().requestHandler(req -> {
//            req.response()
//                    .putHeader("content-type", "text/plain")
//                    .end("Hello from Vert.x!");
//        });
//
//        // Start the server and bind to port 8080
//        server.listen(8080)
//                .onSuccess(httpServer -> {
//                    System.out.println("Server started on port 8080");
//                    startPromise.complete(); // Notify Vert.x that startup is successful
//                })
//                .onFailure(err -> {
//                    System.err.println("Failed to start server: " + err.getMessage());
//                    startPromise.fail(err); // Notify Vert.x about the failure
//                });
//    }


    @Override
    public void stop(Promise<Void> stopPromise) {
        if (server != null) {
            server.close()
                    .onSuccess(v -> {
                        System.out.println("Server stopped successfully");
                        stopPromise.complete();
                    })
                    .onFailure(stopPromise::fail);
        } else {
            stopPromise.complete();
        }
    }
}

public class PromiseInVerticle {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new MyVericle() , res -> {
            if (res.succeeded()) {
                System.out.println("Deployment successful!");
                vertx.undeploy(res.result(), undeployRes -> {
                    if (undeployRes.succeeded()) {
                        System.out.println("Undeployment successful!");
                    } else {
                        System.out.println("Undeployment failed: " + undeployRes.cause().getMessage());
                    }
                });
            } else {
                System.out.println("Deployment failed: " + res.cause().getMessage());

            }
        });


    }
}


