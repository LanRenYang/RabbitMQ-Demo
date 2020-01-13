package com.example.rabbitmq.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


/**
 * rabbitMQ死信队列配置类
 * 生产消息 ---》LanRen队列 ---》LanRen-fanout交换机(死信交换机) ---》zhang-DLX队列(死信队列)
 */
@Component
public class RabbitMQConfig {


    private FanoutExchange fanoutExchange;
    private Queue queue;
    private Queue queueDLX;

    /**
     * 4中交换机之1
     * 创建Fanout类型交换机 --->  发布订阅模式 不处理路由键 将消息转发到绑定的队列上
     *
     * @return
     */
    @Bean
    public FanoutExchange createFanoutExchange() {
        fanoutExchange = new FanoutExchange("LanRen-fanout", true, false);
        return fanoutExchange;
    }


    /**
     * 创建 (普通) LanRen队列
     *
     * @return
     */
    @Bean
    public Queue createLanRenQueue() {
        //  name:队列名
        //  durable: 队列是否持久
        //  exclussive: 是否具有排他性 只允许创建该队列的连接访问，当该连接断开时，删除该队列
        //  autoDelete: 当最后一个消费者断开连接时，自动删除该队列
        // x-message-ttl  设置队列过期时间，队列过期，队列中的消息全部过期
        // x-max-length   该队列允许保存的最大消息个数

        /*
            x-dead-letter-exchange   为队列声明死信交换机(也叫DLX交换机)
            x-dead-letter-routing-key  死信交换机所用的 routingKey
            死信队列的全过程：
            消息生成 ---》交换机 ---》队列 ---》消息失效 ---》DLX交换机 ---》死信队列
         */

        //  等等等的一些东西
        // spring整合rabbitMQ的amq包，创建队列、交换机、绑定的两种方式：Builder模式，直接调用构造方法模式

        //QueueBuilder.durable("LanRen").withArgument("x-dead-letter-exchange", "DLX交换机名称").withArgument("x-dead-letter-routing-key", "死信交换机的路由key").autoDelete().ttl(1000 * 24).maxLength(10000).build();
        //return QueueBuilder.durable("LanRen").deadLetterExchange("DLX交换机名称").deadLetterRoutingKey("DLX交换机的路由key").autoDelete().ttl(1000 * 24).maxLength(10000).build();

        /*
            Map<String,Object> map = new HashMap<>();
            map.put("x-dead-letter-exchange"，"DLX交换机名称");
            map.put("x-dead-letter-routing-key"，"死信交换机的路由key");
            map.put("x-message-ttl":"1000 * 1800");
            return new Queue("LanRen", true, false, false, map);
         */

        queue = QueueBuilder.durable("LanRen").deadLetterExchange("LanRen-fanout").build();
        return queue;
    }


    /**
     * 创建死信队列
     *
     * @return
     */
    @Bean
    public Queue createDLXZhangQueue() {
        //  return QueueBuilder.durable("zhang-DLX").build();
        queueDLX = new Queue("zhang-DLX", true, false, false);
        return queueDLX;
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
        return BindingBuilder.bind(queueDLX).to(fanoutExchange);
    }
}
