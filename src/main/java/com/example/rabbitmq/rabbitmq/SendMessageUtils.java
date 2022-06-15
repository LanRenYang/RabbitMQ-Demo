package com.example.rabbitmq.rabbitmq;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * rabbitMQ发送工具类
 */
@Component
public class SendMessageUtils {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    /*
        看源码可知，amqp给RabbitMQ发送消息的主要使用对象是Message，Message对象里面两个重成员变量 byte[] body和MessageProperties messageProperties
         body   存放消息内容信息
         messageProperties  设置获取消息的属性配置
     */

    /**
     * 直接发送消息给队列
     *
     * @param queueName 消息队列名称
     * @param message   消息(使用send()方法，传参String类型消息)
     */
    public void sendQueueByString(String queueName, String message, String ttl) {
        MessageProperties messageProperties = new MessageProperties();
        if (ttl != null) {
            messageProperties.setExpiration(ttl);
        }
        Message messagea = new Message(message.getBytes(), messageProperties);
        //  send(String routingKey, Message message)   routingKey接受队列名称或交换机匹配规则 和 message对象
        // 设置确认机制
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            /**
            * MQ服务器回调逻辑
            * @param correlationData 消息信息
            * @param b 投递结果。true表示投递成功，false表示投递失败
            * @param s 投递失败原因
            */
            @Override
            public void confirm(CorrelationData correlationData, boolean b, String s) {
                if (b) {
                    System.out.println("投递成功！");
                } else {
                    // 做补偿处理
                    System.out.println("投递失败！");
                }
             }});
        rabbitTemplate.send(queueName, messagea);
    }


    /**
     * 直接发送消息给队列
     *
     * @param queueName 消息队列名称
     * @param message   消息，承载消息的实体类需要遵从Serializable接口，使bean可以被序列化以此来传输（使用convertAndSend()传参Object类型对象，方法内部会将对象转换为byte[]）
     */
    public void sendQueueByObject(String queueName, Object message, String ttl) {
        //convertAndSend()会将实体类对象转换在Message类中的成员变量byte[]中，实体类对象需要继承Serializable接口
        rabbitTemplate.convertAndSend(queueName, message, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                if (ttl != null) {
                    message.getMessageProperties().setExpiration(ttl);
                }
                return message;
            }
        });
    }


    /**
     * 通过交换机发送消息给队列
     *
     * @param exchangeName 交换机名称
     * @param routingKey   路由规则   topic模式和direct模式传参具体匹配规则 ，fanout模式传参空串""
     * @param message      消息
     */
    public void sendExchangeByObject(String exchangeName, String routingKey, Object message, String ttl) {
        // 通过交换机发送消息给队列  --》 传参交换机名字、匹配规则、消息信息
        //rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
//        rabbitTemplate.convertAndSend(exchangeName, routingKey, message, new MessagePostProcessor() {
//            @Override
//            public Message postProcessMessage(Message message) throws AmqpException {
//                if (ttl != null) {
//                    message.getMessageProperties().setExpiration("60000");
//                }
//                return message;
//            }
//        });


        /*
            lambda表达式替换匿名内部类 语法：
                (...) -> {
                    ...
                }
                以 “->” 为间隔，左边是要实现方法的参数申明，右边是要实现方法的具体代码
         */
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message, message1 -> {
            message1.getMessageProperties().setExpiration("60000");
            return message1;
        });
    }
    


    /**
     * 通过交换机发送消息给队列
     *
     * @param exchangeName 交换机名称
     * @param routingKey   路由规则   topic模式和direct模式传参具体匹配规则 ，fanout模式传参空串""
     * @param message      消息
     */
    public void sendExchangeByString(String exchangeName, String routingKey, String message, String ttl) {
        MessageProperties messageProperties = new MessageProperties();
        if (ttl != null) {
            messageProperties.setExpiration(ttl);
        }
        Message message1 = new Message(message.getBytes(), messageProperties);
        rabbitTemplate.send(exchangeName, routingKey, message1);
    }


}
