package com.liuyun.common;

public enum ResultCodeEnum {
    SUCCESS(200,"success"),
    USERNAME_NOT_EXIST(501,"usernameNotExist"),
    PASSWORD_ERROR(503,"passwordError"),
    NOT_LOGIN(504,"notLogin"),
    USERNAME_USED(505,"usernameUsed"),
    EMPTY_FOLDER(506,"emptyFolder"),
    FILENAME_EXIST(507,"fileNameExist");
    private Integer code;
    private String message;
    private ResultCodeEnum( Integer code ,String message){
        this.code=code;
        this.message=message;
    }
    public Integer getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
}
