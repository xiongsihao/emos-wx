package com.xsh.emos.wx.db.dao;

import com.xsh.emos.wx.db.pojo.TbCheckin;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TbCheckinDao {
    int deleteByPrimaryKey(Integer id);

    int insert(TbCheckin record);

    int insertSelective(TbCheckin record);

    TbCheckin selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TbCheckin record);

    int updateByPrimaryKey(TbCheckin record);
}