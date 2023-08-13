package org.extremely.marble;

import org.joml.Math;
import org.joml.Vector3f;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.function.Consumer;

import static java.lang.Float.parseFloat;

public class InputServer {
    public void run(Consumer<Vector3f> consumer) {
        var serverThread = new Thread(() -> {
            try (var server = new ServerSocket(9999)) {
                System.out.println("Input server is running on port " + server.getLocalPort());

                while (true) {
                    var client = server.accept();
                    System.out.println("Client connected: " + client.getInetAddress().getHostAddress());

                    spawnClientThread(client, consumer);
                }
            } catch (Exception e) {
                throw new RuntimeException("Cannot listen for game pad", e);
            }
        });
        serverThread.setName("Input Server Thread");
        serverThread.setDaemon(true);
        serverThread.start();
    }

    private void spawnClientThread(Socket clientSocket, Consumer<Vector3f> consumer) {
        try {
            var handler = new InputClientHandler(consumer, clientSocket);
            var thread = new Thread(handler::run);
            thread.setName("Input Thread");
            thread.setDaemon(true);
            thread.start();
        } catch (Exception e) {
            throw new RuntimeException("Could not create input client handler", e);
        }
    }

    private class InputClientHandler {
        private static final int VECTOR_ARRAY_SIZE = 130;
        private final Consumer<Vector3f> consumer;
        private final Socket clientSocket;
        private final Scanner reader;
        private final Vector3f[] vectorArray;

        private boolean running = false;
        private int vectorArrayIndex = 0;

        public InputClientHandler(Consumer<Vector3f> consumer, Socket clientSocket) throws Exception {
            this.consumer = consumer;
            this.clientSocket = clientSocket;
            this.reader = new Scanner(clientSocket.getInputStream());
            this.vectorArray = new Vector3f[VECTOR_ARRAY_SIZE];
            for (int i = 0; i < VECTOR_ARRAY_SIZE; i++) {
                vectorArray[i] = new Vector3f();
            }
        }

        public void run() {
            running = true;
            try {
                while (running) {
                    var text = reader.nextLine();
                    var values = text.split(",");

                    var inVec = new Vector3f(parseFloat(values[0]), parseFloat(values[1]), parseFloat(values[2])).normalize();

                    vectorArray[vectorArrayIndex++] = inVec;
                    if (vectorArrayIndex == VECTOR_ARRAY_SIZE) {
                        vectorArrayIndex = 0;
                    }

                    var reduced = new Vector3f();
                    for (int i = 0; i < VECTOR_ARRAY_SIZE; i++) {
                        reduced.add(vectorArray[i]);
                    }
                    reduced.div(VECTOR_ARRAY_SIZE);
                    reduced.normalize();

//                    consumer.accept(reduced);

                    var roll = Math.atan2(reduced.y, reduced.z) * 57.3f;
                    var pitch = Math.atan2((-reduced.x) , Math.sqrt(reduced.y * reduced.y + reduced.z * reduced.z)) * 57.3f;
                    var yaw = 0f;

                    consumer.accept(new Vector3f(roll, pitch, yaw));


                    //                val gameLogic = engine.logic as GameLogic
                    ////                gameLogic.entity.rotation.x = rotatedVector.x
                    ////                gameLogic.entity.rotation.y = rotatedVector.y
                    ////                gameLogic.entity.rotation.z = rotatedVector.z
                    //                gameLogic.entity.rotation.x = Math.toRadians(-pitch)
                    //                gameLogic.entity.rotation.y = Math.toRadians(roll)
                    //                gameLogic.entity.rotation.z = Math.toRadians(yaw)
                }
            } catch (Exception e) {
                shutdown();
            }
        }

        private void shutdown(){
            running = false;
            try {
                clientSocket.close();
            } catch (Exception e) {
                System.out.println("Cannot gracefully close client socket");
            }
            System.out.println("Client closed connection, ip=" + clientSocket.getInetAddress().getHostAddress());
        }
    }
}
