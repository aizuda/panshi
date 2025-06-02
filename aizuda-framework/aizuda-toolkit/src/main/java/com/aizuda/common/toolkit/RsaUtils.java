/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.common.toolkit;

import com.baomidou.kisso.common.util.Base64Util;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;

import javax.crypto.Cipher;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Objects;

/**
 * RSA 工具类
 * <p>
 * 使用该类需要依赖包 `com.baomidou:kisso` 加密包 `org.bouncycastle:bcprov-jdk15on:1.70`
 * </p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author 青苗
 * @since 1.1.0
 */
public final class RsaUtils {
    public final static String PKCS1 = "RSA/ECB/PKCS1Padding";
    public final static String RSA = "RSA";

    private String keyFormat;
    private int keyLength;

    private RsaUtils(String keyFormat, int keyLength) {
        this.keyFormat = keyFormat;
        this.keyLength = keyLength;
        java.security.Security.addProvider(
                new org.bouncycastle.jce.provider.BouncyCastleProvider()
        );
    }

    public static RsaUtils newInstance(String keyFormat, int keyLength) {
        return new RsaUtils(keyFormat, keyLength);
    }

    public static RsaUtils get1024PKCS1() {
        return newInstance(PKCS1, 1024);
    }

    public static RsaUtils get2048PKCS1() {
        return newInstance(PKCS1, 2048);
    }

    public static RsaUtils get1024PKCS8() {
        return newInstance(RSA, 1024);
    }

    public static RsaUtils get2048PKCS8() {
        return newInstance(RSA, 2048);
    }

    /**
     * 创建公钥私钥
     *
     * @return 返回公私钥对
     * @throws Exception
     */
    public KeyStore createKeys() throws Exception {
        KeyPairGenerator keyPairGeno = KeyPairGenerator.getInstance(RSA);
        keyPairGeno.initialize(keyLength);
        KeyPair keyPair = keyPairGeno.generateKeyPair();

        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        KeyStore keyStore = new KeyStore();
        if (Objects.equals(PKCS1, this.keyFormat)) {
            keyStore.setPublicKey(Base64Util.encode(publicKey.getEncoded()));
            keyStore.setPrivateKey(convertPkcs8ToPkcs1(privateKey.getEncoded()));
        } else {
            keyStore.setPublicKey(Base64Util.encode(publicKey.getEncoded()));
            keyStore.setPrivateKey(Base64Util.encode(privateKey.getEncoded()));
        }
        return keyStore;
    }

