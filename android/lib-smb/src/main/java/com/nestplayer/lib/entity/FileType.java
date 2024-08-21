package com.nestplayer.lib.entity;

import java.util.List;

public enum FileType {
    VIDEO("VIDEO", new String[]{"avi", "wmv", "mpeg", "mpg", "mov", "rm", "rmvb"}),
    AUDIO("AUDIO", new String[]{"mp3", "wav", "wma", "aac", "flac"}),
    IMAGE("IMAGE", new String[]{"jpeg", "jpg", "png", "gif", "tiff", "tif", "bmp", "psd"}),
    FILE("FILE"),
    DIRECTORY("DIRECTORY");

    private String type;

    private String[] defaultExtName;

    FileType(String type) {
        this.type = type;
    }

    FileType(String type, String[] defaultExtName) {
        this.type = type;
        this.defaultExtName = defaultExtName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String[] getDefaultExtName() {
        return defaultExtName;
    }

    public void setDefaultExtName(String[] defaultExtName) {
        this.defaultExtName = defaultExtName;
    }

    /**
     * 获取文件类型
     *
     * @param fullName 完整名称
     * @return 枚举
     */
    public static FileType getTypeEnum(String fullName) {
        if (fullName != null && !fullName.isEmpty()) {
            String lowerCase = fullName.toLowerCase();
            for (FileType value : values()) {
                for (String name : value.getDefaultExtName()) {
                    if (lowerCase.endsWith("." + name)) {
                        return value;
                    }
                }
            }
        }
        return FileType.FILE;
    }


}
