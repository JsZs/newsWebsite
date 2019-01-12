package com.nowcoder;

import com.nowcoder.dao.CommentDAO;
import com.nowcoder.dao.NewsDAO;
import com.nowcoder.dao.UserDAO;
import com.nowcoder.model.Comment;
import com.nowcoder.model.EntityType;
import com.nowcoder.model.News;
import com.nowcoder.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.swing.text.html.parser.Entity;
import java.util.Date;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
@Sql("/init-schema.sql")

public class     InitDatabaseTests {
    @Autowired
    UserDAO userDAO;
    @Autowired
    NewsDAO newsDAO;
    @Autowired
    CommentDAO commentDAO;

    @Test
    public void initData(){
        Random random=new Random();
        for(int i=0;i<11;++i){
            User user=new User();
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",random.nextInt(1000)));
            user.setName(String.format("USER%d",i));
            user.setPassword("");
            user.setSalt("");
            userDAO.addUser(user);

            user.setPassword("NewPassWord");
            userDAO.updatePassword(user);

            News news=new News();
            Date date=new Date();
            date.setTime(date.getTime()+60*60*1000*i);
            news.setTitle(String .format("TITLE{%d}",i));
            news.setLink(String .format("http://www.nowcoder.com/%d.html",i));
            news.setImage(String .format("http://images.nowcoder.com/head/%dm.png",random.nextInt(1000)));
            news.setCommentCount(i);
            news.setLikeCount(i+1);
            news.setUserId(i+1);
            news.setCreatedDate(date);
            newsDAO.addNews(news);

            for(int j=0;j<3;j++){
                Comment comment=new Comment();
                comment.setUserId(i+1);
                comment.setEntityId(news.getId());
                comment.setEntityType(EntityType.ENTITY_NEWS);
                comment.setStatus(0);
                comment.setCreatedDate(new Date());
                comment.setContent("Comment"+String.valueOf(j));
                commentDAO.addComment(comment);


            }



        }
        Assert.assertEquals("NewPassWord",userDAO.selectById(1).getPassword());//测试两个参数是否相等
        userDAO.deleteById(1);
        Assert.assertNull(userDAO.selectById(1 ));
        Assert.assertNotNull(commentDAO.selectByEntity(1,EntityType.ENTITY_NEWS).get(0));
    }
}
//insert into user(name,pwd) values ('','');
//select id,name from user where id < =#{id } and order by id desc,asc limit 2,1;
//update user set pwd='nn5' where id=5;
//delete from user where id=5；
//除了注解，还有一种语句是写在XML里，更复杂。体现出MYBATIS。
