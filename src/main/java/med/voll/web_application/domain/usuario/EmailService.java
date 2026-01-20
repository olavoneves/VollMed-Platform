package med.voll.web_application.domain.usuario;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import med.voll.web_application.domain.RegraDeNegocioException;
import med.voll.web_application.domain.medico.DadosCadastroMedico;
import med.voll.web_application.domain.paciente.DadosCadastroPaciente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private static final String EMAIL_ORIGEM = "vollmed@email.com";
    private static final String NOME_ENVIADOR = "Clínica Voll Med";

    public static final String URL_SITE = "http://localhost:8080"; //"voll.med.com.br"

    @Async
    private void enviarEmail(String emailUsuario, String assunto, String conteudo) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setFrom(EMAIL_ORIGEM, NOME_ENVIADOR);
            helper.setTo(emailUsuario);
            helper.setSubject(assunto);
            helper.setText(conteudo, true);

        } catch(MessagingException | UnsupportedEncodingException e){
            throw new RegraDeNegocioException("Erro ao enviar email");
        }

        mailSender.send(message);
    }

    public void enviarEmailSenha(Usuario usuario) {
        String assunto = "Aqui está seu link para alterar a senha";
        String conteudo = gerarConteudoEmail("Olá [[name]],<br>"
                + "Por favor clique no link abaixo para alterar a senha:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">ALTERAR</a></h3>"
                + "Obrigado,<br>"
                + "Clínica Voll Med.", usuario.getNome(), URL_SITE + "/recuperar-conta?codigo=" + usuario.getToken());

        enviarEmail(usuario.getUsername(), assunto, conteudo);
    }

    public void sendEmailForNewPaciente(DadosCadastroPaciente paciente) {
        String title = "Bem Vindo à Clínica Voll Med";
        String body = buildEmailForNewUser("""
                Olá [[name]] !! <br>
                Segue suas informações de login. <br><br>
                
                Email: [[email]] <br>
                Senha: [[senha]] <br>
                
                <h3> <a href=\"[[url]]\" target=\"_self\"> ACESSAR SUA CONTA </a> </h3>
                Não se esqueça de modificar sua senha padrão. <br><br>
                
                Conte com nossa equipe para o que precisar, <br>
                Obrigado <br>
                Clínica Voll Med.
                """, paciente.nome(), paciente.email(), paciente.cpf(), URL_SITE);

        enviarEmail(paciente.email(), title, body);
    }

    public void sendEmailForNewMedico(DadosCadastroMedico medico) {
        String title = "Bem Vindo à Clínica Voll Med";
        String body = buildEmailForNewUser("""
                Olá [[name]] !! <br>
                Segue suas informações de login. <br><br>
                
                Email: [[email]] <br>
                Senha: [[senha]] <br><br>
                
                <h3> <a href=\"[[url]]\" target=\"_self\"> ACESSAR SUA CONTA </a> </h3>
                Não se esqueça de modificar sua senha padrão. <br>
                
                Conte com nossa equipe para o que precisar, <br>
                Obrigado <br>
                Clínica Voll Med.
                """, medico.nome(), medico.email(), medico.crm(), URL_SITE);

        enviarEmail(medico.email(), title, body);
    }

    private String gerarConteudoEmail(String template, String nome, String url) {
        return template.replace("[[name]]", nome).replace("[[URL]]", url);
    }

    private String buildEmailForNewUser(String template, String name, String email, String password, String url) {
        return template.replace("[[name]]", name).replace("[[email]]", email).replace("[[senha]]", password).replace("[[url]]", url);
    }
}
