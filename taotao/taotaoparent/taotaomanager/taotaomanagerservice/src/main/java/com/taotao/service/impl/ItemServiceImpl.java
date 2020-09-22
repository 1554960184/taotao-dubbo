package com.taotao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.*;
import com.taotao.service.ItemService;
import com.taotao.utils.IDUtils;
import com.taotao.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.*;

import java.util.Date;
import java.util.List;

/**
 * 商品管理注解
 */
@Service
public class ItemServiceImpl implements ItemService{
    @Resource
    private TbItemMapper itemMapper;
    @Resource
    private TbItemDescMapper itemDescMapper;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Resource(name = "itemAddTopic")
    private Destination destination;
    @Value("${ITEM_INFO}")
    private String ITEM_INFO;
    @Autowired
    private JedisClient jedisClient;
    @Value("${ITEM_EXPIRE}")
    private Integer ITEM_EXPIRE;
    @Override
    public TbItem getItemById(long itemId) {
        //查询数据库之前先查询缓存
        try {
            //缓存中没有再查询数据库
            String json = jedisClient.get(ITEM_INFO +  ":"+itemId + ":BASE");
            if (StringUtils.isNotBlank(json)){
                TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
                return tbItem;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        TbItem item = itemMapper.selectByPrimaryKey(itemId);
        //把查询结果添加添加到缓存
        try {
            jedisClient.set(ITEM_INFO +  ":"+itemId + ":BASE", JsonUtils.objectToJson(item));
            //设置过期时间，提高利用率
            jedisClient.expire(ITEM_INFO +  ":"+itemId + ":BASE",ITEM_EXPIRE);
        }catch (Exception e){
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public EasyUIDataGridResult getItemList(int page, int rows) {
        //设置分页信息
        PageHelper.startPage(page,rows);
        //执行查询
        TbItemExample example = new TbItemExample();
        List<TbItem> list = itemMapper.selectByExample(example);
        //取查询结果
        PageInfo<TbItem> pageInfo = new PageInfo<>(list);
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setRows(list);
        result.setTotal(pageInfo.getTotal());
        //返回结果
        return result;
    }
    @Override
    public TaotaoResult addItem(TbItem item, String desc) {
        //生成商品id
        final long itemId = IDUtils.genItemId();
        //补全item的属性
        item.setId(itemId);
        //商品状态，1-正常，2-下架，3-删除
        item.setStatus((byte) 1);
        item.setCreated(new Date());
        item.setUpdated(new Date());
        //向商品表插入数据
        itemMapper.insert(item);
        //创建一个商品描述表对应的pojo
        TbItemDesc itemDesc = new TbItemDesc();
        //补全pojo的属性
        itemDesc.setItemId(itemId);
        itemDesc.setItemDesc(desc);
        itemDesc.setUpdated(new Date());
        itemDesc.setCreated(new Date());
        //向商品描述表插入数据
        itemDescMapper.insert(itemDesc);
        //向Actionmq发送商品添加的事件
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                //发送商品id
                TextMessage textMessage = session.createTextMessage(itemId + "");
                return textMessage;
            }
        });
        //返回结果
        return TaotaoResult.ok();
    }

    @Override
    public TbItemDesc getDescCatById(long itemId) {
        //查询数据库之前先查询缓存
        try {
            //缓存中没有再查询数据库
            String json = jedisClient.get(ITEM_INFO +  ":"+itemId + ":DESC");
            if (StringUtils.isNotBlank(json)){
                TbItemDesc desc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
                return desc;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
        //把查询结果添加添加到缓存
        try {
            jedisClient.set(ITEM_INFO +  ":"+itemId + ":DESC", JsonUtils.objectToJson(itemDesc));
            //设置过期时间，提高利用率
            jedisClient.expire(ITEM_INFO +  ":"+itemId + ":DESC",ITEM_EXPIRE);
        }catch (Exception e){
            e.printStackTrace();
        }
        return itemDesc;
    }
}
