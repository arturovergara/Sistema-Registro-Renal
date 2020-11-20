package DAO;

import models.Paciente;

import java.util.List;

public interface PacienteDAO {
    Paciente getPaciente(String rut);
    Paciente getPaciente(int id);
    List<Paciente> getPacientes();
    Paciente createPaciente(Paciente paciente);
    void deletePaciente(String id);
}
