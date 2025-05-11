package com.project.platform.service.impl;

import cn.hutool.core.net.url.UrlBuilder;
import com.project.platform.exception.CustomException;
import com.project.platform.service.FileService;
import com.project.platform.vo.FileInfoVO;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class FileServiceImpl implements FileService {

    @Value("${server.ip}")
    private String serverIp;
    @Value("${server.port}")
    private int serverPort;
    @Value("${files.uploads.path}")
    private String basePath;

    @Value("${files.uploads.baseUrl:}")
    private String fileBaseUrl;


    public FileInfoVO upload(MultipartFile multipartFile) throws IOException, NoSuchAlgorithmException {
        //获取上传文件扩展名
        String fix = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        //生成文件完整名称
        if (StringUtils.isBlank(fix)) {
            throw new CustomException("文件扩展名不能为空");
        }
        //生成文件名 使用MD5 虽然可能产生哈希碰撞 但是一般的场景足够使用
        String md5 = getMD5Checksum(multipartFile);
        String newFileName = md5 + "." + fix;
        File newFile = createFile(newFileName);
        // 直接转移文件到指定路径
        multipartFile.transferTo(new File(newFile.getAbsolutePath()));
        FileInfoVO fileInfoVO = new FileInfoVO();
        fileInfoVO.setUrl(getServer() + "/" + newFileName);
        fileInfoVO.setName(newFileName);
        return fileInfoVO;
    }


    private File createFile(String fileName) throws IOException {
        File file = new File(Paths.get(basePath, fileName).toString());
        if (file.exists()) {
            return file;
        }
        // 判断配置的文件目录是否存在，若不存在则创建一个新的文件目录
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        return file;
    }


    private String getServer() {
        if (StringUtils.isNotEmpty(fileBaseUrl)) {
            return fileBaseUrl;
        }
        String buildUrl = UrlBuilder.create()
                .setScheme("http")
                .setHost(serverIp)
                .setPort(serverPort)
                .addPath("file")
                .build();
        return buildUrl;
    }


    private String getFilePath(String fileName) {
        return basePath + fileName;

    }

    public File getFile(String fileName) throws IOException {
        File file = new File(getFilePath(fileName));
        return file;
    }

    /**
     * 计算文件的MD5
     *
     * @param file
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    private String getMD5Checksum(MultipartFile file) throws NoSuchAlgorithmException, IOException {
        MessageDigest md5Digest = MessageDigest.getInstance("MD5");
        byte[] fileBytes = file.getBytes();
        md5Digest.update(fileBytes);

        StringBuilder sb = new StringBuilder();
        for (byte b : md5Digest.digest()) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
