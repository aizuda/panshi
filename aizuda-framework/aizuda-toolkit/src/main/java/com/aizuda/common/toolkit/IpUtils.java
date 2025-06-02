/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.common.toolkit;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Objects;

/**
 * IP 工具类
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author 青苗
 * @since 1.1.0
 */
public class IpUtils {

    /**
     * 判断是否为本地 IP
     *
     * @param ip 待判断 IP
     */
    public static boolean isLocalIp(String ip) {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface ni = networkInterfaces.nextElement();
                Enumeration<InetAddress> e2 = ni.getInetAddresses();
                while (e2.hasMoreElements()) {
                    InetAddress ia = e2.nextElement();
                    if (ia instanceof Inet6Address) {
                        continue;
                    }
                    String address = ia.getHostAddress();
                    if (null != address && address.equals(ip)) {
                        return true;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断ip是否在指定网段
     *
     * @param ipAddress   待判断IP地址
     * @param subnetRange 子网范围 x.x.x.x/xx 形式，多个英文 , 符合分割
     */
    public static boolean isInSubnet(String ipAddress, String subnetRange) {
        if (Objects.equals("127.0.0.1", ipAddress) || Objects.equals("*.*.*.*", subnetRange)) {
            return true;
        }
        if (null != subnetRange) {
            String[] ipArray = subnetRange.split(",");
            for (String ipRange : ipArray) {
                if (Objects.equals(ipRange, ipAddress)) {
                    // 指定某个IP情况
                    return true;
                }
                String[] ips = ipAddress.split("\\.");
                // IP地址的十进制值
                int ipAddr = (Integer.parseInt(ips[0]) << 24)
                        | (Integer.parseInt(ips[1]) << 16)
                        | (Integer.parseInt(ips[2]) << 8) | Integer.parseInt(ips[3]);
                // 掩码（0-32）
                int type = 0;
                if (ipRange.contains("/")) {
                    type = Integer.parseInt(ipRange.replaceAll(".*/", ""));
                }
                // 匹配的位数为32 - type位（16进制的1）
                int mask = 0xFFFFFFFF << (32 - type);
                String cidrIp = ipRange.replaceAll("/.*", "");
                // 网段ip十进制
                String[] cidrIps = cidrIp.split("\\.");
                int cidrIpAddr = (Integer.parseInt(cidrIps[0]) << 24)
                        | (Integer.parseInt(cidrIps[1]) << 16)
                        | (Integer.parseInt(cidrIps[2]) << 8)
                        | Integer.parseInt(cidrIps[3]);
                return (ipAddr & mask) == (cidrIpAddr & mask);
            }
        }
        return false;
    }
}
