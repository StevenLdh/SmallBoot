package com.supper.smallboot.infrastructure.config;

import com.handday.formless.framework.common.apiresult.DataResult;
import com.supper.smallboot.infrastructure.serialization.MethodParamValidService;
import com.supper.smallboot.infrastructure.utils.StreamUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.Response;
import feign.codec.Decoder;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import feign.form.MultipartFormContentProcessor;
import feign.form.spring.SpringFormEncoder;
import feign.optionals.OptionalDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.*;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Objects;

import static feign.form.ContentType.MULTIPART;

/**
 * Created by 2022/8/03
 *
 * @author ldh
 * @version V1.0
 * @description: feign接口时加上一些配置
 */
@Slf4j
public class FeignConfig {
    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    @Autowired(required = false)
    private FeignEncoderProperties encoderProperties;

    @Autowired
    private MethodParamValidService methodParamValidService;

//    /**
//     * 自定义入参请求处理
//     */
//    @Bean
//    public RequestInterceptor erpInterceptor(@Value("${erp.openId}") String openId) {
//        return new ConfigInterceptor(openId);
//    }

    /**
     * 自定义解码器, 用来解统一返回的包装
     */
    @Bean
    public Decoder erpDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        return new CustomerDecoder(new OptionalDecoder(new ResponseEntityDecoder(
                new DefaultGzipDecoder(new SpringDecoder(messageConverters)))));
    }

    /**
     * 自定义的编码器, 用来统一hibernate校验入参
     */
    @Bean
    public Encoder validEncoder(ObjectProvider<AbstractFormWriter> formWriterProvider) {
        AbstractFormWriter formWriter = formWriterProvider.getIfAvailable();
        if (formWriter != null) {
            return new ValidEncoder(new SpringEncoder(new SpringPojoFormEncoder(formWriter),
                    this.messageConverters, encoderProperties));
        } else {
            return new ValidEncoder(new SpringEncoder(new SpringFormEncoder(), this.messageConverters,
                    encoderProperties));
        }
    }

    /**
     * 会校验feign入参的编码器, 用在调erp连接器的接口
     */
    public class ValidEncoder implements Encoder {
        private final Encoder delegate;

        public ValidEncoder(Encoder delegate) {
            this.delegate = delegate;
        }

        @Override
        public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
            valid(object, template.methodMetadata().method());
            this.delegate.encode(object, bodyType, template);
        }

        /**
         * 加个类似hibernate的校验入参
         */
        private void valid(Object obj, Method method) {
            boolean hasValidated = StreamUtils.array2ToList(method.getParameterAnnotations()).stream()
                    .anyMatch(a -> a.annotationType().equals(Validated.class));
            if (hasValidated) {
                methodParamValidService.validMethodParam(obj);
            }
        }

    }

    /**
     * erp配置接口专用返回值处理器
     */
    public static class CustomerDecoder implements Decoder {
        final Decoder delegate;
        public CustomerDecoder(Decoder delegate) {
            Objects.requireNonNull(delegate, "Decoder must not be null. ");
            this.delegate = delegate;
        }
        @Override
        public Object decode(Response response, Type type) throws IOException {
            try {
                Class<?> rawType = type instanceof ParameterizedTypeImpl ? ((ParameterizedTypeImpl) type).getRawType() : (Class<?>) type;
                if (rawType.isAssignableFrom(DataResult.class)) {
                    return delegate.decode(response, type);
                }
//                ParameterizedTypeImpl parameterizedType = ParameterizedTypeImpl.make(ErpDataResult.class, new Type[]{type}, null);
//                ErpDataResult<?> erpDataResult = (ErpDataResult<?>) delegate.decode(response, parameterizedType);
//                ErpLinkMsgEnum.ERP_LINK_ERROR.throwException(erpDataResult.getStatusCode() != 0, erpDataResult.getMessage());
//                ErpLinkMsgEnum.ERP_LINK_ERROR.throwException(erpDataResult.getResult() == null, "返回为空");
//                return erpDataResult.getResult();
                ParameterizedTypeImpl parameterizedType = ParameterizedTypeImpl.make(DataResult.class, new Type[]{type}, null);
                DataResult<?> dataResult = (DataResult<?>) delegate.decode(response, parameterizedType);
                return dataResult.getData();
            } catch (Exception ex) {
                return null;
            }
        }
    }
    /**
     *
     * @author ldh
     * @date 2022-08-03 10:22
     * @param null
     */
    private static class SpringPojoFormEncoder extends SpringFormEncoder {
        SpringPojoFormEncoder(AbstractFormWriter formWriter) {
            super();

            MultipartFormContentProcessor processor = (MultipartFormContentProcessor) getContentProcessor(
                    MULTIPART);
            processor.addFirstWriter(formWriter);
        }
    }
}
