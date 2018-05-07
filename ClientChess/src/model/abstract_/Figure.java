package model.abstract_;

import java.io.Serializable;

/**
 * Created by kot on 28.03.18.
 */
public abstract class Figure implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean color;

    public Figure(boolean color) {
        this.color = color;
    }

    public boolean isColor() {
        return color;
    }

}
