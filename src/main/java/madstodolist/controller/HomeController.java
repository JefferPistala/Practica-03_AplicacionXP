package madstodolist.controller;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.controller.exception.UsuarioNoLogeadoException;
import madstodolist.service.UsuarioService;
import madstodolist.dto.UsuarioData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    ManagerUserSession managerUserSession;
    
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/about")
    public String about(Model model) {
        // Obtener información del usuario logueado si existe
        Long idUsuarioLogueado = managerUserSession.usuarioLogeado();

        if (idUsuarioLogueado != null) {
            // Usuario está logueado - añadir información adicional si es necesario
            model.addAttribute("usuarioLogueado", true);
        } else {
            // Usuario no está logueado
            model.addAttribute("usuarioLogueado", false);
        }

        return "about";
    }

    @GetMapping("/registrados")
    public String listarUsuarios(Model model) {
        // Obtener información del usuario logueado si existe
        Long idUsuarioLogueado = managerUserSession.usuarioLogeado();
        
        if (idUsuarioLogueado != null) {
            // Usuario está logueado - añadir información adicional si es necesario
            model.addAttribute("usuarioLogueado", true);
        } else {
            // Usuario no está logueado
            model.addAttribute("usuarioLogueado", false);
        }
        
        List<UsuarioData> usuarios = usuarioService.findAllUsuarios();
        model.addAttribute("usuarios", usuarios);
        return "listaUsuarios";
    }
    
    @GetMapping("/registrados/{id}")
    public String descripcionUsuario(@PathVariable(value="id") Long idUsuario, Model model, HttpServletRequest request) {
        
        Long idUsuarioLogeado = managerUserSession.usuarioLogeado();
        if (idUsuarioLogeado == null) {
            throw new UsuarioNoLogeadoException();
        }
        
        UsuarioData usuario = usuarioService.findById(idUsuario);
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("usuarioLogueado", true);
        
        return "descripcionUsuario";
    }

    // Excepción para usuario no encontrado
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Usuario no encontrado")
    public static class UsuarioNotFoundException extends RuntimeException {
    }
}