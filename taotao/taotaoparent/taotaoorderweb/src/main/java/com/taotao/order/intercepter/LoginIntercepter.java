package com.taotao.order.intercepter;

import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;
import com.taotao.utils.CookieUtils;
import com.taotao.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.ref.PhantomReference;

/**
 * 判断用户是否登录的拦截器
 */
public class LoginIntercepter implements HandlerInterceptor {
    @Value("${TT_TOKEN}")
    private String TT_TOKEN;
    @Value("${SSO_URL}")
    private String SSO_URL;
    @Resource
    private UserService userService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //执行handler之前执行此方法
        //返回true放行，返回false拦截
        //1.从cookie中去token信息
        String token = CookieUtils.getCookieValue(request, TT_TOKEN);
        //2.取不到token，跳转到sso的登录页面，需要把当前登录的页面传递给sso，登录成功之后跳转回当前
        if (StringUtils.isBlank(token)){
            //取当前路径的url
            String requestURL = request.getRequestURL().toString();
            response.sendRedirect(SSO_URL + "/page/login?url="+requestURL);
            return false;
        }
        //3.取到token，调用sso服务判断是否登录
        TaotaoResult result = userService.getUserByToken(token);
        //4.未取到用户信息未登录状态
        if (result.getStatus() !=200){
            //取当前路径的url
            String requestURL = request.getRequestURL().toString();
            response.sendRedirect(SSO_URL + "/page/login?url="+requestURL);
            return false;
        }
        TbUser user = (TbUser) result.getData();
        request.setAttribute("user", user);
        ////取到用户信息，登录状态，放行
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        //hanler执行之后，modelandview返回之前执行此方法
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        //modelandview返回之后执行此方法（异常处理）
    }
}
