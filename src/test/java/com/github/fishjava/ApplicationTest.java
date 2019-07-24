package com.github.fishjava;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Test;

/**
 * 读取配置文件的信息，创建securityManager作为全局变量
 *
 * 通过subject来模拟用户，承担登录登出。
 */
public class ApplicationTest {
    @Test
    public void login(){
        // 读取配置文件内容
        Factory<SecurityManager> factory =
                new IniSecurityManagerFactory("classpath:shiro.ini");

        // 创建manger
        SecurityManager securityManager = factory.getInstance();

        // 将manger设置到全局变量中
        SecurityUtils.setSecurityManager(securityManager);

        // 尝试登录
        tryLogin("Jon", "123");
        Subject subject = tryLogin("bitfish", "123456");

        if (subject.isAuthenticated()){
            subject.logout();
            System.out.println("退出登录");
            System.out.println(subject.isAuthenticated());
        }

    }

    private Subject tryLogin(String username, String password){
        // 创建一个用户
        Subject subject = SecurityUtils.getSubject();

        // 创建一个验证对象
        UsernamePasswordToken token =
                new UsernamePasswordToken(username, password);

        // 通过这个验证信息来登录
        try{
            subject.login(token);
        } catch (AuthenticationException e){
            // 登录失败
            e.printStackTrace();
        }

        if (subject.isAuthenticated()){
            System.out.println(username + ": "+ password + " 登录成功");
        } else {
            System.out.println(username + ": "+ password + " 登录失败");
        }

        return subject;
    }
}
