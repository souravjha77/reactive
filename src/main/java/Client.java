/*
 * Copyright 2019.
 */

import io.grpc.ManagedChannel;
import io.vertx.core.AbstractVerticle;
import io.vertx.grpc.VertxChannelBuilder;
import protos.GreeterGrpc;
import protos.HelloRequest;

import java.nio.charset.Charset;

/**
 * @author Sourav Jha souravj02@gmail.com
 */
public class Client extends AbstractVerticle {

    public static void main(final String[] args) {
        Runner.runExample(Client.class);
    }


    @Override
    public void start() throws Exception {

        // Create the channel
        ManagedChannel channel = VertxChannelBuilder
                .forAddress(vertx, "localhost", 8080)
                .usePlaintext(true)
                .build();

        // Get a stub to use for interacting with the remote service
        GreeterGrpc.GreeterVertxStub stub = GreeterGrpc.newVertxStub(channel);

        // Make a request
        HelloRequest helloRequest = HelloRequest.newBuilder().setName("test").build();

        // Call the remote service
        stub.sayHello(helloRequest, stream -> {
            stream.handler(response -> {
                System.out
                        .println(response.getMessage());
            }).endHandler(v -> {
                System.out.println("Response has ended.");
            });
        });
    }
}
