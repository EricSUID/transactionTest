package com.transaction.test.dao;

import com.transaction.test.pojo.User2;


public interface User2Mapper {

    int deleteByPrimaryKey(Integer id);

    int insert(User2 record);

    int insertSelective(User2 record);

    User2 selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User2 record);

    int updateByPrimaryKey(User2 record);
}
