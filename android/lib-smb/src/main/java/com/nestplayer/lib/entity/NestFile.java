package com.nestplayer.lib.entity;

public class NestFile {

    private String fileName;
    private FileType type;
    private long createTime;
    private long size;


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public FileType getType() {
        return type;
    }

    public void setType(FileType type) {
        this.type = type;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    /**
     * 是否是文件夹
     *
     * @return true / false
     */
    public boolean isDirectory() {
        return FileType.DIRECTORY.equals(type);
    }

    /**
     * 获取扩展名
     *
     * @return 没有扩展名 返回空
     */
    public String getExtName() {
        if (fileName == null || fileName.isEmpty()) return "";
        int lastIndexOfDot = fileName.lastIndexOf('.');
        if (lastIndexOfDot == -1) {
            // 没有找到点，即没有扩展名
            return "";
        } else {
            // 返回点之后的所有字符
            return fileName.substring(lastIndexOfDot + 1);
        }
    }

}
