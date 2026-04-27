package com.yuan.medical.service;

import com.yuan.medical.entity.Department;
import com.yuan.medical.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Transactional(readOnly = true)
    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Department findById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("科室不存在: id=" + id));
    }

    @Transactional
    public Department create(Department department) {
        return departmentRepository.save(department);
    }

    @Transactional
    public Department update(Long id, Department departmentDetails) {
        Department department = findById(id);
        if (departmentDetails.getName() != null) department.setName(departmentDetails.getName());
        if (departmentDetails.getDescription() != null) department.setDescription(departmentDetails.getDescription());
        return departmentRepository.save(department);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new RuntimeException("科室不存在: id=" + id);
        }
        departmentRepository.deleteById(id);
    }
}
