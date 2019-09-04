package com.tttare.springDemo.userCenter.service.impl;

import com.tttare.springDemo.model.LoginResult;
import com.tttare.springDemo.userCenter.service.LoginService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;
/**
 * ClassName: LoginServiceImpl <br/>
 * Description: <br/>
 * date: 2019/9/3 23:46<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Override
    public LoginResult login(String userName, String password) {
        LoginResult loginResult = new LoginResult();
        if(userName==null || userName.isEmpty())
        {
            loginResult.setLogin(false);
            loginResult.setResult("用户名为空");
            return loginResult;
        }
        String msg="";
        // 1、获取Subject实例对象
        Subject currentUser = SecurityUtils.getSubject();

//        // 2、判断当前用户是否登录
//        if (currentUser.isAuthenticated() == false) {
//
//        }

        // 3、将用户名和密码封装到UsernamePasswordToken
        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);

        // 4、认证
        try {
            currentUser.login(token);// 传到MyAuthorizingRealm类中的方法进行认证
            Session session = currentUser.getSession();
            session.setAttribute("userName", userName);
            loginResult.setLogin(true);
            return loginResult;
            //return "/index";
        }catch (UnknownAccountException e)
        {
            e.printStackTrace();
            msg = "账号不存在!";
        }
        catch (IncorrectCredentialsException e)
        {
            msg = "密码不正确!";
        }
        catch (AuthenticationException e) {
            e.printStackTrace();
            msg="用户验证失败!";
        }

        loginResult.setLogin(false);
        loginResult.setResult(msg);

        return loginResult;
    }

    @Override
    public void logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
    }

    @Override
    public String getCurrentUserName() {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        return (String)session.getAttribute("userName");
    }

    @Override
    public Session getSession() {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        return session;
    }
}