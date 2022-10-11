package com.handday.saas.print.util;


import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by 2022/4/11 16:01
 *
 * @author hch
 * @version V1.0
 * @description: 图片获取工具
 */
@Slf4j
public class ImageFileUtils {

    /**
     * 压缩图片
     *
     * @param url    网络图片URL地址 压缩大小为 80*80
     * @param width  宽
     * @param height 高
     * @return byte数组
     */
    public static byte[] compressImageByUrl(String url, int width, int height) throws IOException {
        URL httpUrl = new URL(url);
        byte[] smallImage;
       /* HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5 * 1000);
        InputStream inStream = conn.getInputStream();//通过输入流获取图片数据*/
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Thumbnails.of(httpUrl).size(width, height).outputFormat("JPG").toOutputStream(out);
        smallImage = out.toByteArray();
        return smallImage;
    }

    /**
     * 从输入流中获取数据
     *
     * @param inStream 输入流
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }


    /**
     * 压缩图片
     *
     * @param path   图片地址
     * @param width  压缩后的宽度
     * @param height 压缩后的高度
     * @return byte
     */
    public static byte[] compressImage(String path, int width, int height) throws IOException {
        byte[] smallImage;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Thumbnails.of(path).size(width, height).outputFormat("JPG").toOutputStream(out);
        smallImage = out.toByteArray();
        return smallImage;
    }

    /**
     * 压缩图片
     *
     * @param inStream 图片输入流
     * @param width    压缩后的宽度
     * @param height   压缩后的高度
     * @return byte
     */
    public static byte[] compressImage(InputStream inStream, int width, int height) throws IOException {
        byte[] smallImage;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Thumbnails.of(inStream).size(width, height).outputFormat("JPG").toOutputStream(out);
        smallImage = out.toByteArray();
        return smallImage;
    }

    /**
     * 压缩图片
     *
     * @param bytes  图片输入byte
     * @param width  压缩后的宽度
     * @param height 压缩后的高度
     * @return byte
     */
    public static byte[] compressImage(byte[] bytes, int width, int height) throws IOException {
        byte[] smallImage;
        InputStream inputStream = new ByteArrayInputStream(bytes);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Thumbnails.of(inputStream).size(width, height).outputFormat("JPG").toOutputStream(out);
        smallImage = out.toByteArray();
        return smallImage;
    }

    /**
     * 压缩图片（图片质量压缩为85%）
     *
     * @param bytes  图片输入byte
     * @param width  压缩后的宽度
     * @param height 压缩后的高度
     * @author hch
     * @date 2022/4/19 14:23
     */
    public static BufferedImage getBufferedImage(byte[] bytes, int width, int height) throws Exception {
        InputStream inputStream = new ByteArrayInputStream(bytes);
        //压缩质量为50%
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Thumbnails.of(inputStream).size(width, height).outputQuality(0.85f).outputFormat("JPG").toOutputStream(out);
        //outputQuality 和 asBufferedImage一起使用可能导致outputQuality不会起作用，这个地方做一次转换再调用 asBufferedImage
        log.info("蓝牙打印图片压缩后大小: {}", out.toByteArray().length / 1024);
        InputStream arrayInputStream = new ByteArrayInputStream(out.toByteArray());
        //此方法可以简写成这样暂时不不做改动。
        /*BufferedImage bufferedImage = Thumbnails.of(arrayInputStream).size(width, height)
                .imageType(BufferedImage.TYPE_INT_RGB)//解决图片泛红的问题
                .outputQuality(0.50f).outputFormat("JPG")
                .asBufferedImage();*/
        return Thumbnails.of(arrayInputStream).scale(1).asBufferedImage();
    }

    /**
     * 压缩图片（图片质量压缩为85%）
     *
     * @param url    图片输入httpUrl
     * @param width  压缩后的宽度
     * @param height 压缩后的高度
     * @author hch
     * @date 2022/4/19 14:23
     */
    public static BufferedImage getBufferedImage(String url, int width, int height) throws Exception {
        if (!isNetFileAvailable(url)) return null;
        URL httpUrl = new URL(url);
        //压缩质量为50%
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Thumbnails.of(httpUrl).size(width, height).outputQuality(0.85f).outputFormat("JPG").toOutputStream(out);
        //outputQuality 和 asBufferedImage一起使用可能导致outputQuality不会起作用，这个地方做一次转换再调用 asBufferedImage
        InputStream arrayInputStream = new ByteArrayInputStream(out.toByteArray());
        log.info("蓝牙打印图片压缩后大小: {}", out.toByteArray().length / 1024);
        return Thumbnails.of(arrayInputStream).scale(1).asBufferedImage();
    }


    /**
     * 检测网络资源是否存在
     *
     * @param strUrl url地址
     */
    public static boolean isNetFileAvailable(String strUrl) {
        InputStream netFileInputStream = null;
        try {
            URL url = new URL(strUrl);
            URLConnection urlConn = url.openConnection();
            netFileInputStream = urlConn.getInputStream();
            return netFileInputStream != null;
        } catch (IOException e) {
            return false;
        } finally {
            if (netFileInputStream != null) {
                try {
                    netFileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
