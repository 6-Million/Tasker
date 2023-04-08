package com.example.demo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Task;
import com.example.demo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    TaskService taskService;

    @PostMapping("/addIndp")
    public Result<?> addIndp(@RequestBody Task task){
        String res = taskService.add(task);
        if(res.equals("fail")){
            return Result.error("-1","对接人UID不存在！");
        }
        return Result.success();
    }

    @GetMapping("/load")
    public Result<?> load(@RequestParam(defaultValue = "1") Integer pageNum,
                          @RequestParam(defaultValue = "9") Integer pageSize,
                          @RequestParam String uid){
        Page<Task> taskPage = taskService.getList(pageNum,pageSize,uid);
        return Result.success(taskPage);
    }

    @GetMapping("/loadInProj")
    public Result<?> loadInProj(@RequestParam(defaultValue = "1") Integer pageNum,
                          @RequestParam(defaultValue = "9") Integer pageSize,
                          @RequestParam String pid){
        Page<Task> taskPage = taskService.getListInP(pageNum,pageSize,pid);
        return Result.success(taskPage);
    }

    @GetMapping("/byid")
    public Result<?> byid(@RequestParam Integer tid){
        Task task = taskService.getByID(tid);
        if(task == null)
            return Result.error("-1","未查询到该任务！");
        return  Result.success(task);
    }
    @PutMapping("/done")
    public Result<?> done(@RequestBody Task task){
        taskService.done(task);
        return Result.success();
    }

    @PutMapping("/end")
    public Result<?> end(@RequestBody Task task){
        taskService.end(task);
        return Result.success();
    }

    @PutMapping("/submit")
    public Result<?> submit(@RequestBody Task task){
        taskService.submit(task);
        return Result.success();
    }

    @PutMapping("/undo")
    public Result<?> undo(@RequestBody Task task){
        taskService.undo(task);
        return Result.success();
    }
}
