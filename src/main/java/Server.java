/*
 * Copyright 2019.
 */

import io.vertx.core.AbstractVerticle;
import io.vertx.grpc.VertxServer;
import io.vertx.grpc.VertxServerBuilder;
import protos.GreeterGrpc;
import protos.HelloReply;

import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 * @author Sourav Jha souravj02@gmail.com
 */
public class Server extends AbstractVerticle {


    public static void main(final String[] args) {
       Runner.runExample(Server.class);
    }

    @Override
    public void start() throws Exception {

        // The rcp service
        GreeterGrpc.GreeterVertxImplBase service =
                new GreeterGrpc.GreeterVertxImplBase() {
                    @Override
                    public void sayHello(protos.HelloRequest request,
                                         io.vertx.grpc.GrpcWriteStream<protos.HelloReply> response) {

                        for (int i = 0; i < 2; ++i) {
                            response.write(HelloReply.newBuilder()
                                    .setMessage("Got-" + System.nanoTime()).build());
                        }
                        response.end();
                    }
                };

        // Create the server
        VertxServer rpcServer = VertxServerBuilder
                .forPort(vertx, 8080)
                .addService(service)
                .build();

        // start the server
        rpcServer.start(ar -> {
            if (ar.failed()) {
                ar.cause().printStackTrace();
            }
        });
    }
}
