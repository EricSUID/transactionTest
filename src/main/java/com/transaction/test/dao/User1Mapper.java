package com.transaction.test.dao;

import com.transaction.test.pojo.User1;

public interface User1Mapper {

    int deleteByPrimaryKey(Integer id);

    int insert(User1 record);

    int insertSelective(User1 record);

    User1 selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User1 record);

    int updateByPrimaryKey(User1 record);
}
