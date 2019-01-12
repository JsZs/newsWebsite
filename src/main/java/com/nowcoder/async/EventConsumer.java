package com.nowcoder.async;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
//首先consumer要有映射表
@Service
public class EventConsumer implements InitializingBean,ApplicationContextAware{

    @Autowired
    JedisAdapter jedisAdapter;

    public static final Logger logger=LoggerFactory.getLogger(EventConsumer.class);
    private Map<EventType,List<EventHandler>> config=new HashMap<EventType,List<EventHandler>>();//映射
    private ApplicationContext applicationContext;
//通过找handler找type,放入config



    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String,EventHandler> beans=applicationContext.getBeansOfType(EventHandler.class);//找到所有handler
        if(beans!=null){
            for(Map.Entry<String,EventHandler>entry :beans.entrySet()){
                List<EventType> eventTypes= entry.getValue().getSupprotEventTypes();//看哪个handler登记哪些type
                for(EventType type:eventTypes){
                    if(!config.containsKey(type)){//没有type就put新的进去
                        config.put(type,new ArrayList<EventHandler>());

                    }
                    config.get(type).add(entry.getValue());
                }

            }

        }
//阻塞队列用线程搞事
            Thread thread=new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true){
                        String key= RedisKeyUtil.getEventQueueKey();
                        List<String> events=jedisAdapter.brpop(0,key);//取到事件后遍历事件
                        for(String message:events){
                            if(message.equals(key)){
                                    continue;

                            }
                            //toJsonString和parseObject
                            EventModel eventModel= JSONObject.parseObject(message, EventModel.class);//找到eventModel
                            if(!config.containsKey(eventModel.getType())){
                                logger.error("不能识别的事件");
                                continue;
                            }
                            for(EventHandler handler:config.get(eventModel.getType())){
                                handler.doHandler(eventModel);
                            }
                        }
                    }
                }
            });
            thread.start();

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }
}
