package com.coderdream.controller;

import com.coderdream.service.EpubProcessingService;
import com.coderdream.util.FileUtils;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
public class EpubController {

    @Autowired
    private EpubProcessingService epubProcessingService;

    @PostMapping(value = "/uploadEpub", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> uploadEpub(@RequestParam("file") MultipartFile epubFile) {
        try {
            // 将MultipartFile转换为File对象（可以选择更合适的临时文件处理方式，这里为简单示例）
            File tempFile = File.createTempFile("temp", ".epub");
            epubFile.transferTo(tempFile);

            // 调用Service层方法处理文件，获取生成的zip文件
            File zipFile = epubProcessingService.processEpubToZip(tempFile);

            // 读取zip文件内容为字节数组
            byte[] zipBytes = java.nio.file.Files.readAllBytes(zipFile.toPath());

            // 设置响应头信息，指定文件名以及内容类型为zip格式
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + zipFile.getName());
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            // 返回包含zip文件字节数组的响应实体
            return new ResponseEntity<>(zipBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


//    @RequestMapping(value = "/")
//    public ResponseEntity<byte[]> downloadInterviewFile() throws Exception {
//        // 根据面试官主键编码 下载文件
//        List<InterviewFile> interviewFiles =  this.interviewFileService.getAll(interviewfile);
//
//        ByteArrayOutputStream byteOutPutStream = null;
//
//        if (!interviewFiles.isEmpty()) {
//            //单个文件名称
//            String interviewFileName = "";
//            //文件后缀
//            String fileNameSuffix = "";
//            //创建一个集合用于 压缩打包的参数
//            List<Map<String, String>> parms = new ArrayList<>();
//            //创建一个map集合
//            Map<String, String> fileMaps =null;
//            //得到存储文件盘符 例  D:
//            String root = properties.getValue("upload.root");
//            //创建一个字节输出流
//            byteOutPutStream = new ByteArrayOutputStream();
//
//            for (InterviewFile file : interviewFiles) {
//
//                fileMaps = new HashMap<>();
//
//                interviewFileName = file.getFileName();
//
//                fileNameSuffix = file.getFileSuffix();
//                //将单个存储路径放入
//                fileMaps.put("filePath", root.concat(file.getFilePath()));
//                //将单个文件名放入
//                fileMaps.put("fileName", interviewFileName.concat("." + fileNameSuffix));
//                //放入集合
//                parms.add(fileMaps);
//
//            }
//            //压缩文件
//            FileUtils.batchFileToZIP(parms, byteOutPutStream);
//        }
//
//
//        HttpHeaders headers = new HttpHeaders();
//
//        String fileName = null;
//        try {
//            fileName = new String("附件.zip".getBytes("UTF-8"), "ISO-8859-1");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//
//        headers.setContentDispositionFormData("attachment", fileName);// 文件名称
//
//        ResponseEntity<byte[]> responseEntity = new ResponseEntity<byte[]>(byteOutPutStream.toByteArray(), headers, HttpStatus.CREATED);
//
//        return responseEntity;
//
//    }


    @RequestMapping(value = "/")
    public ResponseEntity<byte[]> downloadInterviewFile() throws Exception {

        // ---------------------------压缩文件处理-------------------------------
        ByteArrayOutputStream byteOutPutStream = new ByteArrayOutputStream();

        // 创建一个集合用于 压缩打包的参数
        List<Map<String, String>> params = new ArrayList<>();
        // 创建一个map集合
        Map<String, String> fileMaps = new HashMap<>();
        fileMaps.put("filePath", "D:/文件路径");
        fileMaps.put("fileName", "HRM-Package.txt");

        Map<String, String> fileMaps1 = new HashMap<>();
        fileMaps1.put("filePath", "D:/文件路径1");
        fileMaps1.put("fileName", "java.txt");
        // 放入集合
        params.add(fileMaps);
        params.add(fileMaps1);

        // 压缩文件
        FileUtils.batchFileToZIP(params, byteOutPutStream);
        // ---------------------------压缩文件处理-------------------------------
        HttpHeaders headers = new HttpHeaders();

        String fileName = null;
        try {
            fileName = new String("附件.zip".getBytes("UTF-8"), "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        headers.setContentDispositionFormData("attachment", fileName);// 文件名称

        ResponseEntity<byte[]> responseEntity = new ResponseEntity<byte[]>(byteOutPutStream.toByteArray(), headers,
            HttpStatus.CREATED);

        return responseEntity;

    }
}
