package com.taotao.controller;

import com.taotao.content.service.ContentCategoryService;
import com.taotao.pojo.EasyUITreeNode;
import com.taotao.pojo.TaotaoResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 内容分类管理controller
 */
@Controller
public class ContentCategoryController {
    @Resource
    private ContentCategoryService categoryService;
    @RequestMapping("/content/category/list")
    @ResponseBody
    public List<EasyUITreeNode>getContentCategoryList(@RequestParam(value = "id",defaultValue = "0")Long partmentId){
        List<EasyUITreeNode> list = categoryService.getContentCategoryList(partmentId);
        return list;
    }
    @RequestMapping("/content/category/create")
    @ResponseBody
    public TaotaoResult addContentCategory(Long parentId,String name){
        TaotaoResult result = categoryService.addContentCategory(parentId, name);
        return result;
    }

    @RequestMapping("/content/category/update")
    @ResponseBody
    public TaotaoResult notifyContentCategory(@RequestParam(value = "id")Long categoryId,String name){
        TaotaoResult result = categoryService.notifyContentCategory(categoryId, name);
        return result;
    }

    @RequestMapping("/content/category/delete")
    @ResponseBody
    public TaotaoResult removeContentCategory(@RequestParam(value = "id")Long categoryId){
        TaotaoResult result = categoryService.removeContentCateGory(categoryId);
        return result;
    }
}
