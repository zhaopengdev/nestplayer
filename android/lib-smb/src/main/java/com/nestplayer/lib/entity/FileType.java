package com.nestplayer.lib.entity;

public enum FileType {
    VIDEO("VIDEO"),
    AUDIO("AUDIO"),
    IMAGE("IMAGE"),
    FILE("FILE"),
    DIRECTORY("DIRECTORY");

    private String type;

    FileType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
