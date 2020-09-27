package com.taotao.cart.controller;

import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;
import com.taotao.utils.CookieUtils;
import com.taotao.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车controller
 */
@Controller
public class CartController {
    @Value("${CART_KEY}")
    private String CART_KEY;
    @Value("${CART_EXPIER}")
    private Integer CART_EXPIER;
    @Resource
    private ItemService service;
    @RequestMapping("/cart/add/{itemId}")
    public String addItemCart(@PathVariable Long itemId, @RequestParam(defaultValue = "1") Integer num,
                              HttpServletRequest request, HttpServletResponse response){
        //取购物车商品列表
        List<TbItem> cartItemList = getCartItemList(request);
        //是否存在
        boolean flag = false;
        for (TbItem item:cartItemList) {
            if (item.getId() == itemId.longValue()){
                //如果存在数量
                //存在，数量相加
                item.setNum(item.getNum()+num);
                flag = true;
                break;
            }
        }
        //不存在，添加一个新的商品
        if (!flag){
            //调用服务查询商品id
            TbItem tbItem = service.getItemById(itemId);
            //设置购买的数量
            tbItem.setNum(num);
            //取一张图片
            String image = tbItem.getImage();
            if (StringUtils.isNotBlank(image)){
                String[] images = image.split(",");
                tbItem.setImage(images[0]);
            }
            cartItemList.add(tbItem);
        }
        //写入cookie
        CookieUtils.setCookie(request, response, CART_KEY, JsonUtils.objectToJson(cartItemList),
                CART_EXPIER, true);
        //返回成功页面
        return "cartSuccess";

    }
    @RequestMapping("/cart/cart")
    public String showCartList(HttpServletRequest request, Model model){
        List<TbItem> cartItemList = getCartItemList(request);
        model.addAttribute("cartList",cartItemList);
        return "cart";
    }

    @RequestMapping("/cart/update/num/{itemId}/{num}")
    public TaotaoResult updateItemNum(@PathVariable Long itemId,@PathVariable Integer num,
                                      HttpServletRequest request,HttpServletResponse response){
        List<TbItem> cartItemList = getCartItemList(request);
        for (TbItem item:cartItemList) {
            if (item.getId() == itemId.longValue()){
                item.setNum(num);
                break;
            }
        }
        //写入cookie
        CookieUtils.setCookie(request, response, CART_KEY, JsonUtils.objectToJson(cartItemList),
                CART_EXPIER, true);
        return TaotaoResult.ok();
    }

    @RequestMapping("/cart/delete/{itemId}")
    @ResponseBody
    public String deleteCartItem(@PathVariable Long itemId,HttpServletRequest request,
                                 HttpServletResponse response){
        List<TbItem> cartItemList = getCartItemList(request);
        for (TbItem item:cartItemList) {
            if (item.getId() == itemId.longValue()){
                cartItemList.remove(item);
                break;
            }
        }
        //写入cookie
        CookieUtils.setCookie(request, response, CART_KEY, JsonUtils.objectToJson(cartItemList),
                CART_EXPIER, true);
        return "redirect:/cart/cart.html";
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
