package com.webserver.servlet;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class ChangeServlet {
    public void service(HttpRequest request, HttpResponse response){
        System.out.println("LoginServlet:开始处理用户修改密码...");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String newPassword = request.getParameter("newPassword");
        if (username==null||password==null||newPassword==null){
            File file = new File("./webapps/myweb/change_password_false.html");
            response.setEntity(file);
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
                        raf.seek(i*100+32);
                        data= newPassword.getBytes("utf-8");
                        data= Arrays.copyOf(data,32);
                        raf.write(data);
                        File file = new File("./webapps/myweb/change_password_true.html");
                        response.setEntity(file);
                        System.out.println("修改成功！");
                        dl=true;
                        break;
                    }

                }

            }
            if (!dl){
                File file = new File("./webapps/myweb/change_password_false.html");
                response.setEntity(file);
                System.out.println("修改失败！");
            }
        }catch (IOException e) {
            e.printStackTrace();
        }

    }


}
