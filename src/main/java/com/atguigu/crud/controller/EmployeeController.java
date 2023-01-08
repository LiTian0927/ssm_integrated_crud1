package com.atguigu.crud.controller;

import com.atguigu.crud.bean.Employee;
import com.atguigu.crud.bean.Msg;
import com.atguigu.crud.service.EmployeeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 导入jackson包,负责将对象转为json字符串
     *
     * @param pageNum
     * @return
     */
    @RequestMapping(value = "/emps", method = RequestMethod.GET)
    @ResponseBody
    public Msg getEmpsWithJson(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        //引入PageHelper
        //在查询之前调用
        PageHelper.startPage(pageNum, 5);
        //startPage后紧跟的查询就是一个分页查询
        List<Employee> employeeList = employeeService.getAll();
        //使用PageInfo包装查询后的结果，只需要将pageInfo交给页面就可以了
        //封装了详细的分页信息，包括我们查询出来的数据，传入连续显示的页数
        PageInfo<Employee> pageInfo = new PageInfo<>(employeeList, 5);
        return Msg.success().add("pageInfo", pageInfo);
    }

    //    @RequestMapping(value = "/emps")
    public String getEmps(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, Model model) {
        //引入PageHelper
        //在查询之前调用
        PageHelper.startPage(pageNum, 5);
        //startPage后紧跟的查询就是一个分页查询
        List<Employee> employeeList = employeeService.getAll();
        //使用PageInfo包装查询后的结果，只需要将pageInfo交给页面就可以了
        //封装了详细的分页信息，包括我们查询出来的数据，传入连续显示的页数
        PageInfo<Employee> pageInfo = new PageInfo<>(employeeList, 5);
        model.addAttribute("pageInfo", pageInfo);
        return "list";
    }

    /**
     * 定义员工的保存
     * 后端保存时再进行校验数据，JSR303校验。
     *
     * @return
     */
    @RequestMapping(value = "/emps", method = RequestMethod.POST)
    @ResponseBody
    public Msg saveEmp(@RequestBody @Valid Employee employee, BindingResult result) {
        //校验失败，返回错误信息
        if (result.hasErrors()) {
            Map<String, Object> errMap = new HashMap<>();
            List<FieldError> errors = result.getFieldErrors();
            errors.forEach(error -> {
                System.out.println("错误的字段名:" + error.getField());
                System.out.println("错误信息:" + error.getDefaultMessage());
                errMap.put(error.getField(), error.getDefaultMessage());
            });
            return Msg.fail().add("errMap", errMap);
        }
        System.out.println(employee);
        employeeService.saveEmp(employee);
        return Msg.success();
    }

    @RequestMapping(value = "/checkEmpName", method = RequestMethod.POST)
    @ResponseBody
    public Msg checkEmpName(@RequestBody Employee employee) {
        //校验数据库中用户名是否重复
        boolean isDuplicated = employeeService.checkEmpName(employee);
        if (isDuplicated) {
            return Msg.fail().add("validationFailMsg", "用户名已经存在");
        } else return Msg.success();
    }

    @RequestMapping(value = "/emps/{empId}", method = RequestMethod.GET)
    @ResponseBody
    public Msg editEmp(@PathVariable("empId") Integer empId) {
        Employee employee = employeeService.getEmp(empId);
        if (employee != null) {
            return Msg.success().add("employee", employee);
        } else {
            return Msg.fail();
        }
    }

    @RequestMapping(value = "/emps", method = RequestMethod.PUT)
    @ResponseBody
    public Msg updateEmp(@RequestBody @Valid Employee employee, BindingResult result) {
        //检验信息
        if (result.hasErrors()) {
            Map<String, Object> errMap = new HashMap<>();
            List<FieldError> errors = result.getFieldErrors();
            errors.forEach(error -> {
                errMap.put(error.getField(), error.getDefaultMessage());
            });
            return Msg.fail().add("errMap", errMap);
        }
        employeeService.updateEmp(employee);
        return Msg.success();
    }

    @RequestMapping(value = "/emps/{empId}", method = RequestMethod.DELETE)
    @ResponseBody
    public Msg deleteEmp(@PathVariable("empId") Integer empId) {
        employeeService.deleteEmp(empId);
        return Msg.success();
    }

    @RequestMapping(value = "/emps", method = RequestMethod.DELETE)
    @ResponseBody
    public Msg deleteEmpInBatch(@RequestBody List<Integer> empIds) {
        employeeService.deleteEmpInBatch(empIds);
        return Msg.success();
    }
}
