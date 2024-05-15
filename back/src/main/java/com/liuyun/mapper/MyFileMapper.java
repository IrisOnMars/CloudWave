package com.liuyun.mapper;

import com.liuyun.pojo.MyFile;
import com.liuyun.pojo.User;

import java.util.List;

/**
 * @PackageName com.liuyun.mapper
 * @Description TODO
 * @Author Ren
 * @Date 2024/4/14 21:05
 * @Version 1.0
 */
public interface MyFileMapper {
    int saveFile(MyFile myFile);

    List<MyFile> getFiles(MyFile myfile);

    MyFile queryCopy(MyFile myFile);

    MyFile queryById(int fileId);


    int updateFile(MyFile myFile);

    List<MyFile> queryByPid(int filePid);

    MyFile queryByNewName(String fileNewName);

    MyFile querySameNameInSamePath(String fileName, int filePid);

    int updateFilePid(MyFile myFile);

    List<MyFile> getFolders(MyFile myfile);

    List<MyFile> getFilesByCategory(MyFile myfile);

    List<MyFile> getBin(MyFile myfile);

    Integer deleteFile(MyFile file);

    List<MyFile> getRecent(String userName);
}
