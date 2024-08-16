package com.nestplayer.lib.smb;

import com.nestplayer.lib.inter.INestConnectionService;
import com.nestplayer.lib.inter.INestFileService;
import com.nestplayer.lib.utils.LocalNetworkUtil;

import java.util.Collections;
import java.util.List;

import jcifs.CIFSContext;
import jcifs.CIFSException;
import jcifs.context.SingletonContext;
import jcifs.smb.NtlmPasswordAuthenticator;
import jcifs.smb.SmbFile;

/**
 * smb 链接类
 */
public class SMBNestConnectionService implements INestConnectionService {

    CIFSContext authContext;
    SmbFile smbRoot;

    private INestFileService nestFileService;


    public INestFileService getNestFileService() {
        return nestFileService;
    }

    /**
     * 获取本地可用smb IP
     *
     * @return ip地址集合
     */
    @Override
    public List<String> scanNetwork() {
        try {
            return LocalNetworkUtil.scanNetwork();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    /**
     * 打开链接
     *
     * @param ip       地址
     * @param userName 用户名
     * @param password 密码
     * @return 链接是否成功
     */

    @Override
    public boolean open(String ip, String userName, String password) {
        try {
            // 使用默认配置创建 CIFS 上下文
            CIFSContext baseContext = SingletonContext.getInstance();
            // 配置身份验证信息
            authContext = baseContext.withCredentials(new NtlmPasswordAuthenticator(userName, password));
            // 构造 SMB URL
            String smbUrl = "smb://" + ip + "/";
            // 连接到服务器
            smbRoot = new SmbFile(smbUrl, authContext);
            nestFileService = new NestSMBFileServiceImpl(authContext, smbRoot);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void close() {
        try {
            authContext.close();
            smbRoot.close();
        } catch (CIFSException e) {
            throw new RuntimeException(e);
        }
    }
}
