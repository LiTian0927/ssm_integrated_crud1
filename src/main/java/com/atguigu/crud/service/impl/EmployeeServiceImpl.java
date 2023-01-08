package com.atguigu.crud.service.impl;

import com.atguigu.crud.bean.Employee;
import com.atguigu.crud.bean.EmployeeExample;
import com.atguigu.crud.dao.EmployeeMapper;
import com.atguigu.crud.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;
    /**
     * 获取所有员工信息列表
     * @return
     */
    @Override
    public List<Employee> getAll() {
        return employeeMapper.selectByExampleWithDept(null);
    }

    /**
     * 保存员工
     *
     * @param employee
     */
    @Override
    public void saveEmp(Employee employee) {
        employeeMapper.insertSelective(employee);
    }

    /**
     * 检查用户名是否已经在数据库中存在
     *
     * @param employee
     * @return
     */
    @Override
    public boolean checkEmpName(Employee employee) {
        EmployeeExample employeeExample = new EmployeeExample();
        EmployeeExample.Criteria criteria = employeeExample.createCriteria();
        criteria.andEmpNameEqualTo(employee.getEmpName());
        long empNameCount = employeeMapper.countByExample(employeeExample);
        return empNameCount > 0;
    }

    /**
     * 根据id查询员工
     *
     * @param empId
     * @return
     */
    @Override
    public Employee getEmp(Integer empId) {
        return employeeMapper.selectByPrimaryKey(empId);
    }

    /**
     * 根据empId，以及相关信息修改员工信息
     *
     * @param employee
     */
    @Override
    public void updateEmp(Employee employee) {
        employeeMapper.updateByPrimaryKey(employee);
    }

    /**
     * 删除员工
     *
     * @param empId
     */
    @Override
    public void deleteEmp(Integer empId) {
        employeeMapper.deleteByPrimaryKey(empId);
    }

    /**
     * 根据id批量删除员工信息
     *
     * @param empIds
     * @return
     */
    @Override
    public void deleteEmpInBatch(List<Integer> empIds) {
        EmployeeExample example = new EmployeeExample();
        EmployeeExample.Criteria criteria = example.createCriteria();
        criteria.andEmpIdIn(empIds);
        employeeMapper.deleteByExample(example);
    }
}
