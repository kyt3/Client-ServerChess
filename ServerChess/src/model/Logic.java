package model;

import model.abstract_.Figure;
import model.real.*;

import java.util.ArrayList;

/**
 * Created by kot on 28.03.18.
 */
public class Logic {
    private static Figure figureForVirtualMove = null;
    private static Cell previousStartCell = null;
    private static Cell previousEndCell = null;
    private static Figure previousMovedFigure = null;

    static void setPreviousStartCell(Cell previousStartCell) {
        Logic.previousStartCell = previousStartCell;
    }

    static void setPreviousEndCell(Cell previousEndCell) {
        Logic.previousEndCell = previousEndCell;
    }

    static void setPreviousMovedFigure(Figure previousMovedFigure) {
        Logic.previousMovedFigure = previousMovedFigure;
    }

    public static boolean isPossibleMove(Player player, Cell startCell, Cell endCell) {

        //если фигуры нету false
        if (startCell.getFigure() == null) {
            return false;
        }

        //фигура на старте должна совпадать цветом с игроком
        if (player.isColor() != startCell.getFigure().getColor()) {
            return false;
        }

        //клетки не совпадают
        if (startCell == endCell) {
            return false;
        }

        //на конечной клетке не может быть союзной фигуры
        if (endCell.getFigure() != null) {
            if (player.isColor() == endCell.getFigure().getColor()) {
                return false;
            }
        }

        //возможность походить в зависимости от фигуры
        boolean ability = abilityMoveDependsFigure(startCell.getFigure(), startCell, endCell);

        //после хода не должно быть шаха
        if (ability) {
            virtualMove(startCell, endCell);
            if (!isCheck(player)) {
                inverseVirtualMove(endCell, startCell);
                return true;
            }
            inverseVirtualMove(endCell, startCell);
        }
        return false;
    }

    public static boolean isPossibleTakeOnTheAisle(Player player, Cell startCell, Cell endCell) {
        //возможно ли взять на проходе
        Figure figure = startCell.getFigure();
        //проверка на null
        if (figure == null) {
            return false;
        }

        //только пешка может взять на проходе
        if (!(figure instanceof Pawn)) {
            return false;
        }

        //цвета пешки и игрока должны совпадать
        if (player.isColor() != figure.getColor()) {
            return false;
        }

        //предидущий ход должна делать пешка
        if (!(previousMovedFigure instanceof Pawn)) {
            return false;
        }

        //причем на 2 клетки вперед
        if (Math.abs(previousStartCell.getY() - previousEndCell.getY()) != 2) {
            return false;
        }

        //х должен совпадать с х на котором стоит пешка
        if (endCell.getX() != previousEndCell.getX()) {
            return false;
        }

        //пешка бьет по диагонали, соответственно х измениться на 1 в любую сторону
        if (Math.abs(startCell.getX() - endCell.getX()) != 1) {
            return false;
        }

        //в зависимости от цвета пешка не может ходить и бить назад
        //при взятии на проходе y строго определен, чтобы нельзя было бить по 6 и 7 y-ку нужно установить четко
        //что y == 5 для белых и y == 2 для черных
        if (!player.isColor()) {
            if (endCell.getY() != 5) {
                return false;
            }

            if (endCell.getY() - startCell.getY() == 1) {
                return true;
            }
        } else {
            if (endCell.getY() != 2) {
                return true;
            }
            if (startCell.getY() - endCell.getY() == 1) {
                return true;
            }
        }

        return false;
    }

    public static boolean isPossibleCasterling(Player player, Figure king, Cell endCell){
        King king1 = (King) king;
        //цвет короля совпадает с цветом игрока
        if (player.isColor() != king.getColor()) {
            return false;
        }

        //нельзя рокироваться шахе
        if (isCheck(player)) {
            return false;
        }

        //король не должен сделать ход
        if (king1.isWasFirstMove()) {
            return false;
        }

        if (!player.isColor() && endCell.getY() != 0) {
            return false;
        }

        if (player.isColor() && endCell.getY() != 7) {
            return false;
        }

        int y;

        if (!player.isColor()) {
            y = 0;
        } else y = 7;

        if (endCell.getX() == 6) {
            return isPossibleShortCasterling(player, y);
        } else return isPossibleLongCasterling(player, y);
    }

