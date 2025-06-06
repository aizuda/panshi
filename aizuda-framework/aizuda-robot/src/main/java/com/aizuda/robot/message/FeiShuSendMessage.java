/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.robot.message;

import com.aizuda.common.toolkit.StringUtils;
import com.aizuda.robot.autoconfigure.RobotProperties;
import lombok.AllArgsConstructor;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;

/**
 * 飞书机器人
 * <p>
 * <a href="https://open.feishu.cn/document/ukTMukTMukTM/ucTM5YjL3ETO24yNxkjN">文档</a>
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author LoveHuahua
 * @since 1.1.0
 */
@AllArgsConstructor
public class FeiShuSendMessage extends AbstractRobotSendMessage {
    private RobotProperties robotProperties;
    private RestTemplate restTemplate;

    /**
     * 发送消息
     *
     * @param message 消息内容
     * @return true 发送成功 false 发送失败
     * @throws Exception
     */
    @Override
    public boolean send(String message) throws Exception {
        return this.request(restTemplate, new HashMap<String, Object>(4) {{
            RobotProperties.FeiShu feiShu = robotProperties.getFeiShu();
            final String secret = feiShu.getSecret();
            if (StringUtils.hasLength(secret)) {
                final long currentTimeMillis = System.currentTimeMillis() / 1000L;
                String stringToSign = currentTimeMillis + "\n" + secret;
                // 使用HmacSHA256算法计算签名
                Mac mac = Mac.getInstance("HmacSHA256");
                mac.init(new SecretKeySpec(stringToSign.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
                byte[] signData = mac.doFinal(new byte[]{});
                put("timestamp", currentTimeMillis);
                put("sign", new String(Base64.getEncoder().encode(signData)));
            }
            put("msg_type", "text");
            put("content", new HashMap<String, Object>(1) {{
                put("text", message);
            }});
        }});
    }

    /**
     * 请求发送地址
     */
    @Override
    public String getUrl() throws Exception {
        StringBuilder url = new StringBuilder();
        url.append("https://open.feishu.cn/open-apis/bot/v2/hook/");
        url.append(robotProperties.getFeiShu().getKey());
        return url.toString();
    }
}
