package com.leyou;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.domain.ThumbImageConfig;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FastClientTest {

    @Autowired
    private FastFileStorageClient storageClient;

    @Autowired
    private ThumbImageConfig imageConfig;

    @Test
    public void testUpload() throws FileNotFoundException{
        File file = new File("D:\\test\\1.png");
        StorePath storePath = this.storageClient.uploadFile(new FileInputStream(file),file.length(),"png",null);
        System.out.println(storePath.getFullPath());
        System.out.println(storePath.getPath());
    }

    @Test
    public void testUploadAndGreateThumb() throws FileNotFoundException {
        File file = new File("D:\\test\\1.png");
        StorePath storePath = this.storageClient.uploadImageAndCrtThumbImage(new FileInputStream(file), file.length(), "png", null);
        System.out.println(storePath.getPath());
        System.out.println(storePath.getFullPath());
        String path = imageConfig.getThumbImagePath(storePath.getPath());
        System.out.println(path);
    }


}