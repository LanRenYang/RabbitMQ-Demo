package com.example.rabbitmq.rabbitmq;

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


    /**
     * 直接发送消息给队列
     *
     * @param queueName 消息队列名称
     * @param message   消息
     */
    public void send(String queueName, String message) {
        // 直接发送消息给队列  --》 传参队列名  消息信息
        rabbitTemplate.convertAndSend(queueName, message);
    }


    /**
     * 通过交换机发送消息给队列
     *
     * @param queueName  消息队列名称
     * @param routingKey 路由规则   topic模式和direct模式传参具体匹配规则 ，fanout模式传参空串""
     * @param message    消息
     */
    public void send(String queueName, String routingKey, String message) {
        // 通过交换机发送消息给队列  --》 传参交换机名字、匹配规则、消息信息
        rabbitTemplate.convertAndSend(queueName, routingKey, message);
    }


}