    private static boolean isPossibleShortCasterling(Player player, int y) {
        if (isWasFirstMoveRook(7, y)) {
            return false;
        }

        if (!notExistFiguresBetweenCellsYForRookMove(y, 4, 7)) {
            return false;
        }

        for (int i = 5; i < 7; i++) {
            if (isAttackedCell(player, i, y)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isPossibleLongCasterling(Player player, int y) {
        if (isWasFirstMoveRook(0, y)) {
            return false;
        }

        if (!notExistFiguresBetweenCellsYForRookMove(y, 0, 4)) {
            return false;
        }

        for (int i = 1; i < 4; i++) {
            if (isAttackedCell(player, i, y)) {
                return false;
            }
        }
        return true;
    }

    public static boolean existPawnInTheEndOfChessBoard(boolean color) {
        if (!color) {
            //для белых конец доски по 7-му y
            return existPawnInTheLine(7);
        } else {
            //для черных наоборот по 0-му
            return existPawnInTheLine(0);
        }
    }

    public static boolean isMate(Player player) {
        //нет шаха - нет мата
        if (isCheck(player)) {
            //проверить возможен ли хоть 1 ход, если нет - это мат
            if (isPossibleAtLeastOneMove(player)) {
                return false;
            }

            return true;
        }

        return false;
    }

    public static boolean isStalemate(Player player) {
        return !isPossibleAtLeastOneMove(player);
    }

    private static boolean isCheck(Player player) {
        //найти короля стороны чей ход
        Cell kingCell = getKingCell(player.isColor());

        //угроза чужого цвета?
        ArrayList<Cell> cells = getArrayListOfCellsWithFiguresOfThisColor(!player.isColor());
        for (Cell cell : cells) {
            Figure figure = cell.getFigure();
            if (abilityMoveDependsFigure(figure, cell, kingCell)) {
                return true;
            }
        }

        return false;
    }

    private static Cell getKingCell(boolean color) {
        //найти короля
        Cell[][] cells = ChessBoard.getInstance().getCells();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Cell cell = cells[j][i];
                Figure figure = cell.getFigure();
                if (figure instanceof King && figure.getColor() == color) {
                    return cell;
                }
            }
        }

        return null;
    }

    private static boolean existPawnInTheLine(int y) {
        Cell[][] cells = ChessBoard.getInstance().getCells();

        for (int i = 0; i < 8; i++) {
            if (cells[i][y].getFigure() instanceof Pawn) {
                return true;
            }
        }

        return false;
    }

    private static boolean isAttackedCell(Player player, int x, int y) {
        //метод проверяет может ли вражеская сторона атаковать клетку по х y
        Cell[][] chessBoardCells = ChessBoard.getInstance().getCells();
        //получаем клетки вражеских фигур
        ArrayList<Cell> cells = getArrayListOfCellsWithFiguresOfThisColor(!player.isColor());

        //если хоть 1 вражеская фигура может походить на клетку, значит true
        for (Cell cell : cells) {
            Figure figure = cell.getFigure();
            if (abilityMoveDependsFigure(figure, cell, chessBoardCells[x][y])) {
                return true;
            }
        }

        return false;
    }

    private static ArrayList<Cell> getArrayListOfCellsWithFiguresOfThisColor(boolean color) {
        //возвращает клетки с фигурами определенного цвета
        ArrayList<Cell> resultList = new ArrayList<>();
        Cell[][] cells = ChessBoard.getInstance().getCells();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Cell cell = cells[j][i];
                Figure figure = cell.getFigure();

                if (figure != null && figure.getColor() == color) {
                    resultList.add(cell);
                }
            }
        }

