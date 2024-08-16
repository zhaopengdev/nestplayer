package com.nestplayer.quicktv;

import com.nestplayer.lib.entity.NestFile;
import com.tencent.mtt.hippy.common.HippyMap;

public class ClientUtils {

    public static HippyMap nestFileToHippyJson(NestFile nestFile){
        assert ( nestFile != null);
        HippyMap hm = new HippyMap();
        hm.pushString("fileName",nestFile.getFileName());
        hm.pushString("fileName",nestFile.getType().getType());
        hm.pushLong("fileName",nestFile.getSize());
        hm.pushLong("createTime",nestFile.getCreateTime());
        //TODO 添加更多字段
        return hm;
    }
}
