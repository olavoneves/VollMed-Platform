package med.voll.web_application.controller;

import jakarta.validation.Valid;
import med.voll.web_application.domain.RegraDeNegocioException;
import med.voll.web_application.domain.usuario.DadosAlterarSenha;
import med.voll.web_application.domain.usuario.Usuario;
import med.voll.web_application.domain.usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    private static final String PAGINA_CADASTRO = "autenticacao/formulario-de-alterar-senha";
    private static final String REDIRECT_HOME = "redirect:home";

    @GetMapping("/login")
    public String carregarLogin(){
        return "autenticacao/login";
    }

    @GetMapping("/alterar-senha")
    public String carregarAlterarSenha() {
        return "autenticacao/formulario-de-alterar-senha";
    }

    @PostMapping("/alterar-senha")
    public String alterarSenha(@Valid @ModelAttribute("dados") DadosAlterarSenha dados, BindingResult result, Model model, @AuthenticationPrincipal Usuario usuarioLogado) {
        if (result.hasErrors()) {
            model.addAttribute("dados", dados);
            return PAGINA_CADASTRO;
        }

        try {
            usuarioService.alterarSenha(dados, usuarioLogado);
            return REDIRECT_HOME;

        } catch (RegraDeNegocioException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("dados", dados);
            return PAGINA_CADASTRO;
        }
    }
}
