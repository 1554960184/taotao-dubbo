package com.taotao.search.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * 接收activemq发送的消息
 */
public class MyMessageListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        //接收消息
        try {
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            System.out.println(text);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