    /**
     * 获取公钥对象
     *
     * @param pubKeyData 公钥
     * @return 返回公钥对象
     * @throws Exception
     */
    public RSAPublicKey getPublicKey(byte[] pubKeyData) throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pubKeyData);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

    /**
     * 获取公钥对象
     *
     * @param pubKey 公钥
     * @return 返回私钥对象
     * @throws Exception
     */
    public RSAPublicKey getPublicKey(String pubKey) throws Exception {
        return getPublicKey(Base64Util.decode(pubKey));

    }

    /**
     * 获取私钥对象
     *
     * @param priKey 私钥
     * @return 私钥对象
     * @throws Exception
     */
    public RSAPrivateKey getPrivateKey(String priKey) throws Exception {
        return getPrivateKey(Base64Util.decode(priKey));
    }

    /**
     * 通过私钥byte[]将公钥还原，适用于RSA算法
     *
     * @param keyBytes
     * @return 返回私钥
     * @throws Exception
     */
    public RSAPrivateKey getPrivateKey(byte[] keyBytes) throws Exception {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);

    }

    public Cipher getCipher(String transformation, int mode, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(mode, key);
        return cipher;
    }

    /**
     * 公钥加密
     *
     * @param data      待加密内容
     * @param publicKey 公钥
     * @return 返回密文
     * @throws Exception
     */
    public String encryptByPublicKey(String data, RSAPublicKey publicKey) throws Exception {
        Cipher cipher = this.getCipher(keyFormat, Cipher.ENCRYPT_MODE, publicKey);
        // 模长
        int key_len = publicKey.getModulus().bitLength() / 8;
        // 加密数据长度 <= 模长-11
        String[] dataArr = splitString(data, key_len - 11);
        String text = "";
        // 如果明文长度大于模长-11则要分组加密
        for (String s : dataArr) {
            text += bcd2Str(cipher.doFinal(s.getBytes()));
        }
        return text;
    }

    public String encryptByPrivateKey(String data, String privateKey) throws Exception {
        return encryptByPrivateKey(data, getPrivateKey(privateKey));
    }

    /**
     * 私钥加密
     *
     * @param data       待加密数据
     * @param privateKey 私钥
     * @return 返回密文
     * @throws Exception
     */
    public String encryptByPrivateKey(String data, RSAPrivateKey privateKey) throws Exception {
        Cipher cipher = this.getCipher(keyFormat, Cipher.ENCRYPT_MODE, privateKey);
        // 模长
        int key_len = privateKey.getModulus().bitLength() / 8;
        // 加密数据长度 <= 模长-11
        String[] dataArr = splitString(data, key_len - 11);
        String text = "";
        // 如果明文长度大于模长-11则要分组加密
        for (String s : dataArr) {
            text += bcd2Str(cipher.doFinal(s.getBytes()));
        }
        return text;
    }

    public String decryptByPrivateKey(String data, String privateKey) throws Exception {
        return decryptByPrivateKey(data, getPrivateKey(privateKey));
    }

    /**
     * 私钥解密
     *
     * @param data       待解密内容
     * @param privateKey 私钥
     * @return 返回明文
     * @throws Exception
     */
    public String decryptByPrivateKey(String data, RSAPrivateKey privateKey) throws Exception {
        Cipher cipher = this.getCipher(keyFormat, Cipher.DECRYPT_MODE, privateKey);
        // 模长
        int key_len = privateKey.getModulus().bitLength() / 8;
        byte[] bytes = data.getBytes();
        byte[] bcd = ASCII_To_BCD(bytes, bytes.length);
        // 如果密文长度大于模长则要分组解密
        String text = "";
        byte[][] arrays = splitArray(bcd, key_len);
        for (byte[] arr : arrays) {
            text += new String(cipher.doFinal(arr));
        }
        return text;
    }

    /**
     * 公钥解密
     *
     * @param data         待解密内容
     * @param rsaPublicKey 公钥
     * @return 返回明文
     * @throws Exception
     */
    public String decryptByPublicKey(String data, RSAPublicKey rsaPublicKey) throws Exception {
        Cipher cipher = this.getCipher(keyFormat, Cipher.DECRYPT_MODE, rsaPublicKey);
        // 模长
        int key_len = rsaPublicKey.getModulus().bitLength() / 8;
        byte[] bytes = data.getBytes();
        byte[] bcd = ASCII_To_BCD(bytes, bytes.length);
        // 如果密文长度大于模长则要分组解密
        String text = "";
        byte[][] arrays = splitArray(bcd, key_len);
        for (byte[] arr : arrays) {
            text += new String(cipher.doFinal(arr));
        }
        return text;
    }

    public static String convertPkcs8ToPkcs1(byte[] privateKeyData) throws Exception {
        PrivateKeyInfo pkInfo = PrivateKeyInfo.getInstance(privateKeyData);
        ASN1Encodable asn1Encodable = pkInfo.parsePrivateKey();
        ASN1Primitive primitive = asn1Encodable.toASN1Primitive();
        byte[] privateKeyPKCS1 = primitive.getEncoded();
        return Base64Util.encode(privateKeyPKCS1);
    }


    /**
     * ASCII码转BCD码
     */
    public static byte[] ASCII_To_BCD(byte[] ascii, int asc_len) {
        byte[] bcd = new byte[asc_len / 2];
        int j = 0;
        for (int i = 0; i < (asc_len + 1) / 2; i++) {
            bcd[i] = asc_to_bcd(ascii[j++]);
            bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++]) & 0xff) + (bcd[i] << 4));
        }
        return bcd;
    }

    public static byte asc_to_bcd(byte asc) {
        byte bcd;

        if ((asc >= '0') && (asc <= '9')) {
            bcd = (byte) (asc - '0');
        } else if ((asc >= 'A') && (asc <= 'F')) {
            bcd = (byte) (asc - 'A' + 10);
        } else if ((asc >= 'a') && (asc <= 'f')) {
            bcd = (byte) (asc - 'a' + 10);
        } else {
            bcd = (byte) (asc - 48);
        }
        return bcd;
    }

    /**
     * BCD转字符串
     */
    public String bcd2Str(byte[] bytes) {
        char[] temp = new char[bytes.length * 2];
        char val;

        for (int i = 0; i < bytes.length; i++) {
            val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
            temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

            val = (char) (bytes[i] & 0x0f);
            temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
        }
        return new String(temp);
    }

    /**
     * 拆分字符串
     */
    public String[] splitString(String string, int len) {
        int x = string.length() / len;
        int y = string.length() % len;
        int z = 0;
        if (y != 0) {
            z = 1;
        }
        String[] strings = new String[x + z];
        String str = "";
        for (int i = 0; i < x + z; i++) {
            if (i == x + z - 1 && y != 0) {
                str = string.substring(i * len, i * len + y);
            } else {
                str = string.substring(i * len, i * len + len);
            }
            strings[i] = str;
        }
        return strings;
    }

    /**
     * 拆分数组
     */
    public byte[][] splitArray(byte[] data, int len) {
        int x = data.length / len;
        int y = data.length % len;
        int z = 0;
        if (y != 0) {
            z = 1;
        }
        byte[][] arrays = new byte[x + z][];
        byte[] arr;
        for (int i = 0; i < x + z; i++) {
            arr = new byte[len];
            if (i == x + z - 1 && y != 0) {
                System.arraycopy(data, i * len, arr, 0, y);
            } else {
                System.arraycopy(data, i * len, arr, 0, len);
            }
            arrays[i] = arr;
        }
        return arrays;
    }

    public static class KeyStore {
        private String publicKey;
        private String privateKey;

        public String getPublicKey() {
            return publicKey;
        }

        public void setPublicKey(String publicKey) {
            this.publicKey = publicKey;
        }

        public String getPrivateKey() {
            return privateKey;
        }

        public void setPrivateKey(String privateKey) {
            this.privateKey = privateKey;
        }
    }
}
