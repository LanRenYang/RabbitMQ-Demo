package com.example.rabbitmq.rabbitmq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * rabbitMQ发送消息示例
 */
@RestController
public class SendMsgDemo {

    @Autowired
    private SendMessageUtils sendMessageUtils;

    @RequestMapping(value = "/sendQueueByString", method = RequestMethod.GET)
    public String sendQueueByString() {
        sendMessageUtils.sendQueueByString("LanRen", "Hello RabbitMQ，死信队列我来了！！！","60000");
        String s = "发送成功";
        return s;
    }


    @RequestMapping(value = "/sendQueueByObject", method = RequestMethod.GET)
    public String sendQueueByObject() {
        User user = new User();
        user.setName("邓");
        user.setAge(18);
        user.setNum("12311");
        sendMessageUtils.sendQueueByObject("Yang", user,"60000");
        return "发送成功";
    }


}
