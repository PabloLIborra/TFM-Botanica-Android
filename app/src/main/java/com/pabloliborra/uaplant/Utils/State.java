package com.pabloliborra.uaplant.Utils;

import androidx.room.Ignore;

import java.io.Serializable;

public enum State implements Serializable {
    IN_PROGRESS(0),
    AVAILABLE(1),
    COMPLETE(2),
    INACTIVE(3);

    private int code;

    State(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
