package com.tftte.chatroom.client;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class WriteDataToServerThread extends Thread {

    private final Socket socket;

    public WriteDataToServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try {
            OutputStream outputStream = this.socket.getOutputStream();
            PrintStream printStream = new PrintStream(outputStream);
            // 从键盘读入数据
            Scanner scanner = new Scanner(System.in);
            System.out.print("发送消息>>");
            while (true) {
                String msg = scanner.nextLine();
                printStream.println(msg);
                printStream.flush();
                if ("quit".equals(msg)) {
                    break;
                }
                System.out.print("发送消息>>");
            }
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