        return resultList;
    }

    private static boolean isWasFirstMoveRook(int x, int y) {
        Rook rook = (Rook) ChessBoard.getInstance().getCells()[x][y].getFigure();
        return rook.isWasFirstMove();
    }

    private static boolean isPossibleAtLeastOneMove(Player player) {
        //возможен ли хоть 1 ход игрока
        Cell[][] chessBoardCells = ChessBoard.getInstance().getCells();

        ArrayList<Cell> cells = getArrayListOfCellsWithFiguresOfThisColor(player.isColor());
        for (Cell startCell : cells) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    Cell endCell = chessBoardCells[j][i];
                    if (isPossibleMove(player, startCell, endCell)) {
                        return true;
                    }
                }
            }

        }

        return false;
    }

    private static void virtualMove(Cell startCell, Cell endCell) {
        //виртуальный ход, для проверки, возможного шаха после хода
        figureForVirtualMove = endCell.getFigure();
        endCell.setFigure(startCell.getFigure());
        startCell.setFigure(null);
    }

    private static void inverseVirtualMove(Cell startCell, Cell endCell) {
        //обратный виртуальный ход, для сохранения позиции на доске после virtualMove
        endCell.setFigure(startCell.getFigure());
        startCell.setFigure(figureForVirtualMove);
        figureForVirtualMove = null;
    }

    private static boolean abilityMoveDependsFigure(Figure figure, Cell startCell, Cell endCell) {
        //проверка возможности хода в зависимости от фигуры

        if (figure instanceof Pawn) {
            return isPossibleMovePawn(startCell, endCell);
        } else if (figure instanceof Rook) {
            return isPossibleMoveRook(startCell, endCell);
        } else if (figure instanceof Knight) {
            return isPossibleMoveKnight(startCell, endCell);
        } else if (figure instanceof Bishop) {
            return isPossibleMoveBishop(startCell, endCell);
        } else if (figure instanceof Queen) {
            return isPossibleMoveQueen(startCell, endCell);
        } else if (figure instanceof King) {
            return isPossibleMoveKing(startCell, endCell);
        }

        return false;
    }

    private static boolean isPossibleMoveBishop(Cell startCell, Cell endCell) {
        //слон ходит по диагонали
        int startX = startCell.getX();
        int startY = startCell.getY();
        int endX = endCell.getX();
        int endY = endCell.getY();

        //значит дистанция по х и по y будет одинаковой
        if (Math.abs(startX - endX) == Math.abs(startY - endY)) {
            ArrayList<Cell> cellsToCheck = getListToCheckForBishop(startX, endX, startY, endY);

            //если в этих клетках есть фигуры - ход невозможен
            return !isExistFiguresInCells(cellsToCheck);

        }

        return false;
    }

    private static ArrayList<Cell> getListToCheckForBishop(int startX, int endX, int startY, int endY) {
        //возвращает диагональ клеток между начальной и конечной клеткой(начало, конец)
        Cell[][] cells = ChessBoard.getInstance().getCells();
        ArrayList<Cell> cellsToCheck = new ArrayList<>();

        //определить максимум и минимум для диапазона
        int maxX = startX > endX ? startX : endX;
        int minX = startX > endX ? endX : startX;
        int maxY = startY > endY ? startY : endY;
        int minY = startY > endY ? endY : startY;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Cell cell = cells[j][i];

                int x = cell.getX();
                int y = cell.getY();

                //проверка лежит ли клетка на диагонали между началом и концом тоесть модуль по х == модулю по y
                //дальше проверяеться лежит ли клетка между началом и концом
                if (Math.abs(startX - x) == Math.abs(startY - y) && x < maxX && x > minX && y < maxY && y > minY) {
                    //если да - добавляем в список
                    cellsToCheck.add(cell);
                }
            }
        }

        return cellsToCheck;

    }

    private static boolean isExistFiguresInCells(ArrayList<Cell> cells) {
        //существуют ли фигуры в списке ячеек
        for (Cell cell : cells) {
            if (cell.getFigure() != null) {
                return true;
            }
        }

        return false;
    }

    private static boolean isPossibleMovePawn(Cell startCell, Cell endCell) {
        //пешка ходит только вперед, бьет по диагонали, может ходить на 2 клетки в начальной позиции
        boolean colorPlayer = startCell.getFigure().getColor();

        int startX = startCell.getX();
        int startY = startCell.getY();
        int endX = endCell.getX();
        int endY = endCell.getY();

        //х-ы должны совпадать для хода
        if (startX == endX) {
            //фигуры в конечной клетке быть не должно
            if (endCell.getFigure() == null) {
                //дистанция должна быть 1 и ходить можно только вперед
                if (!colorPlayer && endY - startY == 1) {
                    return true;
                }

                if (colorPlayer && startY - endY == 1) {
                    return true;
                }

                //ходить на 2 клетки вперед в начальной позиции
                if (!colorPlayer && endY - startY == 2 && startCell.getY() == 1) {
                    return notExistFiguresBetweenCellsXForRookMove(startX, startY, endY);
                }

                if (colorPlayer && startY - endY == 2 && startCell.getY() == 6) {
                    return notExistFiguresBetweenCellsXForRookMove(startX, startY, endY);
                }
            }
        }

        //бить по диагонали на 1 клетку
        if (Math.abs(startX - endX) == 1) {
            if (endCell.getFigure() != null) {
                if (!colorPlayer && endY - startY == 1) {
                    return true;
                }

                if (colorPlayer && startY - endY == 1) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean isPossibleMoveRook(Cell startCell, Cell endCell) {
        //ладья ходит по вертикали или горизонтали
        int startX = startCell.getX();
        int endX = endCell.getX();
        int startY = startCell.getY();
        int endY = endCell.getY();

        //если по верникали
        if (startX == endX) {
            //проверить есть ли между клетками фигуры
            return notExistFiguresBetweenCellsXForRookMove(startX, startY, endY);
        }

        //та же идея
        if (startY == endY) {
            return notExistFiguresBetweenCellsYForRookMove(startY, startX, endX);
        }

        return false;
    }

    private static boolean isPossibleMoveKnight(Cell startCell, Cell endCell) {
        //конь ходит буквой г прыгая через фигуры
        int startX = startCell.getX();
        int startY = startCell.getY();
        int endX = endCell.getX();
        int endY = endCell.getY();

        //если по х расстояние 1, тогда по y расстояние должно быть 2 и наоборот, чтобы получилась Г
        if (Math.abs(startX - endX) == 1 && Math.abs(startY - endY) == 2) {
            return true;
        }

        if (Math.abs(startX - endX) == 2 && Math.abs(startY - endY) == 1) {
            return true;
        }

        return false;
    }

    private static boolean isPossibleMoveQueen(Cell startCell, Cell endCell) {
        //королева может ходить как ладья или как слон
        if (isPossibleMoveRook(startCell, endCell) || isPossibleMoveBishop(startCell, endCell)) {
            return true;
        }

        return false;
    }

    private static boolean isPossibleMoveKing(Cell startCell, Cell endCell) {
        //король может ходить на 1 клетку в любую сторону
        int startX = startCell.getX();
        int endX = endCell.getX();
        int startY = startCell.getY();
        int endY = endCell.getY();

        int distX = Math.abs(startX - endX);
        int distY = Math.abs(startY - endY);

        //расстояние не должно быть > ходьбы по диагонали
        double dist = Math.sqrt(distX * distX + distY * distY);
        if (dist <= Math.sqrt(2)) {
            return true;
        }

        return false;
    }

    private static boolean notExistFiguresBetweenCellsYForRookMove(int y, int startX, int endX) {
        int max;
        int min;
        if (endX > startX) {
            max = endX;
            min = startX;
        } else {
            max = startX;
            min = endX;
        }


        Cell[][] cells = ChessBoard.getInstance().getCells();
        ArrayList<Cell> cellsToCheck = new ArrayList<>();
        //поиск промежуточных клеток
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Cell cell = cells[j][i];

                if (cell.getY() == y && cell.getX() > min && cell.getX() < max) {
                    cellsToCheck.add(cell);
                }
            }
        }
        //проверка есть ли фигура
        if (isExistFiguresInCells(cellsToCheck)) {
            return false;
        }

        return true;
    }

    private static boolean notExistFiguresBetweenCellsXForRookMove(int x, int startY, int endY) {
        int max;
        int min;
        if (endY > startY) {
            max = endY;
            min = startY;
        } else {
            max = startY;
            min = endY;
        }


        Cell[][] cells = ChessBoard.getInstance().getCells();
        ArrayList<Cell> cellsToCheck = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Cell cell = cells[j][i];

                if (cell.getX() == x && cell.getY() > min && cell.getY() < max) {
                    cellsToCheck.add(cell);
                }
            }
        }

        if (isExistFiguresInCells(cellsToCheck)) {
            return false;
        }

        return true;
    }

}
