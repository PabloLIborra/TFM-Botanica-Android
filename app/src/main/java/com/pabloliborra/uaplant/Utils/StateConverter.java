package com.pabloliborra.uaplant.Utils;

import androidx.room.TypeConverter;

public class StateConverter {
    @TypeConverter
    public static State toState(int state) {
        if (state == State.IN_PROGRESS.getCode()) {
            return State.IN_PROGRESS;
        } else if (state == State.AVAILABLE.getCode()) {
            return State.AVAILABLE;
        } else if (state == State.COMPLETE.getCode()) {
            return State.COMPLETE;
        } else if (state == State.INACTIVE.getCode()) {
            return State.INACTIVE;
        } else {
            throw new IllegalArgumentException("Could not recognize state");
        }
    }

    @TypeConverter
    public static Integer toInteger(State state) {
        return state.getCode();
    }
}
