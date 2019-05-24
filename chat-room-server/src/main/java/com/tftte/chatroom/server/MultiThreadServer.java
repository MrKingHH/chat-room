package com.tftte.chatroom.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadServer {
    public static void main(String[] args) {
        int defaultPort = 23333;
        int defaultThread = 10;
        int port = defaultPort;
        int threadNum = defaultThread;
        for (String arg : args) {
            if (arg.startsWith("--port=")) {
                port = Integer.parseInt(arg.substring("--port=".length()));
            } else if (arg.startsWith("--thread=")) {
                threadNum = Integer.parseInt(arg.substring("--thread=".length()));
            }
        }

        try {
            final ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("服务器启动: "
                    + serverSocket.getInetAddress()
                    + " : "
                    + serverSocket.getLocalPort());

            // 线程池调度器
            final ExecutorService executorService = Executors.newFixedThreadPool(threadNum);

            while (true) {
                final Socket socket = serverSocket.accept();
                executorService.execute(new ClientHandler(socket));
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
