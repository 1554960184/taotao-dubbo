package com.taotao.order.service.impl;

import com.taotao.mapper.TbOrderItemMapper;
import com.taotao.mapper.TbOrderMapper;
import com.taotao.mapper.TbOrderShippingMapper;
import com.taotao.order.jedis.JedisClient;
import com.taotao.order.pojo.OrderInfo;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbOrder;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 订单处理service
 */
@Service
public class OrderServiceImpl implements OrderService{
    @Resource
    private TbOrderMapper tbOrderMapper;
    @Resource
    private TbOrderShippingMapper shippingMapper;
    @Resource
    private TbOrderItemMapper orderItemMapper;
    @Resource
    private JedisClient jedisClient;
    @Value("${ORDER_ID_GEN_KEY}")
    private String ORDER_ID_GEN_KEY;
    @Value("${ORDER_ID_BEGIN_VALUE}")
    private String ORDER_ID_BEGIN_VALUE;
    @Value("${ORDER_ITEM_ID_GEN_KEY}")
    private String ORDER_ITEM_ID_GEN_KEY;
    @Override
    public TaotaoResult createOrder(OrderInfo orderInfo) {
        //生成订单号
        if (!jedisClient.exists(ORDER_ID_GEN_KEY)){
            //设置初试值
            jedisClient.set(ORDER_ID_GEN_KEY,ORDER_ID_BEGIN_VALUE);
        }
        String orderId = jedisClient.incr(ORDER_ID_GEN_KEY).toString();
        orderInfo.setOrderId(orderId);
        //免邮费
        orderInfo.setPostFee("0");
        //1.未付款，2.已付款，3.未发货，4.已发货，5.交易成功，6.交易关闭
        orderInfo.setStatus(1);
        orderInfo.setCreateTime(new Date());
        orderInfo.setUpdateTime(new Date());
        //向订单表插入数据
        tbOrderMapper.insert(orderInfo);
        List<TbOrderItem> orderItems = orderInfo.getOrderItems();
        for (TbOrderItem orderItem:orderItems) {
            String oId = jedisClient.incr(ORDER_ITEM_ID_GEN_KEY).toString();
            orderItem.setId(oId);
            orderItem.setOrderId(orderId);
            //插入明细表
            orderItemMapper.insert(orderItem);
        }
        TbOrderShipping orderShipping = orderInfo.getOrderShipping();
        orderShipping.setOrderId(orderId);
        orderShipping.setCreated(new Date());
        orderInfo.setUpdateTime(new Date());
        //插入物流表
        shippingMapper.insert(orderShipping);
        return TaotaoResult.ok(orderId);
    }
}
