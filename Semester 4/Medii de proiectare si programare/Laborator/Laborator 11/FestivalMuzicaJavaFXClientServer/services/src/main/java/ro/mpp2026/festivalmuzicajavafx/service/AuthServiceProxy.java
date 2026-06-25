package ro.mpp2026.festivalmuzicajavafx.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2026.festivalmuzicajavafx.domain.User;
import ro.mpp2026.festivalmuzicajavafx.dto.UserRequestDTO;
import ro.mpp2026.festivalmuzicajavafx.dto.UserResponseDTO;
import ro.mpp2026.festivalmuzicajavafx.network.*;
import ro.mpp2026.festivalmuzicajavafx.utils.Observer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AuthServiceProxy implements AuthService {
    private final int port;
    private final String host;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private static final Logger logger = LogManager.getLogger(AuthServiceProxy.class);

    private final BlockingQueue<Response> responses;
    private volatile boolean finished;

    public AuthServiceProxy(int port, String host) {
        this.port = port;
        this.host = host;

        responses = new LinkedBlockingQueue<>();
    }

    @Override
    public User login(String email, String password, Observer client) {
        initializeConnection();
        UserRequestDTO userRequestDTO = new UserRequestDTO(email, password);
        sendRequest(new LoginRequest(userRequestDTO));
        Response response = readResponse();
        if (response instanceof OkResponse okResponse){
            Map<String, Object> data = okResponse.getData();
            UserResponseDTO userResponseDTO = (UserResponseDTO) data.get("user");
            return new User(
                    userResponseDTO.id(),
                    "",
                    ""
            );
        }
        if (response instanceof ErrorResponse err){
            closeConnection();
            throw new RuntimeException(err.message());
        }

        return null;
    }

    @Override
    public void register(String email, String password) {
        initializeConnection();
        UserRequestDTO userRequestDTO = new UserRequestDTO(email, password);
        sendRequest(new RegisterRequest(userRequestDTO));
        Response response = readResponse();
        if (response instanceof OkResponse okResponse){
            return;
        }
        if (response instanceof ErrorResponse err){
            closeConnection();
            throw new RuntimeException(err.message());
        }
    }

    @Override
    public void logout(Long userId, Observer client) {
        initializeConnection();
        sendRequest(new LogoutRequest(userId));
        Response response = readResponse();

        if (response instanceof ErrorResponse err){
            closeConnection();
            throw new RuntimeException(err.message());
        }
    }

    private void closeConnection() {
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            logger.error(e);
            logger.error(e.getStackTrace());
        }
    }

    private void sendRequest(Request request) {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException("Error sending object " + e);
        }
    }

    private Response readResponse() {
        Response response = null;

        try{
            response = responses.take();
        } catch (InterruptedException e) {
            logger.error(e);
            logger.error(e.getStackTrace());
        }

        return response;
    }
    private void initializeConnection() {
        if(connection != null && !connection.isClosed())
            return;

        try {
            connection=new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (IOException e) {
            logger.error(e);
            logger.error(e.getStackTrace());
        }
    }
    private void startReader(){
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    Object response = input.readObject();
                    logger.debug("response received {}",response);
                    try {
                        if(response instanceof UpdateResponse)
                            continue;

                        responses.put((Response) response);
                    } catch (InterruptedException e) {
                        logger.error(e);
                        logger.error(e.getStackTrace());
                    }
                } catch (IOException | ClassNotFoundException e) {
                    logger.error("Reading error {}", e);
                }
            }
        }
    }
}
