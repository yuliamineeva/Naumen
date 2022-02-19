package com.example.tutorial.controller;


import com.example.tutorial.dao.EmployeeDAO;
import com.example.tutorial.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class MainController {

    @Autowired
    private EmployeeDAO employeeDAO;


    @RequestMapping("/")
    public String handleRequest(Model model) {

        List<Employee> employees = employeeDAO.getEmployees();
        model.addAttribute("employees", employees);
        return "employee";
    }
}