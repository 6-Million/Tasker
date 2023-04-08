package com.example.demo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Group;
import com.example.demo.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/group")
public class GroupController {
    @Autowired
    GroupService groupService;

    @PostMapping("/add")
    public Result<?> add(@RequestBody Group group){
        String res = groupService.add(group);
        if(res.equals("fail")){
            return Result.error("-1","用户不存在！");
        }
        if(res.equals("exist")){
            return Result.error("-1","该用户已在当前团队！");
        }
        return Result.success();
    }

    @GetMapping("/load")
    public Result<?> load(@RequestParam(defaultValue = "1") Integer pageNum,
                          @RequestParam(defaultValue = "9") Integer pageSize,
                          @RequestParam String uid){
        Page<Group> groupPage = groupService.getList(pageNum,pageSize,uid);
        return Result.success(groupPage);
    }

    @GetMapping("/bykey")
    public Result<?> bykey(@RequestParam Integer key){
        Group group = groupService.getByKey(key);
        if(group == null)
            return Result.error("-1","未查询到该任务！");
        return  Result.success(group);
    }

    @GetMapping("/bypid")
    public Result<?> bypid(@RequestParam Integer pid, @RequestParam String uid){
        Group group = groupService.getByPid(pid,uid);
        return Result.success(group);
    }

    @GetMapping("/bygid")
    public Result<?> bygid(@RequestParam(defaultValue = "1") Integer pageNum,
                          @RequestParam(defaultValue = "9") Integer pageSize,
                          @RequestParam String gid){
        Page<Group> groupPage = groupService.getByGid(pageNum,pageSize,gid);
        return Result.success(groupPage);
    }
}
