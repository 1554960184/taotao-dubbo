package com.taotao.item.controller;

import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * 商品详情页面controller
 */
@Controller
public class ItemController {
    @Resource
    private ItemService itemService;
    @RequestMapping("/item/{itemId}")
    public String showItem(@PathVariable Long itemId, Model model){
        TbItem tbItem = itemService.getItemById(itemId);
        //取商品基本信息
        Item item = new Item(tbItem);
        //取商品详情
        TbItemDesc tbItemDesc = itemService.getDescCatById(itemId);
        //把数据传递给页面
        model.addAttribute("item",item);
        model.addAttribute("itemDesc",tbItemDesc);
        return "item";
    }
}
