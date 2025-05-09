package com.hospital.agenda.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import com.hospital.agenda.model.Cita;
import com.hospital.agenda.model.Consultorio;
import com.hospital.agenda.model.Doctor;
import com.hospital.agenda.repository.CitaRepository;
import com.hospital.agenda.repository.ConsultorioRepository;
import com.hospital.agenda.repository.DoctorRepository;


@Service
public class CitaService {

    private final CitaRepository citaRepository;
    private final DoctorRepository doctorRepository;
    private final ConsultorioRepository consultorioRepository;
    private final Logger logger = LoggerFactory.getLogger(CitaService.class);

    public CitaService(CitaRepository citaRepository, DoctorRepository doctorRepository, ConsultorioRepository consultorioRepository) {
        this.citaRepository = citaRepository;
        this.doctorRepository = doctorRepository;
        this.consultorioRepository = consultorioRepository;
    }

    public List<Cita> buscarCitas(Long doctorId, Long consultorioId, LocalDate fecha) {
        try {
            return citaRepository.findByFiltros(doctorId, consultorioId, fecha);
        } catch (Exception e) {
            logger.error("Error al buscar citas con filtros", e);
            throw new RuntimeException("Error al buscar las citas.");
        }
    }

    public void agregarCita(Long doctorId, Long consultorioId, String horarioStr, String nombrePaciente) {
        try {
            LocalDateTime fechaHora = LocalDateTime.parse(horarioStr);
            LocalDate fecha = fechaHora.toLocalDate();

            Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new IllegalArgumentException("Doctor no encontrado."));
            Consultorio consultorio = consultorioRepository.findById(consultorioId).orElseThrow(() -> new IllegalArgumentException("Consultorio no encontrado."));

            // Reglas de validación
            if (citaRepository.existsByHorarioAndConsultorio(fechaHora, consultorio)) {
                throw new IllegalStateException("El consultorio ya tiene una cita en esa hora.");
            }

            if (citaRepository.existsByDoctorAndHorario(doctor, fechaHora)) {
                throw new IllegalStateException("El doctor ya tiene una cita en esa hora.");
            }

            List<Cita> citasPaciente = citaRepository.findByNombrePacienteAndHorarioBetween(
                    nombrePaciente,
                    fechaHora.minusHours(2),
                    fechaHora.plusHours(2)
            );
            if (!citasPaciente.isEmpty()) {
                throw new IllegalStateException("El paciente tiene otra cita muy cerca.");
            }

            long totalCitasDoctor = citaRepository.countByDoctorAndHorarioBetween(
                    doctor,
                    fecha.atStartOfDay(),
                    fecha.atTime(23, 59)
            );
            if (totalCitasDoctor >= 8) {
                throw new IllegalStateException("El doctor ya tiene 8 citas ese día.");
            }

            // Guardar cita
            Cita nueva = new Cita();
            nueva.setDoctor(doctor);
            nueva.setConsultorio(consultorio);
            nueva.setHorario(fechaHora);
            nueva.setNombrePaciente(nombrePaciente);
            citaRepository.save(nueva);

            logger.info("Cita creada correctamente para el paciente {}", nombrePaciente);

        } catch (Exception e) {
            logger.error("Error al agregar la cita", e);
            throw e;
        }
    }

    public void eliminarCita(Long id) {
        try {
            Cita cita = citaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Cita no encontrada."));
            if (cita.getHorario().isAfter(LocalDateTime.now())) {
                citaRepository.delete(cita);
                logger.info("Cita eliminada correctamente.");
            } else {
                throw new IllegalStateException("La cita ya pasó y no puede eliminarse.");
            }
        } catch (Exception e) {
            logger.error("Error al eliminar la cita", e);
            throw e;
        }
    }

    public List<Cita> obtenerTodasLasCitas() {
        return citaRepository.findAll();
    }

    public List<Doctor> obtenerDoctores() {
        return doctorRepository.findAll();
    }

    public List<Consultorio> obtenerConsultorios() {
        return consultorioRepository.findAll();
    }
}

