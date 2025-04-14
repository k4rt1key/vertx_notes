package org.example;

import io.netty.handler.logging.ByteBufFormat;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.*;

class Server extends AbstractVerticle {
    @Override
    public void start() {
        NetServerOptions options = new NetServerOptions()
                .setHost("localhost")
                .setPort(5000)
                .setReuseAddress(true)
                .setLogActivity(true)
                .setActivityLogDataFormat(ByteBufFormat.SIMPLE);

        NetServer server = vertx.createNetServer(options);

        server.connectHandler(socket -> {
            System.out.println("Client connected!");

            socket.handler(buff -> {
                System.out.println("Server received: " + buff.toString());

                socket.write(Buffer.buffer("Server says: Got your message!"));
            });

            vertx.setPeriodic(3000, id -> {
                socket.write(Buffer.buffer("Server: How are you?"));
            });

            socket.closeHandler(v -> System.out.println("Client disconnected"));
            socket.exceptionHandler(err -> System.out.println("Socket error: " + err.getMessage()));
        });

        server.listen().onComplete(result -> {
            if (result.succeeded()) {
                System.out.println("Server started on port 5000");
            } else {
                System.out.println("Server failed to start: " + result.cause().getMessage());
            }
        });
    }
}

class Client extends AbstractVerticle {
    @Override
    public void start() {
        NetClientOptions options = new NetClientOptions()
                .setConnectTimeout(10000)
                .setLogActivity(true)
                .setActivityLogDataFormat(ByteBufFormat.SIMPLE);

        NetClient client = vertx.createNetClient(options);

        System.out.println("Connecting to server...");
        client.connect(5000, "localhost").onComplete(res -> {
            if (res.succeeded()) {
                System.out.println("Connected to server!");

                NetSocket socket = res.result();

                // Read messages from the server
                socket.handler(buff -> {
                    System.out.println("Client received: " + buff.toString());
                });

                vertx.setPeriodic(5000, id -> {
                    socket.write("Client: Hello Server!");
                });

            }
            else
            {
                System.out.println("Failed to connect: " + res.cause().getMessage());
            }
        });
    }
}

public class TCPServer {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new Server(), res -> {
            if (res.succeeded()) {
                System.out.println("Server deployed successfully!");

                vertx.deployVerticle(new Client(), clientRes -> {
                    if (clientRes.succeeded()) {
                        System.out.println("Client deployed successfully!");
                    } else {
                        System.out.println("Failed to deploy client: " + clientRes.cause().getMessage());
                    }
                });


            } else {
                System.out.println("Failed to deploy server: " + res.cause().getMessage());
            }
        });
    }
}
