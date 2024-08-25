package com.nestplayer.lib.smb;

import com.nestplayer.lib.entity.NestFile;
import com.nestplayer.lib.inter.INestConnectionService;
import com.nestplayer.lib.inter.INestFileService;
import com.nestplayer.lib.result.NestResult;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class SMBTest {

    @Test
    public void search() {
        List<String> stringList = Arrays.asList("mp4");

        INestConnectionService service = new SMBNestConnectionService();
        NestResult<Boolean> result = service.open("192.168.0.104", "lanzhenkai", "lanlove61599616L");

        INestFileService fileService = service.getNestFileService();
        List<NestFile> nestFiles = fileService.searchByFileExtName(stringList, "/homes/lanzhenkai/public/视频/教学/05.分布式专题（五）/2019-5-21（94）-使用缓存问题之缓存击穿解决方案&redis实现分布式-太白/缓存击穿和分布式锁.mp4", 20);

        for (NestFile nestFile : nestFiles) {
            System.out.println(nestFile.getPath());
        }


        service.close();
    }
}
