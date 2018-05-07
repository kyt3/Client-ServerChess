package model.events;

import model.real.Cell;

/**
 * Created by kot on 01.04.18.
 */
public interface ModelChangedListener {
    void modelChangedActions(ModelChangedEvent event, Cell startCell, Cell endCell);
}
