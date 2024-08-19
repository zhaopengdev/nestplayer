package com.nestplayer.lib.inter;

import java.util.List;

/**
 * 链接
 */
public interface INestConnectionService {


    List<String> scanNetwork();

    /**
     * 创建连接
     *
     * @param ip       地址
     * @param userName 用户名
     * @param password 密码
     */
    boolean open(String ip, String userName, String password);

    /**
     * 关闭连接
     */
    void close();

    /**
     * 打开连接后可以获取文件操作服务类
     *
     * @return 文件操作实现
     */
    INestFileService getNestFileService();
}
