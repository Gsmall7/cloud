package com.lyl.study.cloud.base.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lyl.study.cloud.base.controller.MaintainErrorController;
import com.lyl.study.cloud.base.exception.handler.CommonExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Configurable
@ConditionalOnWebApplication
@Import({MaintainErrorController.class, CommonExceptionHandler.class})
@AutoConfigureBefore(name = "org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration")
@AutoConfigureAfter(name = "com.lyl.study.cloud.base.config.SystemCommonConfig")
@ConditionalOnClass(name = "org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter")
public class WebCommonConfig {
    /**
     * Get请求时间戳转换时间类型
     *
     * @return 时间戳类型转换器
     */
    @Bean
    public Converter<String, Date> convertDateTime() {
        return new Converter<String, Date>() {
            @Override
            public Date convert(String source) {
                return new Date(Long.parseLong(source));
            }
        };
    }

    @Configuration
    @ConditionalOnBean(ObjectMapper.class)
    static class WebMvcConfig extends WebMvcConfigurerAdapter {
        @Autowired
        private ObjectMapper objectMapper;

        @Override
        public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
            /**
             * 排除掉Spring自带的MappingJackson2HttpMessageConverter
             * 使用项目定义的ObjectMapper作为HttpMessage的Json序列化工具
             */
            List<MappingJackson2HttpMessageConverter> originalConverters = new ArrayList<>();
            for (HttpMessageConverter<?> converter : converters) {
                if (converter instanceof MappingJackson2HttpMessageConverter) {
                    originalConverters.add((MappingJackson2HttpMessageConverter) converter);
                }
            }
            if (!originalConverters.isEmpty()) {
                converters.removeAll(originalConverters);
            }
            MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
            jackson2HttpMessageConverter.setObjectMapper(objectMapper);
            converters.add(jackson2HttpMessageConverter);
        }
    }
}
