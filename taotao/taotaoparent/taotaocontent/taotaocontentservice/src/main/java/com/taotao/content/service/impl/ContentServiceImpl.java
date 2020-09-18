package com.taotao.content.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.content.service.ContentService;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.EasyUIDataGridResult;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {
    @Resource
    private TbContentMapper contentMapper;
    @Override
    public TaotaoResult addContent(TbContent content) {
        //补全pojo的属性
        content.setCreated(new Date());
        content.setUpdated(new Date());
        //插入到内容表
        contentMapper.insert(content);
        return TaotaoResult.ok();
    }

    @Override
    public EasyUIDataGridResult queryContentList(int page, int rows) {
        //设置分页信息
        PageHelper.startPage(page,rows);
        //执行查询
        TbContentExample example = new TbContentExample();
        List<TbContent> list = contentMapper.selectByExample(example);
        //取查询结果
        PageInfo<TbContent> pageInfo = new PageInfo<>(list);
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setRows(list);
        result.setTotal(pageInfo.getTotal());
        return result;
    }

    @Override
    public List<TbContent> getContentByCid(long cid) {
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(cid);
        List<TbContent> list = contentMapper.selectByExample(example);
        return list;
    }
}
