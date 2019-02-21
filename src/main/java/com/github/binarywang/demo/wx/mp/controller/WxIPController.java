package com.github.binarywang.demo.wx.mp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取微信服务器IP地址
 * wiki:https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140187
 */
@Controller
public class WxIPController {

    @Autowired
    private RestTemplate restTemplate;

    //http://u3au8w.natappfree.cc/getIp?accessToken=
    @ResponseBody
    @RequestMapping(value="/getIp",method = RequestMethod.GET)
    public String getAccessToken(HttpServletRequest request,
                                 @RequestParam(value = "accessToken", required = true) String accessToken){

        String result = restTemplate.getForObject("https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=" + accessToken, String.class);
        return result;
    }
}
