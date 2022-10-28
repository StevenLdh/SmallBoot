package com.supper.smallboot.infrastructure.utils;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.*;

/**
 * @Author ldh
 * @Description 用于模拟登录
 * @Date 17:09 2022-10-21
 **/
public class LoginApp {
    //DEV pc登录地址
    private static String pcEnv = "https://dev-hdsaas.facehand.cn/pmweb/home?token=";
    //DEV H5登录地址
    private static String h5Env = "https://dev-hdsaas.facehand.cn/dhwap/?token=";
    //DEV 接口地址
    private static String apiUrl = "https://dev-hdsaas.facehand.cn/gateway";
    /**
     * 启动项
     * @author ldh
     * @date 2022-10-25 10:31
     * @param args
     */
    public static void main(String[] args) {
        loginMethod();
    }

    /**
     * 用于登录使用
     *
     * @author ldh
     * @date 2022-10-21 13:39
     */
    private static void loginMethod() {
        System.out.println("--------------------用于登录使用开始----------------------------------");
        Scanner input = new Scanner(System.in);
        System.out.println("是否使用默认账号(dev pc 150000004 李德华)(是：1，否：0)");
        String temp = input.next();
        if (temp.equals("1")) {
            loginDefault(input);
        } else {
            loginSelect(input);
        }
        input.close();
        System.out.println("--------------------用于登录使用结束----------------------------------");
    }

    /**
     * 使用默认账号登录
     *
     * @param input
     * @author ldh
     * @date 2022-10-25 10:20
     */
    public static void loginDefault(Scanner input) {
        String token = getToken("wwb41f00107f94f73d", "LiDeHua", 150000004L, apiUrl);
        if (StringUtils.isNotEmpty(token)) {
            openUrl(String.format("%s%s", pcEnv, token));
        } else {
            System.out.println("获取token失败");
        }
    }

    /**
     * 选择登录方式
     *
     * @param input
     * @author ldh
     * @date 2022-10-25 10:15
     */
    public static void loginSelect(Scanner input) {
        String val = null;
        System.out.println("请输入需要登录的环境(例如：dev,fat,pre)：");
        val = input.next();
        switch (val) {
            case "fat":
                pcEnv = pcEnv.replace("dev", "fat");
                h5Env = h5Env.replace("dev", "fat");
                apiUrl = apiUrl.replace("dev", "fat");
                break;
            case "pre":
                pcEnv = pcEnv.replace("dev", "pre");
                h5Env = h5Env.replace("dev", "pre");
                apiUrl = apiUrl.replace("dev", "pre");
                break;
            default:
                break;
        }
        System.out.println("请输入需要登录的企业账号,如果不输入默认为(例如：150000000)：");
        //获取职员信息
        Long corpId = Long.parseLong(input.next());
        String getStafferInfo = String.format("%s/saas-statistics-service/api/v1/test/get_staffer?corpId=%s", apiUrl, corpId);
        String stafferData = OkHttpUtil.get(getStafferInfo, null);
        List<StafferVo> list = new ArrayList<>();
        if (StringUtils.isNotEmpty(stafferData)) {
            DataResult temp = JSON.parseObject(stafferData, DataResult.class);
            if (Objects.nonNull(temp.getData()) && temp.getData().size() > 0) {
                list = temp.getData();
            } else {
                System.out.println("获取地址职员列表信息失败");
                input.close(); // 关闭资源
                return;
            }
        } else {
            System.out.println("获取地址职员列表信息失败");
            input.close(); // 关闭资源
            return;
        }
        System.out.println("请选择职员信息：");
        int size = Math.min(list.size(), 10);
        for (int i = 0; i < size; i++) {
            System.out.printf("%s---%s%n", list.get(i).getStafferName(), list.get(i).getWxUserId());
        }
        System.out.println("请输入职员名称：");
        String stafferName = input.next();
        Optional<StafferVo> optional = list.stream().filter(p -> p.getStafferName().equals(stafferName)).findFirst();
        String wxCorpId = "";
        String wxUserId = "";
        if (optional.isPresent()) {
            wxUserId = optional.get().getWxUserId();
            wxCorpId = optional.get().getWxCorpId();
        } else {
            System.out.println("获取地址职员信息失败");
            input.close(); // 关闭资源
            return;
        }
        String pcUrl = "";
        String h5Url = "";
        String token = getToken(wxCorpId, wxUserId, corpId, apiUrl);
        if (StringUtils.isNotEmpty(token)) {
            pcUrl = String.format("%s%s", pcEnv, token);
            h5Url = String.format("%s%s", h5Env, token);
            System.out.printf("PC地址：%s%n", pcUrl);
            System.out.printf("H5地址：%s%n", h5Url);
        } else {
            System.out.println("获取地址token失败");
            input.close(); // 关闭资源
            return;
        }
        System.out.println("请输出要登录的端（pc,h5）");
        String temp = input.next();
        String url = "";
        if (temp.equals("pc")) {
            url = pcUrl;
        } else if (temp.equals("h5")) {
            url = h5Url;
        } else {
            System.out.println("请输入正确的打开端");
            input.close(); // 关闭资源
            return;
        }
        openUrl(url);
    }
    /**
     * 获取token数据
     *
     * @param wxCorpId
     * @param wxUserId
     * @param corpId
     * @author ldh
     * @date 2022-10-25 9:53
     */
    private static String getToken(String wxCorpId, String wxUserId, Long corpId, String apiUrl) {
        String getTokenUrl = String.format("%s/oauth-service/oauth/token?wxCorpId=%s&wxUserId=%s&grant_type=work_wx_authentication&client_id=40087685617893436&client_secret=40087685617893436&corpId=%s", apiUrl, wxCorpId, wxUserId, corpId);
        String data = OkHttpUtil.postFormParams(getTokenUrl, null);
        if (StringUtils.isNotEmpty(data)) {
            UserTokenVO uv = JSON.parseObject(data, UserTokenVO.class);
            return uv.getData().getAccess_token();
        } else {
            return "";
        }
    }

    /**
     * 打开地址
     *
     * @param url
     * @author ldh
     * @date 2022-10-25 9:55
     */
    private static void openUrl(String url) {
        try {
            java.net.URI uri = java.net.URI.create(url);
            // 获取当前系统桌面扩展
            java.awt.Desktop dp = java.awt.Desktop.getDesktop();
            // 判断系统桌面是否支持要执行的功能
            if (dp.isSupported(java.awt.Desktop.Action.BROWSE)) {
                // 获取系统默认浏览器打开链接
                dp.browse(uri);
            }
        } catch (NullPointerException | IOException e) {
            // 此为uri为空时抛出异常
            e.printStackTrace();
        }
    }


    @Data
    @Accessors(chain = true)
    public static class DataResult {

        private String code;

        private String message;

        private List<StafferVo> data;
    }


    @Data
    @Accessors(chain = true)
    public static class UserTokenVO {

        private UserToken data;
    }

    @Data
    @Accessors(chain = true)
    public static class UserToken {

        private String access_token;

        private String token_type;

        private String refresh_token;
    }


    @Data
    public static class StafferVo {
        private String stafferName;

        private String wxCorpId;

        private String wxUserId;
    }
}
