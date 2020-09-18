package com.taotao.content.service;

import com.taotao.pojo.EasyUIDataGridResult;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;

import java.util.List;

public interface ContentService {
    TaotaoResult addContent(TbContent content);
    EasyUIDataGridResult queryContentList(int page, int rows);
    List<TbContent>getContentByCid(long cid);
}
