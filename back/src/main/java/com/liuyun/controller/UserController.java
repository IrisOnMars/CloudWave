package com.liuyun.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.liuyun.common.Result;
import com.liuyun.common.ResultCodeEnum;
import com.liuyun.common.Token;
import com.liuyun.pojo.MyFile;
import com.liuyun.pojo.User;
import com.liuyun.service.MyFileService;
import com.liuyun.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @PackageName com.liuyun.controller
 * @Description TODO
 * @Author Ren
 * @Date 2024/4/6 14:23
 * @Version 1.0
 */
@RestController
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;


    @PostMapping ("/login")
    public Result login(@RequestBody User user){
            Token token=new Token();
            User localUser=userService.queryByUserName(user.getUserName());
            if(localUser==null){
                return Result.build(null,ResultCodeEnum.USERNAME_NOT_EXIST);
            }
            if(user.getPassword().equals(localUser.getPassword())){
                Result result=null;
                Map data = new HashMap<>();
                String myToken=token.createToken(user);
                data.put("localUser",localUser);
                data.put("token",myToken);
                result=Result.ok(data);
                return result;
            }
            return Result.build(null,ResultCodeEnum.PASSWORD_ERROR);
    }
    @PostMapping("/register")
    public Result register(@RequestBody User registerUser){

        int row=userService.register(registerUser);
        Result result=Result.ok(null);
        if(row<1){
            result=Result.build(null,"注册失败");
        }
        return result;
    }
    @PostMapping("/checkUserExist")
    public Result checkUserExist(@RequestBody User registerUser){
        User localUser = userService.queryByUserName(registerUser.getUserName());
        if(null!=localUser){
            return Result.build(null, ResultCodeEnum.USERNAME_USED);
        }
        return Result.ok(null);
    }
    public static final String FILE_PATH="D:/liuyun/static/avatar/";
    public static final String SERVICE_PATH="http://localhost:8080/avatar/";
    @PostMapping("avatar/upload")
    public Result uploadAvatar(MultipartFile file, @ModelAttribute User sysUser) throws IOException {
        System.out.println(FILE_PATH);
        String originalFilename = file.getOriginalFilename();
        String mainName = FileUtil.mainName(originalFilename);
        String extName = FileUtil.extName(originalFilename);

        System.out.println(originalFilename);
        System.out.println(mainName);
        System.out.println(extName);
        //如果保存文件夹不存在，则新建
        if(!FileUtil.exist(FILE_PATH)){
            FileUtil.mkdir(FILE_PATH);
        }
        if(FileUtil.exist(FILE_PATH+originalFilename)){//如果要上传的文件已经存在，则改名
            originalFilename= UUID.randomUUID()+"_"+mainName+"."+extName;
        }
        File saveFile= new File(FILE_PATH+originalFilename);
        file.transferTo(saveFile);//文件保存至本地

        User user=userService.queryByUserName(sysUser.getUserName());
        user.setAvatar(SERVICE_PATH+originalFilename);

        Map data = new HashMap<>();

        if(userService.updateUser(user)>=1){
            data.put("avatarUrl",SERVICE_PATH+originalFilename);
            return Result.ok(data);
        }else {
            return Result.build(null,"未更新数据库");
        }

    }
    @PostMapping("/getSpace")
    public Result getSpace(@RequestBody User sysUser){
        User localUser=userService.queryByUserName(sysUser.getUserName());
        if(localUser==null){
            return Result.build(null,ResultCodeEnum.USERNAME_NOT_EXIST);
        }else {
            Map data = new HashMap<>();
            long useSpace=userService.getUseSpaceByUserName(sysUser.getUserName());
            localUser.setUseSpace(useSpace);
            if(userService.updateUser(localUser)>=1){
                data.put("localUser",localUser);
                return Result.ok(data);
            }else{
                return Result.build(data,"数据库存储出错");
            }

        }

    }
    @PostMapping("/changePassword")
    public Result changePassword(@RequestBody User changeUser){
        User user=userService.queryByUserName(changeUser.getUserName());
        user.setPassword(changeUser.getPassword());
        Map data = new HashMap<>();
        if(userService.updateUser(user)>=1){
            data.put("newUser",user);
            return Result.ok(data);
        }else{
           return Result.build(null,"修改失败") ;
        }
    }
    @PostMapping("/changeNickName")
    public Result changeNickName(@RequestBody User changeUser){
        User user=userService.queryByUserName(changeUser.getUserName());
        user.setNickName(changeUser.getNickName());
        Map data = new HashMap<>();
        if(userService.updateUser(user)>=1){
            data.put("newUser",user);
            return Result.ok(data);
        }else{
            return Result.build(null,"修改失败") ;
        }
    }
    @RequestMapping("/test")
    public Result query(){
        Result r=null;
        Map data = new HashMap<>();
        List<User> allUsers = userService.queryAllUsers();
        data.put("allUser",allUsers);
        r=Result.ok(data);
        return r;
    }
}
