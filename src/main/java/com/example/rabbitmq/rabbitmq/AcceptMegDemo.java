package com.example.rabbitmq.rabbitmq;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * rabbitMQ消息消费者示例
 * 消息消费者消费zhang-DLX死信队列
 */
@Component
public class AcceptMegDemo {


    @RabbitListener(queues = "zhang-DLX")
    public void receive(Message message, Channel channel) throws InterruptedException, IOException {
        // 设置消费者只接受一条消息，在队列没有得到该消费者的回应之前，不会给该消费者再发送消息。开启能者多劳模式
        //channel.basicQos(1);

        // channel.basicConsume();
        String string = new String(message.getBody());
        if (string.contains("6")) {
            System.out.println("消费失败：------>" + string);
            // //消费失败用basicNack()回应，告诉队列的处理办法，
            // 三个参数意义： 队列中的消息标记、是否批量将一次性ack所有小于deliveryTag的消息确认收到消息、对该消息是否重新入队列
            // 重入队列的消息会被再次发送给消费者，如果该消息一直消费失败，会造成程序死循环，可以考虑尝试消费3次，如果依旧失败则不重入队列

            //message.getMessageProperties().getDeliveryTag()是一个简单的自增序列，从1开始自增，用来标记队列中的消息
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
        } else {
            System.out.println("消费成功:" + string);
            // 如果消费成功用basicAck()回应，告诉队列我消费成功了，可以删除了,如果队列没有收到该应答，就一直不删除队列中的该消息
            // 两个参数意义： 队列中的消息标记、是否批量将一次性ack所有小于deliveryTag的消息确认收到消息。
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
        string = null;
        Thread.sleep(500);

    }
}