package com.gabitosoft.model;

import java.util.ArrayList;

/**
 * Created by Gabriel Delgado on 23-09-15.
 */
public class Command {

    private String name;
    private String action;
    private String parameter;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String path) {
        this.action = path;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
}
