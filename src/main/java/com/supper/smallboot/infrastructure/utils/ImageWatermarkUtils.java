package com.supper.smallboot.infrastructure.utils;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 图片添加水印工具类 文字水印 图片水印 利用jdk ,不依赖第三方
 */
@Slf4j
public class ImageWatermarkUtils {

    static final String NEW_IMAGE_NAME_PRE_STR = "_water";
    // 水印透明度
    private static float alpha = 0.5f;
    // 水印横向位置
    private static int positionWidth = 300;
    // 水印纵向位置
    private static int positionHeight = 300;

    // 水印文字颜色
    private static Color color = new Color(226, 228, 234);
    //通过比例压缩
    private static  float scale = 0.4f;

    private static  float String  = 0.5f;

    /**
     * 字体所在windows上的路径
     */
    private static final String WINDOWS_PATH = "src/main/resources/fonts/AlibabaPuHuiTi-2-45-Light.ttf";
    /**
     * 字体所在linux上的路径
     */
    private static final String LINUX_PATH = "/usr/share/fonts/hdsaas/AlibabaPuHuiTi-2-45-Light.ttf";
    /**
     * 字体
     */
    private static String fontName="思源黑体 CN Medium";
    /**
     * @param args
     */
    public static void main(String[] args) {
        String srcImgPath = "C:\\Users\\123\\Desktop\\IMG\\WW.png";
        String iconPath =  "C:\\Users\\123\\Desktop\\IMG\\WWW.png";
        markImageByText("报价专用", srcImgPath, iconPath, 1);
    }

    /**
     * 获取字体
     *
     * @return
     */
    private static Font getFont() {
        Font font = null;
        //String fontUrl = getRunSystem() ? WINDOWS_PATH : LINUX_PATH;
        try {
            font = new Font(fontName, Font.BOLD, (int) (scale * 60));;
        } catch (Exception e) {
            Assert.assertException( "图片加水印获取字体失败");
        }
        return font;
    }

    /**
     * 获取当前项目运行时系统  windows是为true
     *
     * @return
     */
    public static boolean getRunSystem() {
        String os = System.getProperty("os.name");
        if (os != null && os.toLowerCase().startsWith("windows")) {
            return true;
        }
        return false;
    }
    /**
     * 给图片添加水印文字、可设置水印文字的旋转角度
     *
     * @param logoText
     * @param srcImgPath
     * @param targerPath
     * @param degree
     */
    public static void markImageByText(String logoText, String srcImgPath, String targerPath, Integer degree) {

        InputStream is = null;
        OutputStream os = null;

        try {
//			targerPath = srcImgPath.substring(0, srcImgPath.lastIndexOf(".")) + NEW_IMAGE_NAME_PRE_STR + "f"
//					+ srcImgPath.substring(srcImgPath.lastIndexOf("."));
            // 1、源图片
            Image srcImg = ImageIO.read(new File(srcImgPath));
            int width = (int) (scale *srcImg.getWidth(null));
            int height =  (int) (scale *srcImg.getHeight(null));
            positionWidth = width - (int) (scale *300);
            positionHeight = height - (int) (scale *80);

            BufferedImage buffImg = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);

            // 2、得到画笔对象
            Graphics2D g = buffImg.createGraphics();
            // 3、设置对线段的锯齿状边缘处理
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(srcImg.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0,
                    0, null);
            // 4、设置水印旋转
            if (null != degree) {
                g.rotate(Math.toRadians(degree), (double) buffImg.getWidth() / 2, (double) buffImg.getHeight() / 2);
            }
            // 5、设置水印文字颜色
            g.setColor(color);
            // 6、设置水印文字Font
            g.setFont(getFont());
            // 7、设置水印文字透明度
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            // 8、第一参数->设置的内容，后面两个参数->文字在图片上的坐标位置(x,y)
            g.drawString(logoText, positionWidth, positionHeight);
            // 9、释放资源
            g.dispose();
            // 10、生成图片
            os = new FileOutputStream(targerPath);
            ImageIO.write(buffImg, "JPG", os);

            System.out.println("图片完成添加水印文字");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != is)
                    is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (null != os)
                    os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
