package com.taotao.controller;

import com.taotao.pojo.TaotaoResult;
import com.taotao.search.service.SearchItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 索引库维护
 */
@Controller
public class IndexManagerController {
    @Resource
    private SearchItemService searchItemService;
    @RequestMapping("/index/import")
    @ResponseBody
    public TaotaoResult importIndex(){
        TaotaoResult result = searchItemService.importItemsToIndex();
        return result;
    }
}
