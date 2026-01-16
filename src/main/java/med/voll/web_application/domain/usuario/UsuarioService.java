package med.voll.web_application.domain.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import med.voll.web_application.domain.RegraDeNegocioException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("O usuário não foi encontrado!"));
    }

    public void saveUser(String nome,String email, String password) {
        if (nome.isEmpty() || email.isEmpty() || password.isEmpty()) {
            throw new RegraDeNegocioException("Campos não podem ser vazios");
        } else if (usuarioRepository.existsByEmail(email)) {
            throw new RegraDeNegocioException("Este email já está em uso !!");
        }

        var passwordHash = passwordEncoder.encode(password);
        usuarioRepository.save(new Usuario(nome, email, passwordHash));
    }
}
