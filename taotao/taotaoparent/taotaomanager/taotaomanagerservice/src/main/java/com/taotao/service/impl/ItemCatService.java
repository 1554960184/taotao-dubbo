package com.taotao.service.impl;

import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.EasyUITreeNode;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品类目service
 */
@Service
public class ItemCatService implements com.taotao.service.ItemCatService{
    @Autowired
    TbItemCatMapper catMapper;

    @Override
    public List<EasyUITreeNode> getItemCatList(long parentId) {
        //根据父节点id查询子节点列表
        TbItemCatExample example = new TbItemCatExample();
        //设置查询条件
        TbItemCatExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        //执行查询
        List<TbItemCat> list = catMapper.selectByExample(example);
        //装换成EasyUITreeNode列表
        List<EasyUITreeNode>resultList = new ArrayList<>();
        for (TbItemCat tbItemCat:list) {
            EasyUITreeNode node = new EasyUITreeNode();
            node.setId(tbItemCat.getId());
            node.setText(tbItemCat.getName());
            node.setState(tbItemCat.getIsParent()?"closed":"open");
            resultList.add(node);
        }
        return resultList;
    }
}
