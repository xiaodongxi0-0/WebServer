package com.webserver.servlet;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

/**
 * Servlet是JAVAEE标准中的一个接口，意思是运行在服务端的小程序
 * 我们用它来处理某个具体请求
 *
 * 当前Servlet用于处理用户注册业务
 */
public class RegServlet {
    public void service(HttpRequest request, HttpResponse response){
        System.out.println("RegServlet:开始处理用户注册...");
        /*
           1:通过request获取用户在注册页面上输入的注册信息（表单上的信息）
           2：将用户的注册信息写入文件user.dat中
           3：设置response给客户端响应注册结果页面
         */
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String nickname = request.getParameter("nickname");
        String ageStr   = request.getParameter("age");
        int age = Integer.parseInt(ageStr);//将年龄转换为int值
        System.out.println(username+","+password+","+nickname+","+age);
        /*
           2
           每条用户信息占用100字节，其中用户名，密码，昵称为字符串各占32字节，年龄为int值占4字节
         */

        try (RandomAccessFile raf = new RandomAccessFile("user.dat","rw");){
            raf.seek(raf.length());
            byte[] data = username.getBytes("utf-8");
            data = Arrays.copyOf(data,32);
            raf.write(data);

            data = password.getBytes("utf-8");
            data = Arrays.copyOf(data,32);
            raf.write(data);

            data = nickname.getBytes("utf-8");
            data = Arrays.copyOf(data,32);
            raf.write(data);

            raf.writeInt(age);
            System.out.println("注册完毕！");

            //3
            File file = new File("./webapps/myweb/reg_success.html");
            response.setEntity(file);

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("RegServlet:用户注册完毕！");
    }
}
