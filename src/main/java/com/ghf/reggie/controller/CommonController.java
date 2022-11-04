package com.ghf.reggie.controller;

import com.ghf.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/*
* 文件上传和下载
* */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")/*spel表达式获取配置文件中路径*/
    private String baseImgPath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        /*参数名要和前端绑定的保持一致，前端绑定的name是file*/
        log.info(file.toString());
        /*这里的file是临时文件，需要转存*/
        final String originalFilename = file.getOriginalFilename();/*h获得原始文件名，
            原始文件名包含后缀
            */

        String substring = originalFilename.substring(originalFilename.lastIndexOf('.'));/*此时是带点的*/

        /*适用UUID来防止文件名字重复*/
        final String FileName = UUID.randomUUID().toString();

        /*
        * 名字加路径
        * */
        String finalPath = FileName+substring;

        /*创建目录*/
        final File dir = new File(baseImgPath);
        if (!dir.exists()){
            dir.mkdirs();
        }
        try {

            file.transferTo(new File(baseImgPath+finalPath));

            /*
            * 将临时文件转存到指定位置*/
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(finalPath);

    }
    /*前端会传入name*/
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){

        /*
        * 输出流要通过response来获得
        * */

        /*
        * 输入流*/
        try {
            String path = baseImgPath+name;
            System.out.println(path);
            FileInputStream fileInputStream = new FileInputStream(new File(baseImgPath + name));

            /*通过输出流将文件写回数据,因为要向浏览器返回数据，要通过response*/
            final ServletOutputStream outputStream = response.getOutputStream();
            byte[] bytes =new  byte[1024];
            int len=0;
            while ((len=fileInputStream.read(bytes))!= -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();

            }
            response.setContentType("image/jpg");
            /*
            * 关闭资源
            * */
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("下载成功");


    }

}
