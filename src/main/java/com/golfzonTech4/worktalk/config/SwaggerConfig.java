package com.golfzonTech4.worktalk.config;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
@SuppressWarnings("unchecked")
@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurationSupport {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    //swagger 설정.
    public Docket getDocket(String groupName, Predicate<String> predicate) {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(groupName)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.demo"))
                .paths(predicate)
                .apis(RequestHandlerSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("WorkTalk").description("사무공간 렌탈 플랫폼 WorkTalk입니다.").version("0.0.1").build();
    }

    @Bean
    public UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder().displayRequestDuration(true).validatorUrl("").build();
    }

    // API마다 구분짓기 위한 설정.
    @Bean
    public Docket memberApi() {
        return getDocket("회원", Predicates.or(
                PathSelectors.regex("/member.*")));
    }

    @Bean
    public Docket spaceApi() {
        return getDocket("사무공간", Predicates.or(
                PathSelectors.regex("/space.*")));
    }

    @Bean
    public Docket roomApi() {
        return getDocket("세부 사무공간", Predicates.or(
                PathSelectors.regex("/room.*")));

    }

    @Bean
    public Docket reservationApi() {
        return getDocket("예약", Predicates.or(
                PathSelectors.regex("/reservation.*")));

    }

    @Bean
    public Docket payApi() {
        return getDocket("결제", Predicates.or(
                PathSelectors.regex("/pay.*")));

    }

    @Bean
    public Docket qnaApi() {
        return getDocket("사무공간Q&A", Predicates.or(
                PathSelectors.regex("/qna.*")));

    }

    @Bean
    public Docket reviewApi() {
        return getDocket("후기", Predicates.or(
                PathSelectors.regex("/review.*")));

    }

    @Bean
    public Docket customerCenterApi() {
        return getDocket("1대1문의", Predicates.or(
                PathSelectors.regex("/customerCenter.*")));

    }

    @Bean
    public Docket mileageApi() {
        return getDocket("마일리지", Predicates.or(
                PathSelectors.regex("/mileage.*")));

    }

    @Bean
    public Docket panaltyApi() {
        return getDocket("패널티", Predicates.or(
                PathSelectors.regex("/panalty.*")));

    }

    @Bean
    public Docket commonApi() {
        return getDocket("공통", Predicates.or(
                PathSelectors.regex("/common.*")));

    }

    @Bean
    public Docket allApi() {
        return getDocket("전체", Predicates.or(
                PathSelectors.regex("/*.*")));
    }
}
