package com.hospital.agenda.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hospital.agenda.model.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {}

