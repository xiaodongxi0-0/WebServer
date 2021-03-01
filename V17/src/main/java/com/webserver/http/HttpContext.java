package com.webserver.http;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 当前类用于保存所有与HTTP协议相关的规定内容一遍重用
 */
public class HttpContext {
    /**
     * 资源后缀名与响应头Content-Type值的对应关系
     * key：资源后缀名
     * value：Content-Type对应的值
     */
    private  static Map<String,String> mimeMapping = new HashMap<>();

    static{
        initMimeMapping();
    }
    private static void initMimeMapping(){
        try {
            SAXReader reader = new SAXReader();
            Document doc = reader.read("./config/web.xml");
            Element root = doc.getRootElement();

            List<Element> list = root.elements("mime-mapping");

            for (Element empEle:list){
                String key = empEle.elementText("extension");
                String value = empEle.elementText("mime-type");
                mimeMapping.put(key,value);


            }
            System.out.println(mimeMapping.size());





        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }

    /**
     * 根据给定的资源后缀名获取到对应的Content-Type的值
     * @param ext
     * @return
     */
    public static String getMimeType(String ext){
        return mimeMapping.get(ext);
    }

    public static void main(String[] args) {
        String type = getMimeType("png");
        System.out.println(type);
    }
}
