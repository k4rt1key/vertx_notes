package org.example.SockJs;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions;
import io.vertx.ext.web.handler.sockjs.SockJSSocket;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class ChatServer extends AbstractVerticle {

    private Set<SockJSSocket> connectedSockets = new HashSet<>();

    @Override
    public void start(Promise<Void> startPromise) {
        // Create the HTTP server
        HttpServer server = vertx.createHttpServer();

        // Create a router
        Router router = Router.router(vertx);
        router.route("/static/*").handler(StaticHandler.create());
        // Create SockJS handler options

        SockJSHandlerOptions options = new SockJSHandlerOptions()
                .setHeartbeatInterval(2000);

        // Create the SockJS handler
        SockJSHandler sockJSHandler = SockJSHandler.create(vertx, options);

        // Handle socket connections
        sockJSHandler.socketHandler(socket -> {
            // Add socket to connected clients
            connectedSockets.add(socket);

            // Send welcome message to new client
            String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
            socket.write("Server: Welcome to the chat! " + timestamp);
            broadcastMessage("System: New user connected");

            // Handle incoming messages
            socket.handler(buffer -> {
                String message = buffer.toString();
                broadcastMessage("User: " + message);
            });

            // Handle disconnections
            socket.endHandler(v -> {
                connectedSockets.remove(socket);
                broadcastMessage("System: A user disconnected");
            });
        });

        // Mount the SockJS handler at the /chat endpoint
        router.route("/chat/*").handler(sockJSHandler);

        // Serve static files
        router.route().handler(StaticHandler.create());

        // Start the HTTP server
        server.requestHandler(router)
                .listen(7000, http -> {
                    if (http.succeeded()) {
                        System.out.println("Server started on port 7000");
                        startPromise.complete();
                    } else {
                        System.out.println("Server failed to start: " + http.cause());
                        startPromise.fail(http.cause());
                    }
                });
        
    }

    private void broadcastMessage(String message) {
        String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
        String formattedMessage = message + " [" + timestamp + "]";

        // Send message to all connected sockets
        for (SockJSSocket socket : connectedSockets) {
            socket.write(formattedMessage);
        }
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new ChatServer());
    }
}