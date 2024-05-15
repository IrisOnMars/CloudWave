package com.liuyun.service.Impl;

import com.liuyun.mapper.MyFileMapper;
import com.liuyun.pojo.MyFile;
import com.liuyun.pojo.User;
import com.liuyun.service.MyFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @PackageName com.liuyun.service.Impl
 * @Description TODO
 * @Author Ren
 * @Date 2024/4/14 21:37
 * @Version 1.0
 */
@Service
public class MyFileServiceImpl implements MyFileService {
    @Autowired
    private MyFileMapper myFileMapper;
    @Override
    public int saveFile(MyFile myFile) {
        return  myFileMapper.saveFile(myFile);
    }

    @Override
    public MyFile queryCopy(MyFile myFile) {
        return myFileMapper.queryCopy(myFile);
    }

    @Override
    public List<MyFile> getFiles(MyFile file) {
        return myFileMapper.getFiles(file);
    }

    @Override
    public MyFile queryById(int fileId) {
        return myFileMapper.queryById(fileId);
    }

    @Override
    public int updateFile(MyFile myFile) {
        return myFileMapper.updateFile(myFile);
    }

    @Override
    public int updateFilePid(MyFile myFile) {
        return myFileMapper.updateFilePid(myFile);
    }

    @Override
    public List<MyFile> queryByPid(int filePid) {
        return myFileMapper.queryByPid(filePid);
    }

    @Override
    public MyFile queryByNewName(String fileNewName) {
        return myFileMapper.queryByNewName(fileNewName);
    }

    @Override
    public MyFile querySameNameInSamePath(String fileName,int filePid) {
        return myFileMapper.querySameNameInSamePath(fileName,filePid);
    }

    @Override
    public List<MyFile> getFolders(MyFile myfile) {
        return myFileMapper.getFolders(myfile);
    }

    @Override
    public List<MyFile> getFilesByCategory(MyFile myfile) {
        return myFileMapper.getFilesByCategory(myfile);
    }

    @Override
    public List<MyFile> getBin(MyFile myfile) {
        return myFileMapper.getBin(myfile);
    }

    @Override
    public Integer deleteFile(MyFile file) {
        return myFileMapper.deleteFile(file);
    }

    @Override
    public List<MyFile> getRecent(String userName) {
        return myFileMapper.getRecent(userName);
    }
}
