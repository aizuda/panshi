/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.common.toolkit;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 签名工具类
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author 青苗
 * @since 1.1.0
 */
public class AlgorithmUtils {

    /**
     * Base64 HmacSHA256 算法签名
     *
     * @param secret 密钥
     * @param input  加密内容
     */
    public static String encodeBase64HmacSHA256(String secret, String input) throws Exception {
        return encodeBase64Hmac("HmacSHA256", secret, input);
    }

    /**
     * Base64 MAC 算法签名
     *
     * @param algorithm MAC算法支持 HmacMD5 HmacSHA1 HmacSHA256
     * @param secret    密钥
     * @param input     加密内容
     */
    public static String encodeBase64Hmac(String algorithm, String secret, String input) throws Exception {
        Mac mac = Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), algorithm));
        byte[] signData = mac.doFinal(input.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(signData);
    }
}
