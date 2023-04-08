package com.example.demo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Project;
import com.example.demo.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/project")
public class ProjectController {
    @Autowired
    ProjectService projectService;

    @PostMapping("/save")
    public Result<?> save(@RequestBody Project project){
        String res = projectService.add(project);
        if(res.equals("fail"))
            return Result.error("-1","请先创建团队！");
        else
            return Result.success();
    }

    //这个方法后期要加uid进行load
    @GetMapping("/load")
    public Result<?> load(@RequestParam(defaultValue = "1") Integer pageNum,
                          @RequestParam(defaultValue = "9") Integer pageSize,
                          @RequestParam String uid){
        Page<Project> projectPage = projectService.getList(pageNum,pageSize,uid);
        return Result.success(projectPage);
    }

    @GetMapping("/byid")
    public Result<?> byid(@RequestParam Integer pid){
        Project project = projectService.getByID(pid);
        if(project == null)
            return Result.error("-1","未查询到该项目！");
        return  Result.success(project);
    }

    @PutMapping("/done")
    public Result<?> done(@RequestBody Project project){
        projectService.done(project);
        return Result.success();
    }

    @PutMapping("/end")
    public Result<?> end(@RequestBody Project project){
        projectService.end(project);
        return Result.success();
    }
}
