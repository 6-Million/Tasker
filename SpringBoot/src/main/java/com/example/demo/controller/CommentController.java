package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Comment;
import com.example.demo.mapper.CommentMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Resource
    CommentMapper commentMapper;

    @PostMapping("/add")
    public Result<?> add(@RequestBody Comment comment){
        commentMapper.insert(comment);
        return Result.success();
    }

    @GetMapping("/load")
    public Result<?> load(@RequestParam(defaultValue = "1") Integer pageNum,
                          @RequestParam(defaultValue = "9") Integer pageSize,
                          @RequestParam String tid){
        LambdaQueryWrapper<Comment> wrapper = Wrappers.<Comment>lambdaQuery().eq(Comment::getTid, tid);
        Page<Comment> commentPage = commentMapper.selectPage(new Page<>(pageNum,pageSize),wrapper);
        return Result.success(commentPage);
    }
}
