package com.nestplayer.lib.entity;

public class NestFile {
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 文件类型
     */
    private FileType type;
    /**
     * 创建时间
     */
    private long createTime;
    /**
     * 文件大小
     */
    private long size;
    /**
     * 文件路径
     * 例如: /data/opt/file.name
     */
    private String path;
    /**
     * 文件链接地址
     * smb://username@password:host/a/b/c
     * ftp://username@password:host/a/b/c
     */
    private String url;


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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
