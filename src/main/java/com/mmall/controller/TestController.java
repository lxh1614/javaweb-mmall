package com.mmall.controller;

import com.mmall.object.User;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Justin on 2017/6/15.
 */

@Controller
public class TestController {

    @RequestMapping(value = "baseType.do")
    @ResponseBody
    public String baseType(@RequestParam("xage") int age){//使用参数别名，下次请求时得用xage
        return "age:"+age;
    }

    @RequestMapping(value = "baseType2.do")
    @ResponseBody
    public String baseType2(Integer age){
        return "age:"+age;
    }

    @RequestMapping(value = "array.do")
    @ResponseBody
    public String array(String[] name){
        StringBuilder builder = new StringBuilder();
        for (String item:name) {
            builder.append(item).append(" ");
        }
        return builder.toString();
    }

   //todo http://localhost:8080/object.do?name=lanxinhua&age=30&contactInfo.phone=1234
    @RequestMapping(value = "object.do")
    @ResponseBody
    public String object(User user){
        return user.toString();
    }

    @RequestMapping(value = "date1.do")
    @ResponseBody
    public String date1(Date date1){
        return date1.toString();
    }

//    @InitBinder("date1")
//    public void initDate1(WebDataBinder binder){
//        binder.registerCustomEditor(Date.class,new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"),true));
//    }

    @RequestMapping(value = "date2.do")
    @ResponseBody
    public String date2(Date date2){
        return date2.toString();
    }

    @RequestMapping(value = "/book",method = RequestMethod.GET)
    @ResponseBody
    public String book(HttpServletRequest request){
       String contentType = request.getContentType();
       if(contentType == null){
           return "book.default";
       }else if(contentType.equals("txt")){
           return "book.txt";
       }else if(contentType.equals("html")){
            return "book.html";
        }
        return "book.default";
    }
    @RequestMapping(value = "/subject/{subjectid}",method = RequestMethod.GET)
    @ResponseBody
    public String subjectGet(@PathVariable("subjectid") String subjectid,HttpServletRequest request){
        String version = request.getHeader("version");
        return "this is a get method subjectid:"+subjectid+" and version:"+version;
    }
    @RequestMapping(value = "/subject/{subjectid}",method = RequestMethod.POST)
    @ResponseBody
    public String subjectPost(@PathVariable("subjectid") String subjectid){
        return "this is a post method subjectid:"+subjectid;
    }
    @RequestMapping(value = "/subject/{subjectid}",method = RequestMethod.PUT)
    @ResponseBody
    public String subjectPut(@PathVariable("subjectid") String subjectid){
        return "this is a put method subjectid:"+subjectid;
    }
    @RequestMapping(value = "/subject/{subjectid}",method = RequestMethod.DELETE)
    @ResponseBody
    public String subjectDelete(@PathVariable("subjectid") String subjectid){
        return "this is a delete method subjectid:"+subjectid;
    }

}
