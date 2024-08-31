package com.nestplayer.quicktv;

import com.nestplayer.lib.entity.NestFile;
import com.nestplayer.lib.result.NestResult;
import com.tencent.mtt.hippy.common.HippyArray;
import com.tencent.mtt.hippy.common.HippyMap;

import eskit.sdk.support.args.EsArray;
import eskit.sdk.support.args.EsMap;
import eskit.sdk.support.module.es.ESModule;

public class ClientUtils {

    public static EsMap errorToHippyJson(int code,String message){
        EsMap hm = new EsMap();
        hm.pushInt("code",code);
        hm.pushString("message",message);
        hm.pushLong("time",System.currentTimeMillis());
        return hm;
    }

    public static EsMap errorToHippyJson(String message){
        return errorToHippyJson(400,message);
    }

    public static EsMap nestFileToHippyJson(NestFile nestFile){
        assert ( nestFile != null);
        EsMap hm = new EsMap();
        hm.pushString("fileName",nestFile.getFileName());
        hm.pushString("type",nestFile.getType().getType());
        hm.pushLong("size",nestFile.getSize());
        hm.pushString("path",nestFile.getPath());
        hm.pushString("url",nestFile.getUrl());
        hm.pushLong("createTime",nestFile.getCreateTime());
        return hm;
    }

    public static EsMap arrayToHippyJson(EsArray array){
        EsMap hm = new EsMap();
        hm.pushInt("code",200);
        hm.pushLong("time",System.currentTimeMillis());
        hm.pushArray("data",array);
        return hm;
    }

    public static EsMap nestResultToHippyJson(NestResult nestResult){
        assert ( nestResult != null);
        EsMap hm = new EsMap();
        hm.pushInt("code",nestResult.getCode());
        hm.pushString("message",nestResult.getMessage());
        hm.pushLong("time",nestResult.getTime());
        return hm;
    }
}
