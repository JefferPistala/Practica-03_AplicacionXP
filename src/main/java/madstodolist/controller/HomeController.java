package madstodolist.controller;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.service.UsuarioService;
import madstodolist.dto.UsuarioData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
}