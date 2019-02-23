package com.github.binarywang.demo.wx.mp.job;

import com.github.binarywang.demo.wx.mp.utils.AccessToken;
import com.github.binarywang.demo.wx.mp.utils.WeixinUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class WechatJob {

//    @Value(value = "${wechat.appId}")
//    String appid;
//    @Value(value = "${wechat.secret}")
//    String secret;

//    @Scheduled(cron = "${job.wechat_access_token.cron}")
////    @Scheduled(cron = "cron = 0 0/1 * * * ? ")
//    public void wechatAccessToken(){
//
//        String appid = "wxa64b9ed20f0e49c5";
//        String secret = "a78e2ef6d68dc285ec6d6bd295b82536";
//        String access_token = "";//凭据
//        int expires_in = -1;//凭据有效时间
//        String ticket = "";
//        int ticket_expires_in = -1;
//        try {
//            AccessToken accessToken = WeixinUtil.getAccessToken(appid, secret);
//            access_token = accessToken.getToken();
//            expires_in = accessToken.getExpiresIn();
//            System.out.println("access_token为" + access_token + "，超时时间为：" + expires_in);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
