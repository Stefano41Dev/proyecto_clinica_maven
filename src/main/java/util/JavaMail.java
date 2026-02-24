package util;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class JavaMail {
    public static void enviarCorreo(String destinoCorreo, String nombres, String apellidos, String passwordGenerada, String linkVerificacion) throws Exception {

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        "cibertec.clinica@gmail.com",
                        "zgge qemu qrkr ughl"
                );
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("cibertec.clinica@gmail.com"));
        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(destinoCorreo)
        );
        message.setSubject("Credenciales de acceso - Sistema Clínica");

        String cuerpo = """
        Estimado/a %s %s,
        
        Le damos la bienvenida al Sistema de Gestión Clínica.
        
        Sus credenciales de acceso han sido generadas correctamente:
        
        Correo electrónico: %s
        Contraseña: %s
        Para acceder al sistema debe de verificarse: %s
        
        Si usted no solicitó este registro, comuníquese con el área administrativa.
        
        Atentamente,
        Equipo de Soporte
        Clínica Cibertec
        """.formatted(nombres, apellidos, destinoCorreo, passwordGenerada, linkVerificacion);

        message.setText(cuerpo);
        Transport.send(message);
    }
}
