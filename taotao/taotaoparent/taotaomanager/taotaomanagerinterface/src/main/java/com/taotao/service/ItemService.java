package com.taotao.service;

import com.taotao.pojo.*;

public interface ItemService {
    TbItem getItemById(long itemId);
    EasyUIDataGridResult getItemList(int page,int rows);
    TaotaoResult addItem(TbItem item, String desc);
    TbItemDesc getDescCatById(long itemId);
}
