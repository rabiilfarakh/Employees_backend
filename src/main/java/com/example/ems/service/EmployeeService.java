package com.example.ems.service;

import com.example.ems.dto.EmployeeDto;
import com.example.ems.entity.Employee;
import com.example.ems.mapper.EmployeeMapper;
import com.example.ems.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);
        Employee savedEmployee = employeeRepository.save(employee);
        return EmployeeMapper.mapToEmployeeDto(savedEmployee);
    }

    public EmployeeDto getEmployeeById(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        return employee.map(EmployeeMapper::mapToEmployeeDto).orElse(null);
    }

    public List<EmployeeDto> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(EmployeeMapper::mapToEmployeeDto)
                .collect(Collectors.toList());
    }

    public EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto) {
        Optional<Employee> existingEmployeeOpt = employeeRepository.findById(id);
        if (existingEmployeeOpt.isPresent()) {
            Employee existingEmployee = existingEmployeeOpt.get();

            // Update fields if they are not null in EmployeeDto
            if (employeeDto.getFirstName() != null) {
                existingEmployee.setFirstName(employeeDto.getFirstName());
            }
            if (employeeDto.getLastName() != null) {
                existingEmployee.setLastName(employeeDto.getLastName());
            }
            if (employeeDto.getEmail() != null) {
                existingEmployee.setEmail(employeeDto.getEmail());
            }
            // Optionally, handle the password field if needed
            // if (employeeDto.getPassword() != null) {
            //     existingEmployee.setPassword(employeeDto.getPassword());
            // }

            Employee updatedEmployee = employeeRepository.save(existingEmployee);
            return EmployeeMapper.mapToEmployeeDto(updatedEmployee);
        }
        return null;
    }

    public void deleteEmployee(Long id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
        }
    }
}
