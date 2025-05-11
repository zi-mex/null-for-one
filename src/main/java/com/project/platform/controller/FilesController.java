package com.project.platform.controller;

import com.alibaba.fastjson2.JSONObject;
import com.project.platform.service.FileService;
import com.project.platform.vo.FileInfoVO;
import com.project.platform.vo.ResponseVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/file")
@Slf4j
public class FilesController {

    @Resource
    private FileService fileService;


    @PostMapping("/upload")
    public ResponseVO<FileInfoVO> upload(@RequestParam MultipartFile file) throws IOException, NoSuchAlgorithmException {
        return ResponseVO.ok(fileService.upload(file));
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<org.springframework.core.io.Resource> get(@PathVariable("fileName") String filename) throws IOException {
        File file = fileService.getFile(filename);
        if (file.exists() && !file.isDirectory()) {
            org.springframework.core.io.Resource resource = new FileSystemResource(file);
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").contentType(MediaType.APPLICATION_OCTET_STREAM) // 可以根据文件类型设置更具体的MediaType
                    .contentLength(file.length()).body(resource);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
