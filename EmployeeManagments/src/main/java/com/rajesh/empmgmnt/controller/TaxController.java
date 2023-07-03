package com.rajesh.empmgmnt.controller;


	
    import org.springframework.http.ResponseEntity;
	import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
	import org.springframework.web.bind.annotation.RestController;

import com.rajesh.empmgmnt.model.Employee;

import java.time.LocalDate;
	import java.time.Month;
import java.util.Arrays;
import java.util.HashMap;
	import java.util.Map;

	@RestController
	@RequestMapping("/tax")
	public class TaxController {
	    private Map<Integer, Employee> employeeMap;

	    public TaxController() {
	        employeeMap = new HashMap<Integer, Employee>();
	        // Add some dummy employees for testing
	        Employee employee1 = new Employee(1, "John", "Doe", "john@example.com",
	                Arrays.asList("1234567890"), LocalDate.of(2023, 5, 16), 750000);
	        employeeMap.put(employee1.getEmployeeId(), employee1);
	    }

	    @GetMapping("/{employeeId}")
	    public ResponseEntity<Map<String, Object>> calculateTax(@PathVariable int employeeId) {
	        Employee employee = employeeMap.get(employeeId);
	        if (employee == null) {
	            return ResponseEntity.notFound().build();
	        }

	        double yearlySalary = calculateYearlySalary(employee);
	        double taxAmount = calculateTaxAmount(yearlySalary);
	        double cessAmount = calculateCessAmount(yearlySalary);

	        Map<String, Object> response = new HashMap<String, Object>();
	        response.put("employeeId", employee.getEmployeeId());
	        response.put("firstName", employee.getFirstName());
	        response.put("lastName", employee.getLastName());
	        response.put("yearlySalary", yearlySalary);
	        response.put("taxAmount", taxAmount);
	        response.put("cessAmount", cessAmount);

	        return ResponseEntity.ok(response);
	    }

	    private double calculateYearlySalary(Employee employee) {
	        LocalDate currentDate = LocalDate.now();
	        LocalDate joiningDate = employee.getDoj();

	        int currentYear = currentDate.getYear();
	        int joiningYear = joiningDate.getYear();
	        int joiningMonth = joiningDate.getMonthValue();

	        int monthsWorked = 0;

	        if (joiningYear < currentYear) {
	            if (joiningMonth <= Month.MARCH.getValue()) {
	                monthsWorked = 12 - (Month.MARCH.getValue() - joiningMonth);
	            } else {
	                monthsWorked = 12;
	            }
	        } else if (joiningYear == currentYear && joiningMonth <= Month.MARCH.getValue()) {
	            monthsWorked = Month.MARCH.getValue() - joiningMonth + 1;
	        }

	        return employee.getSalary() * monthsWorked;
	    }

	    private double calculateTaxAmount(double yearlySalary) {
	        if (yearlySalary <= 250000) {
	            return 0;
	        } else if (yearlySalary <= 500000) {
	            return (yearlySalary - 250000) * 0.05;
	        } else if (yearlySalary <= 1000000) {
	            return 12500 + (yearlySalary - 500000) * 0.1;
	        } else {
	            return 12500 + 50000 + (yearlySalary - 1000000) * 0.2;
	        }
	    }

	    private double calculateCessAmount(double yearlySalary) {
	        if (yearlySalary > 2500000) {
	            return (yearlySalary - 2500000) * 0.02;
	        } else {
	            return 0;
	        }
	    }
	}


