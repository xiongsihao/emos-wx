package com.xsh.emos.wx.db.dao;

import com.xsh.emos.wx.db.pojo.SysConfig;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysConfigDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SysConfig record);

    int insertSelective(SysConfig record);

    SysConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysConfig record);

    int updateByPrimaryKey(SysConfig record);
}