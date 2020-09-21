package com.taotao.activemq;


import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;

import javax.jms.*;

public class TestActiveMQ {
    //queue
    //producer

    public void testQueueProducer() throws Exception{
        //1.创建一个连接工厂对象ConnectionFactory，需要指定mq的ip及端口
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.109.140:61616");
        //2.使用ConnectionFactory创建一个Connection连接对象
        Connection connection = connectionFactory.createConnection();
        //3.开启连接，调用Connection的start方法
        connection.start();
        //4.使用Connection创建一个Session
        //第一个参数是否开启事务（非数据库事务）一般不使用事务，保证事务的最终一致可以使用消息队列
        //如果第一个参数为true，第二个参数自动过滤，如果不开启事务（即第一个参数为false），则启动消息的应答模式，一般自动应答就可以了
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5.使用Session对象创建一个Destination对象，两种形式，queue与topic，当前用queue
        //参数：消息队列的名称
        Queue queue = session.createQueue("test-Queue");
        //6.使用Session对象创建一个Producer对象
        MessageProducer producer = session.createProducer(queue);
        //7.创建一个TextMessage对象
        /*TextMessage textMessage = new ActiveMQTextMessage();
        textMessage.setText("hello activemq");*/
        TextMessage textMessage = session.createTextMessage("hel activemq");
        //8.发送消息
        producer.send(textMessage);
        //9.关闭资源
        producer.close();
        session.close();
        connection.close();

    }

    public void testQueueConsumer()throws Exception{
        //1.创建一个连接工厂对象ConnectionFactory，需要指定mq的ip及端口
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.109.140:61616");
        //2.使用ConnectionFactory创建一个Connection连接对象
        Connection connection = connectionFactory.createConnection();
        //3.开启连接，调用Connection的start方法
        connection.start();
        //4.使用Connection创建一个Session
        //第一个参数是否开启事务（非数据库事务）一般不使用事务，保证事务的最终一致可以使用消息队列
        //如果第一个参数为true，第二个参数自动过滤，如果不开启事务（即第一个参数为false），则启动消息的应答模式，一般自动应答就可以了
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5.使用Session对象创建一个Destination对象，两种形式，queue与topic，当前用queue
        //参数：消息队列的名称
        Queue queue = session.createQueue("test-Queue");
        MessageConsumer consumer = session.createConsumer(queue);
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if (message instanceof TextMessage){
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        String text = textMessage.getText();
                        System.out.println(text);
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        System.in.read();
        consumer.close();
        session.close();
        connection.close();

    }
}
