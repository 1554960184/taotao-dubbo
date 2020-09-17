package com.taotao.controller;

import com.taotao.content.service.ContentService;
import com.taotao.pojo.EasyUIDataGridResult;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 内容添加controller
 */
@Controller
public class ContentController {
    @Resource
    private ContentService contentService;
    @RequestMapping("/content/save")
    @ResponseBody
    public TaotaoResult addContent(TbContent content){
        TaotaoResult result = contentService.addContent(content);
        return result;
    }
    @RequestMapping("/content/query/list")
    @ResponseBody
    public EasyUIDataGridResult queryContentList(int page,int rows){
        EasyUIDataGridResult queryContentList = contentService.queryContentList(page, rows);
        return queryContentList;
    }
}
