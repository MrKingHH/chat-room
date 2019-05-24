package com.tftte.chatroom.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

public class ReadDataFromServerThread extends Thread {

    private final Socket socket;

    public ReadDataFromServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        // 客户端读数据
        try {
            InputStream inputStream = this.socket.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            while (true) {
                System.out.println(scanner.nextLine());
                System.out.print("发送消息>>");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
