package com.graphqljava.employee.service;

import java.util.List;

import com.graphqljava.employee.model.Employee;

public interface IEmployeeService {
    public Employee addEmployee(Employee employee);
    public List<Employee> getEmployees();
    public Employee getEmployeeById(Long empId);
}
