package com.hospital.agenda.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hospital.agenda.service.CitaService;

@Controller
public class CitaController {
	
	 private final CitaService citaService;

	    public CitaController(CitaService citaService) {
	        this.citaService = citaService;
	    }

	    @GetMapping("/citas")
	    public String mostrarCitas(Model model) {
	        model.addAttribute("citas", citaService.obtenerTodasLasCitas());
	        model.addAttribute("doctores", citaService.obtenerDoctores());
	        model.addAttribute("consultorios", citaService.obtenerConsultorios());
	        return "citas";
	    }

	    @GetMapping("/citas/buscar")
	    public String buscarCitas(@RequestParam(required = false) Long doctorId,
	                              @RequestParam(required = false) Long consultorioId,
	                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
	                              Model model) {
	        model.addAttribute("citas", citaService.buscarCitas(doctorId, consultorioId, fecha));
	        model.addAttribute("doctores", citaService.obtenerDoctores());
	        model.addAttribute("consultorios", citaService.obtenerConsultorios());
	        return "citas";
	    }

	    @PostMapping("/citas")
	    public String agregarCita(@RequestParam Long doctorId,
	                              @RequestParam Long consultorioId,
	                              @RequestParam String horario,
	                              @RequestParam String nombrePaciente,
	                              RedirectAttributes redirectAttrs) {
	        try {
	            citaService.agregarCita(doctorId, consultorioId, horario, nombrePaciente);
	            redirectAttrs.addFlashAttribute("success", "Cita registrada correctamente.");
	        } catch (IllegalStateException | IllegalArgumentException e) {
	            redirectAttrs.addFlashAttribute("error", e.getMessage());
	        } catch (Exception e) {
	            redirectAttrs.addFlashAttribute("error", "Error inesperado al registrar la cita.");
	        }
	        return "redirect:/citas";
	    }

	    @PostMapping("/citas/eliminar")
	    public String eliminarCita(@RequestParam Long id, RedirectAttributes redirectAttrs) {
	        try {
	            citaService.eliminarCita(id);
	            redirectAttrs.addFlashAttribute("success", "Cita cancelada correctamente.");
	        } catch (IllegalStateException | IllegalArgumentException e) {
	            redirectAttrs.addFlashAttribute("error", e.getMessage());
	        } catch (Exception e) {
	            redirectAttrs.addFlashAttribute("error", "Error inesperado al cancelar la cita.");
	        }
	        return "redirect:/citas";
	    }

}
