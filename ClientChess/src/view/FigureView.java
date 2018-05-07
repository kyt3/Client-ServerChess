package view;

import model.abstract_.Figure;
import model.real.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by kot on 05.04.18.
 */
public class FigureView extends JComponent{
    private BufferedImage bufferedImage;
    private Figure figure;

    public FigureView(Figure figure) throws IOException {
        this.figure = figure;

        if (figure instanceof Pawn) {
            if (figure.isColor()) {
                bufferedImage = ImageIO.read(new File("./ChessImg/blackPawn.png"));
            } else {
                bufferedImage = ImageIO.read(new File("./ChessImg/whitePawn.png"));
            }


        } else if (figure instanceof Rook) {
            if (figure.isColor()) {
                bufferedImage = ImageIO.read(new File("./ChessImg/blackRook.png"));
            } else {
                bufferedImage = ImageIO.read(new File("./ChessImg/whiteRook.png"));
            }


        } else if (figure instanceof Knight) {
            if (figure.isColor()) {
                bufferedImage = ImageIO.read(new File("./ChessImg/blackKnight.png"));
            } else {
                bufferedImage = ImageIO.read(new File("./ChessImg/whiteKnight.png"));
            }


        } else if (figure instanceof Bishop) {
            if (figure.isColor()) {
                bufferedImage = ImageIO.read(new File("./ChessImg/blackBishop.png"));
            } else {
                bufferedImage = ImageIO.read(new File("./ChessImg/whiteBishop.png"));
            }


        } else if (figure instanceof Queen) {
            if (figure.isColor()) {
                bufferedImage = ImageIO.read(new File("./ChessImg/blackQueen.png"));
            } else {
                bufferedImage = ImageIO.read(new File("./ChessImg/whiteQueen.png"));
            }


        } else if (figure instanceof King) {
            if (figure.isColor()) {
                bufferedImage = ImageIO.read(new File("./ChessImg/blackKing.png"));
            } else {
                bufferedImage = ImageIO.read(new File("./ChessImg/whiteKing.png"));
            }
        }
    }

    @Override
    public void paintComponent(Graphics graphics) {
        int width = getParent().getSize().width;
        int height = getParent().getSize().height;
        int positionWidthOfImage = 0;
        int positionHeightOfImage = 0;
        int widthOfImage = 0;
        int heightOfImage = 0;

        if (figure instanceof Pawn) {
            positionWidthOfImage = width / 3;
            positionHeightOfImage = height / 4;
            widthOfImage = (int) (width / 2.5);
            heightOfImage = (int) (height / 1.8);

        } else if (figure instanceof Rook) {
            positionWidthOfImage = (int) (width / 6.2);
            positionHeightOfImage = 0;
            widthOfImage = (int) (width / 1.8);
            heightOfImage = height;

        } else if (figure instanceof Knight) {
            positionWidthOfImage = width / 4;
            positionHeightOfImage = height / 6;
            widthOfImage = (int) (width / 1.8);
            heightOfImage = (int) (height / 1.3);


        } else if (figure instanceof Bishop) {
            positionWidthOfImage = width / 4;
            positionHeightOfImage = height / 6;
            widthOfImage = (int) (width / 2.5);
            heightOfImage = (int) (height / 1.3);

        } else if (figure instanceof Queen) {
            positionWidthOfImage = width / 6;
            positionHeightOfImage = height / 6;
            widthOfImage = (int) (width / 1.2);
            heightOfImage = (int) (height / 1.3);


        } else if (figure instanceof King) {
            positionWidthOfImage = width / 6;
            positionHeightOfImage = height / 6;
            widthOfImage = (int) (width / 1.2);
            heightOfImage = (int) (height / 1.3);

        }

        graphics.drawImage(bufferedImage, positionWidthOfImage, positionHeightOfImage, widthOfImage, heightOfImage, null);
        super.paintComponent(graphics);
    }
}
