package model.events;

import java.util.EventObject;

/**
 * Created by kot on 01.04.18.
 */
public class ModelChangedEvent extends EventObject {
    public ModelChangedEvent(Object o) {
        super(o);
    }
}
