package com.nestplayer.lib.inter;

/**
 * 链接
 */
public interface INestConnectionService {
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
}
