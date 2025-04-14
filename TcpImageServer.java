package org.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;

import java.io.FileOutputStream;

public class TcpImageServer extends AbstractVerticle {

    @Override
    public void start() {
        vertx.createNetServer().connectHandler(socket -> {
            System.out.println("Client connected from: " + socket.remoteAddress());

            Buffer totalBuffer = Buffer.buffer();

            socket.handler(buffer -> {
                totalBuffer.appendBuffer(buffer);
            });

            socket.closeHandler(v -> {
                System.out.println("Client disconnected. Saving image...");
                try (FileOutputStream out = new FileOutputStream("received_image.png")) {
                    out.write(totalBuffer.getBytes());
                    System.out.println("Image saved as received_image.png");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }).listen(1234, "0.0.0.0", res -> {
            if (res.succeeded()) {
                System.out.println("🚀 Server is listening on all interfaces (port 1234)");
            } else {
                System.out.println("❌ Failed to start server: " + res.cause());
            }
        });
    }

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(new TcpImageServer());
    }
}

