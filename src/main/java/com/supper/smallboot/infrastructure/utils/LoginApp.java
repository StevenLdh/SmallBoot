package com.supper.smallboot.infrastructure.utils;

import com.handday.formless.framework.common.apiresult.DataResult;
import com.handday.formless.framework.common.utils.JsonUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @Author ldh
 * @Description
 * @Date 17:09 2022-10-21
 **/
public class LoginApp {
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
        String val = null;
        String pcEnv = "https://dev-hdsaas.facehand.cn/pmweb/home?token=";
        String h5Env = "https://dev-hdsaas.facehand.cn/dhwap/?token=";
        String apiUrl = "https://dev-hdsaas.facehand.cn/gateway";
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
        System.out.println("请输入需要登录的企业账号(例如：150000000)：");
        Long corpId = Long.parseLong(input.next());
        String getStafferInfo = String.format("%s/saas-statistics-service/api/v1/test/get_staffer?corpId=%s", apiUrl, corpId);
        String stafferData = OkHttpUtil.get(getStafferInfo, null);
        List<StafferVo> list = new ArrayList<>();
        if (StringUtils.isNotEmpty(stafferData)) {
            DataResult result = JsonUtil.toObject(stafferData, DataResult.class);
            if(Objects.nonNull(result.getData())){
                list =JsonUtil.toList(JsonUtil.toJSONString(result.getData()),StafferVo.class);
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
        String getTokenUrl = String.format("%s/oauth-service/oauth/token?wxCorpId=%s&wxUserId=%s&grant_type=work_wx_authentication&client_id=40087685617893436&client_secret=40087685617893436&corpId=%s", apiUrl, wxCorpId, wxUserId, corpId);
        String data = OkHttpUtil.postFormParams(getTokenUrl, null);
        String pcUrl = "";
        String h5Url = "";
        if (StringUtils.isNotEmpty(data)) {
            UserTokenVO uv = JsonUtil.toObject(data, UserTokenVO.class);
            pcUrl = String.format("%s%s", pcEnv, uv.getData().getAccess_token());
            h5Url = String.format("%s%s", h5Env, uv.getData().getAccess_token());
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
        try {
            java.net.URI uri = java.net.URI.create(url);
            // 获取当前系统桌面扩展
            java.awt.Desktop dp = java.awt.Desktop.getDesktop();
            // 判断系统桌面是否支持要执行的功能
            if (dp.isSupported(java.awt.Desktop.Action.BROWSE)) {
                // 获取系统默认浏览器打开链接
                dp.browse(uri);
            }
        } catch (java.lang.NullPointerException e) {
            // 此为uri为空时抛出异常
            e.printStackTrace();
        } catch (java.io.IOException e) {
            // 此为无法获取系统默认浏览器
            e.printStackTrace();
        } finally {
            input.close(); // 关闭资源
        }
        System.out.println("--------------------用于登录使用结束----------------------------------");
    }


    @ApiModel(description = "客户资料")
    @Data
    @Accessors(chain = true)
    public static class UserTokenVO {

        @ApiModelProperty(value = "data", required = true)
        private UserToken data;
    }
    @ApiModel(description = "客户资料")
    @Data
    @Accessors(chain = true)
    public static class UserToken {

        @ApiModelProperty(value = "access_token", required = true)
        private String access_token;

        @ApiModelProperty(value = "token_type", required = true)
        private String token_type;

        @ApiModelProperty(value = "refresh_token", required = true)
        private String refresh_token;
    }


    @ApiModel("获取职员信息vo")
    @Data
    public static class StafferVo {
        @ApiModelProperty("职员名称")
        private String stafferName;

        @ApiModelProperty("wxCorpId")
        private String wxCorpId;

        @ApiModelProperty("wxUserId")
        private String wxUserId;
    }
}
