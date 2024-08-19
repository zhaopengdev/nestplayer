package com.nestplayer.quicktv;

import com.nestplayer.lib.entity.NestFile;
import com.tencent.mtt.hippy.common.HippyMap;

import eskit.sdk.support.args.EsMap;

public class ClientUtils {

    public static EsMap nestFileToHippyJson(NestFile nestFile){
        assert ( nestFile != null);
        EsMap hm = new EsMap();
        hm.pushString("fileName",nestFile.getFileName());
        hm.pushString("fileName",nestFile.getType().getType());
        hm.pushLong("fileName",nestFile.getSize());
        hm.pushLong("createTime",nestFile.getCreateTime());
        //TODO 添加更多字段
        return hm;
    }
}
