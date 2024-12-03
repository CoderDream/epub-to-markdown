package com.coderdream.service;

import com.coderdream.util.EpubToMarkdownUtil;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;

@Service
public class EpubProcessingService {

    public File processEpubToZip(File epubFile) throws IOException {
        String folderPath = epubFile.getParent();
        String pureFileName = epubFile.getName().substring(0, epubFile.getName().lastIndexOf("."));

        // 调用之前的工具类方法进行处理（假设EpubToMarkdownUtil类的process方法实现了核心逻辑）
        EpubToMarkdownUtil.process(folderPath, pureFileName);

        // 根据处理后的文件名构建生成的zip文件对象并返回
        return new File(folderPath + pureFileName + ".zip");
    }
}
