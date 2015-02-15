package com.itvirtuoso.pingpong2.server;

import com.itvirtuoso.pingpong2.server.model.SocketPlayer;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Server {
    private static final Logger sLogger = Logger.getLogger(Server.class.getName());

    public static void main(String[] args) throws Exception {
        sLogger.info("start pingpong2 server");

        try (ServerSocket listener = new ServerSocket()) {
            listener.setReuseAddress(true);
            listener.bind(new InetSocketAddress(5000));
            while (true) {
                SocketPlayer player = new SocketPlayer(listener.accept());
                Executor executor = Executors.newSingleThreadExecutor();
                executor.execute(player);
            }
        }
    }
}
