package com.github.binarywang.demo.wx.mp.controller;

//import com.yj.wechat.scheduler.GetAccessTokenScheduler;
//import com.yj.wechat.utils.CommonUtil;
//import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;

/**
 * @Author: yuanj
 * @Date: 2019/1/23 14:20
 */
@RestController
public class TestController {

    private final String TOKEN = "moerlong";

//    @Autowired
//    private GetAccessTokenScheduler getAccessTokenScheduler;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(
            HttpServletRequest request,
            @RequestParam(value = "signature", required = true) String signature,
            @RequestParam(value = "timestamp", required = true) String timestamp,
            @RequestParam(value = "nonce", required = true) String nonce,
            @RequestParam(value = "echostr", required = true) String echostr
    ) {

        System.out.println(request.getRemoteAddr() + "|" + signature + "|" + timestamp + "|" + nonce + "|" + echostr);

        String sortStr = sort(TOKEN, timestamp, nonce);

        String mySignature = shal(sortStr);
        System.out.println("-----mySignature-----"+mySignature);

        if(!"".equals(signature) && !"".equals(mySignature) && signature.equals(mySignature)){
            System.out.println("-----签名校验通过-----"+echostr);
            return echostr;
        }else {
            System.out.println("-----校验签名失败-----");
        }

        return "校验签名失败";
    }

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public String test1(
            HttpServletRequest request
    ) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();

        ServletInputStream inputStream = request.getInputStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
        String line = null;
        while ((line = br.readLine()) != null) {
            stringBuilder.append(line);
        }

        System.out.println(stringBuilder.toString());

        return "<xml> <ToUserName>< ![CDATA[toUser] ]></ToUserName> <FromUserName>< ![CDATA[fromUser] ]></FromUserName> <CreateTime>12345678</CreateTime> <MsgType>< ![CDATA[text] ]></MsgType> <Content>< ![CDATA[你好] ]></Content> </xml>";
    }


    /**
     * 参数排序
     * @param token
     * @param timestamp
     * @param nonce
     * @return
     */
    public static String sort(String token, String timestamp, String nonce) {
        String[] strArray = {token, timestamp, nonce};
        Arrays.sort(strArray);
        StringBuilder sb = new StringBuilder();
        for (String str : strArray) {
            sb.append(str);
        }
        return sb.toString();
    }



    /**
     * 字符串进行shal加密
     * @param str
     * @return
     */
    public String shal(String str){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(str.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) {
        String moerlong = sort("moerlong", "201901221301123123", "3214");
        System.out.println("-----------------------"+ moerlong);

    }
}
