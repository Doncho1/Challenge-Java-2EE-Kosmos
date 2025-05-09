package com.hospital.agenda.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hospital.agenda.model.Cita;
import com.hospital.agenda.model.Consultorio;
import com.hospital.agenda.model.Doctor;

public interface CitaRepository extends JpaRepository<Cita, Long> {

	    boolean existsByHorarioAndConsultorio(LocalDateTime horario, Consultorio consultorio);

	    boolean existsByDoctorAndHorario(Doctor doctor, LocalDateTime horario);

	    List<Cita> findByNombrePacienteAndHorarioBetween(String nombrePaciente, LocalDateTime desde, LocalDateTime hasta);

	    long countByDoctorAndHorarioBetween(Doctor doctor, LocalDateTime desde, LocalDateTime hasta);

	    @Query("SELECT c FROM Cita c WHERE " +
	           "(:doctorId IS NULL OR c.doctor.id = :doctorId) AND " +
	           "(:consultorioId IS NULL OR c.consultorio.id = :consultorioId) AND " +
	           "(:fecha IS NULL OR DATE(c.horario) = :fecha)")
	    List<Cita> findByFiltros(@Param("doctorId") Long doctorId,
	                             @Param("consultorioId") Long consultorioId,
	                             @Param("fecha") LocalDate fecha);

}
