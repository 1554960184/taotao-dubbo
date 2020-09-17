package com.taotao.content.service.impl;

import com.taotao.content.service.ContentCategoryService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.EasyUITreeNode;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 商品内容service
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService{
    @Autowired
    private TbContentCategoryMapper contentCategoryMapper;
    @Override
    public List<EasyUITreeNode> getContentCategoryList(long parentId) {
        //根据parentId查询子节点
        TbContentCategoryExample example = new TbContentCategoryExample();
        //查询条件
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
        List<EasyUITreeNode>resultList = new ArrayList<>();
        for (TbContentCategory category:list) {
            EasyUITreeNode node = new EasyUITreeNode();
            node.setId(category.getId());
            node.setState(category.getIsParent()?"closed":"open");
            node.setText(category.getName());
            //添加到结果列表
            resultList.add(node);
        }
        return resultList;
    }

    @Override
    public TaotaoResult addContentCategory(Long parentId, String name) {
        //创建一个pojo
        TbContentCategory category = new TbContentCategory();
        //补全
        category.setParentId(parentId);
        category.setName(name);
        //状态，可选值，1-正常，2-删除
        category.setStatus(1);
        //排序，默认为1
        category.setSortOrder(1);
        category.setIsParent(false);
        category.setCreated(new Date());
        category.setUpdated(new Date());
        //插入数据库
        contentCategoryMapper.insert(category);
        //判断父节点的状态
        TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentId);
        if (!parent.getIsParent()){
            //如果parent为叶子节点则改为父节点
            parent.setIsParent(true);
            contentCategoryMapper.updateByPrimaryKey(parent);
        }

        //返回结果
        return TaotaoResult.ok(category);
    }

    @Override
    public TaotaoResult notifyContentCategory(long categoryId, String name) {
        TbContentCategory category = contentCategoryMapper.selectByPrimaryKey(categoryId);
        category.setName(name);
        contentCategoryMapper.updateByPrimaryKey(category);
        return TaotaoResult.ok(category);
    }

    @Override
    public TaotaoResult removeContentCateGory(long categoryId) {
        try {
                TbContentCategory category = contentCategoryMapper.selectByPrimaryKey(categoryId);
                if (category.getIsParent()){
                    return TaotaoResult.build(500,"此节点是父节点，不能删除！");
                }
                Long parentId = category.getParentId();
                contentCategoryMapper.deleteByPrimaryKey(categoryId);
                TbContentCategoryExample example = new TbContentCategoryExample();
                TbContentCategoryExample.Criteria criteria = example.createCriteria();
                criteria.andParentIdEqualTo(parentId);
                List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
                if (list==null||list.size()==0){
                    TbContentCategory parentContentCatrgory = contentCategoryMapper.selectByPrimaryKey(parentId);
                    parentContentCatrgory.setIsParent(false);
                    contentCategoryMapper.updateByPrimaryKey(parentContentCatrgory);
                }
                return TaotaoResult.ok();
        }catch (Exception e){
            e.printStackTrace();
            return TaotaoResult.build(500,"删除失败");
        }
    }

}
