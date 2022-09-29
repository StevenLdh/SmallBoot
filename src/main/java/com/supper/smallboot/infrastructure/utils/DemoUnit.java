package com.supper.smallboot.infrastructure.utils;

import com.supper.smallboot.biz.vo.CustomerVO;
import com.supper.smallboot.infrastructure.anaotation.ExcelCell;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
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
      * @author ldh
      * @date 2022-07-26 10:28
      */
     public static void main(String[] args) {
         new DemoUnit().readResources("/settings/setting.txt");
     }

     /**
      * 测试注解解析
      * @author ldh
      * @date 2022-09-29 11:43
      */
     private static  void readAnnotation(){
         List<CustomerVO.CustomerInfoVO> list = new ArrayList<>();
         list.add(new CustomerVO.CustomerInfoVO().setCustomerName("111").setCustomerNum("001").setCorpId(1L));
         Class<? extends CustomerVO.CustomerInfoVO> cls = list.get(0).getClass();
         Field[] fields= cls.getDeclaredFields();
         for (Field f: fields) {
             System.out.println(f.getAnnotation(ExcelCell.class).name());
         }
     }

     /**
      * 测试读取资源文件
      * @author ldh
      * @date 2022-09-29 11:44
      */
     private void readResources(String fileName){
         try {
             InputStream inputStream = this.getClass().getResourceAsStream(fileName);
             FileUtils.getFileContent(inputStream);
         } catch (IOException e) {
             e.printStackTrace();
         }
     }
}
