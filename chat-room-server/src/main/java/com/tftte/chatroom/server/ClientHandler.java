package com.tftte.chatroom.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class ClientHandler implements Runnable {

    // 存储所有的注册到服务端的客户端Socket和name
    private static Map<String, Socket> SOCKET_MAPS = new ConcurrentHashMap<>();

    private final Socket client;

    private String name;

    public ClientHandler(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        // 此处服务器socket和客户端socket进行数据传输
        try {
            InputStream inputStream = this.client.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            while (true) {
                String msg = scanner.nextLine();
                if (msg.startsWith("register:")) {
                    // register:<name>
                    String[] segments = msg.split(":");
                    if (segments[0].equals("register")) {
                        String name = segments[1];
                        this.register(name);
                    }
                    continue;
                }
                if (msg.startsWith("group:")) {
                    // group:<message>
                    String[] segments = msg.split(":");
                    group(segments[1]);
                    continue;
                }
                if (msg.startsWith("private:")) {
                    // private:<name>:<message>
                    String[] segments = msg.split(":");
                    Socket socket = SOCKET_MAPS.get(segments[1]);
                    sendMessage(socket, segments[2]);
                    continue;
                }
                if (msg.equals("quit")) {
                    this.quit();
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void group(String str) {
        for (Socket socket : SOCKET_MAPS.values()) {
            if (socket != this.client) {
                sendMessage(socket, str);
            }
        }
    }

    private void quit() {
        SOCKET_MAPS.remove(this.name);
        this.printOnlineClient();
    }

    private void register(String name) {
        // this.client 表示当前客户端连接的Socket
        // this.name 表示当前客户端注册的名称
        this.name = name;
        SOCKET_MAPS.put(name, this.client);
        this.sendMessage(this.client, name + "注册成功!");
        this.printOnlineClient();
    }

    private void printOnlineClient() {
        System.out.println("当前在线用户数: " + SOCKET_MAPS.size());
        System.out.println("名称列表如下: ");
        for (String str : SOCKET_MAPS.keySet()) {
            System.out.println(str);
        }
    }

    private void sendMessage(Socket socket, String msg) {
        try {
            OutputStream outputStream = socket.getOutputStream();
            PrintStream printStream = new PrintStream(outputStream);
            printStream.println("来自" + this.name + "的消息: " + msg);
            printStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
