package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("usertable")
@Data
public class User {
    @TableId(value = "uid")
    String uid;
    String password;
    String name;
    String company;
}
