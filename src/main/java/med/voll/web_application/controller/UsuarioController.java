package med.voll.web_application.controller;

import jakarta.validation.Valid;
import med.voll.web_application.domain.RegraDeNegocioException;
import med.voll.web_application.domain.usuario.DadosAlterarSenha;
import med.voll.web_application.domain.usuario.DadosRecuperacaoConta;
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
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    private static final String PAGINA_CADASTRO = "autenticacao/formulario-de-alterar-senha";
    private static final String REDIRECT_HOME = "redirect:home";
    public static final String FORMULARIO_RECUPERACAO_SENHA = "autenticacao/formulario-recuperacao-senha";
    private static final String FORMULARIO_RECUPERACAO_CONTA = "autenticacao/formulario-recuperacao-conta.html";

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

    @GetMapping("/esqueci-senha")
    public String carregarEsqueciSenha() {
        return FORMULARIO_RECUPERACAO_SENHA;
    }

    @PostMapping("/esqueci-senha")
    public String sendTokenEmail(@ModelAttribute("email") String email, Model model) {
        try {
            usuarioService.sendToken(email);
            return "redirect:esqueci-senha?verificar";

        } catch (RegraDeNegocioException e){
            model.addAttribute("erro", e.getMessage());
            return FORMULARIO_RECUPERACAO_SENHA;
        }
    }

    @GetMapping("/recuperar-conta")
    public String carregarAlterarSenha(@RequestParam(name = "codigo", required = false) String codigo, Model model) {
        if(codigo != null)
            model.addAttribute("codigo", codigo);

        return FORMULARIO_RECUPERACAO_CONTA;
    }

    @PostMapping("/recuperar-conta")
    public String carregarAlterarSenha(@RequestParam(name = "codigo") String codigo, Model model, DadosRecuperacaoConta dados) {
        try {
            usuarioService.recuperarConta(codigo, dados);
            return "redirect:login";

        } catch (RegraDeNegocioException e){
            model.addAttribute("error", e.getMessage());
            return FORMULARIO_RECUPERACAO_CONTA;
        }
    }
}
