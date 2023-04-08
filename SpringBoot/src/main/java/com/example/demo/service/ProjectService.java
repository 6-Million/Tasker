package com.example.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.Project;

public interface ProjectService {

    public String add(Project project);

    public Page<Project> getList(Integer pageNum, Integer pageSize, String uid);

    public Project getByID(Integer pid);

    public String done(Project project);

    public String end(Project project);
}
