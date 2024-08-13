package com.nestplayer.lib.smb;

import com.nestplayer.lib.entity.NestFile;
import com.nestplayer.lib.inter.INestFileService;

import java.util.Collections;
import java.util.List;

/**
 * SMB 文件
 */
public class NestSMBFileServiceImpl implements INestFileService {


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
    public List<String> search(String str) {
        return Collections.emptyList();
    }
}
