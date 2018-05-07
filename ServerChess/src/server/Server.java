package server;

import model.Model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by kot on 09.04.18.
 */
public class Server {
    private ServerSocket serverSocket;
    public static ArrayList<ClientThread> clientThreads = new ArrayList<ClientThread>(2);

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(2000);
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        Model model = new Model();

        Socket socket = server.serverSocket.accept();
        System.out.println("connect");
        ClientThread clientThread = new ClientThread(socket, model, false);
        clientThreads.add(clientThread);
        addListenerToModel(model, clientThread);

        socket = server.serverSocket.accept();
        System.out.println("connect");
        clientThread = new ClientThread(socket, model, true);
        clientThreads.add(clientThread);
        addListenerToModel(model, clientThread);

        clientThreads.get(0).start();
        clientThreads.get(1).start();

    }

    private static void addListenerToModel(Model model, ClientThread clientThread) {
        model.addGameFinishedListener(clientThread);
        model.addModelChangedListener(clientThread);
        model.addNotPossibleMoveListener(clientThread);
        model.addPawnHasComeToEndOfChessBoardListener(clientThread);
        model.addModelChangedWithoutMoveListener(clientThread);
    }
}
