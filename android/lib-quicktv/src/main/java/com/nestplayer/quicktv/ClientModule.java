package com.nestplayer.quicktv;

import android.content.Context;

import com.nestplayer.lib.entity.FileType;
import com.nestplayer.lib.entity.NestFile;
import com.nestplayer.lib.inter.INestConnectionService;
import com.nestplayer.lib.result.NestResult;
import com.nestplayer.lib.smb.SMBNestConnectionService;
import com.nestplayer.lib.utils.LocalNetworkUtil;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import eskit.sdk.support.EsPromise;
import eskit.sdk.support.args.EsArray;
import eskit.sdk.support.args.EsMap;
import eskit.sdk.support.module.IEsModule;
import eskit.sdk.support.module.es.ESModule;

import android.util.Log;

public class ClientModule implements IEsModule {
    INestConnectionService mFileService = new SMBNestConnectionService();
    public static final String TAG = "DebugNestPlayer";
    public ExecutorService mExecutor = Executors.newFixedThreadPool(2);


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
        mExecutor.submit(() -> {
            NestResult<Boolean> result = mFileService.open(ip, username, password);
            if (result.isSuccess()) {
                Log.i(TAG, "connectServer  open success");
                promise.resolve(ClientUtils.nestResultToHippyJson(result));
            } else {
                Log.e(TAG, "connectServer  open failed " + result.getMessage());
                promise.reject(ClientUtils.errorToHippyJson(result.getMessage()));
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
     * @param promise
     */
    public void searchFilesByType(EsArray array,EsPromise promise) {

        EsArray esArray = array.getArray(0);
        final EsArray pathArray = esArray.getArray(0);
        int pageSize = esArray.getInt(1);
        String lastPath = esArray.getString(2);
        Log.i(TAG, "searchFilesByType called pathArray: " + pathArray + " pageSize: " + pageSize + " lastPath: " + lastPath);
        if(mFileService == null || mFileService.getNestFileService() == null){
            promise.reject("请先链接服务器");
            return;
        }
        mExecutor.submit(() -> {
            try {
                ArrayList<String> postfixes = new ArrayList<>();
                for(int i = 0; i < pathArray.size(); i++){
                    String post = pathArray.getString(i);
                    postfixes.add(post);
                }
                List<NestFile> files =  mFileService.getNestFileService().search(postfixes, lastPath, pageSize);
                Log.i(TAG, "searchFilesByType from service fileCount is : " + (files == null ? 0 : files.size()));
                //TODO 这里返回的结果不是视频文件
                EsArray result = new EsArray();
                for(NestFile file : files){
                    final EsMap fileMap = ClientUtils.nestFileToHippyJson(file);
                    Log.i(TAG,"searchFilesByType file: " + fileMap);
                    result.pushMap(fileMap);
                }
                promise.resolve(ClientUtils.arrayToHippyJson(result));
            } catch (Exception e) {
                promise.reject(ClientUtils.errorToHippyJson("搜索失败"));
            }
        });
    }


    @Override
    public void init(Context context) {
        Log.i(TAG, "ClientModule init context: " + context);
    }

    @Override
    public void destroy() {

    }
}
