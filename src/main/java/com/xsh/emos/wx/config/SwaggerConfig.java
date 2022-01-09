package com.xsh.emos.wx.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : xsh
 * @create : 2022-01-10 - 2:53
 * @describe:
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        ApiInfoBuilder builder = new ApiInfoBuilder();
        builder.title("EMOS在线办公系统");
        ApiInfo info = builder.build();
        docket.apiInfo(info);

        //配置哪个包下的哪些类需要Swagger-ui
        ApiSelectorBuilder selectorBuilder = docket.select();
        //PathSelectors.any()意思是扫描所有包下的所有类，不指定路径
        selectorBuilder.paths(PathSelectors.any());
        //因为上面没有指定路径扫描全部类，所以此处限定只有@ApiOperation注解的方法才添加到Swagger-Ui里
        selectorBuilder.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class));
        docket = selectorBuilder.build();

        //因为登录是基于JWT而不是Session，而有的请求是需要登录才能调用
        //所以需要在swagger里配置登录令牌在哪里；配置完成后就能在swagger里面输入token令牌调用接口（swagger-ui界面有个Authorize按钮，点击可以设置请求的令牌）
        /**
         * 告知Swagger客户端发起请求时令牌是放在请求头还是请求体，参数名是什么
         * 第一个参数指定参数名，第二个参数是描述性信息，第三个参数是是在哪里提前的令牌(表示在请求头提取参数为token的令牌)
         */
        ApiKey apiKey = new ApiKey("token", "token", "header");
        List<ApiKey> apiKeys = new ArrayList<>();
        apiKeys.add(apiKey);
        docket.securitySchemes(apiKeys);

        //设定token令牌在Swagger-Ui的作用域; 此处设置为全局
        AuthorizationScope scope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] scopes = {scope};
        SecurityReference reference = new SecurityReference("token", scopes);
        //构建SecurityContextList
        List refList = new ArrayList();
        refList.add(reference);
        SecurityContext context=SecurityContext.builder().securityReferences(refList).build();
        List ctxList = new ArrayList();
        ctxList.add(context);

        docket.securityContexts(ctxList);
        return docket;
    }
}
