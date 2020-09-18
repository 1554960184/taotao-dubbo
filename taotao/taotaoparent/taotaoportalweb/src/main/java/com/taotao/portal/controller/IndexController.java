package com.taotao.portal.controller;

import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;
import com.taotao.portal.pojo.AD1Node;
import com.taotao.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 首页展示controller
 */
@Controller
public class IndexController {
    @Resource
    private ContentService contentService;
    @Value("${AD1_CATRGORY_ID}")
    private Long AD1_CATRGORY_ID;
    @Value("${AD1_WIDTH}")
    private Integer AD1_WIDTH;
    @Value("${AD1_WIDTH_B}")
    private Integer AD1_WIDTH_B;
    @Value("${AD1_HEIGHT}")
    private Integer AD1_HEIGHT;
    @Value("${AD1_HEIGHT_B}")
    private Integer AD1_HEIGHT_B;
    @RequestMapping("/index")
    public String showIndex(Model model){
        //根据cid查询轮播内容列表
        List<TbContent> contentList = contentService.getContentByCid(AD1_CATRGORY_ID);
        //把列表转换为AD1Node列表
        List<AD1Node>ad1Nodes = new ArrayList<>();
        for (TbContent content:contentList) {
            AD1Node node = new AD1Node();
            node.setAlt(content.getTitle());
            node.setHeight(AD1_HEIGHT);
            node.setHeightB(AD1_HEIGHT_B);
            node.setWidth(AD1_WIDTH);
            node.setWidthB(AD1_WIDTH_B);
            node.setSrc(content.getPic());
            node.setSrcB(content.getPic2());
            node.setHref(content.getUrl());
            //添加
            ad1Nodes.add(node);
        }
        //把列表转换为json数据
        String ad1Json = JsonUtils.objectToJson(ad1Nodes);
        //把json数据传送到列表
        model.addAttribute("ad1",ad1Json);
        return "index";
    }
}
