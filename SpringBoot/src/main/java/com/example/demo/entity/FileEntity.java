package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("files")
@Data
public class FileEntity {
    @TableId(value = "fid", type = IdType.AUTO)
    int fid;

    String name;

    String flag;

    int tid;

    public FileEntity(){}

    public FileEntity(String name, String flag, int tid){
        this.name = name;
        this.flag = flag;
        this.tid = tid;
    }
}
