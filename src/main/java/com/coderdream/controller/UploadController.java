package com.coderdream.controller;

import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

@Controller
public class UploadController {

    //用于转到upload.html
    @RequestMapping("/totest")
    public String toUpload(){
        return "upload";
    }


    //用于接收文件
    @RequestMapping("/upload")
    public ModelAndView upload(MultipartFile photo) throws IOException {
        ModelAndView mv = new ModelAndView();
        //判断用户是否上传了文件
        if(!photo.isEmpty()){

            //文件上传的地址
            String path = ResourceUtils.getURL("classpath:").getPath()+"static/upload";
            String realPath = path.replace('/', '\\').substring(1,path.length());
            //用于查看路径是否正确
            System.out.println(realPath);

            //获取文件的名称
            final String fileName = photo.getOriginalFilename();

            //限制文件上传的类型
            String contentType = photo.getContentType();
            if("image/jpeg".equals(contentType) || "image/jpg".equals(contentType) ){
                File file = new File(realPath,fileName);

                //完成文件的上传
                photo.transferTo(file);
                System.out.println("图片上传成功!");
                String path01 = "../upload/"+fileName;
                mv.addObject("path" ,path01);
                mv.setViewName("lookphoto");
                return mv;
            } else {
                System.out.println("上传失败！");
                mv.setViewName("upload");
                return mv;
            }
        } else {
            System.out.println("上传失败！");
            mv.setViewName("upload");
            return mv;
        }
    }
}
