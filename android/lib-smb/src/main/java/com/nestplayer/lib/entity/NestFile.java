package com.nestplayer.lib.entity;

public class NestFile {

    private String fileName;
    private String extName;
    private FileType type;
    private long createTime;
    private long size;

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
