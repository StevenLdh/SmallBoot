package com.supper.smallboot.infrastructure.utils;

import com.handday.formless.framework.common.apiresult.DataResult;
import com.handday.formless.framework.common.utils.JsonUtil;
import com.supper.smallboot.biz.vo.CustomerVO;
import com.supper.smallboot.infrastructure.anaotation.ExcelCell;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.List;

/**
 * @Author ldh
 * @Description
 * @Date 15:34 2022-07-22
 **/
@Slf4j
public class DemoUnit {

    /**
     * 启动类
     *
     * @author ldh
     * @date 2022-07-26 10:28
     */
    public static void main(String[] args) {
        //new DemoUnit().readResources("/settings/setting.txt");
        //creatImg();
    }
    /**
     * 测试注解解析
     *
     * @author ldh
     * @date 2022-09-29 11:43
     */
    private static void readAnnotation() {
        List<CustomerVO.CustomerInfoVO> list = new ArrayList<>();
        list.add(new CustomerVO.CustomerInfoVO().setCustomerName("111").setCustomerNum("001").setCorpId(1L));
        Class<? extends CustomerVO.CustomerInfoVO> cls = list.get(0).getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field f : fields) {
            System.out.println(f.getAnnotation(ExcelCell.class).name());
        }
    }

    /**
     * 测试读取资源文件
     *
     * @author ldh
     * @date 2022-09-29 11:44
     */
    private void readResources(String fileName) {
        try {
            InputStream inputStream = this.getClass().getResourceAsStream(fileName);
            FileUtils.getFileContent(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建图片
     *
     * @author ldh
     * @date 2022-10-11 10:04
     */
    private static void creatImg() {
        //https://test-1251330838.cos.ap-chengdu.myqcloud.com/goods-init/init_product5-1.jpg?imageView2/1
        String srcImgPath = "C:\\Users\\123\\Desktop\\IMG\\狗2.png";
        String iconPath = "C:\\Users\\123\\Desktop\\IMG\\狗3.png";
        String netSrcImgPath = "https://test-1251330838.cos.ap-chengdu.myqcloud.com/goods-init/init_product5-1.jpg?imageView2/1";
        InputStream is = null;
        OutputStream os = null;
        //生成图片
        try {
            //获取线上图片
            HttpURLConnection httpUrl = (HttpURLConnection) new URL(netSrcImgPath).openConnection();
            Image srcImg = ImageIO.read(httpUrl.getInputStream());
            //Image srcImg = ImageIO.read(new File(srcImgPath));
            BufferedImage buffImg = ImageWatermarkUtils.markImageByText("报价专用", srcImg, 10);
            //BufferedImage buffImg = ImageFileUtils.getBufferedImage(netSrcImgPath, 80, 80);
            os = new FileOutputStream(iconPath);
            if (buffImg != null) {
                ImageIO.write(buffImg, "JPG", os);
                System.out.println("图片完成添加水印文字");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (null != os) {
                    os.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
