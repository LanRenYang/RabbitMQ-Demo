package com.example.rabbitmq.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


/**
 * rabbitMQ配置
 */
@Component
public class RabbitMQConfig {

    //===============================================创建队列==================================

    /**
     * 创建LanRen队列
     *
     * @return
     */
    @Bean
    public Queue createLanRenQueue() {
        //  name:队列名
        //  durable: 队列是否持久
        //  exclussive: 是否具有排他性 只允许创建该队列的连接访问，当该连接断开时，删除该队列
        //  autoDelete: 当最后一个消费者断开连接时，自动删除该队列
        return new Queue("LanRen", true, false, false);
    }


    /**
     * 创建Yang队列
     *
     * @return
     */
    @Bean
    public Queue createYangQueue() {
        return new Queue("Yang", true, false, false);
    }
    //===============================================创建队列==================================


    //===============================================创建交换机==================================

    /**
     * 4中交换机
     * 创建Topic类型交换机  --->  匹配模式  根据 routingKey（路由键） 进行模糊匹配
     * * --> 匹配多个单词  # --> 匹配单个单词  单词之间用 . 隔开
     *
     * @return
     */
    @Bean
    public TopicExchange createTopicExchange() {
        return new TopicExchange("LanRen-topic", true, false);
    }


    /**
     * 4中交换机
     * 创建Direct类型交换机 --->  匹配模式 根据 routingKey（路由键） 进行完全匹配
     *
     * @return
     */
    @Bean
    public DirectExchange createDirectExchange() {
        return new DirectExchange("LanRen-direct", true, false);
    }


    /**
     * 4中交换机
     * 创建Fanout类型交换机 --->  发布订阅模式 不处理路由键 将消息转发到绑定的队列上
     *
     * @return
     */
    @Bean
    public FanoutExchange createFanoutExchange() {
        return new FanoutExchange("LanRen-fanout", true, false);
    }


    /**
     * 4中交换机
     * 创建headers类型交换机  --》 不常用，根据请求头匹配
     *
     * @return
     */
    @Bean
    public HeadersExchange createHeadersExchange() {
        return new HeadersExchange("LanRen-headers", true, false);
    }
//===============================================创建交换机==================================


//===============================================交换机和队列绑定============================

    /**
     * 交换机为匹配模式时 和 队列的绑定
     * 根据路由键和交换机进行绑定
     *
     * @return
     */
    @Bean
    public Binding topicBinding() {
        //  将                     指定队列  和  指定交换机    按   指定路由规则  进行绑定,
        // 通过交换机发送消息的时候，指定交换机 和 路由规则，由交换机发送消息给绑定的队列
        return BindingBuilder.bind(createLanRenQueue()).to(createTopicExchange()).with("ldk");
    }


    /**
     * 交换机为匹配模式时 和 队列的绑定
     * 根据路由键和交换机进行绑定
     *
     * @return
     */
    @Bean
    public Binding directBinding() {
        //  将                     指定队列  和  指定交换机    按   指定路由规则  进行绑定,
        // 通过交换机发送消息的时候，指定交换机 和 路由规则，由交换机发送消息给绑定的队列
        return BindingBuilder.bind(createLanRenQueue()).to(createDirectExchange()).with("ldk");
    }


    /**
     * 交换机为订阅发布模式时 和 队列的绑定
     * LanRen-fanout交换机 和 LanRen队列的绑定
     *
     * @return
     */
    @Bean
    public Binding fanoutBindingLanRen() {
        // 指定队列 订阅 指定交换机发布的消息
        return BindingBuilder.bind(createLanRenQueue()).to(createFanoutExchange());
    }


    /**
     * 交换机为订阅发布模式时 和 队列的绑定
     * LanRen-fanout交换机 和 Yang队列的绑定
     *
     * @return
     */
    @Bean
    public Binding fanoutBindingYang() {
        // 指定队列 订阅 指定交换机发布的消息
        return BindingBuilder.bind(createYangQueue()).to(createFanoutExchange());
    }
    //===============================================交换机和队列绑定============================

}
