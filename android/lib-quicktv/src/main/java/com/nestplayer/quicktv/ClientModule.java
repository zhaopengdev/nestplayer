package com.nestplayer.quicktv;

import com.nestplayer.lib.entity.FileType;
import com.nestplayer.lib.entity.NestFile;
import com.tencent.mtt.hippy.HippyEngineContext;
import com.tencent.mtt.hippy.annotation.HippyMethod;
import com.tencent.mtt.hippy.annotation.HippyNativeModule;
import com.tencent.mtt.hippy.common.HippyArray;
import com.tencent.mtt.hippy.common.HippyMap;
import com.tencent.mtt.hippy.modules.Promise;
import com.tencent.mtt.hippy.modules.nativemodules.HippyNativeModuleBase;

import java.util.Date;

@HippyNativeModule(name = "ClientModule")
public class ClientModule extends HippyNativeModuleBase {


    public ClientModule(HippyEngineContext hippyEngineContext) {
        super(hippyEngineContext);
    }


    /**
     * 链接nas
     * @param params
     * @param promise
     */
    @HippyMethod(name="connectServe")
    public void connectServe(HippyMap params, Promise promise){

        String protocol = params.getString("protocol");

        String ip = params.getString("ip");
        String port = params.getString("port");
        String username = params.getString("username");
        String password = params.getString("password");

        switch (protocol){
            case "smb":

                break;
        }
        boolean success = true;
        if(success){
            promise.resolve("");
        }else{
            promise.reject("链接失败");
        }

    }

    /**
     * 根据路径获取文件列表
     * @param path
     * @param promise
     */
    @HippyMethod(name="getFiles")
    public void getFiles(String path, Promise promise){

        HippyArray array = new HippyArray();

        int fileCount = 100;
        for(int i = 0; i < fileCount; i ++){
            NestFile file = new NestFile();
            file.setFileName("测试"+i);
            file.setType(FileType.VIDEO);
            file.setSize(100 * 1024);
            file.setCreateTime(new Date().getTime());
            array.pushMap(ClientUtils.nestFileToHippyJson(file));
        }
        promise.resolve(array);

    }


    /**
     * 分页搜索所有视频列表
     * @param path
     * @param type
     * @param page
     * @param pageSize
     * @param promise
     */
    @HippyMethod(name="searchFilesByType")
    public void searchFiles(String path,String type,int page,int pageSize, Promise promise){

        HippyArray array = new HippyArray();

        int fileCount = 100;
        for(int i = 0; i < fileCount; i ++){
            NestFile file = new NestFile();
            file.setFileName("测试视频"+i);
            file.setType(FileType.VIDEO);
            file.setSize(100 * 1024);
            file.setCreateTime(new Date().getTime());
            array.pushMap(ClientUtils.nestFileToHippyJson(file));
        }
        promise.resolve(array);

    }


}
