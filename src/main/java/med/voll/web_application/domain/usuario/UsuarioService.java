package med.voll.web_application.domain.usuario;

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

    public Long saveUser(String nome,String email, String password, Perfil perfil) {
        if (nome.isEmpty() || email.isEmpty() || password.isEmpty()) {
            throw new RegraDeNegocioException("Campos não podem ser vazios");
        } else if (usuarioRepository.existsByEmail(email)) {
            throw new RegraDeNegocioException("Este email já está em uso !!");
        }

        var passwordHash = passwordEncoder.encode(password);

        // método save retorna o objeto que salvou no banco de dados
        Usuario usuario = usuarioRepository.save(new Usuario(nome, email, passwordHash, perfil));
        return usuario.getId();
    }

    public void delete(Long id) {
        usuarioRepository.deleteById(id);
    }
}
