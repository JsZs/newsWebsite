package com.nowcoder.async;


import com.alibaba.fastjson.JSONObject;

import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//作用是把事件放入（优先）队列中
@Service
public class EventProducer {
    @Autowired
    JedisAdapter jedisAdapter;
//跳到util中的RedisKeyUtil
    public  boolean fireEvent(EventModel model){
        try{
            String json= JSONObject.toJSONString(model);//序列化成json
            String key= RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key,json);
            return true;

        }catch (Exception e){

            return false;
        }

    }


}
