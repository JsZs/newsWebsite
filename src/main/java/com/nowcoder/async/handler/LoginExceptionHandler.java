package com.nowcoder.async.handler;

import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.model.Message;
import com.nowcoder.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class LoginExceptionHandler implements EventHandler {
    @Autowired
    MessageService messageService;

    @Override
    public void doHandler(EventModel model) {
        //判断是否有异常登录
        Message message=new Message();
        message.setToId(18);
        message.setFromId(model.getActorId());
        message.setConversationId("");
        message.setContent("你的登录信息异常");
        message.setCreatedDate(new Date());
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupprotEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
