package com.project.platform.service;

import com.project.platform.vo.FileInfoVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public interface FileService {

    FileInfoVO upload(MultipartFile file) throws IOException, NoSuchAlgorithmException;

    File getFile(String fileName) throws IOException;
}
