package com.example.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.Group;

public interface GroupService {
    public String add(Group group);

    public Page<Group> getList(Integer pageNum, Integer pageSize, String uid);

    public Group getByKey(Integer key);

    public Group getByPid(Integer pid, String uid);

    public Page<Group> getByGid(Integer pageNum, Integer pageSize, String gid);
}
