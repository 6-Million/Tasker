package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.Group;
import com.example.demo.entity.User;
import com.example.demo.mapper.GroupMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.GroupService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

@Service
public class GroupServiceImpl implements GroupService{

    @Resource
    GroupMapper groupMapper;
    @Resource
    UserMapper userMapper;

    public String add(Group group){
        User resu = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUid,group.getUid()));
        if(resu==null){
            return "fail";
        }
        Group resg = groupMapper.selectOne(Wrappers.<Group>lambdaQuery().eq(Group::getUid,group.getUid()).eq(Group::getGid,group.getGid()));
        if(resg!=null){
            return "exist";
        }
        if(group.getGid()==null)
            group.setGid(UUID.randomUUID().toString().replace("-", ""));
        groupMapper.insert(group);
        return "success";
    }

    public Page<Group> getList(Integer pageNum, Integer pageSize, String uid){
        LambdaQueryWrapper<Group> wrapper = Wrappers.<Group>lambdaQuery().eq(Group::getUid, uid);
        Page<Group> groupPage = groupMapper.selectPage(new Page<>(pageNum,pageSize),wrapper);
        return groupPage;
    }

    public Group getByKey(Integer key){
        Group group = groupMapper.selectById(key);
        return group;
    }

    public Group getByPid(Integer pid, String uid){
        Group group = groupMapper.selectOne(Wrappers.<Group>lambdaQuery().eq(Group::getPid,pid).eq(Group::getUid,uid));
        return group;
    }

    public Page<Group> getByGid(Integer pageNum, Integer pageSize, String gid){
        LambdaQueryWrapper<Group> wrapper = Wrappers.<Group>lambdaQuery().eq(Group::getGid, gid);
        Page<Group> groupPage = groupMapper.selectPage(new Page<>(pageNum,pageSize),wrapper);
        return groupPage;
    }
}
