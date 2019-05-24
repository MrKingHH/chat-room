package com.tftte.chatroom.client;

import java.io.IOException;
import java.net.Socket;

public class MultiThreadClient {
    public static void main(String[] args) {
        String defaultHost = "127.0.0.1";
        int defaultPort = 23333;
        String host = defaultHost;
        int port = defaultPort;

        for (String arg : args) {
            if (arg.startsWith("--port=")) {
                port = Integer.parseInt(arg.substring("--port=".length()));
            } else if (arg.startsWith("--host=")) {
                host = arg.substring("--host=".length());
            }
        }

        try {
            Socket socket = new Socket(host, port);

            // 读
            // 1. 不用理会 Socket关闭
            // 2. 守护线程
            // 3. 中断
            // 4. 标记位
            System.out.println("=============使用说明=============\n" +
                    "1. register:<name> 注册\n" +
                    "2. group:<message> 群聊\n" +
                    "3. private:<name>:<message> 私聊\n" +
                    "4. quit 退出聊天室");

            new ReadDataFromServerThread(socket).start();

            // 写
            new WriteDataToServerThread(socket).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
