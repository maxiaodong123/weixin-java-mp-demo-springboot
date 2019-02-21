package com.github.binarywang.demo.wx.mp.controller;

import com.github.binarywang.demo.wx.mp.utils.AccessToken;
import com.github.binarywang.demo.wx.mp.utils.ParamesAPI;
import com.github.binarywang.demo.wx.mp.utils.WeixinUtil;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@Controller
public class OauthController {

    public static String GET_CODE = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=CORPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_userinfo&state=a123#wechat_redirect";
    /**
     * 企业获取code地址处理
     *
     * */
    public static String getCode() {
        String get_code_url = "";
        get_code_url = GET_CODE.replace("CORPID", ParamesAPI.appId).replace("REDIRECT_URI", WeixinUtil.URLEncoder(ParamesAPI.REDIRECT_URI));
        try{
            JSONObject jsonobject = WeixinUtil.httpRequest(get_code_url, "GET", null);

            String code = "";
            if (null != jsonobject) {
                code = jsonobject.getString("code");
                if (!"".equals(code)) {
                    System.out.println("获取信息成功，o(∩_∩)o ————UserID:" + code);
                } else {
                    int errorrcode = jsonobject.getInt("errcode");
                    String errmsg = jsonobject.getString("errmsg");
                    System.out.println("错误码：" + errorrcode + "————" + "错误信息：" + errmsg);
                }
            } else {
                System.out.println("获取授权失败了，●﹏●，自己找原因。。。");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

//        return UserId;
        return get_code_url;
    }

    public static String CODE_TO_USERINFO = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=ACCESS_TOKEN&code=CODE&agentid=AGENTID";
//	https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN
    /**
     * 根据code获取成员信息
     *
     * @param access_token
     *            调用接口凭证
     * @param code
     *            通过员工授权获取到的code，每次员工授权带上的code将不一样，code只能使用一次，5分钟未被使用自动过期
     * @param agentid
     *            跳转链接时所在的企业应用ID 管理员须拥有agent的使用权限；agentid必须和跳转链接时所在的企业应用ID相同
     * */
    public static String getUserID(String access_token, String code, String agentid) {
        String UserId = "";
        CODE_TO_USERINFO = CODE_TO_USERINFO.replace("ACCESS_TOKEN", access_token).replace("CODE", code).replace("AGENTID", agentid);
        JSONObject jsonobject = WeixinUtil.httpRequest(CODE_TO_USERINFO, "GET", null);
        if (null != jsonobject) {
            UserId = jsonobject.getString("UserId");
            if (!"".equals(UserId)) {
                System.out.println("获取信息成功，o(∩_∩)o ————UserID:" + UserId);
            } else {
                int errorrcode = jsonobject.getInt("errcode");
                String errmsg = jsonobject.getString("errmsg");
                System.out.println("错误码：" + errorrcode + "————" + "错误信息：" + errmsg);
            }
        } else {
            System.out.println("获取授权失败了，●﹏●，自己找原因。。。");
        }
        return UserId;
    }

    // 示例
    public static void main(String[] args) {

        String access_token = WeixinUtil.getAccessToken(ParamesAPI.appId, ParamesAPI.secret).getToken();
        // 地址
        String code = getCode();
        System.out.println("ssssssssssss----" + code);

    }

    /**
     * 用户允许授权获取code值
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/auth")
    @ResponseBody
    public void GuideServlet(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 设置编码
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        /**
         * 第一步：用户同意授权，获取code:https://open.weixin.qq.com/connect/oauth2/authorize
         * ?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE
         * &state=STATE#wechat_redirect
         */
        String redirect_uri = URLEncoder.encode(
            "http://1.1.1.1/wechatServer/login", "UTF-8");// 授权后重定向的回调链接地址，请使用urlencode对链接进行处理（文档要求）
        // 按照文档要求拼接访问地址
//        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
//            + ParamesAPI.appId
//            + "&redirect_uri="
//            + redirect_uri
//            + "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";

        String url = GET_CODE.replace("CORPID", ParamesAPI.appId).replace("REDIRECT_URI", WeixinUtil.URLEncoder(ParamesAPI.REDIRECT_URI));

        response.sendRedirect(url);// 跳转到要访问的地址


    }

    /**
     * 获取用户openid 及信息
     * @param request
     * @return
     * @throws JSONException
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/login")
    public String login(HttpServletRequest request) throws JSONException, IOException {
        String code = request.getParameter("code");
        System.out.println("终于获取到CODE了:" + code);
        /**
         * 第三步：通过code换取网页授权access_token
         */
        // 同意授权
        if (code != null) {
            // 拼接请求地址
            String url = "https://api.weixin.qq.com/sns/oauth2/access_token?"
                + "appid=" + ParamesAPI.appId + "&secret="
                + ParamesAPI.secret
                + "&code=" + code
                + "&grant_type=authorization_code";
            JSONObject jsonobject = WeixinUtil.httpRequest(url, "GET", null);
            String openid = "";
            String token = "";
            // 如果请求成功
            if (null != jsonobject) {
                try {
                    openid = (String) jsonobject.get("openid");
                    token = (String)jsonobject.get("access_token");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
//            JSONObject json =ReadUrlUtil.readJsonFromUrl(url, "");// 拿去返回值
//            System.out.println("返回信息:"+json);
//            AutoWebParams autoWebParams = (AutoWebParams) JSONObject.toBean(json, AutoWebParams.class);
            /**
             * 第四步：拉取用户信息(需scope为 snsapi_userinfo)001MeAlp01IRjp1LlKkp0zPLlp0MeAl-
             */

//            AccessToken at = WeixinUtil.getAccessToken(ParamesAPI.appId, ParamesAPI.secret);
//            String token = "18_sC0wzE0D224yFRwAnBrLsRzqngRxfDNKMXCpLbsGJSbsv3fgQjCdYoOPL4myUnUC_fK3eCu7-NutQUgpWFHlJk0hqsoWuYq2xh2cs4Ssmih_UAl-vIW5Dd9ZdpBX7NOf3DO3Cqybash1WhK-GSJdAGAZLC";

            String url3 = "https://api.weixin.qq.com/sns/userinfo?access_token="
//                + at.getToken()
                + token
                + "&openid="
                + openid + "&lang=zh_CN";
            JSONObject jsonobject2 = WeixinUtil.httpRequest(url3, "GET", null);
//            JSONObject json1 =ReadUrlUtil.readJsonFromUrl(url3, "");// 拿去返回值
//            UserInfo userInfo = (UserInfo) JSONObject.toBean(json1, UserInfo.class);
            System.out.println("用户头像:"+jsonobject2.get("headimgurl"));
            System.out.println("用户昵称:"+jsonobject2.get("nickname"));
        }
        return null;
    }


}
