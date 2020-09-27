package com.taotao.order.controller;

import com.taotao.order.pojo.OrderInfo;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbUser;
import com.taotao.utils.CookieUtils;
import com.taotao.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单确认页面处理controller
 */
@Controller
public class OrderCartController {
    @Value("${CART_KEY}")
    private String CART_KEY;
    @Resource
    private OrderService orderService;
    @RequestMapping("/order/order-cart")
    public String showOrderCart(HttpServletRequest request){
        //用户必须是登录状态
        //取用户id
        TbUser user = (TbUser) request.getAttribute("user");
        //根据用户信息取收获地址
        System.out.println(user.getUsername());
        //把收货地址取出来
        //从cookie取商品列表展示到页面
        List<TbItem> cartItemList = getCartItemList(request);
        request.setAttribute("cartList",cartItemList);
        //返回逻辑视图
        return "order-cart";

    }

    @RequestMapping(value = "/order/create",method = RequestMethod.POST)
    public String createOrder(OrderInfo orderInfo, Model model){
        TaotaoResult result = orderService.createOrder(orderInfo);
        model.addAttribute("orderId",result.getData().toString());
        model.addAttribute("payment",orderInfo.getPayment());
        DateTime dateTime = new DateTime();
        dateTime.plusDays(3);
        model.addAttribute("date",dateTime.toString("yyyy-MM-dd"));
        return "success";
    }

    private List<TbItem> getCartItemList(HttpServletRequest request){
        String json = CookieUtils.getCookieValue(request, CART_KEY, true);
        if (StringUtils.isBlank(json)){
            //为空，返回空列表
            return new ArrayList<>();
        }
        List<TbItem> items = JsonUtils.jsonToList(json, TbItem.class);
        return items;
    }
}
