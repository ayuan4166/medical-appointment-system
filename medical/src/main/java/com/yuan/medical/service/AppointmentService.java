package com.yuan.medical.service;

import com.yuan.medical.entity.Appointment;
import com.yuan.medical.entity.Doctor;
import com.yuan.medical.entity.User;
import com.yuan.medical.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private static final int DEFAULT_DAILY_APPOINTMENT_LIMIT = 20;

    private final AppointmentRepository appointmentRepository;
    private final UserService userService;
    private final DoctorService doctorService;

    @Transactional(readOnly = true)
    public List<Appointment> findAll() {
        List<Appointment> list = appointmentRepository.findAll();
        for (Appointment a : list) {
            if (a.getDoctor() != null) Hibernate.initialize(a.getDoctor());
            if (a.getUser() != null) Hibernate.initialize(a.getUser());
        }
        return list;
    }

    @Transactional(readOnly = true)
    public Appointment findById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("预约不存在: id=" + id));
    }

    @Transactional(readOnly = true)
    public List<Appointment> findByUserId(Long userId) {
        List<Appointment> list = appointmentRepository.findByUser_Id(userId);
        for (Appointment a : list) {
            if (a.getDoctor() != null) Hibernate.initialize(a.getDoctor());
            if (a.getUser() != null) Hibernate.initialize(a.getUser());
        }
        return list;
    }

    @Transactional(readOnly = true)
    public List<Appointment> findByDoctorId(Long doctorId) {
        List<Appointment> list = appointmentRepository.findByDoctor_Id(doctorId);
        for (Appointment a : list) {
            if (a.getDoctor() != null) Hibernate.initialize(a.getDoctor());
            if (a.getUser() != null) Hibernate.initialize(a.getUser());
        }
        return list;
    }

    @Transactional
    public Appointment create(Appointment appointment) {
        User user = userService.findById(appointment.getUser().getId());
        Doctor doctor = doctorService.findById(appointment.getDoctor().getId());

        // 校验同一时间医生不能重复预约（排除已取消的预约）
        boolean conflict = appointmentRepository.existsByDoctorIdAndAppointmentDateTimeAndStatusNot(
                doctor.getId(),
                appointment.getAppointmentDateTime(),
                Appointment.AppointmentStatus.CANCELLED
        );
        if (conflict) {
            throw new RuntimeException("该医生在此时间段已有预约，请选择其他时间");
        }

        appointment.setUser(user);
        appointment.setDoctor(doctor);
        if (appointment.getStatus() == null) {
            appointment.setStatus(Appointment.AppointmentStatus.PENDING);
        }
        if (appointment.getStatus() != Appointment.AppointmentStatus.CANCELLED) {
            validateDoctorDailyLimit(doctor.getId(), appointment.getAppointmentDateTime(), null);
        }
        return appointmentRepository.save(appointment);
    }

    @Transactional
    public Appointment update(Long id, Appointment appointmentDetails) {
        Appointment appointment = findById(id);

        // 如果修改了医生或时间，需要重新校验（排除当前预约）
        Long doctorId = appointmentDetails.getDoctor() != null && appointmentDetails.getDoctor().getId() != null
                ? appointmentDetails.getDoctor().getId() : appointment.getDoctor().getId();
        LocalDateTime dateTime = appointmentDetails.getAppointmentDateTime() != null
                ? appointmentDetails.getAppointmentDateTime() : appointment.getAppointmentDateTime();
        Appointment.AppointmentStatus targetStatus = appointmentDetails.getStatus() != null
                ? appointmentDetails.getStatus() : appointment.getStatus();

        boolean conflict = appointmentRepository.existsByDoctorIdAndAppointmentDateTimeAndIdNotAndStatusNot(
                doctorId, dateTime, id, Appointment.AppointmentStatus.CANCELLED);
        if (conflict) {
            throw new RuntimeException("该医生在此时间段已有预约，请选择其他时间");
        }
        if (targetStatus != Appointment.AppointmentStatus.CANCELLED) {
            validateDoctorDailyLimit(doctorId, dateTime, id);
        }

        if (appointmentDetails.getUser() != null && appointmentDetails.getUser().getId() != null) {
            User user = userService.findById(appointmentDetails.getUser().getId());
            appointment.setUser(user);
        }
        if (appointmentDetails.getDoctor() != null && appointmentDetails.getDoctor().getId() != null) {
            Doctor doctor = doctorService.findById(appointmentDetails.getDoctor().getId());
            appointment.setDoctor(doctor);
        }
        if (appointmentDetails.getAppointmentDateTime() != null) {
            appointment.setAppointmentDateTime(appointmentDetails.getAppointmentDateTime());
        }
        if (appointmentDetails.getStatus() != null) {
            appointment.setStatus(appointmentDetails.getStatus());
        }
        if (appointmentDetails.getNote() != null) {
            appointment.setNote(appointmentDetails.getNote());
        }
        return appointmentRepository.save(appointment);
    }

    private void validateDoctorDailyLimit(Long doctorId, LocalDateTime appointmentDateTime, Long excludeId) {
        LocalDate date = appointmentDateTime.toLocalDate();
        LocalDateTime dayStart = date.atStartOfDay();
        LocalDateTime dayEnd = date.plusDays(1).atStartOfDay();
        long count = excludeId == null
                ? appointmentRepository.countByDoctorIdAndAppointmentDateTimeBetweenAndStatusNot(
                        doctorId, dayStart, dayEnd, Appointment.AppointmentStatus.CANCELLED)
                : appointmentRepository.countByDoctorIdAndAppointmentDateTimeBetweenAndIdNotAndStatusNot(
                        doctorId, dayStart, dayEnd, excludeId, Appointment.AppointmentStatus.CANCELLED);
        Doctor doctor = doctorService.findById(doctorId);
        int dailyLimit = doctor.getDailyAppointmentLimit() != null && doctor.getDailyAppointmentLimit() > 0
                ? doctor.getDailyAppointmentLimit() : DEFAULT_DAILY_APPOINTMENT_LIMIT;
        if (count >= dailyLimit) {
            throw new RuntimeException("该医生预约达上限，请明天预约");
        }
    }

    @Transactional
    public Appointment updateStatus(Long id, Appointment.AppointmentStatus status) {
        Appointment appointment = findById(id);
        appointment.setStatus(status);
        return appointmentRepository.save(appointment);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new RuntimeException("预约不存在: id=" + id);
        }
        appointmentRepository.deleteById(id);
    }
}
