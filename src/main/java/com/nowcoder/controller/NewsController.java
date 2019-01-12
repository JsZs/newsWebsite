package com.nowcoder.controller;

import com.nowcoder.model.*;
import com.nowcoder.service.*;
import com.nowcoder.util.ToutiaoUtil;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.util.FileCopyUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletResponse;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;



@Controller
public class NewsController{
        private static final Logger logger= (Logger) LoggerFactory.getLogger(NewsController.class);

     @Autowired
     NewsService newsService;
     @Autowired
     HostHolder  hostHolder;
     @Autowired
     UserService userService;
     @Autowired
     QiniuService qiniuService;
     @Autowired
     CommentService commentService;
     @Autowired
     LikeService likeService;

    //get到本地，返回String，也叫图片上传
    @RequestMapping(path = {"/uploadImage/"},method = {RequestMethod.POST})
    @ResponseBody
    public String uploadImage(@RequestParam ("file")MultipartFile file){
        try {
            String fileUrl=newsService.saveImage(file);
            if(fileUrl==null){

                return ToutiaoUtil.getJSONString(1,"上传图片失败");
            }
            return  ToutiaoUtil.getJSONString(0,fileUrl);
        }catch (Exception e){

            logger.error("上传图片失败"+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"上传失败");
        }

    }



    //set到网上，返回void,也叫图片下载
    @RequestMapping(path = {"/image"},method = {RequestMethod.GET})
    @ResponseBody
    public void getImage(@RequestParam("name") String imageName,
                         HttpServletResponse response){
        try{
            response.setContentType("image/jpeg");
            StreamUtils.copy(new FileInputStream(new File(ToutiaoUtil.IMAGE_DIR+ imageName)),
                    response.getOutputStream());

        }catch (Exception e){
            logger.error("读取图片错误"+imageName+e.getMessage());

        }



    }



    //咨询详情
    @RequestMapping(path = {"/news/{newsId}"},method = {RequestMethod.GET})
    public String newsDetail(@PathVariable("newsId")int newsId,Model model){
        try{
            News news=newsService.getById(newsId);
            if(news!=null){
                int localUserId=hostHolder.getUser()!=null?hostHolder.getUser().getId():0;
                if (localUserId!=0){
                    model.addAttribute("like",likeService.getLikeStatus(localUserId,EntityType.ENTITY_NEWS,news.getId()));

                }else {

                    model.addAttribute("like",0);
                }
                List<Comment> comments=commentService.getCommentsByEntity(news.getId(),EntityType.ENTITY_NEWS);
                List<ViewObject> commentVOs=new ArrayList<ViewObject>();
                for(Comment comment:comments){//前面是输出属性，后面是被遍历的管道一号

                    ViewObject commentVO=new ViewObject();//定义管道二号
                    commentVO.set("comment",comment);//元素放入二号
                    commentVO.set("user",userService.getUser(comment.getUserId()));
                    commentVOs.add(commentVO);//管道三号加入二号
                }

                model.addAttribute("comments",commentVOs);//循环后model加入三号

            }
            model.addAttribute("news",news);
            model.addAttribute("owner",userService.getUser(news.getId()));


        }catch(Exception e){
            logger.error("获取资讯明细错误"+e.getMessage());
        }

            return "detail";
        }
     //model参数必备
    //


    @RequestMapping(path = {"/user/addNews/"} ,method = {RequestMethod.POST})
    @ResponseBody
    public String addNews(@RequestParam("image")String image,
                          @RequestParam("title")String title,
                          @RequestParam("link")String link){
        try {
            News news=new News();
            news.setCreatedDate(new Date());
            news.setTitle(title);
            news.setImage(image);
            news.setLink(link);
            if(hostHolder.getUser()!=null){
                news.setUserId(hostHolder.getUser().getId());

            }else{
                news.setUserId(3);
                //设置一个匿名用户

            }
            newsService.addNews(news);
            return ToutiaoUtil.getJSONString(0);
        }catch (Exception e){
            logger.error("添加咨询失败"+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"发布失败");
        }


    }
    @RequestMapping(path = {"/addComment"},method = {RequestMethod.POST})
    public String addComment(@RequestParam("newsId") int newsId,
                                @RequestParam("content") String content){
        try{
            Comment comment=new Comment();
            comment.setUserId(hostHolder.getUser().getId());
            comment.setContent(content);
            comment.setEntityId(newsId);
            comment.setEntityType(EntityType.ENTITY_NEWS);
            comment.setCreatedDate(new Date());
            comment.setStatus(0);
            commentService.addComment(comment);

            int count=commentService.getCommentCount(comment.getEntityId(),comment.getEntityType());
            newsService.updateCommentCount(comment.getEntityId(),count);
        }catch (Exception e){

            logger.error("增加评论失败"+e.getMessage());
        }
        return "redirect:/news/"+String.valueOf(newsId);
    }

}
