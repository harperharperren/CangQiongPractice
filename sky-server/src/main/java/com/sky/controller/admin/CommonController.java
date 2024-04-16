package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Api("通用接口")
@Slf4j
public class CommonController {
    @Autowired
    private AliOssUtil aliOssUtil;
    /**
     * 文件上传
     * @param file
     * @return
     */
    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file){//这里file要和前端一样
        log.info("上传文件{}",file);
        try {
            String originalFilename = file.getOriginalFilename();
            String extension=originalFilename.substring(originalFilename.lastIndexOf("."));
            String aliyunName= UUID.randomUUID().toString()+extension;
            String filePath=aliOssUtil.upload(file.getBytes(),aliyunName);//aliyun上存储的名字
            return Result.success(filePath);
        } catch (IOException e) {
            log.error("文件上传失败");
        }
        return null;
    }
}
