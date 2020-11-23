package models;

import models.enums.PersonalEnum;
import models.enums.PrevisionEnum;

import java.time.LocalDateTime;
import java.util.Date;

public class Paciente extends Usuario{

    private Date fechaNacimiento;
    private String nacionalidad;
    private PrevisionEnum prevision;
    private String telefonoAlternativo;
    private String emailAlternativo;
    private FichaMedica fichaPaciente;

    public Paciente(String rut, String contrasena, String nombre, String direccion, String email, String telefono) {
        super(rut, contrasena, nombre, direccion, email, telefono);
    }

    public Paciente(int id, String rut, String contrasena, String nombre, String direccion, String email, String telefono, LocalDateTime fechaCreacion) {
        super(id, rut, contrasena, nombre, direccion, email, telefono, fechaCreacion);
    }

    public Paciente(int id, String rut, String contrasena, String nombre, String direccion, String email, String telefono, LocalDateTime fechaCreacion,
                    Date fechaNacimiento, String nacionalidad, int prevision, String telefonoAlternativo, String emailAlternativo) {
        super(id, rut, contrasena, nombre, direccion, email, telefono, fechaCreacion);
        this.fechaNacimiento = fechaNacimiento;
        this.nacionalidad=nacionalidad;
        this.telefonoAlternativo= telefonoAlternativo;
        this.emailAlternativo =emailAlternativo;
        this.prevision = PrevisionEnum.fromInteger(prevision);
        this.fichaPaciente = null;
    }

    public FichaMedica getFichaPaciente(){
        return fichaPaciente;
    }

    public void setFichaPaciente(FichaMedica fichaPaciente1){
        this.fichaPaciente = fichaPaciente1;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public PrevisionEnum getPrevision() {
        return prevision;
    }

    public void setPrevision(PrevisionEnum prevision) {
        this.prevision = prevision;
    }

    public String getTelefonoAlternativo() {
        return telefonoAlternativo;
    }

    public void setTelefonoAlternativo(String telefonoAlternativo) {
        this.telefonoAlternativo = telefonoAlternativo;
    }

    public String getEmailAlternativo() {
        return emailAlternativo;
    }

    public void setEmailAlternativo(String emailAlternativo) {
        this.emailAlternativo = emailAlternativo;
    }

    public void showUserData(){
        System.out.print(
                        "Id: " + this.getId() + "\n" +
                        "Nombre: " + this.getNombre() + "\n" +
                        "Rut: " +     this.getRut() + "\n" +
                        "Telefono: " + this.getTelefono()+ "\n" +
                        "Direccion: " + this.getDireccion() + "\n" +
                        "Email: " + this.getEmail() + "\n" +
                        "Fecha de nacimiento: " + this.getFechaNacimiento() + "\n" +
                        "Nacionalidad: " + this.getNacionalidad() + "\n" +
                        "Prevision: " + this.getPrevision() + "\n" +
                        "Telefono alternativo: " + this.getTelefonoAlternativo() + "\n" +
                        "Email alternativo: " + this.getEmailAlternativo() + "\n"
        );
    }
}
