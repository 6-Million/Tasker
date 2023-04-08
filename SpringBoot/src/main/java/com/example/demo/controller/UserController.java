package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.common.Result;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    UserMapper userMapper;

    @PostMapping("/register")
    public Result<?> save(@RequestBody User user){
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUid,user.getUid()));
        if(res!=null){
            return Result.error("-1","ID已被占用，请重试！");
        }
        userMapper.insert(user);
        return Result.success();
    }

    @PostMapping("/login")
    public Result<?> login(@RequestBody User user){
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUid,user.getUid()).eq(User::getPassword,user.getPassword()));
        if(res==null){
            return Result.error("-1","ID或密码错误！");
        }
        return Result.success(res);
    }

    @GetMapping("byid") Result<?> byid(@RequestParam String uid){
        User user = userMapper.selectById(uid);
        if(user == null){
            return Result.error("-1","用户不存在");
        }
        return Result.success(user);
    }
}
