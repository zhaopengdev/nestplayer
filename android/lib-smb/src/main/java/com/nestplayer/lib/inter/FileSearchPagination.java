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
    //排序规则 -1 倒序 0 默认 1 正序
    private int order = 0;
    //排序字段 date或name  默认为data
    private String orderBy = "date";

    private Integer PAGE_SIZE;

    private List<String> EXT_LIST;

    private String FILE_NAME;

    private String SEARCH_TYPE;

    private Integer DEPTH = 0;

    private List<String> PATH_STACK;

    private String MARK;

    private List<NestFile> RESULT;

    private boolean FROM_MARK;

    private static final String TYPE_BY_NAME = "NAME";
    private static final String TYPE_BY_EXT_LIST = "EXT";

    /**
     * 获取当前文件夹所有文件
     *
     * @param dirPath 文件夹列表
     * @return 文件列表
     */
    protected abstract List<NestFile> getDirFileList(String dirPath);


    protected List<NestFile> searchAbs(String fileName, String mark, int pageSize) {
        List<NestFile> result = new ArrayList<>();
        PAGE_SIZE = pageSize <= 0 ? 10 : pageSize;
        FILE_NAME = fileName;
        DEPTH = 0;
        SEARCH_TYPE = TYPE_BY_NAME;
        MARK = mark;
        PATH_STACK = this.getPathStack(mark);
        this.searchAndPaginate(mark, result, false);
        return result;

    }

    /**
     * @param extList
     * @param mark     标记位，上一次返回的文件最后一个
     * @param pageSize
     * @return
     */
    protected List<NestFile> searchAbs(List<String> extList, String mark, int pageSize) {
        RESULT = new ArrayList<>();
        PAGE_SIZE = pageSize;
        EXT_LIST = extList;
        DEPTH = 0;
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
                List<NestFile> childFiles = this.getDirFileList(file.getPath());
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

    /**
     * 分页搜索
     *
     * @param mark   标记位，上一次返回的文件最后一个
     * @param result 返回结果
     * @param isUp   true 向上查找 false 向下或同级查找
     */
    private void searchAndPaginate(String mark, List<NestFile> result, boolean isUp) {
        String dirPath;
        //如果是空文件 则查询根目录
        if (mark == null || mark.isEmpty()) {
            dirPath = "";
        } else if (mark.endsWith("/") && !isUp) {
            dirPath = mark;
        } else {
            dirPath = this.getParentPath(mark);
        }
        boolean resumeFromMark = (mark != null && !mark.isEmpty() && mark.startsWith(dirPath) && !mark.equals(dirPath));  // 是否从标记位开始恢复搜索
        List<NestFile> files = this.getDirFileList(dirPath);
        if (files == null || files.isEmpty()) {
            return;  // 如果目录为空，结束递归
        }

        for (int i = 0; i < files.size() && result.size() < PAGE_SIZE; i++) {
            NestFile file = files.get(i);
            // 如果上次有标记位，则跳过标记位之前的文件
            if (resumeFromMark) {
                if (file.getPath().equals(mark)) {
                    resumeFromMark = false;  // 从标记位恢复
                }
                continue;
            }
            mark = file.getPath();
            // 如果是目录，递归搜索子目录
            if (file.isDirectory()) {
                this.searchAndPaginate(mark, result, false);
            }
            // 如果是文件且符合条件，记录
            else {
                if ((Objects.equals(SEARCH_TYPE, TYPE_BY_EXT_LIST) && EXT_LIST.contains(file.getExtName())) || (Objects.equals(SEARCH_TYPE, TYPE_BY_NAME) && file.getExtName().contains(FILE_NAME))) {
                    result.add(file);
                    if (result.size() >= PAGE_SIZE) {
                        return;
                    }
                }
            }
        }
        if (result.size() >= PAGE_SIZE) {
            return;
        }
        // 如果当前目录已遍历完毕，返回上层继续搜索  需要转换两次第一次是当前文件夹例如 /a/b/c/d.mp4  遍历完成后 需要将 /a/b/ 传递给递归
        String parentPath = this.getParentPath(mark);
        if (parentPath == null || parentPath.isEmpty()) return;
        this.searchAndPaginate(parentPath, result, true);
    }

    private String getParentPath(String mark) {
        if (mark == null || mark.isEmpty()) return null;
        if (mark.endsWith("/")) mark = mark.substring(0, mark.length() - 1);
        if (!mark.contains("/")) return null;
        String sub = mark.substring(0, mark.lastIndexOf("/"));
        if (sub.isEmpty()) return ""; //代表根目录
        return sub + "/";
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
