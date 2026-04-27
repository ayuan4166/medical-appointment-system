package com.yuan.medical.service;

import com.yuan.medical.entity.Department;
import com.yuan.medical.entity.Doctor;
import com.yuan.medical.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private static final int DEFAULT_DAILY_APPOINTMENT_LIMIT = 20;

    private final DoctorRepository doctorRepository;
    private final DepartmentService departmentService;

    @Transactional(readOnly = true)
    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Doctor findById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("医生不存在: id=" + id));
    }

    @Transactional(readOnly = true)
    public List<Doctor> findByDepartmentId(Long departmentId) {
        return doctorRepository.findByDepartmentId(departmentId);
    }

    @Transactional(readOnly = true)
    public Doctor findByUserId(Long userId) {
        return doctorRepository.findByUserId(userId);
    }

    @Transactional
    public Doctor create(Doctor doctor) {
        if (doctor.getDailyAppointmentLimit() == null || doctor.getDailyAppointmentLimit() <= 0) {
            doctor.setDailyAppointmentLimit(DEFAULT_DAILY_APPOINTMENT_LIMIT);
        }
        if (doctor.getDepartment() != null && doctor.getDepartment().getId() != null) {
            Department dept = departmentService.findById(doctor.getDepartment().getId());
            doctor.setDepartment(dept);
        }
        return doctorRepository.save(doctor);
    }

    @Transactional
    public Doctor update(Long id, Doctor doctorDetails) {
        Doctor doctor = findById(id);
        if (doctorDetails.getName() != null) doctor.setName(doctorDetails.getName());
        if (doctorDetails.getSpecialization() != null) doctor.setSpecialization(doctorDetails.getSpecialization());
        if (doctorDetails.getPhone() != null) doctor.setPhone(doctorDetails.getPhone());
        if (doctorDetails.getDailyAppointmentLimit() != null && doctorDetails.getDailyAppointmentLimit() > 0) {
            doctor.setDailyAppointmentLimit(doctorDetails.getDailyAppointmentLimit());
        }
        if (doctorDetails.getDepartment() != null && doctorDetails.getDepartment().getId() != null) {
            Department dept = departmentService.findById(doctorDetails.getDepartment().getId());
            doctor.setDepartment(dept);
        }
        return doctorRepository.save(doctor);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new RuntimeException("医生不存在: id=" + id);
        }
        doctorRepository.deleteById(id);
    }
}
