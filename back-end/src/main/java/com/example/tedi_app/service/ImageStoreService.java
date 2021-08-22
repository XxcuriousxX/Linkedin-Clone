package com.example.tedi_app.service;


import antlr.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

@Service
@AllArgsConstructor
@Transactional
@Getter
public class ImageStoreService {


    public String save_img(MultipartFile multipartFile){

        //create directory to store images
        new File("profile_images").mkdirs();


        String img_path = "profile_images/"+ multipartFile.getOriginalFilename();

        //create the file for current profile image
        File new_img = new File(img_path);

        try{
            new_img.createNewFile();
            FileOutputStream stream = new FileOutputStream(new_img);
            stream.write(multipartFile.getBytes());
            stream.close();
            System.out.println("SUCCESS uploading image!");
            return multipartFile.getOriginalFilename();

        }catch (IOException err){
            System.out.println("Error uploading image!");
            return null;
        }


    }

    public String retrieve_img(String img_name){
        String path = "profile_images/" + img_name;
        File img = new File(path);
        try {
            byte[] imageBytes = Files.readAllBytes(img.toPath());
            System.out.println("SUCCESS retriving image");
            return Base64.encodeBase64String(imageBytes);
        } catch (Exception ex) {
            System.out.println("Error retriving image");
            return null;
        }
    }


}
