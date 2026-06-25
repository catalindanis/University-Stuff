package ro.mpp2026.festivalmuzicajavafx.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.Socket;

public abstract class AbstractConcurrentServer extends AbstractServer {
    private static Logger logger = LogManager.getLogger(AbstractConcurrentServer.class);

    public AbstractConcurrentServer(int port) { super(port); }

    @Override
    protected void processRequest(Socket client) {
        Thread workerThread = createWorker(client);
        workerThread.start();
    }

    protected abstract Thread createWorker(Socket client);
}
