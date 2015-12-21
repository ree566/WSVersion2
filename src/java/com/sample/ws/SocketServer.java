/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ws;

import java.net.*;
import java.io.*;
/**
 *
 * @author Wei.Cheng
 */
public class SocketServer {

    private static ServerSocket SS;
    private static final int port = 1234;

    public SocketServer() {
        try {
            SS = new ServerSocket(port);
            System.out.println("Server已啟動，等待使用者連線中...");

            while (true) {
                Socket socket = SS.accept();
                System.out.println("用戶" + socket.getInetAddress().getHostAddress() + "登入");

                ObjectOutputStream outstream = new ObjectOutputStream(socket.getOutputStream());

                String s[] = new String[2];
                s[0] = "aa";
                outstream.writeObject(s);
                
                outstream.reset();

                s[1] = "bb";
                outstream.writeObject(s);

            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public static void main(String[] args) {
        SocketServer t = new SocketServer();
    }
}
