package com.atguigu.crud.service.impl;

import com.atguigu.crud.bean.Department;
import com.atguigu.crud.dao.DepartmentMapper;
import com.atguigu.crud.service.DepartmetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmetService{
    @Autowired
    private DepartmentMapper departmentMapper;
    /**
     * 查询部门信息
     *
     * @return
     */
    @Override
    public List<Department> getDepts() {
        return departmentMapper.selectByExample(null);
    }
}
