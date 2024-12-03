package com.coderdream.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class DownloadController {

    /**
     * 2.1、方式1:HttpServletResponse.Write
     * 代码如下，直接将文件一次性读取到内存中，然后通过response将文件内容输出到客户端，这种方式适合小文件，若文件比较大的时候，将文件一次性加载到内
     * 存中会导致OOM，这点需要注意
     *
     * @param response  HttpServletResponse
     * @throws Exception     异常
     */
    @GetMapping("/download1")
    public void download1(HttpServletResponse response) throws Exception {
        // 指定要下载的文件
        File file = ResourceUtils.getFile("classpath:文件说明.txt");
        // 文件转成字节数组
        byte[] fileBytes = Files.readAllBytes(file.toPath());
        // 文件名编码，防止中文乱码
        String fileName = URLEncoder.encode(file.getName(), StandardCharsets.UTF_8);
        // 设置响应头信息
        response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
        // 内容类型为通用类型，表示二进制数据流
        response.setContentType("application/octet-stream");
        // 输出文件内容
        try (OutputStream outputStream = response.getOutputStream()) {
            outputStream.write(fileBytes);
        }
    }

    /**
     * 2.2、方式2: ResponseEntity<byte[]>
     * 代码如下，方法需要返回 ResponseEntity类型的对象，这个类是SpringBoot中自带的，是对http响应结果的一种封装，可以用来构建http响应结果：
     * 包含状态码、头、响应体等信息。
     *
     * @throws Exception     异常
     */
    @GetMapping("/download2")
    public ResponseEntity<byte[]> download2() throws Exception {
        log.info("开始下载文件...");
        // 指定要下载的文件
        File file = ResourceUtils.getFile("classpath:文件说明.txt");
        // 文件转成字节数组
        byte[] fileBytes = Files.readAllBytes(file.toPath());
        // 文件名编码，防止中文乱码
        String fileName = URLEncoder.encode(file.getName(), StandardCharsets.UTF_8);
        // 构建响应实体：ResponseEntity, 包含状态码、头、响应体等信息
        ResponseEntity<byte[]> responseEntity = ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + fileName + "\"") // 设置响应头信息
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE) // 设置响应内容类型
            .body(fileBytes);// 设置响应体
        log.info("下载文件成功");
        log.info("responseEntity:{}", responseEntity);
        return responseEntity;
    }

    /**
     * 3、1 通用方案（适合任何大小文件），建议采用
     * 2.1、方式1：边读取+边输出
     *
     * @param response  HttpServletResponse
     * @throws Exception     异常
     */
    @GetMapping("/download3")
    public void download3(HttpServletResponse response) throws Exception {
        log.info("方式3：边读取+边输出 开始下载文件...");
        // 指定要下载的文件
        File file = ResourceUtils.getFile("classpath:文件说明.txt");
        // 文件转成字节数组
        byte[] fileBytes = Files.readAllBytes(file.toPath());
        // 文件名编码，防止中文乱码
        String fileName = URLEncoder.encode(file.getName(), StandardCharsets.UTF_8);
        // 设置响应头信息
        response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
        // 内容类型为通用类型，表示二进制数据流
        response.setContentType("application/octet-stream");
        // 循环，变读取变输出，可编码大文件OOM
        try (InputStream inputStream = new FileInputStream(file);
            OutputStream outputStream = response.getOutputStream()) {
            byte[] bytes = new byte[1024]; // 缓冲区
            int readLength;     // 每次读取的长度
            while ((readLength = inputStream.read(bytes)) != -1) {    // 读取到缓冲区，返回读取的长度
                outputStream.write(bytes, 0, readLength);  // 输出到响应流
            }
        }
    }

    /**
     * 2.3、方式2:返回 ResponseEntity<Resource>
     *
     * @throws Exception     异常
     */
    @GetMapping("/download4")
    public ResponseEntity<Resource> download4() throws Exception {
        log.info("方式4:返回 ResponseEntity<Resource> 开始下载文件...");
        // 通过ResponseEntity下载文件，body 为 org.springframework.core.io.Resource 类型
        // Resource是Spring中的一个资源接口，是对资源的一种抽象，常见的几个实现类
        //  classPathResource：classpath下面的文件资源
        //  FileSystemResource：文件系统资源
        //  InputStreamResource：流资源
        //  ByteArrayResource：字节数组资源
        String fileNameRaw = "文件说明.txt";
        Resource resource = new ClassPathResource(fileNameRaw);
        // 文件名编码，防止中文乱码
        String fileName = URLEncoder.encode(fileNameRaw, StandardCharsets.UTF_8);
        // 构建响应实体：ResponseEntity, 包含状态码、头、响应体等信息
        ResponseEntity<Resource> responseEntity = ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + fileName + "\"") // 设置响应头信息
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE) // 设置响应内容类型
            .body(resource);// 设置响应体
        log.info("方式4: 下载文件成功");
        log.info("responseEntity:{}", responseEntity);
        return responseEntity;
    }

}
