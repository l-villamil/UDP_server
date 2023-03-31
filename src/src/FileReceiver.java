package src;

import java.net.DatagramSocket;
import java.net.Socket;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import java.io.*;
import java.net.*;

public class FileReceiver {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        FileOutputStream fos = null;
        String fileName = "D:\\SEPTIMO SEMESTRE\\INFRAESTRUCTURA COMPUTACIONAL\\UDP_server\\src\\src\\prueba.txt";

        try {
            // crear el socket UDP
            socket = new DatagramSocket(12345);
            
            // crear un buffer para recibir los paquetes
            byte[] buffer = new byte[1024];

            // crear un paquete para recibir los datos
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            
            
            DatagramPacket packet1 = new DatagramPacket(fileName.getBytes(), fileName.getBytes().length, InetAddress.getLocalHost(), 9876);
            socket.send(packet1);
            // recibir el nombre del archivo
            socket.receive(packet);
            String filename = new String(packet.getData(), 0, packet.getLength());

            // crear el archivo de destino
            File file = new File(filename);
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
                fos.write(packet.getData(), 0, packet.getLength());
            }

            // cerrar el archivo
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

