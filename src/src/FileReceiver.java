package src;

import java.net.DatagramSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import java.io.*;
import java.net.*;

public class FileReceiver {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        FileOutputStream fos = null;
        final String SERVER_ADDRESS = "localhost";
        try {
            // crear el socket UDP
            socket = new DatagramSocket(12345);
            Socket socketTCP = new Socket(SERVER_ADDRESS, 12345);
            // Get the output stream from the socket
            OutputStream outputStream = socketTCP.getOutputStream();

            // Send data to the server
            String message = "Hello, server!";
            outputStream.write(message.getBytes());

            // Close the output stream and socket
            outputStream.close();
            socketTCP.close();

            // crear un buffer para recibir los paquetes
            byte[] buffer = new byte[1024];

            // crear un paquete para recibir los datos
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            // crear un archivo de registro
            File logFile = new File("received_packets.log");
            BufferedWriter writer = new BufferedWriter(new FileWriter(logFile));

//            // recibir el nombre del archivo
//            socket.receive(packet);
//            String filename = new String(packet.getData(), 0, packet.getLength());
            File file = new File("recibido");
            fos = new FileOutputStream(file);

            // recibir el archivo
            while (true) {
                // recibir un paquete
                socket.receive(packet);

                // si el paquete es una señal de fin, salir del bucle
                if (new String(packet.getData(), 0, packet.getLength()).equals("FIN")) {
                    break;
                }

                // escribir los datos del paquete en el archivo
                String packetData = new String(packet.getData(), 0, packet.getLength());
                fos.write(packet.getData(), 0, packet.getLength());
                
                // escribir los datos del paquete en el archivo de registro
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
                writer.write("Se ha recibido el paquete: "+packetData+" a las"+dateFormat.format(new Date())+
                		" del servidor: "+packet.getAddress()+" de tamaño: "+packet.getLength());
                writer.newLine();
            }

            // cerrar el archivo de registro y el archivo
            writer.close();

            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // cerrar el socket
            if (socket != null) {
                socket.close();
            }
        }
    }
}
