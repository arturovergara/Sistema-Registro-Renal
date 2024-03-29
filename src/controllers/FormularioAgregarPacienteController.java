package controllers;

import DAO.PacienteDAOImpl;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import models.Paciente;
import models.PersonalMedico;
import models.enums.PrevisionEnum;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class FormularioAgregarPacienteController implements Initializable
{
    @FXML
    private BorderPane parentContainer;
    @FXML
    private Label nombreUsuario;
    @FXML
    private JFXTextField nombre;
    @FXML
    private JFXTextField rut;
    @FXML
    private JFXDatePicker fechaNacimiento;
    @FXML
    private JFXTextField direccion;
    @FXML
    private JFXComboBox<PrevisionEnum> prevision;
    @FXML
    private JFXTextField email;
    @FXML
    private JFXTextField telefono;
    @FXML
    private JFXTextField nacionalidad;
    @FXML
    private Label errorNombre;
    @FXML
    private Label errorRut;
    @FXML
    private Label errorFechaNacimiento;
    @FXML
    private Label errorDireccion;
    @FXML
    private Label errorPrevision;
    @FXML
    private Label errorEmail;
    @FXML
    private Label errorTelefono;
    @FXML
    private Label errorNacionalidad;

    private PersonalMedico usuario;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    public void inicializar(PersonalMedico usuario)
    {
        this.usuario = usuario;

        nombreUsuario.setText(usuario.getNombre());
        prevision.setItems(FXCollections.observableArrayList(PrevisionEnum.FONASA, PrevisionEnum.ISAPRE, PrevisionEnum.CAPREDENA));

        //StringConverter para el combobox de prevision sobre el enum de prevision
        prevision.setConverter(new StringConverter<PrevisionEnum>() {
            @Override
            public String toString(PrevisionEnum objeto)
            {
                return objeto == null ? "" : objeto.getNombre();
            }

            @Override
            public PrevisionEnum fromString(String string)
            {
                return PrevisionEnum.valueOf(string);
            }
        });

        //Rut como los dioses
        rut.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                String temporalNuevo = newValue.replaceAll("[^\\d]", "");
                String temporalViejo = oldValue.replaceAll("[^\\d]", "");

                if (temporalNuevo.length() < 8)
                    rut.setText(String.format("%,d", Integer.parseInt(temporalNuevo)));
                else if (temporalNuevo.length() < 9)
                    rut.setText(String.format("%,d", Integer.parseInt(temporalNuevo.substring(0,7))) + "-" + temporalNuevo.substring(7));
                else if (temporalNuevo.length() < 10)
                    rut.setText(String.format("%,d", Integer.parseInt(temporalNuevo.substring(0,8))) + "-" + temporalNuevo.substring(8));
                else
                    rut.setText(temporalViejo);
            }
        });

        //Asignar al campo telefono la propiedad de aceptar solo un input númerico y con un signo mas antes (+)
        // Ejemplo: +56911223344
        telefono.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                if (!newValue.matches("([\\+])?\\d{0,11}"))
                    telefono.setText(oldValue);
            }
        });
    }

    public boolean verificarRut(String rut)
    {
        byte[] serie = {2,3,4,5,6,7};
        int digito, suma = 0;

        rut = rut.replaceAll("[^\\d]", "");

        for (int i = rut.length() - 2, j = 0; i >= 0; i--, j++)
        {
            if (j > 5)
                j = 0;

            suma += Character.getNumericValue(rut.charAt(i)) * serie[j];
        }

        digito = 11 - (suma - (11 * (suma / 11)));
        return (digito == Character.getNumericValue(rut.charAt(rut.length() - 1)));
    }

    @FXML
    private void cerrarSesion()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/Login.fxml"));
            Parent root = loader.load();
            Scene escena = new Scene(root);

            //Obtiene el controlador de TablaPacientes
            LoginController controlador = (LoginController) loader.getController();

            Stage ventana = (Stage) parentContainer.getScene().getWindow();
            ventana.setScene(escena);
            ventana.show();
        }
        catch (IOException | IllegalStateException excepcion)
        {
            alertaExcepcion(excepcion);
        }
    }

    @FXML
    private void cargarVistaTablaPacientes()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/TablaPacientes.fxml"));
            Parent root = loader.load();
            Scene escena = new Scene(root);

            //Obtiene el controlador de TablaPacientes
            TablaPacientesController controlador = (TablaPacientesController) loader.getController();
            controlador.inicializar(usuario);

            Stage ventana = (Stage) parentContainer.getScene().getWindow();
            ventana.setScene(escena);
            ventana.show();
        }
        catch (IOException | IllegalStateException excepcion)
        {
            alertaExcepcion(excepcion);
        }
    }

    private void cargarVistaAgregarFichaMedica(Paciente dato)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/FormularioAgregarFichaMedica.fxml"));
            Parent root = loader.load();
            Scene escena = new Scene(root);

            //Obtiene el controlador de FormularioAgregarFichaMedica
            FormularioAgregarFichaMedicaController controlador = (FormularioAgregarFichaMedicaController) loader.getController();
            controlador.inicializar(usuario, dato);

            Stage ventana = (Stage) parentContainer.getScene().getWindow();
            ventana.setScene(escena);
            ventana.show();
        }
        catch (IOException | IllegalStateException excepcion)
        {
            alertaExcepcion(excepcion);
        }
    }

    @FXML
    private void agregarPaciente(ActionEvent evento)
    {
        //Resetear todos los estilos
        nombre.setFocusColor(Color.rgb(106,114,239));
        errorNombre.setText("");
        rut.setFocusColor(Color.rgb(106,114,239));
        errorRut.setText("");
        fechaNacimiento.setDefaultColor(Color.rgb(106,114,239));
        errorFechaNacimiento.setText("");
        direccion.setFocusColor(Color.rgb(106,114,239));
        errorDireccion.setText("");
        prevision.setFocusColor(Color.rgb(106,114,239));
        errorPrevision.setText("");
        email.setFocusColor(Color.rgb(106,114,239));
        errorEmail.setText("");
        telefono.setFocusColor(Color.rgb(106,114,239));
        errorTelefono.setText("");
        nacionalidad.setFocusColor(Color.rgb(106,114,239));
        errorNacionalidad.setText("");


        if (nombre.getText().isEmpty())
        {
            nombre.requestFocus();
            nombre.setFocusColor(Color.rgb(255,23,68));
            errorNombre.setText("Por favor ingrese un nombre.");
            return;
        }

        if (rut.getText().isEmpty())
        {
            rut.requestFocus();
            rut.setFocusColor(Color.rgb(255,23,68));
            errorRut.setText("Por favor ingrese un rut.");
            return;
        }

        if (!verificarRut(rut.getText()))
        {
            rut.requestFocus();
            rut.setFocusColor(Color.rgb(255,23,68));
            errorRut.setText("Por favor ingrese un rut válido.");
            return;
        }

        if (fechaNacimiento.getValue() == null)
        {
            fechaNacimiento.requestFocus();
            fechaNacimiento.setDefaultColor(Color.rgb(255,23,68));
            errorFechaNacimiento.setText("Por favor ingrese una fecha de nacimiento.");
            return;
        }

        if (direccion.getText().isEmpty())
        {
            direccion.requestFocus();
            direccion.setFocusColor(Color.rgb(255,23,68));
            errorDireccion.setText("Por favor ingrese una dirección de domicilio.");
            return;
        }

        if (prevision.getValue() == null)
        {
            prevision.requestFocus();
            prevision.setFocusColor(Color.rgb(255,23,68));
            errorPrevision.setText("Por favor seleccione una previsión médica.");
            return;
        }

        if (email.getText().isEmpty())
        {
            email.requestFocus();
            email.setFocusColor(Color.rgb(255,23,68));
            errorEmail.setText("Por favor ingrese una dirección de correo electrónico.");
            return;
        }

        if (!email.getText().matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$"))
        {
            email.requestFocus();
            email.setFocusColor(Color.rgb(255,23,68));
            errorEmail.setText("Por favor ingrese un email válido.");
            return;
        }

        if (telefono.getText().isEmpty())
        {
            telefono.requestFocus();
            telefono.setFocusColor(Color.rgb(255,23,68));
            errorTelefono.setText("Por favor ingrese un número de teléfono.");
            return;
        }

        if (nacionalidad.getText().isEmpty())
        {
            nacionalidad.requestFocus();
            nacionalidad.setFocusColor(Color.rgb(255,23,68));
            errorNacionalidad.setText("Por favor ingrese la nacionalidad.");
            return;
        }

        Paciente retorno;
        PacienteDAOImpl pacienteDAO = new PacienteDAOImpl();

        System.out.println(rut.getText());
        System.out.println(pacienteDAO.getPaciente(rut.getText()));

        if (pacienteDAO.getPaciente(rut.getText()) != null)
        {
            alertaError("Ya existe un paciente registrado con ese rut en el sistema.");
            rut.requestFocus();
            rut.setFocusColor(Color.rgb(255,23,68));
            return;
        }

        Paciente dato = new Paciente(
                rut.getText(),
                nombre.getText(),
                direccion.getText(),
                email.getText(),
                telefono.getText(),
                Date.from(fechaNacimiento.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                prevision.getValue(),
                nacionalidad.getText()
        );

        if ((retorno = pacienteDAO.createPaciente(dato)) != null)
        {
            alertaInfo();

            if (alertaConfirmacion())
                cargarVistaAgregarFichaMedica(retorno);
            else
                cargarVistaTablaPacientes();
        }
        else
            alertaError("Ocurrió un error al ingresar el paciente en la base de datos.");
    }

    private boolean alertaConfirmacion()
    {
        Alert ventana = new Alert(Alert.AlertType.CONFIRMATION);

        ventana.setTitle("Confirmar Creación de Ficha Médica");
        ventana.initStyle(StageStyle.UTILITY);
        ventana.setContentText("¿Desea agregar ahora la ficha médica del paciente");


        Optional<ButtonType> opcion=ventana.showAndWait();

        if (opcion.get() == ButtonType.OK)
            return true;

        return false;
    }

    private void alertaInfo()
    {
        Alert ventana=new Alert(Alert.AlertType.INFORMATION);
        ventana.setTitle("¡Éxito al ingresar!");
        ventana.setHeaderText("Se ha ingresado el paciente satisfactioramente.");
        ventana.initStyle(StageStyle.UTILITY);
        java.awt.Toolkit.getDefaultToolkit().beep();
        ventana.showAndWait();
    }

    //Muestra un cuadro de dialogo de error, con un mensaje del porqué ocurrió dicho error
    private void alertaError(String mensaje)
    {
        Alert ventana=new Alert(Alert.AlertType.ERROR);
        ventana.setTitle("¡Error al ingresar!");
        ventana.setHeaderText("Error: No se pudo ingresar el paciente");
        ventana.setContentText(mensaje);
        ventana.initStyle(StageStyle.UTILITY);
        java.awt.Toolkit.getDefaultToolkit().beep();
        ventana.showAndWait();
    }

    //Muestra una alerta con toda la información detallada de la excepción
    private void alertaExcepcion(Exception excepcion)
    {
        Alert alerta = new Alert(Alert.AlertType.ERROR);

        alerta.setTitle("Alerta Excepción");
        alerta.setHeaderText(excepcion.getMessage());
        alerta.setContentText(excepcion.toString());

        //Se imprime el stacktrace de la excepcion en un cajón expandible de texto
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        excepcion.printStackTrace(pw);
        TextArea texto = new TextArea(sw.toString());
        texto.setEditable(false);
        texto.setWrapText(true);
        texto.setMaxWidth(Double.MAX_VALUE);
        texto.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(texto, Priority.ALWAYS);
        GridPane.setHgrow(texto, Priority.ALWAYS);
        GridPane contenido = new GridPane();
        contenido.setMaxWidth(Double.MAX_VALUE);
        contenido.add(new Label("El Stacktrace de la excepción fue:"),0,0);
        contenido.add(texto,0, 1);

        //Se ajusta el texto en la alerta y se muestra por pantalla
        alerta.getDialogPane().setExpandableContent(contenido);
        alerta.showAndWait();
    }
}
