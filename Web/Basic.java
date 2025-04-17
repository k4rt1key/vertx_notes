package org.example.Web;

import io.vertx.core.Vertx;
import io.vertx.core.http.Cookie;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

import static javax.swing.UIManager.put;

public class Basic
{
    public static void main(String[] args)
    {
        Router router = Router.router(Vertx.vertx());

        router
                .route(HttpMethod.GET, "/some/path")
                .handler(ctx -> {
                    int a = 10;
                    int b = 20;

                    int sum = a + b;

                    ctx
                            .response()
                            .putHeader("content-type", "text/plain")
                            .end(new JsonObject().put("sum", sum).toBuffer());

                });


        HttpServer httpServer = Vertx.vertx().createHttpServer();
        httpServer
                .requestHandler(router)
                .listen(8080, result -> {
                    if (result.succeeded()) {
                        System.out.println("Server started on port 8080");
                    } else {
                        System.out.println("Failed to start server: " + result.cause());
                    }
                });

    }
}
