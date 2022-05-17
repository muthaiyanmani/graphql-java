package com.graphqljava.employee.datafetcher;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import graphql.schema.DataFetcher;

import com.graphqljava.employee.model.Employee;
import com.graphqljava.employee.service.impl.EmployeeServiceImpl;

@Component
public class GraphQLDataFetcher {
    @Autowired
    private EmployeeServiceImpl employeeService;

    public DataFetcher getEmployees() {
        return dataFetchingEnvironment -> {
            return employeeService.getEmployees();

        };
    }

    public DataFetcher getEmployeeById() {
        return dataFetchingEnvironment -> {
            String empId = dataFetchingEnvironment.getArgument("empId");
            return employeeService.getEmployees()
                    .stream()
                    .filter(emp->emp.getEmpId().equals(Long.valueOf(empId)))
                    .findFirst()
                    .orElse(null);

        };
    }

    public DataFetcher<Employee> addEmployee() {
        return dataFetchingEnvironment -> {
            String name=dataFetchingEnvironment.getArgument("name");
            String email=dataFetchingEnvironment.getArgument("email");
            String mobile=dataFetchingEnvironment.getArgument("mobile");
            String salary=dataFetchingEnvironment.getArgument("salary");

            Employee employee = new Employee();
            employee.setName(name);
            employee.setEmail(email);
            employee.setMobile(mobile);
            employee.setSalary(Long.valueOf(salary));
            return employeeService.addEmployee(employee);
        };
    }

}
