package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.Task;
import com.example.demo.entity.User;
import com.example.demo.mapper.TaskMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.TaskService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TaskServiceImpl implements TaskService {
    @Resource
    TaskMapper taskMapper;
    @Resource
    UserMapper userMapper;

    @Override
    public String add(Task task){
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUid,task.getUid()));
        if(res==null){
            return "fail";
        }
        taskMapper.insert(task);
        return "success";
    }

    @Override
    public Page<Task> getList(Integer pageNum, Integer pageSize, String uid){
        LambdaQueryWrapper<Task> wrapper = Wrappers.<Task>lambdaQuery().eq(Task::getUid, uid);
        Page<Task> taskPage = taskMapper.selectPage(new Page<>(pageNum,pageSize),wrapper);
        return taskPage;
    }

    @Override
    public Page<Task> getListInP(Integer pageNum, Integer pageSize, String pid){
        LambdaQueryWrapper<Task> wrapper = Wrappers.<Task>lambdaQuery().eq(Task::getPid, pid).eq(Task::getCoop,1);
        Page<Task> taskPage = taskMapper.selectPage(new Page<>(pageNum,pageSize),wrapper);
        return taskPage;
    }

    @Override
    public Task getByID(Integer tid){
        Task task = taskMapper.selectById(tid);
        return task;
    }

    @Override
    public String done(Task task){
        UpdateWrapper<Task> taskUpdateWrapper = new UpdateWrapper<Task>().eq("tid",task.getTid()).set("done",1);
        taskMapper.update(null,taskUpdateWrapper);
        return "success";
    }

    @Override
    public String end(Task task){
        UpdateWrapper<Task> taskUpdateWrapper = new UpdateWrapper<Task>().eq("tid",task.getTid()).set("done",3);
        taskMapper.update(null,taskUpdateWrapper);
        return "success";
    }

    @Override
    public String submit(Task task){
        UpdateWrapper<Task> taskUpdateWrapper = new UpdateWrapper<Task>().eq("tid",task.getTid()).set("done",2);
        taskMapper.update(null,taskUpdateWrapper);
        return "success";
    }

    @Override
    public String undo(Task task){
        UpdateWrapper<Task> taskUpdateWrapper = new UpdateWrapper<Task>().eq("tid",task.getTid()).set("done",0);
        taskMapper.update(null,taskUpdateWrapper);
        return "success";
    }
}
