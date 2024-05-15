package com.liuyun.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import com.liuyun.common.Result;
import com.liuyun.common.ResultCodeEnum;
import com.liuyun.pojo.DelCount;
import com.liuyun.pojo.MyFile;
import com.liuyun.pojo.User;
import com.liuyun.service.MyFileService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @PackageName com.liuyun.controller
 * @Description TODO
 * @Author Ren
 * @Date 2024/4/15 15:40
 * @Version 1.0
 */
@RestController
@CrossOrigin
public class FileController {
    @Autowired
    private MyFileService myFileService;
    public static final String FILE_PATH="D:\\liuyun\\static\\myfiles";

    @PostMapping("file/upload")
    public Result upload(MultipartFile file, @ModelAttribute MyFile fileMsg) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String mainName = FileUtil.mainName(originalFilename);
        String extName = FileUtil.extName(originalFilename);
        String setPath=FILE_PATH+File.separator+fileMsg.getUserName();
        System.out.println(originalFilename);
        System.out.println(mainName);
        System.out.println(extName);
        //如果保存文件夹不存在，则新建
        if(!FileUtil.exist(setPath)){
            FileUtil.mkdir(setPath);
        }
        MyFile myFile=new MyFile();
        MyFile ifFileExist=myFileService.queryCopy(fileMsg);//根据文件名同文件下是否有重复
        long timestamp = System.currentTimeMillis();
        // 将时间戳转换为字符串
        String timestampStr = Long.toString(timestamp);//利用时间戳作为文件后缀名
        String newFileName=mainName+"-"+timestampStr+"."+extName;
        myFile.setFileName(newFileName);
        if(ifFileExist==null){ //如果没找到就默认创建数据
            myFile.setFileNewName(originalFilename);
        }else {//找到了就把fileName和fileNewName都改成fileId+originalFilename的形式
//            int copyTimes=(ifFileExist.getFileCopy()+1);
//            newFileName=mainName+"("+String.valueOf(copyTimes)+")."+extName;
            System.out.println(newFileName);
            myFile.setFileNewName(newFileName);
//            myFile.setFileCopy(copyTimes);
        }

        File saveFile= new File(setPath+File.separator+newFileName);
        file.transferTo(saveFile);//文件保存至本地

