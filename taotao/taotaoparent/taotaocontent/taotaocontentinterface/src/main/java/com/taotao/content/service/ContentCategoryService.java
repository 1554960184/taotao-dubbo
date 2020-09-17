package com.taotao.content.service;

import com.taotao.pojo.EasyUITreeNode;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbContentCategory;

import java.util.List;

public interface ContentCategoryService {
    List<EasyUITreeNode>getContentCategoryList(long parentId);
    TaotaoResult addContentCategory(Long parentId, String name);
    TaotaoResult notifyContentCategory(long categoryId, String name);
    TaotaoResult removeContentCateGory(long categoryId);
}
