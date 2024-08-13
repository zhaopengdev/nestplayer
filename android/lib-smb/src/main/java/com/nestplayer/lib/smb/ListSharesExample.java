package com.nestplayer.lib.smb;
import jcifs.CIFSContext;
import jcifs.context.SingletonContext;
import jcifs.smb.NtlmPasswordAuthenticator;
import jcifs.smb.SmbFile;

public class ListSharesExample {
    public static void main(String[] args) {
        String hostname = "192.168.80.51";
        String username = "lanzhenkai";
        String password = "lanzhenkai";

        try {
            // 使用默认配置创建 CIFS 上下文
            CIFSContext baseContext = SingletonContext.getInstance();

            // 配置身份验证信息
            CIFSContext authContext = baseContext.withCredentials(new NtlmPasswordAuthenticator(username, password));

            // 构造 SMB URL
            String smbUrl = "smb://" + hostname + "/";

            // 连接到服务器
            SmbFile smbRoot = new SmbFile(smbUrl, authContext);

            // 列出所有共享目录
            SmbFile[] shares = smbRoot.listFiles();
            for (SmbFile share : shares) {
                System.out.println("Share: " + share.getName());
                listAllFilesAndDirectories(share); // 递归列出文件夹和文件
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void listAllFilesAndDirectories(SmbFile directory) {
        try {
            if (directory.isDirectory()) {
                SmbFile[] files = directory.listFiles();
                if (files != null) {
                    for (SmbFile file : files) {
                        if (file.isDirectory()) {
                            System.out.println("Directory: " + file.getPath());
                            listAllFilesAndDirectories(file); // 递归调用
                        } else {
                            System.out.println("File: " + file.getPath());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