        DateTime now = DateUtil.date();//当前的时间
        System.out.println(now);
        //设置各种信息
        myFile.setFileType(file.getContentType());
        myFile.setUserName(fileMsg.getUserName());
        myFile.setFilePid(fileMsg.getFilePid());
        myFile.setFileSize(fileMsg.getFileSize());
        myFile.setFileCategory(fileMsg.getFileCategory());
        myFile.setCreateTime(now);
        myFile.setFilePath(setPath+File.separator+newFileName);
        myFile.setStatus(1);
        if (myFileService.saveFile(myFile)>=1){
            return Result.ok(null);
        }else {
            return Result.build(null,"上传失败");
        }

    }

    @PostMapping("file/checkCopy")
    public Result checkCopy(@RequestBody MyFile fileMsg){
        MyFile ifFileExist=myFileService.queryCopy(fileMsg);//根据文件名同文件下是否有重复
        if(ifFileExist==null){
            return Result.ok(null);
        }else {
            return Result.build(null,"文件夹内有重复文件");
        }
    }

    @GetMapping("download/{fileId}")
    public void download(@PathVariable int fileId, HttpServletResponse response) throws IOException {
        MyFile myFile=myFileService.queryById(fileId);
        response.addHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode(myFile.getFileNewName(), StandardCharsets.UTF_8));

        String filePath=myFile.getFilePath();
        if(!FileUtil.exist(filePath)){
            return ;
        }
        byte[] bytes = FileUtil.readBytes(filePath);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();

    }
    @GetMapping("getShareFile/{fileId}")
    public Result getShareFile(@PathVariable int fileId){
        MyFile  myFile=myFileService.queryById(fileId);
        Map data = new HashMap<>();
        if(myFile!=null){
            myFile.setAccessCode(null);
            data.put("shareFile",myFile);
            return Result.ok(data);
        }else {
            return Result.build(null,"数据库未找到该分享文件");
        }

    }
    @PostMapping("/getShare")
    public ResponseEntity<byte[]> getShare(@RequestBody MyFile shareInfo, HttpServletResponse response) throws IOException {
        MyFile myFile = myFileService.queryById(shareInfo.getFileId());
        if (!myFile.getAccessCode().equals(shareInfo.getAccessCode())) {
            // 返回错误信息
            return new ResponseEntity<>("提取码错误".getBytes(), HttpStatus.BAD_REQUEST);
        } else {
            // 构造文件名和文件路径
            String fileName = myFile.getFileNewName();
            String filePath = myFile.getFilePath();
            // 检查文件是否存在
            if(!FileUtil.exist(filePath)){
                // 文件不存在，返回错误信息
                System.out.println(filePath);
                return new ResponseEntity<>("文件不存在".getBytes(), HttpStatus.NOT_FOUND);
            }

            // 设置Content-Disposition头，以便浏览器知道如何处理响应体中的文件
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

            // 读取文件内容
            try (InputStream inputStream = new FileInputStream(filePath)) {
                byte[] bytes = inputStream.readAllBytes();

                // 构建并返回ResponseEntity，包含文件内容和状态码
                return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
            }
        }
    }



    @PostMapping("file/getFiles")//全部文件
    public Result getFiles(@RequestBody MyFile folder){

        System.out.println(myFileService.getFiles(folder));
        List<MyFile> myFiles=myFileService.getFiles(folder);

        Map data = new HashMap<>();
        data.put("fileList",myFiles);
        return Result.ok(data);
    }

    @PostMapping("file/getFilesByCategory")//分类文件
    public Result getFilesByCategory(@RequestBody MyFile folder) {
        List<MyFile> myFiles=myFileService.getFilesByCategory(folder);

        Map data = new HashMap<>();
        data.put("fileList",myFiles);

            return Result.ok(data);

    }
    @PostMapping("file/getBin")//回收站文件
    public Result getBin(@RequestBody MyFile folder) {
        List<MyFile> myFiles=myFileService.getBin(folder);

        Map data = new HashMap<>();
        data.put("fileList",myFiles);

        return Result.ok(data);

    }

    @PostMapping("file/getFolders")//移动文件时只显示文件夹
    public Result getFolders(@RequestBody MyFile folder ){

        System.out.println(myFileService.getFolders(folder));
        List<MyFile> myFiles=myFileService.getFolders(folder);
        Map data = new HashMap<>();
        data.put("fileList",myFiles);
        if(!myFiles.isEmpty()){
            return Result.ok(data);
        }else {
            return Result.build(data, ResultCodeEnum.EMPTY_FOLDER);
        }

    }
    @PostMapping("file/setName")
    public Result setName(@RequestBody MyFile newNameFile){
        System.out.println(newNameFile);
        MyFile ifExist=myFileService.queryCopy(newNameFile);
        if (ifExist!=null){
            return Result.build(null,ResultCodeEnum.FILENAME_EXIST);
        }else{
            MyFile myFile=myFileService.queryById(newNameFile.getFileId());
            myFile.setFileNewName(newNameFile.getFileNewName());
            //myFile.setFileCopy(0);
            int row=myFileService.updateFile(myFile);
            if (row>=1){
                System.out.println("success");
                return Result.ok(null);
            }else{
                return Result.build(null,"未成功更新数据库");
            }
        }
    }

    public void setChildFolderStatus(List<MyFile> files , DelCount delCount,int status){
        for (MyFile file:files) {
            file = myFileService.queryById(file.getFileId());
            delCount.total++;
            file.setStatus(status);
            delCount.count += myFileService.updateFile(file);
            if (file.getIsFolder() == 1) {
                List<MyFile> childFiles = myFileService.queryByPid(file.getFileId());
                if (!childFiles.isEmpty()) {
                    setChildFolderStatus(childFiles, delCount,status);
                }
            }
        }
    }

    @PostMapping("file/moveToBin")//移动到回收站
    public Result moveToBin(@RequestBody List<MyFile> file){
        DelCount delCount=new DelCount();
        for (MyFile item:file) {
            item = myFileService.queryById(item.getFileId());
            item.setFilePid(0);
            myFileService.updateFile(item);
        }
        setChildFolderStatus(file,delCount,0);
        Map data = new HashMap<>();
        data.put("count",delCount.count);
        data.put("total",delCount.total);
        if(Objects.equals(delCount.count, delCount.total)){
            return  Result.ok(data);
        }else {
            return Result.build(data,"删除不完整");
        }
    }

    public void delChildForever(List<MyFile> files ,DelCount delCount){
        for (MyFile file:files) {
            file = myFileService.queryById(file.getFileId());
            delCount.total++;
            delCount.count += myFileService.deleteFile(file);
            if (file.getIsFolder() == 1) {
                List<MyFile> childFiles = myFileService.queryByPid(file.getFileId());
                if (!childFiles.isEmpty()) {
                    delChildForever(childFiles, delCount);
                }
            }
        }
    }
    @PostMapping("file/delForever")//从回收站恢复
    public Result delForever(@RequestBody List<MyFile> files){
        DelCount delCount=new DelCount();
        delChildForever(files,delCount);
        Map data = new HashMap<>();
        data.put("count",delCount.count);
        data.put("total",delCount.total);
        if(Objects.equals(delCount.count, delCount.total)){
            return  Result.ok(data);
        }else {
            return Result.build(data,"删除不完整");
        }
    }
    @PostMapping("file/getRecent")//最近文件
    public Result getRecent(@RequestBody  User user){
        List<MyFile> myFiles=myFileService.getRecent(user.getUserName());
        System.out.println(user);
        System.out.println("----------------");
        System.out.println(myFiles);
        Map data = new HashMap<>();
        data.put("fileList",myFiles);
        return Result.ok(data);

    }
    @PostMapping("file/refresh")//从回收站恢复
    public Result refresh(@RequestBody List<MyFile> file){
        DelCount delCount=new DelCount();
        setChildFolderStatus(file,delCount,1);
        Map data = new HashMap<>();
        data.put("count",delCount.count);
        data.put("total",delCount.total);
        if(Objects.equals(delCount.count, delCount.total)){
            return  Result.ok(data);
        }else {
            return Result.build(data,"恢复不完整");
        }
    }

    @PostMapping("file/newFolder")
    public Result newFolder(@RequestBody MyFile folder){
        //判断同一个目录下是否有重名
        MyFile copyFile=myFileService.queryCopy(folder);
        if (copyFile!=null){
            return Result.build(null,"重复");
        }else{
        MyFile myFile=new MyFile();

        DateTime now = DateUtil.date();
        myFile.setUserName(folder.getUserName());
        myFile.setFileType("folder");
        myFile.setCreateTime(now);
        myFile.setStatus(1);
        myFile.setIsFolder(1);
        System.out.println(folder.getFileId());
        myFile.setFilePid(folder.getFileId());
        myFile.setFileNewName(folder.getFileNewName());
        System.out.println(myFile);
        if (myFileService.saveFile(myFile)>=1){
                System.out.println("成功");
                return Result.ok(myFile);
        }
        return Result.build(null,"创建失败！");}
    }

    @PostMapping("file/moveFiles")
    public Result moveFiles(@RequestBody List<MyFile> Files ){

        System.out.println(Files);

        for (MyFile file:Files) {
            if(myFileService.updateFilePid(file)==0){
               return Result.build(null,"有文件未修改路径") ;
            }
        }
        return Result.ok(null);
    }

    @PostMapping("file/enterFolder")
    public Result enterFolder(@RequestBody MyFile folderMsg){
        List<MyFile> myFiles=myFileService.queryByPid(folderMsg.getFileId());
        Map data = new HashMap<>();
        data.put("fileList",myFiles);
        if(!myFiles.isEmpty()){
            return Result.ok(data);
        }else {
            return Result.build(data, ResultCodeEnum.EMPTY_FOLDER);
        }
    }

    @PostMapping("file/getFileByName")
    public Result getFileByName(@RequestBody MyFile file){
        MyFile myFile=myFileService.queryByNewName(file.getFileNewName());
        List<MyFile> myFiles=myFileService.queryByPid(myFile.getFileId());
        Map data = new HashMap<>();
        data.put("fileList",myFiles);
        if(!myFiles.isEmpty()){
            return Result.ok(data);
        }else {
            return Result.build(data, ResultCodeEnum.EMPTY_FOLDER);
        }
    }

    @PostMapping("file/setAccessCode")
    public Result setAccessCode(@RequestBody MyFile shareFile){
        MyFile myFile=myFileService.queryById(shareFile.getFileId());
        if (shareFile.getAccessCode()!=null&&!shareFile.getAccessCode().equals(myFile.getAccessCode())){//用户指定
            myFile.setAccessCode(shareFile.getAccessCode());
        }else {
            String randomCode = RandomUtil.randomString("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 5);
            myFile.setAccessCode(randomCode);
        }
        if(myFileService.updateFile(myFile)>=1){
            Map data = new HashMap<>();
            data.put("shareFile",myFile);
            return Result.ok(data);
        }else {
            return Result.build(null,"提取码保存失败");
        }
    }

}
