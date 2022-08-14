package com.example.nijo.common.entity;

import com.example.nijo.common.enums.RespEnum;
import lombok.Data;

@Data
public class RespEntity {
    private Integer code;
    private String msg;
    private Object obj;

    public RespEntity(RespEnum RespEnum, Object obj) {
        this.code = RespEnum.getCode();
        this.msg = RespEnum.getMsg();
        this.obj = obj;
    }

    public RespEntity(RespEnum RespEnum) {
        this.code = RespEnum.getCode();
        this.msg = RespEnum.getMsg();
    }

    public RespEntity(RespEnum RespEnum, String msg) {
        this.code = RespEnum.getCode();
        this.msg = msg;
    }

    public RespEntity() {
        this.code = RespEnum.ERROR.getCode();
        this.msg = RespEnum.ERROR.getMsg();
    }

    public RespEntity(String msg) {
        this.code = RespEnum.ERROR.getCode();
        this.msg = msg;
    }
}
