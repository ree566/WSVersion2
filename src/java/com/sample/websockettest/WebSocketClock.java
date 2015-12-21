/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.websockettest;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author Wei.Cheng
 */
@ServerEndpoint("/clock")
public class WebSocketClock {

    static ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
    private static Set<Session> allSessions;

    SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");

    @OnOpen
    public void showTime(final Session session) {
        //System.out.print("Session is open");
        allSessions = session.getOpenSessions();
        // start the scheduler on the very first connection
        // to call sendTimeToAll every second   
        if (allSessions.size() == 1) {
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    sendTimeToAll(session);
                }
            }, 0, 1, TimeUnit.SECONDS);
        }
    }

    private void sendTimeToAll(Session session) {
        allSessions = session.getOpenSessions();
        for (Session sess : allSessions) {
            try {
                sess.getBasicRemote().sendText("Local time: " + timeFormatter.format(new Date()));
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    @OnClose
    public void onClose(final Session session) {
        // remove the session from the set
        allSessions.remove(session);
        //System.out.print("Session is remove");
    }
}
