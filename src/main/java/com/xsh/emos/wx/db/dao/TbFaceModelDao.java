package com.xsh.emos.wx.db.dao;

import com.xsh.emos.wx.db.pojo.TbFaceModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TbFaceModelDao {
    int deleteByPrimaryKey(Integer id);

    int insert(TbFaceModel record);

    int insertSelective(TbFaceModel record);

    TbFaceModel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TbFaceModel record);

    int updateByPrimaryKey(TbFaceModel record);
}