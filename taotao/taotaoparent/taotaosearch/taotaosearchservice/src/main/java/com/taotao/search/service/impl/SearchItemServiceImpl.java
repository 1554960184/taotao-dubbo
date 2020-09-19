package com.taotao.search.service.impl;

import com.taotao.pojo.SearchItem;
import com.taotao.pojo.TaotaoResult;
import com.taotao.search.mapper.SearchItemMapper;
import com.taotao.search.service.SearchItemService;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品数据导入索引库service
 */

@Service
public class SearchItemServiceImpl implements SearchItemService {
    @Autowired
    private SearchItemMapper searchItemMapper;
    @Autowired
    private SolrServer solrServer;
    @Override
    public TaotaoResult importItemsToIndex() {
        try {
            //1.查询所有商品数据
            List<SearchItem> itemList = searchItemMapper.getItemList();
            //2.便利商品数据添加到索引库
            for (SearchItem item:itemList) {
                //创建文档对象
                SolrInputDocument document = new SolrInputDocument();
                //向文档中添加域
                document.addField("id", item.getId());
                document.addField("item_title", item.getTitle());
                document.addField("item_sell_point", item.getSell_point());
                document.addField("item_price", item.getPrice());
                document.addField("item_image", item.getImage());
                document.addField("item_category_name", item.getCategory_name());
                document.addField("item_desc", item.getItem_desc());
                //把文档写入索引库
                solrServer.add(document);
            }
            //3.提交
            solrServer.commit();
            //返回提交成功
            return TaotaoResult.ok();
        }catch (Exception e){
            e.printStackTrace();
            return TaotaoResult.build(500,"数据导入失败");
        }
    }
}
