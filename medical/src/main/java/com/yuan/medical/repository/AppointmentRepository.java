package com.yuan.medical.repository;

import com.yuan.medical.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    boolean existsByDoctorIdAndAppointmentDateTimeAndStatusNot(
            Long doctorId, LocalDateTime appointmentDateTime, Appointment.AppointmentStatus status);

    boolean existsByDoctorIdAndAppointmentDateTimeAndIdNotAndStatusNot(
            Long doctorId, LocalDateTime appointmentDateTime, Long excludeId, Appointment.AppointmentStatus status);
    
    long countByDoctorIdAndAppointmentDateTimeBetweenAndStatusNot(
            Long doctorId,
            LocalDateTime start,
            LocalDateTime end,
            Appointment.AppointmentStatus status);

    long countByDoctorIdAndAppointmentDateTimeBetweenAndIdNotAndStatusNot(
            Long doctorId,
            LocalDateTime start,
            LocalDateTime end,
            Long excludeId,
            Appointment.AppointmentStatus status);

    @Query("select a from Appointment a left join fetch a.doctor left join fetch a.user where a.user.id = :userId")
    List<Appointment> findByUser_Id(@Param("userId") Long userId);

    @Query("select a from Appointment a left join fetch a.doctor left join fetch a.user where a.doctor.id = :doctorId")
    List<Appointment> findByDoctor_Id(@Param("doctorId") Long doctorId);
}
