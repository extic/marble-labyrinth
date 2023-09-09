package org.extremely.marble;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.DriverManager;
import gnu.io.SerialPort;
import org.joml.Math;
import org.joml.Vector3f;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.function.Consumer;

import static java.lang.Float.parseFloat;

public class InputServer {

    private static final int VECTOR_ARRAY_SIZE = 130;
    private final Vector3f[] vectorArray;
    private int vectorArrayIndex = 0;

    public InputServer() {
        this.vectorArray = new Vector3f[VECTOR_ARRAY_SIZE];
        for (int i = 0; i < VECTOR_ARRAY_SIZE; i++) {
            vectorArray[i] = new Vector3f();
        }
    }

    public void run(Consumer<ServerInput> consumer) {
        try {
            connect("COM6", consumer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void connect(String portName, final Consumer<ServerInput> consumer) throws Exception {
        System.setProperty("os.name", "Windows 10");

        DriverManager.getInstance().loadDrivers();

        Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier portIdentifier = portEnum.nextElement();
            System.out.println(portIdentifier.getName());
        }

        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if (portIdentifier.isCurrentlyOwned()) {
            System.out.println("Error: Port is currently in use");
        } else {
            CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);

            if (commPort instanceof SerialPort serialPort) {
                serialPort.setSerialPortParams(115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

                InputStream in = serialPort.getInputStream();
                (new Thread(new SerialReader(in, (text -> process(text, consumer))))).start();
            } else {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }
    }

    private static class SerialReader implements Runnable {
        private final InputStream in;
        private final Consumer<String> processor;

        public SerialReader(InputStream in, Consumer<String> processor) {
            this.in = in;
            this.processor = processor;
        }

        public void run() {
            var command = new StringBuilder();
            int len;
            try {
                while ((len = this.in.read()) > -1) {
                    if (len == '\n') {
                        processor.accept(command.toString());
                        command.setLength(0);
                    } else {
                        command.append((char) len);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void process(String text, Consumer<ServerInput> consumer) {
//        System.out.println(text);
//
        var values = text.trim().split(",");
        try {
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

            var roll = Math.atan2(reduced.y, reduced.z) * 57.3f;
            var pitch = Math.atan2((-reduced.x), Math.sqrt(reduced.y * reduced.y + reduced.z * reduced.z)) * 57.3f;
            var yaw = 0f;

            consumer.accept(new ServerInput(new Vector3f(roll, pitch, yaw), values[4].equals("1")));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public record ServerInput(Vector3f vector, boolean button1Pressed) {}
}
