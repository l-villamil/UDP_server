package src;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileTransferTask implements Runnable {
    private DatagramSocket socket;
    private DatagramPacket receivePacket;
    private InetAddress clientAddress;
    private int clientPort;
    private String fileName;

    public FileTransferTask(DatagramSocket socket, InetAddress clientAddress, int clientPort, String fileName) {
        this.socket = socket;
        this.clientAddress = clientAddress;
        this.clientPort = clientPort;
        this.fileName = fileName;
    }

    @Override
    public void run() {
        try {
            // Open input stream for selected file
            File file = new File(fileName);
            FileInputStream fileInput = new FileInputStream(file);
            BufferedInputStream bufferedInput = new BufferedInputStream(fileInput);

            // Send file to client in chunks
            byte[] sendData = new byte[1024];
            int bytesRead;
            while ((bytesRead = bufferedInput.read(sendData, 0, sendData.length)) != -1) {
                DatagramPacket sendPacket = new DatagramPacket(sendData, bytesRead, clientAddress, clientPort);
                socket.send(sendPacket);
            }	
            // Close input stream and socket
//            bufferedInput.close();
//            socket.close();

            // Write log entry for file transfer
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            String logFileName = "Logs/Test_" + dateFormat.format(new Date()) + ".log";
            File logFile = new File(logFileName);
            logFile.getParentFile().mkdirs();
            FileWriter logWriter = new FileWriter(logFile, true);
            BufferedWriter bufferedWriter = new BufferedWriter(logWriter);
            String logEntry = receivePacket.getAddress().getHostAddress() + ":" + receivePacket.getPort() + " - " + fileName + " - " + new Date() + "\n";
            bufferedWriter.write(logEntry);
            bufferedWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

