package com.rajesh.EmployeeManagment;


import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private Map<Integer, Employee> employeeMap;

    public EmployeeController() {
        employeeMap = new HashMap<Integer, Employee>();
    }

    @PostMapping
    public ResponseEntity<String> saveEmployee(@RequestBody Employee employee) {
        // Validate the data
        if (employee.getEmployeeId() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Employee ID");
        }

        if (employee.getFirstName() == null || employee.getFirstName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("First Name is required");
        }

        if (employee.getLastName() == null || employee.getLastName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Last Name is required");
        }

        if (employee.getEmail() == null || employee.getEmail().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is required");
        }

        // Save the employee
        employeeMap.put(employee.getEmployeeId(), employee);
        return ResponseEntity.status(HttpStatus.CREATED).body("Employee details saved successfully");
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable int employeeId) {
        Employee employee = employeeMap.get(employeeId);
        if (employee == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(employee);
    }
}
