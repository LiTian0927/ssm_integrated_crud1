package com.atguigu.crud.test;

import com.alibaba.druid.pool.DruidDataSource;
import com.atguigu.crud.bean.Department;
import com.atguigu.crud.bean.Employee;
import com.atguigu.crud.dao.DepartmentMapper;
import com.atguigu.crud.dao.EmployeeMapper;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

/**
 * 测试dao层的工作
 *
 * 1.导入Spring的测试模块
 * 2.@ContextConfiguration指定Spring配置文件的位置
 * 3.直接@Autowire要使用的组件即可
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class MapperTest {
    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private SqlSession sqlSession;
    /**
     * 测试Department的Mapper
     */
    @Test
    public void testCRUD() throws SQLException {
//        //1.创建Spring IOC容器
//        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
//        //2.从容器中获取mapper
//        DepartmentMapper departmentMapper = applicationContext.getBean(DepartmentMapper.class);
//
//        departmentMapper.insertSelective(new Department("开发部"));
//        departmentMapper.insertSelective(new Department(null, "测试部"));

        //2.生成员工数据
//        employeeMapper.insertSelective(new Employee("Jerry", "男", "122@qq.com", 1));
        //批量插入多个员工
//        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
//        for (int i = 0; i < 1000; i++) {
//            String uid = UUID.randomUUID().toString().substring(0, 5) + '_' + i;
//            mapper.insertSelective(new Employee(uid, "男", uid + "@atguigu.com", 1));
//        }

    }
}
