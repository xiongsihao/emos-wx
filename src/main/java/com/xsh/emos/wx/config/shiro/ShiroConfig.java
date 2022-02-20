package com.xsh.emos.wx.config.shiro;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author : xsh
 * @create : 2022-02-20 - 20:24
 * @describe:
 */
@Configuration
public class ShiroConfig {

    /**
     * 封装Realm对象
     * @return
     */
    @Bean("securityManager")
    public SecurityManager securityManager(OAuth2Realm oAuth2Realm){
        DefaultWebSecurityManager securityManager=new DefaultWebSecurityManager();
        securityManager.setRealm(oAuth2Realm);
        //
        securityManager.setRememberMeManager(null);
        return securityManager;
    }

    /**
     * 封装Filter对象，设置Filter拦截路径
     * @return
     */
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager,OAuth2Filter oAuth2Filter){
        ShiroFilterFactoryBean shiroFilter=new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);
        //oauth过滤
        Map<String, Filter> filters=new HashMap<>();
        filters.put("oauth2",oAuth2Filter);
        shiroFilter.setFilters(filters);
        //设置拦截哪些路径下的http请求  anon表示对应路径不拦截，oauth2表示拦截
        Map<String,String> filterMap=new LinkedHashMap<>();
        filterMap.put("/webjars/**", "anon");
        filterMap.put("/druid/**", "anon");
        filterMap.put("/app/**", "anon");
        filterMap.put("/sys/login", "anon");
        filterMap.put("/swagger/**", "anon");
        filterMap.put("/v2/api-docs", "anon");
        filterMap.put("/swagger-ui.html", "anon");
        filterMap.put("/swagger-resources/**", "anon");
        filterMap.put("/captcha.jpg", "anon");
        filterMap.put("/user/register", "anon");
        filterMap.put("/user/login", "anon");
        filterMap.put("/test/**", "anon");
        filterMap.put("/**", "oauth2");

        shiroFilter.setFilterChainDefinitionMap(filterMap);

        return shiroFilter;
    }

    /**
     * 管理Shiro对象的生命周期
     * @return
     */
    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }

    /**
     * aop切面类，web方法执行前验证权限
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor advisor=new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

}
