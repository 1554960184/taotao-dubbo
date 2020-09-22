package com.taotao.item.controller;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * 网页静态化处理
 */
@Controller
public class HtmlGenController {
    @Autowired
    private FreeMarkerConfig freeMarkerConfig;
    @RequestMapping("/genhtml")
    @ResponseBody
    public String genHtml() throws Exception{
        //生成静态页面并返回结果
        Configuration configuration = freeMarkerConfig.getConfiguration();
        Template template = configuration.getTemplate("hello.ftl");
        Map map = new HashMap();
        map.put("hello","hello 梁家宽");
        Writer out = new FileWriter(new File("D:\\taotaodubbofourteen\\taotao-dubbo\\taotao\\taotaoparent\\taotaoitemweb\\src\\main\\webapp\\WEB-INF\\hh.txt"));
        template.process(map,out);
        out.close();
        return "ok";
    }
}
