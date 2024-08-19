package com.nestplayer.lib.inter;


import com.nestplayer.lib.entity.NestFile;

import java.util.List;

public interface INestFileService {

    /**
     * 导航到根目录
     *
     * @return 文件列表
     */
    List<NestFile> rootDir();

    /**
     * 导航到上一级目录 如果上一级不存在则返回当前目录内容
     *
     * @return 文件列表
     */
    List<NestFile> parentDir(String currentDir);

    /**
     * 根据名称排序
     *
     * @param currentDir 当前目录
     * @param order      -1 倒叙 0默认 1正序
     * @return 文件列表
     */
    List<NestFile> sortFileByName(String currentDir, Integer order);

    /**
     * 根据创建时间排序
     *
     * @param currentDir 当前目录
     * @param order      -1 倒叙 0默认 1正序
     * @return 文件列表
     */
    List<NestFile> sortFileByCreateTime(String currentDir, Integer order);

    /**
     * 获取名称集合
     *
     * @param path 当前文件路径  空为根目录
     * @return 文件名称集合
     */
    List<String> getFileNameList(String path);

    /**
     * 搜索
     *
     * @param str 文件名称 首字母 后缀名
     * @return 文件名称集合
     */
    List<NestFile> search(String str);

    /**
     * 根据后缀名搜索文件
     *
     * @param extNameList 后缀名集合
     * @return 列出所有文件
     */
    List<NestFile> searchByFileExtName(List<String> extNameList);
}
