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

        INestConnectionService service = new SMBNestConnectionService();
        NestResult<Boolean> result = service.open("192.168.80.51", "lanzhenkai", "lanzhenkai");
        if (!result.isSuccess()) {
            System.out.println(result.getMessage());
            return;
        }
        INestFileService fileService = service.getNestFileService();

        List<String> stringList = Arrays.asList("mp4", "mkv","avi","flv","webM","3gp");
        List<NestFile> nestFiles = fileService.search(stringList, "", 100);

        for (NestFile nestFile : nestFiles) {
            System.out.println(nestFile.getPath());
        }


        service.close();
    }
}
