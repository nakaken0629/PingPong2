package com.itvirtuoso.pingpong2.server;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws Exception {
        try (ServerSocket listener = new ServerSocket()) {
            listener.setReuseAddress(true);
            listener.bind(new InetSocketAddress(5000));
            while (true) {
                try (Socket socket = listener.accept()) {
                    while (true) {
                        InputStream from = socket.getInputStream();
                        int data = from.read();
                        System.out.println(data);
                        if (data == 0) {
                            socket.close();
                            break;
                        }
                    }
                }
            }
        }
    }
}
