package com.nestplayer.lib.ftp;

import com.nestplayer.lib.entity.FileType;
import com.nestplayer.lib.entity.NestFile;
import com.nestplayer.lib.inter.FileSearchPagination;
import com.nestplayer.lib.inter.INestFileService;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * FTP 文件
 */
public class FTPNestFileServiceImpl extends FileSearchPagination implements INestFileService {

    FTPClient ftpClient;
    private final String username;
    private final String password;
    private final String host;

    public FTPNestFileServiceImpl(FTPClient ftpClient, String username, String password, String host) {
        this.ftpClient = ftpClient;
        this.username = username;
        this.password = password;
        this.host = host;
    }

    @Override
    protected List<NestFile> getDirFileList(String dirPath) {
        List<NestFile> list = new ArrayList<>();

        try {
            // 切换到目标目录
            String remoteDir = dirPath == null || dirPath.isEmpty() ? "/" : dirPath; // 替换为你要浏览的FTP目录
            ftpClient.changeWorkingDirectory(remoteDir);

            // 列出目录中的文件和文件夹
            FTPFile[] files = ftpClient.listFiles();

            // 遍历并输出文件和文件夹的名称
            for (FTPFile file : files) {
                NestFile nestFile = this.convertFile(file);
                if (nestFile != null) {
                    list.add(nestFile);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<NestFile> rootDir() {
        return Collections.emptyList();
    }

    @Override
    public List<NestFile> parentDir(String currentDir) {
        return Collections.emptyList();
    }

    @Override
    public List<NestFile> sortFileByName(String currentDir, Integer order) {
        return Collections.emptyList();
    }

    @Override
    public List<NestFile> sortFileByCreateTime(String currentDir, Integer order) {
        return Collections.emptyList();
    }

    @Override
    public List<String> getFileNameList(String path) {
        return Collections.emptyList();
    }

    @Override
    public List<NestFile> search(String str) {
        return Collections.emptyList();
    }

    @Override
    public List<NestFile> search(String str, String lastPath, Integer pageSize) {
        return Collections.emptyList();
    }

    @Override
    public List<NestFile> search(List<String> extNameList) {
        return Collections.emptyList();
    }

    @Override
    public List<NestFile> search(List<String> extNameList, String lastPath, Integer pageSize) {
        return Collections.emptyList();
    }

    private NestFile convertFile(FTPFile ftpFile) {
        if (ftpFile == null) return null;
        NestFile nestFile = new NestFile();
        nestFile.setFileName(ftpFile.getName());
        nestFile.setSize(ftpFile.getSize());
        nestFile.setPath(this.convertFTPURL(ftpFile.getLink()));
        if (ftpFile.isDirectory()) {
            nestFile.setType(FileType.DIRECTORY);
        } else {
            nestFile.setType(FileType.getTypeEnum(ftpFile.getName()));
        }
        nestFile.setCreateTime(ftpFile.getTimestamp().getTimeInMillis());
        return nestFile;
    }

    private String convertFTPURL(String urlFile) {
        //ftp://userName:passWord@host/path/folderName
        return String.format("ftp://%s:%s@%s%s", username, password, host, urlFile);
    }
}
