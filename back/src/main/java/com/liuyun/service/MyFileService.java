package com.liuyun.service;

import com.liuyun.pojo.MyFile;
import com.liuyun.pojo.User;

import java.util.List;

/**
 * @PackageName com.liuyun.service
 * @Description TODO
 * @Author Ren
 * @Date 2024/4/14 21:36
 * @Version 1.0
 */
public interface MyFileService {
    int saveFile(MyFile myFile);

    MyFile queryCopy(MyFile myFile);
    List<MyFile> getFiles(MyFile file);

    MyFile queryById(int fileId);

    int updateFile(MyFile myFile);

    int updateFilePid(MyFile myFile);
    List<MyFile> queryByPid(int filePid);

    MyFile queryByNewName(String fileNewName);

    MyFile querySameNameInSamePath(String fileName,int filePid);

    List<MyFile> getFolders(MyFile myfile);

    List<MyFile> getFilesByCategory(MyFile myfile);

    List<MyFile> getBin(MyFile myfile);

    Integer deleteFile(MyFile file);

    List<MyFile> getRecent(String userName);
}
