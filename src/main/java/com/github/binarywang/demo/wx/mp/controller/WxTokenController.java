package com.github.binarywang.demo.wx.mp.controller;

import com.github.binarywang.demo.wx.mp.utils.AccessToken;
import com.github.binarywang.demo.wx.mp.utils.WeixinUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * accessToken
 */
@RestController
public class WxTokenController {

    @Autowired
    private RestTemplate restTemplate;

//    @Value(value = "${wx.mp.configs.appId}")
//    String appid;
//    @Value(value = "${wx.mp.configs.secret}")
//    String secret;

    @ResponseBody
    @RequestMapping(value="/getAccessToken",method = RequestMethod.GET)
    public String getAccessToken( HttpServletRequest request,
                                  @RequestParam(value = "appid", required = true) String appid,
                                  @RequestParam(value = "secret", required = true) String secret){

//        String appid = "wx6a184598251d71b9";
//        String secret = "c1863dcf622652de366ff928b06ee2b9";

        AccessToken accessToken = WeixinUtil.getAccessToken(appid, secret);
        return accessToken.getToken();
//        String result = restTemplate.getForObject("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appid + "&secret=" + secret, String.class);
//        return result;
    }
}
