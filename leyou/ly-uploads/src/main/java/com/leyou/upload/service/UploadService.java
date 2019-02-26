package com.leyou.upload.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.upload.config.UploadProper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@EnableConfigurationProperties(UploadProper.class)
public class UploadService {

    private static final List<String> suffixes = Arrays.asList("image/png","image/jpeg");

    @Autowired
    @Qualifier(value = "upload")
    private UploadProper prop;

    @Autowired
    private FastFileStorageClient storageClient;

    public String upload(MultipartFile file) {
        try {
            // 1、图片信息校验
            // 1)校验文件类型
            String type = file.getContentType();
//            if (!suffixes.contains(type)) {
//                throw new LyException(ExceptionEnum.INVALID_FILE_TYPE);
//            }
            if (!prop.getAllowTypes().contains(type)) {
                throw new LyException(ExceptionEnum.INVALID_FILE_TYPE);
            }
            // 2)校验图片内容
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                throw new LyException(ExceptionEnum.INVALID_FILE_TYPE);
            }
            // 2、保存图片
            // 2.1、生成保存目录,保存到nginx所在目录的html下，这样可以直接通过nginx来访问到
//            File dir = new File("F:\\IDEA-web\\nginx-1.12.2\\html\\");
//
//            if (!dir.exists()) {
//                dir.mkdirs();
//            }
//            // 2.2、保存图片
//            file.transferTo(new File(dir, file.getOriginalFilename()));
//
//            // 2.3、拼接图片地址
//            String url = "http://image.leyou.com/" + file.getOriginalFilename();
//            获取图片的后缀名
            String lastName = StringUtils.substringAfter(file.getOriginalFilename(), ".");
            StorePath storePath = this.storageClient.uploadFile(file.getInputStream(), file.getSize(), lastName, null);

            return prop.getBaseUrl() + storePath.getFullPath();
        } catch (Exception e) {
            throw new LyException(ExceptionEnum.FILE_UPLOAD_ERROR);
        }

    }
}
