/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.websockettest;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import org.json.JSONObject;

/**
 *
 * @author Wei.Cheng
 */
public class HostTester {

    private static final int LocalTestPort = 80;
    private static final int DBTestPort = 1433;
    private static final int UnitDBPort = 49845;
    private static JSONObject j = new JSONObject();
    private static JSONObject dbj = new JSONObject();

    private enum Server {

        Way_Chien_MFG_SEVER("172.20.131.52"),
        Way_Chien_TWM3("172.20.131.208"),
        IT_SQL("172.22.2.120"),
        KANBANTEST_SERVER("172.20.131.245");

        Server(String str) {
            this.str = str;
        }
        private final String str;

        @Override
        public String toString() {
            return str;
        }
    }

    public static void main(String args[]) {
        System.out.println("Normal Port:80---------------------------");
        testServer();
        System.out.println(j);
        System.out.println("SQL Port---------------------------");
        testServerDB();
        System.out.println(dbj);
    }

    public static boolean hostAvailabilityCheck(String SERVER_ADDRESS) {
        return hostAvailabilityCheck(SERVER_ADDRESS, LocalTestPort);
    }

    public static boolean hostAvailabilityCheck_DBPort(String SERVER_ADDRESS) {
        return hostAvailabilityCheck(SERVER_ADDRESS, DBTestPort);
    }

    public static boolean hostAvailabilityCheck(String SERVER_ADDRESS, int SERVER_PORT) {
        Socket s = new Socket();
        try {
            s.connect(new InetSocketAddress(SERVER_ADDRESS, SERVER_PORT), 1000);
            s.close();
            return true;
        } catch (IOException ex) {
            /* ignore */
        }
        return false;
    }

    public static int getLocalTestPort() {
        return LocalTestPort;
    }

    public static void testServer() {
        for (Server s : Server.values()) {
            j.put(s.name(), hostAvailabilityCheck(s.toString()));
        }
    }

    public static void testServerDB() {
        dbj.put(Server.IT_SQL.name(), hostAvailabilityCheck(Server.IT_SQL.toString(), DBTestPort));
        dbj.put(Server.Way_Chien_TWM3.name(), hostAvailabilityCheck(Server.Way_Chien_TWM3.toString(), UnitDBPort));
    }
}
