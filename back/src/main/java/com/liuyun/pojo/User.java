package com.liuyun.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @PackageName com.liuyun.pojo
 * @Description TODO
 * @Author Ren
 * @Date 2024/4/6 13:39
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String userName;
    private String nickName;
    private String password;
    private String email;
    private String avatar;
    private Date joinTime;
    private Date lastLoginTime;
    private Boolean status;
    private Long useSpace;
    private Long totalSpace;

}
