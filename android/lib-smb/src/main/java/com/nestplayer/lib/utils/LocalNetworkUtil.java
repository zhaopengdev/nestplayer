package com.nestplayer.lib.utils;

import android.annotation.SuppressLint;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 本地网络
 */
public class LocalNetworkUtil {
    /**
     * 获取当前设备网络
     *
     * @return ip地址
     * @throws SocketException 链接异常
     */
    public static String getLocalIPAddress() throws SocketException {
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                InetAddress inetAddress = inetAddresses.nextElement();
                if (inetAddress.isSiteLocalAddress() && !inetAddress.isLoopbackAddress() && inetAddress instanceof java.net.Inet4Address) {
                    return inetAddress.getHostAddress();
                }
            }
        }
        return null;
    }


    @SuppressLint("DefaultLocale")
    private static String getSubnetMaskFromPrefixLength(short prefixLength) {
        int mask = 0xffffffff << (32 - prefixLength);
        return String.format("%d.%d.%d.%d",
                (mask >> 24) & 0xff,
                (mask >> 16) & 0xff,
                (mask >> 8) & 0xff,
                mask & 0xff);
    }

    public static List<String> scanNetwork(int port) throws Exception {
        String ipAddress = getLocalIPAddress();
        if (ipAddress == null || ipAddress.isEmpty()) {
            return new ArrayList<>();
        }
        int lastDotIndex = ipAddress.lastIndexOf('.');
        if (lastDotIndex == -1) return new ArrayList<>();
        String subnet = ipAddress.substring(0, lastDotIndex);
        return scanNetwork(subnet, port);
    }

    public static List<String> scanNetwork(String subnet, int port) throws Exception {
        List<String> list = new ArrayList<>();
        for (int i = 1; i < 255; i++) {
            String host = subnet + "." + i;
            boolean available = isSmbServiceAvailable(host, port);
            if (available) {
                list.add(host);
            }
        }
        return list;
    }

    public static boolean isSmbServiceAvailable(String host, int port) {
        try (Socket socket = new Socket(host, port)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
