package com.taotao.search.controller;

import com.taotao.pojo.SearchResult;
import com.taotao.pojo.TaotaoResult;
import com.taotao.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 商品搜索controller
 */
@Controller
public class SearchController {
    @Resource
    private SearchService searchService;
    @Value("${SEARCH_RESULT_ROWS}")
    private Integer SEARCH_RESULT_ROWS;
    @RequestMapping("/search")
    public String search(@RequestParam("q") String queryString,
                               @RequestParam(defaultValue = "1")Integer page, Model model) throws Exception{

        queryString = new String(queryString.getBytes("iso-8859-1"),"utf-8");
        SearchResult searchResult = searchService.search(queryString, page, SEARCH_RESULT_ROWS);
        model.addAttribute("query",queryString);
        model.addAttribute("totalPages",searchResult.getTotalPages());
        model.addAttribute("itemList",searchResult.getItemList());
        model.addAttribute("page",page);

        return "search";
    }
}
