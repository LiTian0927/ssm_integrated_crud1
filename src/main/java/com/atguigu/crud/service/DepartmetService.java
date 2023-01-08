package com.atguigu.crud.service;

import com.atguigu.crud.bean.Department;

import java.util.List;

public interface DepartmetService {

    /**
     * 查询部门信息
     * @return
     */
    List<Department> getDepts();
}
