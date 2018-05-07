package client;

import controller.Controller;
import model.events.*;
import model.real.Cell;
import model.real.ChessBoard;
import view.View;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by kot on 09.04.18.
 */
public class Client {
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private View view;
    private boolean color;

    public Client() throws IOException, ClassNotFoundException {
        InetAddress inetAddress = InetAddress.getByName("localhost");//192.168.137.14
        socket = new Socket(inetAddress, 2000);
        output = new ObjectOutputStream(socket.getOutputStream());
        input = new ObjectInputStream(socket.getInputStream());

        color = (Boolean) input.readObject();

        Cell[][] cells = (Cell[][]) input.readObject();
        ChessBoard.getInstance(cells);
    }

    public boolean isColor() {
        return color;
    }

    public void doMove(Cell startCell, Cell endCell) throws IOException {
        output.writeObject(startCell);
        output.writeObject(endCell);
        output.flush();

        output.reset();
    }

    public void startReceiving() throws IOException, ClassNotFoundException {
        while (socket.isConnected()) {
            Object object = input.readObject();

            if (object instanceof GameFinishedEvent) {
                GameFinishedEvent event = (GameFinishedEvent) object;
                receiveServerChessBoard();
                view.gameFinished(event);
            }

            if (object instanceof ModelChangedEvent) {
                ModelChangedEvent event = (ModelChangedEvent) object;
                receiveServerChessBoard();
                Cell startCell = (Cell) input.readObject();
                Cell endCell = (Cell) input.readObject();
                view.modelChangedActions(event, startCell, endCell);

            }

            if (object instanceof NotPossibleMoveEvent) {
                NotPossibleMoveEvent event = (NotPossibleMoveEvent) object;
                receiveServerChessBoard();
                view.actionsWhenNotPossibleMove(event);
            }

            if (object instanceof PawnHasComeToEndOfChessBoardEvent) {
                PawnHasComeToEndOfChessBoardEvent event = (PawnHasComeToEndOfChessBoardEvent) object;
                receiveServerChessBoard();
                boolean clientColor = (Boolean) input.readObject();
                view.changePawnToOtherFigure(event, clientColor);
            }

            if (object instanceof ModelChangedWithoutMoveEvent) {
//                    ModelChangedWithoutMoveEvent eventWithoutMove = (ModelChangedWithoutMoveEvent) object;
                receiveServerChessBoard();
//                    view.modelChangedEventWithoutMove(eventWithoutMove);
            }
        }
    }

    private void receiveServerChessBoard() throws IOException, ClassNotFoundException {
        Cell[][] cells = (Cell[][]) input.readObject();
        ChessBoard.getInstance().setCells(cells);
    }

    public void changePawnToFigure(String nameFigure) {
        try {
            output.writeUTF(nameFigure);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Client client = new Client();
        client.view = new View(client.color);
        Controller.getInstance(client, client.view);

        Thread thread = new Thread(() -> {
            try {
                client.startReceiving();
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    client.closeStreams();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        thread.start();

    }

    private void closeStreams() throws IOException {
        socket.close();
        input.close();
        output.close();
    }
}
