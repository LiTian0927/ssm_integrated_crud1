package com.atguigu.crud.service;

import com.atguigu.crud.bean.Employee;

import java.util.EnumMap;
import java.util.List;

public interface EmployeeService {
    /**
     * 获取所有员工信息列表
     * @return
     */
    List<Employee> getAll();

    /**
     * 保存员工
     * @param employee
     */
    void saveEmp(Employee employee);

    /**
     * 检查用户名是否已经在数据库中存在
     * @param employee
     * @return
     */
    boolean checkEmpName(Employee employee);

    /**
     * 根据id查询员工
     * @param empId
     * @return
     */
    Employee getEmp(Integer empId);

    /**
     * 根据empId，以及相关信息修改员工信息
     * @param employee
     */
    void updateEmp(Employee employee);

    /**
     * 删除员工
     * @param empId
     */
    void deleteEmp(Integer empId);

    /**
     * 根据id批量删除员工信息
     * @param empIds
     * @return
     */
    void deleteEmpInBatch(List<Integer> empIds);
}
