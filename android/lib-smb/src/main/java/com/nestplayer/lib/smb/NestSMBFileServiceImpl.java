package com.nestplayer.lib.smb;

import com.nestplayer.lib.entity.FileType;
import com.nestplayer.lib.entity.NestFile;
import com.nestplayer.lib.inter.INestFileService;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLStreamHandler;
import java.util.*;
import java.util.function.Function;

import jcifs.CIFSContext;
import jcifs.Credentials;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

/**
 * SMB 文件
 */
public class NestSMBFileServiceImpl implements INestFileService {
    CIFSContext authContext;
    SmbFile smbRoot;
    private Map<String, NestFile> fileMap = null;
    private final String username;
    private final String password;

    public NestSMBFileServiceImpl(CIFSContext authContext, SmbFile smbRoot, String username, String password) {
        this.authContext = authContext;
        this.smbRoot = smbRoot;
        this.username = username;
        this.password = password;
    }

    @Override
    public List<NestFile> rootDir() {
        List<NestFile> list = new ArrayList<>();
        try {
            // 列出所有共享目录
            SmbFile[] shares = smbRoot.listFiles();
            if (shares == null || shares.length == 0) {
                return list;
            }
            List<SmbFile> smbFileList = new ArrayList<>();
            for (SmbFile smbFile : shares) {
                if (Objects.equals("IPC$/", smbFile.getName()) || Objects.equals("print$/", smbFile.getName())) {
                    continue;
                }
                smbFileList.add(smbFile);
            }

            //只有一个共享目录 列出所有文件
            if (smbFileList.size() == 1) {
                for (SmbFile share : smbFileList) {
                    this.listAllFilesAndDirectories(share, list, false); // 递归列出文件夹和文件
                }
            } else {
                //列出所有共享文件
                for (SmbFile file : smbFileList) {
                    this.convertFileAddList(list, false, file);
                }
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
                    this.listAllFilesAndDirectories(share, list, true); // 递归列出文件夹和文件
                }
                fileMap = new HashMap<>();
                for (NestFile nestFile : list) {
                    fileMap.put(nestFile.getPath(), nestFile);
                }
            }
            for (Map.Entry<String, NestFile> entry : fileMap.entrySet()) {
                if (str == null || str.isEmpty() || entry.getValue().getFileName().toLowerCase().contains(str.toLowerCase())) {
                    fileList.add(entry.getValue());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileList;
    }

    @Override
    public List<NestFile> searchByFileExtName(List<String> extNameList) {
        List<NestFile> fileList = new ArrayList<>();
        try {
            List<NestFile> search = this.search("");
            for (NestFile nestFile : search) {
                if (extNameList.contains(nestFile.getExtName())) {
                    fileList.add(nestFile);
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
                        this.convertFileAddList(list, recursion, file);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void convertFileAddList(List<NestFile> list, boolean recursion, SmbFile file) throws SmbException {
        URL url = file.getURL();
        String urlFile = url.getFile();
        String host = url.getHost();
        String protocol = url.getProtocol();
        NestFile nestFile = new NestFile();
        nestFile.setPath(urlFile);
        nestFile.setFileName(file.getName());
        nestFile.setSize(file.length());
        nestFile.setUrl(this.convertSMBURL(protocol, host, urlFile));

        if (file.isDirectory()) {
            nestFile.setType(FileType.DIRECTORY);
            list.add(nestFile);
            if (recursion) {
                this.listAllFilesAndDirectories(file, list, true); // 递归调用
            }
        } else {
            nestFile.setType(FileType.getTypeEnum(file.getName()));
            list.add(nestFile);
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

    private String convertSMBURL(String protocol, String host, String urlFile) {
        //smb://userName:passWord@host/path/folderName
        return String.format("%s://%s:%s@%s%s", protocol, username, password, host, urlFile);
    }
}
