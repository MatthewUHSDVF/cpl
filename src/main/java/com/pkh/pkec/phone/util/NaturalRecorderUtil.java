package com.pkh.pkec.phone.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


public class NaturalRecorderUtil {

    public static String downloadFromUrl(String url,String filePath) {
        if("".equals(url) || url==null){
            return "0";
        }

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            Random random = new Random();
            String dir = filePath + format.format(new Date()).substring(0,8)+"/";
            mkfolder(dir);
            URL httpurl = new URL(url);
            String fileName = getFileNameFromUrl(url);
            fileName = format.format(new Date())+(random.nextInt(90)+10) + fileName.substring(fileName.indexOf(".mp3"));
            File f = new File(dir + fileName);
            FileUtils.copyURLToFile(httpurl, f);
            return dir + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    public static String getFileNameFromUrl(String url){
        String name = Long.toString(System.currentTimeMillis()) + ".X";
        int index = url.lastIndexOf("/");
        if(index > 0){
            name = url.substring(index + 1);
            if(name.trim().length()>0){
                return name;
            }
        }
        return name;
    }


    public static void mkfolder(String dir){
        File file =new File(dir);
        //如果文件夹不存在则创建
        if  (!file .exists()  && !file .isDirectory()){
            file .mkdir();
        }
    }

}