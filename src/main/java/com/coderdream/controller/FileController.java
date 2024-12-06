package com.coderdream.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/file")
public class FileController {

    /**
     * @param file 文件
     */
    @PostMapping("/upload")
    public Map<String, Object> uploadFile(
        @RequestParam("file") MultipartFile file) throws Exception {
        String dir = System.getProperty("user.dir") + File.separator + "static"
            + File.separator + "img";
        File fileDir = new File(dir);
        if (!fileDir.exists()) {
            boolean mkdir = fileDir.mkdirs();
            log.info("文件目录创建成功: {}", mkdir);
        }

        // 获取文件名
        Date current = new Date();
        // 获取文件后缀
        String format = DateUtil.format(current,
            "yyyyMMddHHmmss"); //20200325173053

        String newFileName = format + "abs.png";
        String path = dir + File.separator + newFileName;
        File pathFile = new File(path);
        file.transferTo(pathFile);
        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("msg", "上传成功");
        map.put("path", File.separator + "img" + File.separator + newFileName);
        map.put("path2", "/img/" + newFileName);
        map.put("data", path);
        return map;
    }

    /**
     * @param file 文件
     */
    @PostMapping("/upload2")
    public Map<String, Object> uploadFile2(
        @RequestParam("file") MultipartFile file) throws Exception {
        String dir =
            ResourceUtils.getURL("classpath:").getPath() + "static/upload";
        String realPath = dir.replace('/', '\\').substring(1, dir.length());
        //用于查看路径是否正确
        System.out.println(realPath);
        File fileDir = new File(dir);
        if (!fileDir.exists()) {
            boolean mkdir = fileDir.mkdirs();
            log.info("文件目录创建成功: {}", mkdir);
        }

        // 获取文件名
        Date current = new Date();
        // 获取文件后缀
        String format = DateUtil.format(current,
            "yyyyMMddHHmmss"); //20200325173053

        String newFileName = format + "abs.png";
        String path = dir + File.separator + newFileName;
        File pathFile = new File(path);
        file.transferTo(pathFile);
        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("msg", "上传成功");
        map.put("path", File.separator + "img" + File.separator + newFileName);
        map.put("path2", "/img/" + newFileName);
        map.put("data", path);
        return map;
    }

}
