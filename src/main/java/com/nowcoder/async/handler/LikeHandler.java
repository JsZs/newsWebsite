package com.nowcoder.async.handler;

import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.model.Message;
import com.nowcoder.model.User;
import com.nowcoder.service.MessageService;
import com.nowcoder.service.NewsService;
import com.nowcoder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
//找到所有实现EventHandler的对象
@Component
public class  LikeHandler implements EventHandler {

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;
    @Override
    public void doHandler(EventModel model){

       Message message=new Message();
       message.setFromId(17);
        message.setToId(model.getActorId());
        User user=userService.getUser(model.getActorId());
        message.setContent("用户"+user.getName()+"赞了你的分享，http://127.0.0.1:8080/news/"+model.getEntityId());
        message.setCreatedDate(new Date());
        message.setConversationId("");
        messageService.addMessage(message);

    //System.out.println("Liked");
    }
//Event过来记录下来，分配给相应Handler
    @Override
    public List<EventType> getSupprotEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
