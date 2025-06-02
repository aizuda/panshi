package com.aizuda.common.toolkit;

public class RsaTest {

    public static void main(String[] args) throws Exception {

        doTest(RsaUtils.get1024PKCS8());
        doTest(RsaUtils.get2048PKCS8());

        doTest(RsaUtils.get1024PKCS1());
        doTest(RsaUtils.get2048PKCS1());

        // PKCS在Java环境无法测试，可以生成一对，到非java平台测试，如C#
    }

    private static void doTest(RsaUtils rsaUtils) throws Exception {
        RsaUtils.KeyStore keys = rsaUtils.createKeys();
        String pubKey = keys.getPublicKey();
        System.out.println("pubKey:");
        System.out.println(pubKey);
        String priKey = keys.getPrivateKey();
        System.out.println("priKey:");
        System.out.println(priKey);
        System.out.println("--------");

        String ming = "你好，abc123~!@=";
        // 用公钥加密
        String mi = rsaUtils.encryptByPublicKey(ming, rsaUtils.getPublicKey(pubKey));
        System.out.println("mi : " + mi);
        // 用私钥解密
        String ming2 = rsaUtils.decryptByPrivateKey(mi, rsaUtils.getPrivateKey(priKey));
        System.out.println("ming : " + ming2 + ", 结果：" + ming2.equals(ming));

        // 用私钥加密
        String mi2 = rsaUtils.encryptByPrivateKey(ming, rsaUtils.getPrivateKey(priKey));

        System.out.println("mi2 : " + mi2);
        // 用公钥解密
        String ming3 = rsaUtils.decryptByPublicKey(mi2, rsaUtils.getPublicKey(pubKey));
        System.out.println("ming3 : " + ming3 + ", 结果：" + ming3.equals(ming));
        System.out.println("---------------------");
    }
}
