package server;

import model.events.*;
import model.Model;
import model.real.Cell;
import model.real.ChessBoard;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by kot on 09.04.18.
 */
public class ClientThread extends Thread implements GameFinishedListener, ModelChangedListener,
        NotPossibleMoveListener, PawnHasComeToEndOfChessBoardListener, ModelChangedWithoutMoveListener {

    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Model model;
    private boolean clientColor;

    public ClientThread(Socket socket, Model model, boolean clientColor) throws IOException {
        this.socket = socket;
        this.model = model;
        this.clientColor = clientColor;
        inputStream = new ObjectInputStream(socket.getInputStream());
        outputStream = new ObjectOutputStream(socket.getOutputStream());

        outputStream.writeObject(clientColor);
        outputStream.flush();
        outputStream.reset();

        sendChessBoardCells();
    }

    @Override
    public void run() {
        while (socket.isConnected()) {
            try {
                Cell startCell = (Cell) inputStream.readObject();
                Cell endCell = (Cell) inputStream.readObject();
                Cell startServerCell = getCellFromServerChessBoard(startCell);
                Cell endServerCell = getCellFromServerChessBoard(endCell);


                model.doMove(startServerCell, endServerCell, clientColor);

                for (ClientThread clientThread : Server.clientThreads) {
                    clientThread.sendChessBoardCells();
                }

            } catch (IOException e) {

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public Cell getCellFromServerChessBoard(Cell cell) {
        return ChessBoard.getInstance().getCells()[cell.getX()][cell.getY()];
    }

    public void sendChessBoardCells() throws IOException {
        ChessBoard chessBoard = ChessBoard.getInstance();
        outputStream.writeObject(chessBoard.getCells());
        outputStream.flush();

        outputStream.reset();
    }

    private void transmissionToClient(Object event) {
        try {
            outputStream.writeObject(event);
            outputStream.writeObject(ChessBoard.getInstance().getCells());


            outputStream.flush();
            outputStream.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gameFinished(GameFinishedEvent event) {
        transmissionToClient(event);
    }

    @Override
    public void modelChangedActions(ModelChangedEvent event, Cell startCell, Cell endCell) {
        transmissionToClient(event);
        try {
            outputStream.writeObject(startCell);
            outputStream.writeObject(endCell);

            outputStream.flush();
            outputStream.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionsWhenNotPossibleMove(NotPossibleMoveEvent event) {
        transmissionToClient(event);
    }

    @Override
    public void changePawnToOtherFigure(PawnHasComeToEndOfChessBoardEvent event, boolean color) {
        if (clientColor != color) {
            return;
        }
        transmissionToClient(event);
        try {
            outputStream.writeObject(color);

            outputStream.flush();
            outputStream.reset();

            String nameFigure = inputStream.readUTF();

            model.changePawnToFigure(nameFigure);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modelChangedEventWithoutMove(ModelChangedWithoutMoveEvent event) {
        transmissionToClient(event);
    }
}
