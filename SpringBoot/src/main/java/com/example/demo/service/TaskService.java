package com.example.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.Task;

public interface TaskService {

    public String add(Task task);

    public Page<Task> getList(Integer pageNum, Integer pageSize, String uid);

    public Page<Task> getListInP(Integer pageNum, Integer pageSize, String pid);

    public Task getByID(Integer tid);

    public String done(Task task);

    public String end(Task task);

    public String submit(Task task);

    public String undo(Task task);
}
