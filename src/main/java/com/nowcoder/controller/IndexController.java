package com.nowcoder.controller;

import com.nowcoder.service.ToutiaoService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.logging.Logger;

//@Controller
//301永久跳转，302临时跳转
public class IndexController {
    private static final Logger logger= (Logger) LoggerFactory.getLogger(IndexController.class);
    @Autowired
    private ToutiaoService toutiaoService;
    @RequestMapping(path = {"/","/index"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String index(HttpSession session){

        //toutiaoService.say();
        return "Hello XK2,"+session.getAttribute("msg")+"<br>Say:"+toutiaoService.say();
    }

    @RequestMapping(path = {"/","/index"})
    @ResponseBody
    public String index(){
    return "Hello XK";
    }

    @RequestMapping(value = {"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("groupId")String groupId,
                          @PathVariable("userId")int userId,
                          @RequestParam(value = "type",defaultValue = "1") int type,
                          @RequestParam(value="key",defaultValue = "xk")String key){
        return String.format("{%s},{%d},{%d},{%s}",groupId,userId,type,key);  }

     @RequestMapping(value={"/vm"})
    public String news(Model model){
        model.addAttribute("value1","vva");
        return "news";

    }

    @RequestMapping("/admin")
    @ResponseBody
    public String admin(@RequestParam(value = "key",required=false)String key) {
        if ("admin".equals(key)) {
            return "hello admin";
        }
        throw new IllegalArgumentException("key 错误");

    }
    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e){
        return "error:"+e.getMessage();
    }

    public String request(Model model, HttpServletResponse response, HttpServletRequest request,HttpSession httpSession){
        StringBuilder sb=new StringBuilder();//bulider不安全
        Enumeration<String> headerNames=request.getHeaderNames();
        while(headerNames.hasMoreElements()){
            String name=headerNames.nextElement();
            sb.append(name+"："+request.getHeader(name)+"<br>");


        }
        return sb.toString();
    }
    @RequestMapping(path = {"/redirect/{code}"},method = {RequestMethod.GET})
    public RedirectView redirect(@PathVariable("code") int code,HttpSession session){
        session.setAttribute("msg","jump from direct");
        RedirectView red=new RedirectView("/",true);
        if(code==301){
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return red;
    }

}
