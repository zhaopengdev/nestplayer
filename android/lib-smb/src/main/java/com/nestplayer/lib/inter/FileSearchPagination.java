package com.nestplayer.lib.inter;

import com.nestplayer.lib.entity.NestFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 文件分页搜索抽象类
 * FTP SMB 实现此抽象类
 */
public abstract class FileSearchPagination {

    //页容量
    private Integer PAGE_SIZE;
    //后缀名列表
    private List<String> EXT_LIST;
    // 文件名
    private String FILE_NAME;
    // 路径堆栈
    private List<String> PATH_STACK;
    //标记位
    private String MARK;
    //返回结果
    private List<NestFile> RESULT;
    //标记位是否生效 true生效 如果配到标记位则置为失效 false
    private boolean FROM_MARK;

    /**
     * 搜索类型
     * 名称搜索： #{@link FileSearchPagination#TYPE_BY_NAME}
     * 后缀名搜索： #{@link FileSearchPagination#TYPE_BY_EXT_LIST}
     */
    private String SEARCH_TYPE;

    private static final String TYPE_BY_NAME = "NAME";

    private static final String TYPE_BY_EXT_LIST = "EXT";



    /**
     * 获取当前文件夹所有文件
     *
     * @param dirPath 文件夹列表
     * @return 文件列表
     */
    protected abstract List<NestFile> getDirFileList(String dirPath);


    /**
     * 根据文件名搜索
     *
     * @param fileName 文件名
     * @param mark     标记位 上一次返回的文件最后一个
     * @param pageSize 页容量
     * @return 文件列表
     */
    protected List<NestFile> searchAbs(String fileName, String mark, int pageSize) {
        RESULT = new ArrayList<>();
        PAGE_SIZE = pageSize <= 0 ? 10 : pageSize;
        FILE_NAME = fileName;
        SEARCH_TYPE = TYPE_BY_NAME;
        MARK = mark;
        PATH_STACK = this.getPathStack(mark);
        List<NestFile> files = this.getDirFileList(""); //获取根目录
        FROM_MARK = (MARK != null && !MARK.isEmpty());//是否从标记位开始恢复搜索
        this.searchAndPaginate(files, 0);
        return RESULT;
    }

    /**
     * 根据后缀名搜索
     *
     * @param extList  后缀名集合
     * @param mark     标记位，上一次返回的文件最后一个
     * @param pageSize 页大小
     * @return 文件列表
     */
    protected List<NestFile> searchAbs(List<String> extList, String mark, int pageSize) {
        RESULT = new ArrayList<>();
        PAGE_SIZE = pageSize <= 0 ? 10 : pageSize;
        EXT_LIST = extList;
        SEARCH_TYPE = TYPE_BY_EXT_LIST;
        MARK = mark;
        PATH_STACK = this.getPathStack(mark);
        List<NestFile> files = this.getDirFileList(""); //获取根目录
        FROM_MARK = (MARK != null && !MARK.isEmpty());//是否从标记位开始恢复搜索
        this.searchAndPaginate(files, 0);
        return RESULT;
    }


    /**
     * 分页搜索 深度优先
     *
     * @param files 返回结果
     * @param depth 检索文件深度
     */
    private void searchAndPaginate(List<NestFile> files, int depth) {
        if (files == null || files.isEmpty()) {
            return;  // 如果目录为空，结束递归
        }

        String path = PATH_STACK.isEmpty() || depth >= PATH_STACK.size() ? "" : PATH_STACK.get(depth);//当前深度目录
        for (int i = 0; i < files.size() && RESULT.size() < PAGE_SIZE; i++) {
            NestFile file = files.get(i);
            boolean isDir = file.isDirectory();

            // 如果上次有标记位，则跳过标记位之前的文件
            if (FROM_MARK) {
                //未匹配到当前目录 跳过
                if (!file.getPath().equals(path)) {
                    continue;
                }
                // 匹配到了 不是文件夹代表是上个标记位  跳过
                if (!isDir) {
                    FROM_MARK = false; //标记位通过
                    continue;
                }
            }
            // 如果是目录，递归搜索子目录
            if (isDir) {
                //获取下级文件内容
                List<NestFile> childFiles = this.getDirFileList(file.getPath());
                //递归搜索
                this.searchAndPaginate(childFiles, depth + 1);
            }
            // 如果是文件且符合条件，记录
            else {
                if ((Objects.equals(SEARCH_TYPE, TYPE_BY_EXT_LIST) && EXT_LIST.contains(file.getExtName())) || (Objects.equals(SEARCH_TYPE, TYPE_BY_NAME) && file.getExtName().contains(FILE_NAME))) {
                    RESULT.add(file);
                    if (RESULT.size() >= PAGE_SIZE) {
                        return;
                    }
                }
            }

        }
    }

    private List<String> getPathStack(String path) {
        List<String> result = new ArrayList<>();
        if (path == null || path.isEmpty()) return result;
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        String[] parts = path.split("/");
        StringBuilder currentPath = new StringBuilder();
        currentPath.append("/");
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (part.isEmpty()) continue;
            currentPath.append(part);
            if (i < parts.length - 1 || (i == parts.length - 1 && path.endsWith("/"))) {
                currentPath.append("/");
            }
            result.add(currentPath.toString());
        }
        return result;
    }
}
