package com.nestplayer.lib.smb;

import com.nestplayer.lib.entity.FileType;
import com.nestplayer.lib.entity.NestFile;
import com.nestplayer.lib.inter.INestFileService;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import jcifs.CIFSContext;
import jcifs.smb.SmbFile;

/**
 * SMB 文件
 */
public class NestSMBFileServiceImpl implements INestFileService {
    CIFSContext authContext;
    SmbFile smbRoot;
    private Map<String, NestFile> fileMap = null;

    public NestSMBFileServiceImpl(CIFSContext authContext, SmbFile smbRoot) {
        this.authContext = authContext;
        this.smbRoot = smbRoot;
    }

    @Override
    public List<NestFile> rootDir() {
        List<NestFile> list = new ArrayList<>();
        try {
            // 列出所有共享目录
            SmbFile[] shares = smbRoot.listFiles();
            for (SmbFile share : shares) {
                System.out.println("Share: " + share.getName());
                this.listAllFilesAndDirectories(share, list, false); // 递归列出文件夹和文件
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 导航到上一级目录 如果上一级不存在则返回当前目录内容
     *
     * @param currentDir 当前目录
     * @return 上级目录文件内容
     */
    @Override
    public List<NestFile> parentDir(String currentDir) {
        List<NestFile> list = new ArrayList<>();
        try {
            // 计算上一级目录路径
            if (currentDir.equals("/")) {
                // 如果当前路径是根目录，则没有上一级目录
                SmbFile[] shares = smbRoot.listFiles();
                for (SmbFile smbFile : shares) {
                    this.listAllFilesAndDirectories(smbFile, list, false); // 列出文件夹和文件
                }
                return list;
            }
            // 去掉最后一个斜杠及其后的部分
            int lastSlashIndex = currentDir.lastIndexOf('/');
            if (lastSlashIndex == -1) {
                // 如果路径中没有斜杠，说明路径本身就是文件名
                return list;
            }
            // 构建上一级目录路径
            String parentPath = currentDir.substring(0, lastSlashIndex);
            SmbFile smbFile = new SmbFile(parentPath, authContext);
            this.listAllFilesAndDirectories(smbFile, list, false); // 列出文件夹和文件
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<NestFile> sortFileByName(String currentDir, Integer order) {
        return this.orderList(currentDir, order, Comparator.comparing(NestFile::getFileName));
    }

    @Override
    public List<NestFile> sortFileByCreateTime(String currentDir, Integer order) {
        return this.orderList(currentDir, order, Comparator.comparing(NestFile::getCreateTime));
    }

    @Override
    public List<String> getFileNameList(String path) {
        List<String> list = new ArrayList<>();
        try {
            SmbFile smbFile = new SmbFile(path, authContext);
            List<NestFile> fileList = new ArrayList<>();
            this.listAllFilesAndDirectories(smbFile, fileList, false); // 列出文件夹和文件
            for (NestFile nestFile : fileList) {
                list.add(nestFile.getFileName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<NestFile> search(String str) {
        List<NestFile> fileList = new ArrayList<>();
        try {
            if (fileMap == null) {
                // 列出所有共享目录
                SmbFile[] shares = smbRoot.listFiles();
                List<NestFile> list = new ArrayList<>();
                for (SmbFile share : shares) {
                    System.out.println("Share: " + share.getName());
                    this.listAllFilesAndDirectories(share, list, true); // 递归列出文件夹和文件
                }
                fileMap = new HashMap<>();
                for (NestFile nestFile : list) {
                    fileMap.put(nestFile.getPath(), nestFile);
                }
            }
            for (Map.Entry<String, NestFile> entry : fileMap.entrySet()) {
                if (entry.getValue().getFileName().toLowerCase().contains(str.toLowerCase())) {
                    fileList.add(entry.getValue());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileList;
    }

    /**
     * 列出文件
     *
     * @param directory 文件夹
     * @param list      返回值
     * @param recursion 是否递归 true 遍历子目录
     */
    private void listAllFilesAndDirectories(SmbFile directory, List<NestFile> list, boolean recursion) {
        try {
            if (directory.isDirectory()) {
                SmbFile[] files = directory.listFiles();
                if (files != null) {
                    for (SmbFile file : files) {
                        if (file.isDirectory()) {
                            System.out.println("Directory: " + file.getPath());
                            NestFile nestFile = new NestFile();
                            nestFile.setType(FileType.DIRECTORY);
                            nestFile.setFileName(file.getName());
                            nestFile.setSize(file.length());
                            nestFile.setPath(file.getPath());
                            list.add(nestFile);
                            if (recursion) {
                                this.listAllFilesAndDirectories(file, list, true); // 递归调用
                            }
                        } else {
                            System.out.println("File: " + file.getPath());
                            NestFile nestFile = new NestFile();
                            nestFile.setType(FileType.FILE);
                            nestFile.setFileName(file.getName());
                            nestFile.setSize(file.length());
                            nestFile.setPath(file.getPath());
                            list.add(nestFile);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<NestFile> orderList(String currentDir, Integer order, Comparator<NestFile> comparator) {
        List<NestFile> list = new ArrayList<>();
        try {
            SmbFile smbFile = new SmbFile(currentDir, authContext);
            this.listAllFilesAndDirectories(smbFile, list, false); // 列出文件夹和文件
            if (order == null || order.equals(0)) {
                return list;
            }
            if (order == 1) {                // 正序
                list.sort(comparator);
            } else if (order == -1) {                // 倒序
                list.sort(comparator.reversed());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
