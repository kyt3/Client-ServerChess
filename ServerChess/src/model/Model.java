package model;

import model.abstract_.Figure;
import model.events.*;
import model.exceptions.ImpossibleMove;
import model.real.*;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by kot on 01.04.18.
 */
public class Model {
    private Player whitePlayer;
    private Player blackPlayer;
    private boolean whoIsMove;
    private ArrayList<ModelChangedListener> modelChangedListeners = new ArrayList<>();
    private ArrayList<GameFinishedListener> gameFinishedListeners = new ArrayList<>();
    private ArrayList<PawnHasComeToEndOfChessBoardListener> pawnHasComeToEndOfChessBoardListeners = new ArrayList<>();
    private ArrayList<NotPossibleMoveListener> notPossibleMoveListeners = new ArrayList<>();
    private ArrayList<ModelChangedWithoutMoveListener> modelChangedWithoutMoveListeners = new ArrayList<>();


    public Model() {
        createChessBoard();

        whoIsMove = false;
        whitePlayer = new Player(false);
        blackPlayer = new Player(true);
    }

    public void doMove(Cell startCell, Cell endCell, boolean clientColor) {
        try {
            if (clientColor != whoIsMove) {
                generateNotPossibleMoveEvent();
                return;
            }

            if (!whoIsMove) {
                gameMove(whitePlayer, startCell, endCell);

            } else {
                gameMove(blackPlayer, startCell, endCell);

            }

            if (Logic.existPawnInTheEndOfChessBoard(whoIsMove)) {
                generatePawnHasComeToEndOfChessBoardEvent(whoIsMove);
            }

            whoIsMove = !whoIsMove;

            generateModelChangedEvent(startCell, endCell);

            if (Logic.isMate(whitePlayer) || Logic.isMate(blackPlayer)
                    || Logic.isStalemate(whitePlayer) || Logic.isStalemate(blackPlayer)) {
                generateGameFinishedEvent();
            }

        } catch (ImpossibleMove impossibleMove) {
            generateNotPossibleMoveEvent();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changePawnToFigure(String nameFigure) {
        Figure figure = getFigureFromString(nameFigure);
        Cell[][] cells = ChessBoard.getInstance().getCells();
        if (!whoIsMove) {
            for (int i = 0; i < 8; i++) {
                if (cells[i][7].getFigure() instanceof Pawn) {
                    cells[i][7].setFigure(figure);
                    generateModelChangedWithoutMoveEvent();
                    return;
                }
            }
        } else {

            for (int i = 0; i < 8; i++) {
                if (cells[i][0].getFigure() instanceof Pawn) {
                    cells[i][0].setFigure(figure);
                    generateModelChangedWithoutMoveEvent();
                    return;
                }
            }
        }
    }

    private Figure getFigureFromString(String nameFigure) {
        switch (nameFigure) {
            case "Rook":
                return new Rook(whoIsMove);
            case "Knight":
                return new Knight(whoIsMove);
            case "Bishop":
                return new Bishop(whoIsMove);
            case "Queen":
                return new Queen(whoIsMove);
        }

        return null;
    }

    private void gameMove(Player player, Cell startCell, Cell endCell) throws ImpossibleMove, IOException {

        //если возможен ход
        //сделать его
        if (Logic.isPossibleMove(player, startCell, endCell)) {
            player.move(startCell, endCell);
            return;

        }

        if (startCell.getFigure() != null) {
            if (startCell.getFigure() instanceof King) {
                if (Logic.isPossibleCasterling(player, startCell.getFigure(), endCell)) {
                    player.casterling(startCell, endCell);
                    return;
                }
            }

            if (startCell.getFigure() instanceof Pawn) {
                if (Logic.isPossibleTakeOnTheAisle(player, startCell, endCell)) {
                    player.takeOnTheAisle(startCell, endCell);
                    return;
                }
            }
        }

        throw new ImpossibleMove();
        //в ином случае
        //исключение ход невозможен
    }


    public void addModelChangedListener(ModelChangedListener listener) {
        modelChangedListeners.add(listener);
    }

    public void addGameFinishedListener(GameFinishedListener listener) {
        gameFinishedListeners.add(listener);
    }

    public void addPawnHasComeToEndOfChessBoardListener(PawnHasComeToEndOfChessBoardListener listener) {
        pawnHasComeToEndOfChessBoardListeners.add(listener);
    }

    public void addNotPossibleMoveListener(NotPossibleMoveListener listener) {
        notPossibleMoveListeners.add(listener);
    }

    public void addModelChangedWithoutMoveListener(ModelChangedWithoutMoveListener listener) {
        modelChangedWithoutMoveListeners.add(listener);
    }

    private void generateGameFinishedEvent() {
        GameFinishedEvent gameFinishedEvent = new GameFinishedEvent(this);
        for (GameFinishedListener gameFinishedListener : gameFinishedListeners) {
            gameFinishedListener.gameFinished(gameFinishedEvent);
        }
    }

    private void generateModelChangedEvent(Cell startCell, Cell endCell) {
        ModelChangedEvent event = new ModelChangedEvent(this);
        for (ModelChangedListener listener : modelChangedListeners) {
            listener.modelChangedActions(event, startCell, endCell);
        }
    }

    private void generateNotPossibleMoveEvent() {
        NotPossibleMoveEvent event = new NotPossibleMoveEvent(this);
        for (NotPossibleMoveListener listener : notPossibleMoveListeners) {
            listener.actionsWhenNotPossibleMove(event);
        }
    }

    private void generatePawnHasComeToEndOfChessBoardEvent(boolean color) {
        for (PawnHasComeToEndOfChessBoardListener listener : pawnHasComeToEndOfChessBoardListeners) {
            listener.changePawnToOtherFigure(new PawnHasComeToEndOfChessBoardEvent(this), color);
        }
    }

    private void generateModelChangedWithoutMoveEvent() {
        for (ModelChangedWithoutMoveListener listener : modelChangedWithoutMoveListeners) {
            listener.modelChangedEventWithoutMove(new ModelChangedWithoutMoveEvent(this));
        }
    }

    private void createChessBoard() {
        Cell[][] cells = createCellsWithFigures();
        ChessBoard chessBoard = ChessBoard.getInstance(cells);
    }

    private Cell[][] createCellsWithFigures() {
        Cell[][] cells = new Cell[8][8];
        for (int i = 0; i < 8; i++) {
            boolean color;
            color = i % 2 == 0;

            for (int j = 0; j < 8; j++) {
                switch (i) {
                    case 0:
                        cells[j][i] = new Cell(j, i, getFigureInPosition(j, false), color);
                        break;
                    case 1:
                        cells[j][i] = new Cell(j, i, new Pawn(false), color);
                        break;
                    case 6:
                        cells[j][i] = new Cell(j, i, new Pawn(true), color);
                        break;
                    case 7:
                        cells[j][i] = new Cell(j, i, getFigureInPosition(j, true), color);
                        break;
                    default:
                        cells[j][i] = new Cell(j, i, color);
                        break;
                }

                color = !color;
            }
        }

        return cells;
    }

    private Figure getFigureInPosition(int position, boolean color) {
        switch (position) {
            case 0:
                return new Rook(color);
            case 1:
                return new Knight(color);
            case 2:
                return new Bishop(color);
            case 3:
                return new Queen(color);
            case 4:
                return new King(color);
            case 5:
                return new Bishop(color);
            case 6:
                return new Knight(color);
            case 7:
                return new Rook(color);
        }

        return null;
    }
}
