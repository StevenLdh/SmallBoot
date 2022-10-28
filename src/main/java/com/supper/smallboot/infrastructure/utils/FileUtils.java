package com.supper.smallboot.infrastructure.utils;

import java.io.*;

/**
 * @Author ldh
 * @Description
 * @Date 11:40 2022-09-29
 **/
public class FileUtils {
    /**
     * 根据文件路径读取文件内容
     *
     * @param fileInPath
     * @throws IOException
     */
    public static void getFileContent(Object fileInPath) throws IOException {
        BufferedReader br = null;
        if (fileInPath == null) {
            return;
        }
        if (fileInPath instanceof String) {
            br = new BufferedReader(new FileReader(new File((String) fileInPath)));
        } else if (fileInPath instanceof InputStream) {
            br = new BufferedReader(new InputStreamReader((InputStream) fileInPath));
        }
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
        br.close();
    }

    /**
     * 根据文件路径写入文件内容
     * @author ldh
     * @date 2022-10-28 16:19
     * @param fileInPath
     * @param data
     */
    public static void writeFileContent(Object fileInPath,String data) throws IOException {
        BufferedWriter bw = null;
        if (fileInPath == null) {
            return;
        }
        if (fileInPath instanceof String) {
            bw = new BufferedWriter(new FileWriter((String) fileInPath, true));
        } else if (fileInPath instanceof OutputStream) {
            bw = new BufferedWriter(new OutputStreamWriter((OutputStream) fileInPath));
        }
        assert bw != null;
        bw.write(data);
        bw.newLine();
        bw.close();
    }
}
