package com.pabloliborra.uaplant.Routes;

import com.pabloliborra.uaplant.Utils.State;

public class Route {

    private String title;
    private String description;
    private State state;

    public Route(String title, String description, State state) {
        this.title = title;
        this.description = description;
        this.state = state;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public State getState() {
        return this.state;
    }
}
