package com.nestplayer.lib.ftp;

import com.nestplayer.lib.inter.INestConnectionService;
import com.nestplayer.lib.inter.INestFileService;
import com.nestplayer.lib.result.NestResult;
import com.nestplayer.lib.utils.LocalNetworkUtil;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class FTPNestConnectionService implements INestConnectionService {
    FTPClient ftpClient;

    private INestFileService nestFileService;

    @Override
    public List<String> scanNetwork() {
        try {
            return LocalNetworkUtil.scanNetwork(21);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public NestResult<Boolean> open(String ip, String userName, String password) {
        return this.open(ip, 21, userName, password);
    }

    @Override
    public NestResult<Boolean> open(String ip, Integer port, String userName, String password) {
        FTPClient ftpClient = new FTPClient();
        try {
            // 连接到FTP服务器
            ftpClient.connect(ip, port); // 替换为你的FTP服务器地址和端口

            // 登录到FTP服务器
            boolean login = ftpClient.login(userName, password); // 替换为你的用户名和密码

            if (login) {
                nestFileService = new FTPNestFileServiceImpl(ftpClient, userName, password, ip + ":" + port);
                return NestResult.success(true);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return NestResult.error("登录失败");
    }

    @Override
    public void close() {
        try {
            // 登出并断开连接
            ftpClient.logout();
            ftpClient.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public INestFileService getNestFileService() {
        return nestFileService;
    }
}
