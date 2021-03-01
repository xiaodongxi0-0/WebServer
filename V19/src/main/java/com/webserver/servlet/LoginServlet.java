package com.webserver.servlet;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class LoginServlet extends HttpServlet{
    @Override
    public void doGet(HttpRequest request, HttpResponse response) {

    }

    public void doPost(HttpRequest request, HttpResponse response){
        System.out.println("LoginServlet:开始处理用户登录...");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username==null||password==null){
            File file = new File("./webapps/myweb/login_fail.html");
            response.setEntity(file);
            System.out.println("请输入正确的用户名和密码");
            return;
        }
        try (RandomAccessFile raf = new RandomAccessFile("user.dat","rw");){
            boolean dl= false;
            for (int i=0;i<raf.length()/100;i++){
                raf.seek(i*100);
                byte[] data = new byte[32];
                raf.read(data);
                String name =new String(data,"utf-8").trim();
                if (username.equals(name)){
                    raf.read(data);
                    String passw =new String(data,"utf-8").trim();
                    if (password.equals(passw)){
                        File file = new File("./webapps/myweb/login_success.html");
                        response.setEntity(file);
                        System.out.println("登录成功！");
                        dl=true;
                        return;
                    }

                }

            }
            if (!dl){
                File file = new File("./webapps/myweb/login_fail.html");
                response.setEntity(file);
                System.out.println("登录失败！");
            }
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

}
