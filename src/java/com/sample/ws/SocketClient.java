/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ws;

import java.io.*;
import java.net.*;

/**
 *
 * @author Wei.Cheng
 */
public class SocketClient {

    private static final int port = 1234;

    public SocketClient() {
        /*連接Server*/
        try {
            Socket socket = new Socket(InetAddress.getLocalHost(), port);

            ObjectInputStream instream = new ObjectInputStream(socket.getInputStream());

            String s[] = (String[]) instream.readObject();
            for (String item : s) {
                System.out.println(item);
            }

            System.out.println();

            String s2[] = (String[]) instream.readObject();
            for (String s21 : s2) {
                System.out.println(s21);
            }

            System.out.println();

        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e);
        }
    }

    public static void main(String arg[]) {
        SocketClient t = new SocketClient();
    }
}
