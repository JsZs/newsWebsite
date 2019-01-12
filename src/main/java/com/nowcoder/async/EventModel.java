package com.nowcoder.async;

import java.util.HashMap;
import java.util.Map;

public class   EventModel {
    private EventType type;
    private int actorId;//触发主体
    private int entityType;//触发对象
    private int entityId;
    private int entityOwnerId;//触发对象如新闻的拥有者
    private Map<String ,String> exts=new HashMap<>();//触发事件当时的现场
    public Map<String, String> getExts() {
        return exts;
    }

    public EventModel(){

    }
    public EventModel(EventType type){
        this.type=type;
    }


    public String getExt(String name){

        return exts.get(name);
    }
    public EventModel setExt(String name,String value){
        exts.put(name, value);
        return this;

    }//方便写入MAP属性,把set返回都是空





    public EventType getType() {
        return type;
    }

    public EventModel setType(EventType type) {
        this.type = type;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }





}
