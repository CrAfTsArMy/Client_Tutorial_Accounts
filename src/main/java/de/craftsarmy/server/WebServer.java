package de.craftsarmy.server;

import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

public class WebServer {

    private boolean started = false;

    private HttpServer server;

    public void start() {
        try {
            if (!started) {
                System.out.println("WebServer is starting...");
                server = HttpServer.create(new InetSocketAddress(7080), 0);

                server.createContext("/account").setHandler(new AccountServiceContext());

                server.setExecutor(null);
                server.start();
                System.out.println("WebServer is now listening at *:7080");
                started = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            if (started) {
                System.out.println("Stopping WebServer...");
                server.stop(0);
                server = null;
                System.out.println("WebServer has been stopped");
                started = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static WebServer instance;

    public static WebServer instance() {
        if (instance == null)
            instance = new WebServer();
        return instance;
    }

}
