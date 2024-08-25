package com.nestplayer.lib.smb;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
        System.out.println("123--123");
    }

    @Test
    public void test(){
        String filePath = "/data/opt/videos/1942.mp4";

        // 使用字符串操作获取上一级文件夹
        String parentDirectory = filePath.substring(0, filePath.lastIndexOf("/")+1);

        System.out.println("Parent Directory: " + parentDirectory );
    }
}