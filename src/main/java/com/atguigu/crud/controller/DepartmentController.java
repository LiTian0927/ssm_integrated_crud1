package com.atguigu.crud.controller;

import com.atguigu.crud.bean.Department;
import com.atguigu.crud.bean.Msg;
import com.atguigu.crud.service.DepartmetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class DepartmentController {
    @Autowired
    private DepartmetService departmetService;

    @RequestMapping(value = "/depts", method = RequestMethod.GET)
    @ResponseBody
    public Msg getDepts() {
        //查出所有的部门信息
        List<Department> departments = departmetService.getDepts();
        return Msg.success().add("depts", departments);
    }
}
