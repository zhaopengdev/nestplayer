package com.nestplayer.quicktv;

import android.content.Context;

import com.nestplayer.lib.entity.FileType;
import com.nestplayer.lib.entity.NestFile;
import com.nestplayer.lib.inter.INestConnectionService;
import com.nestplayer.lib.result.NestResult;
import com.nestplayer.lib.smb.SMBNestConnectionService;
import com.nestplayer.lib.utils.LocalNetworkUtil;

import java.net.SocketException;
import java.util.Date;
import java.util.concurrent.Executors;

import eskit.sdk.support.EsPromise;
import eskit.sdk.support.args.EsArray;
import eskit.sdk.support.module.IEsModule;

import android.util.Log;

public class ClientModule implements IEsModule {
    INestConnectionService service = new SMBNestConnectionService();
    public static final String TAG = "ClientModule";


    public ClientModule() {
        super();
        Log.i(TAG, "ClientModule create");
    }

    /**
     * 链接nas
     *
     * @param params
     * @param promise
     */
    public void connectServer(EsArray params, EsPromise promise) {
        Log.i(TAG, "connectServer params: " + params);
        //service = new SMBNestConnectionService();
        String protocol = params.getString(0);

        String ip = params.getString(1);
//        String port = params.getString(2);
        String username = params.getString(2);
        String password = params.getString(3);

        if (!"smb".equals(protocol)) {
            promise.reject("不支持的协议");
            return;
        }
        Executors.newCachedThreadPool().submit(() -> {
            Log.i(TAG, "connectServer start open");
            NestResult<Boolean> result = service.open(ip, username, password);
            if (result.isSuccess()) {
                Log.i(TAG, "connectServer  open success");
                promise.resolve("");
            } else {
                Log.e(TAG, "connectServer  open failed");
                promise.reject("链接失败");
            }
        });
    }

    /**
     * 根据路径获取文件列表
     *
     * @param path
     * @param promise
     */
    public void getFiles(String path, EsPromise promise) {

        EsArray array = new EsArray();

        int fileCount = 100;
        for (int i = 0; i < fileCount; i++) {
            NestFile file = new NestFile();
            file.setFileName("测试" + i);
            file.setType(FileType.VIDEO);
            file.setSize(100 * 1024);
            file.setCreateTime(new Date().getTime());
            array.pushMap(ClientUtils.nestFileToHippyJson(file));
        }
        promise.resolve(array);
    }

    public void getLocalIP(EsPromise promise) {
        Log.i(TAG, "getLocalIP called");
        try {
            String s = LocalNetworkUtil.getLocalIPAddress();
            Log.i(TAG, "getLocalIP return " + s);
            promise.resolve(s);
        } catch (SocketException e) {
            // throw new RuntimeException(e);
            promise.reject(e.getMessage());
        }

    }


    /**
     * 分页搜索所有视频列表
     *
     * @param path
     * @param type
     * @param page
     * @param pageSize
     * @param promise
     */
    public void searchFilesByType(String path, String type, int page, int pageSize, EsPromise promise) {

        EsArray array = new EsArray();

        int fileCount = 100;
        for (int i = 0; i < fileCount; i++) {
            NestFile file = new NestFile();
            file.setFileName("测试视频" + i);
            file.setType(FileType.VIDEO);
            file.setSize(100 * 1024);
            file.setCreateTime(new Date().getTime());
            array.pushMap(ClientUtils.nestFileToHippyJson(file));
        }
        promise.resolve(array);

    }


    @Override
    public void init(Context context) {
        Log.i(TAG, "ClientModule init context: " + context);
    }

    @Override
    public void destroy() {

    }
}
