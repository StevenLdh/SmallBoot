package com.supper.smallboot;

import com.handday.formless.framework.common.async.annotation.EnableContextRelatedAsync;
import com.handday.formless.framework.logrecorder.annotation.EnableLogRecorder;
import com.handday.formless.framework.swagger.annotation.EnableOpenSwagger;
import formless.framework.cloud.feign.annotation.EnableHttpDataFeignInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@EnableOpenSwagger
@EnableLogRecorder
@SpringBootApplication
@EnableHttpDataFeignInterceptor
@Slf4j
@ServletComponentScan
@EnableContextRelatedAsync
public class SmallbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmallbootApplication.class, args);
        log.info("=== smallboot start success === ");
    }
}
