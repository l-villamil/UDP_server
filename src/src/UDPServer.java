package src;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.io.InputStreamReader;

public class UDPServer {
	private static final int PORT = 9876;
	private static final int TCPPORT = 12345;

    private static final int MAX_CONNECTIONS = 25;
    private static final int THREAD_TIMEOUT_SECONDS = 60;

    public static void main(String[] args) throws Exception {
        // Create server socket and thread pool for UDP
        DatagramSocket serverSocket = new DatagramSocket(PORT);
        // socket for TCP initial connection
        ServerSocket serverSocketTCP = new ServerSocket(TCPPORT);
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                MAX_CONNECTIONS,
                MAX_CONNECTIONS,
                THREAD_TIMEOUT_SECONDS,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(MAX_CONNECTIONS));

        // Loop to accept client connections and select file to transfer
        while (true) {
            // Read input from console to select file to transfer
            String fileName = "";
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Select file to transfer (enter '100' for 100 MB file or '250' for 250 MB file): ");
            String input = consoleInput.readLine();
            if (input.equals("100")) {
                fileName = "D:\\SEPTIMO SEMESTRE\\INFRAESTRUCTURA COMPUTACIONAL\\UDP_server\\src\\src\\archivo100.txt";
            } else if (input.equals("250")) {
                fileName = "file250mb.dat";
            } else {
                System.out.println("Invalid input, please enter '100' or '250'");
                continue;
            }

            // Loop to accept client connections and transfer selected file
            while (true) {
                byte[] receiveData = new byte[1024];
                Socket clientSocket = serverSocketTCP.accept();
                InetAddress clientAddress =clientSocket.getInetAddress();
                int clientPort=12345;

                // Create new FileTransferTask for client connection and submit to thread pool
                FileTransferTask transferTask = new FileTransferTask(serverSocket, clientAddress, clientPort, fileName);
                threadPool.submit(transferTask);

            }
        }
    }

}


