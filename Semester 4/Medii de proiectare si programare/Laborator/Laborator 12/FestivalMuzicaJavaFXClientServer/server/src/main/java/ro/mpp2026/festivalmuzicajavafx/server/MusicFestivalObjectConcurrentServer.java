package ro.mpp2026.festivalmuzicajavafx.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2026.festivalmuzicajavafx.service.AuthServiceImpl;
import ro.mpp2026.festivalmuzicajavafx.service.ShowsServiceImpl;

import java.net.Socket;

public class MusicFestivalObjectConcurrentServer extends AbstractConcurrentServer {
    private static Logger logger = LogManager.getLogger(MusicFestivalObjectConcurrentServer.class);
    private final AuthServiceImpl authServiceImpl;
    private final ShowsServiceImpl showsServiceImpl;

    public MusicFestivalObjectConcurrentServer(int port, AuthServiceImpl authServiceImpl, ShowsServiceImpl showsServiceImpl) {
        super(port);
        this.authServiceImpl = authServiceImpl;
        this.showsServiceImpl = showsServiceImpl;
    }

    @Override
    protected Thread createWorker(Socket client) {
        MusicFestivalObjectWorker worker = new MusicFestivalObjectWorker(client, authServiceImpl, showsServiceImpl);
        return new Thread(worker);
    }
}
