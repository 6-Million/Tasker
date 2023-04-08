package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.Group;
import com.example.demo.entity.Project;
import com.example.demo.entity.Task;
import com.example.demo.mapper.GroupMapper;
import com.example.demo.mapper.ProjectMapper;
import com.example.demo.mapper.TaskMapper;
import com.example.demo.service.ProjectService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Resource
    ProjectMapper projectMapper;
    @Resource
    GroupMapper groupMapper;
    @Resource
    TaskMapper taskMapper;

    @Override
    public String add(Project project){
        //这里需要先判断project.getGid在Group::getGid里面有没有，首先要建GroupMapper
        Group res = groupMapper.selectOne(Wrappers.<Group>lambdaQuery().eq(Group::getGid,project.getGid()));
        if(res==null)
            return "fail";
        else{
            projectMapper.insert(project);
            UpdateWrapper<Group> updateWrapper = new UpdateWrapper<Group>().eq("gid",project.getGid()).set("pid",project.getPid());
            groupMapper.update(null, updateWrapper);
            return "success";
        }
    }

    @Override
    public Page<Project> getList(Integer pageNum, Integer pageSize, String uid){
        //改：先由uid找到gid，再通过gid查project
        LambdaQueryWrapper<Project> wrapper = Wrappers.<Project>lambdaQuery().inSql(Project::getGid, "select gid from `tasker1.0`.group_table where uid = '"+uid+"'");
        Page<Project> projectPage = projectMapper.selectPage(new Page<>(pageNum,pageSize),wrapper);
        return projectPage;
    }

    @Override
    public Project getByID(Integer pid){
        Project project = projectMapper.selectById(pid);
        return project;
    }

    @Override
    public String done(Project project){
        UpdateWrapper<Task> taskUpdateWrapper = new UpdateWrapper<Task>().eq("pid",project.getPid()).set("done",1);
        taskMapper.update(null,taskUpdateWrapper);
        UpdateWrapper<Project> updateWrapper = new UpdateWrapper<Project>().eq("pid",project.getPid()).set("done",1);
        projectMapper.update(null,updateWrapper);
        return "success";
    }

    @Override
    public String end(Project project){
        UpdateWrapper<Task> taskUpdateWrapper = new UpdateWrapper<Task>().eq("pid",project.getPid()).set("done",3);
        taskMapper.update(null,taskUpdateWrapper);
        UpdateWrapper<Project> updateWrapper = new UpdateWrapper<Project>().eq("pid",project.getPid()).set("done",2);
        projectMapper.update(null,updateWrapper);
        return "success";
    }
}
