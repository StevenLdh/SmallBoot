package com.supper.smallboot.application.mq.subscribe;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.supper.smallboot.application.mq.channel.CustomerUpdateExtChannel;
import com.supper.smallboot.biz.dto.CustomerDTO;
import io.swagger.v3.oas.annotations.media.Encoding;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import java.lang.reflect.Type;

/**
 * @Author ldh
 * @Description
 * @Date 13:57 2022-07-26
 **/
@EnableBinding({CustomerUpdateExtChannel.class})
@Slf4j
public class CustomerUpdateExtSubscribe {

    /**
     * 接收消息
     * @author ldh
     * @date 2022-07-26 14:17
     * @param message
     */
    @StreamListener(CustomerUpdateExtChannel.INPUT)
    public void processCustomerUpdateExt(String message) throws Exception{
        log.info("接收到消息：" + message);
        CustomerDTO.CustomerInfoDTO dto = new  CustomerDTO.CustomerInfoDTO();
        if (StringUtils.isNotBlank(message)) {
            Type LIST_STRING = new TypeReference< CustomerDTO.CustomerInfoDTO>() {
            }.getType();
            dto = JSON.parseObject(message, LIST_STRING);
        }
    }
}
