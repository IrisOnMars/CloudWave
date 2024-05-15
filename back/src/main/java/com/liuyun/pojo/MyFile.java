package com.liuyun.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @PackageName com.liuyun.pojo
 * @Description TODO
 * @Author Ren
 * @Date 2024/4/14 21:05
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyFile {
    private int fileId;
    private String userName;

    private  String accessCode;
    private int fileCopy;
    private int filePid;
    private String fileName;
    private String fileNewName;
    private String pFileNewName;//不存入数据库，仅临时存储信息
    private String fileCover;

    private String filePath;
    private String fileCategory;
    private String fileType;
    private int fileSize;
    private Date createTime;
    private Date lastUpdateTime;
    private int isFolder;
    private int  status;





}
