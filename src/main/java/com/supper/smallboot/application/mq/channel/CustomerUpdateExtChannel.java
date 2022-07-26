package com.supper.smallboot.application.mq.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * @Author ldh
 * @Description
 * @Date 13:46 2022-07-26
 **/
public interface CustomerUpdateExtChannel {

    String INPUT="customerUpdateExt-in-0";

    String OUTPUT="customerUpdateExt-out-0";

    @Output(OUTPUT)
    MessageChannel output();

    @Input(INPUT)
    SubscribableChannel input();
}
