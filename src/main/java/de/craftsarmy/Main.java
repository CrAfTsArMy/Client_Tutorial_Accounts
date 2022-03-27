package de.craftsarmy;

import de.craftsarmy.server.WebServer;
import de.craftsarmy.utils.Config;

public class Main {

    public static void main(String[] args) {
        Thread shutdownHook = new Thread(() -> {
            WebServer.instance().stop();
            Config.instance().save();
        });
        shutdownHook.setName("Shutdown Hook # 1");
        Runtime.getRuntime().addShutdownHook(shutdownHook);
        Config.instance().load();
        WebServer.instance().start();
    }

}
