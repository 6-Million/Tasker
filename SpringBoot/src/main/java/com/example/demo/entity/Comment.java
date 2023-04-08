package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("comments")
@Data
public class Comment {
    @TableId(value = "cid", type = IdType.AUTO)
    int cid;

    String comment;

    int tid;

    int pass;
}
