package ro.mpp2026.festivalmuzicajavafx.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2026.festivalmuzicajavafx.service.Service;

import java.net.Socket;

public class MusicFestivalObjectConcurrentServer extends AbstractConcurrentServer {
    private static Logger logger = LogManager.getLogger(MusicFestivalObjectConcurrentServer.class);
    private final Service service;

    public MusicFestivalObjectConcurrentServer(int port, Service service) {
        super(port);
        this.service = service;
    }

    @Override
    protected Thread createWorker(Socket client) {
        MusicFestivalObjectWorker worker = new MusicFestivalObjectWorker(client, service);
        return new Thread(worker);
    }
}
