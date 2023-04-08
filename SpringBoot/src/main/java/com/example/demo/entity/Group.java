package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("group_table")
@Data
public class Group {
    @TableId(value = "group_key",type = IdType.AUTO)
    @TableField("group_key")
    int groupKey;
    String uid;
    int resp;
    String gid;
    int pid;
    @TableField("group_name")
    String groupName;

}
