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

    @RequestMapping(value = "/sendTest", method = RequestMethod.GET)
    public void sendTest() {
        sendMessageUtils.send("LanRen", "发送1条消息");
    }


    @RequestMapping(value = "/send", method = RequestMethod.GET)
    public void send() {
        sendMessageUtils.send("LanRen", "发送1条消息");
    }


}
