package com.webserver.servlet;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;
import com.webserver.vo.User;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ShowAllUserServlet extends HttpServlet{
    @Override
    public void doPost(HttpRequest request, HttpResponse response) {

    }
    public void doGet(HttpRequest request, HttpResponse response){
        System.out.println("ShowAllUserServlet:开始处理用户列表页面...");
        //1：先将user.dat文件中所有用户信息读取出来
        List<User> list = new ArrayList<>();//保存所有用户记录的集合
        try (
                RandomAccessFile raf= new RandomAccessFile("user.dat","r")
                ){
            for (int i = 0;i<raf.length()/100;i++){
                //读取用户名
                byte[] data = new byte[32];
                raf.read(data);
                String username = new String(data,"utf-8").trim();
                //读取密码
                raf.read(data);
                String password = new String(data,"utf-8").trim();
                //读取密码
                raf.read(data);
                String nickname = new String(data,"utf-8").trim();
                //读取年龄
                int age = raf.readInt();
                User user = new User(username,password,nickname,age);
                System.out.println(user);
                list.add(user);
            }

        }catch (IOException e ){
            e.printStackTrace();
        }
        //2:使用thymeleaf将数据与静态页面userList.html结合生成动态页面
        //2.1：创建Context实例，thymeleaf提供的，用于保存所有在页面上要显示的数据
        Context context = new Context();//使用集合
        //将存放所偶用户信息的List集合存放如Context
        context.setVariable("list",list);//类似Map的key，value

        //2.2初始化thymeleaf模版引擎
        //模版解释器，用来告知模版引擎模版的相关情况（模版就是要结合的静态页面）
        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setTemplateMode("hrml");//模版是html
        resolver.setCharacterEncoding("utf-8");//模版使用的字符集
        //实例化模版引擎
        TemplateEngine te = new TemplateEngine();
        //将模版解释器设置给引擎，这样它就能了解模版的相关信息了
        te.setTemplateResolver(resolver);

        //2.3利用模版引擎将数据与静态页面结合，生成动态页面
        /*
          process方法用于生成动态页面
          参数1：模版位置（静态页面的位置）
          参数2：要在页面上显示的动态数据
          返回值：生成好的动态页面源代码
         */
        String html = te.process("./webapps/myweb/userList.html",context);
        System.out.println(html);
        System.out.println("页面生成完毕！");

        //将生成好的html代码交代给response
        PrintWriter pw = response.getWriter();
        pw.println(html);

        //设置正文类型，告知浏览器它是一个页面
        response.setContentType("text/html");



        System.out.println("ShowAllUserServlet:用户列表页面处理完毕！");
    }


}
