package madstodolist.controller;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.dto.TareaData;
import madstodolist.dto.UsuarioData;
import madstodolist.service.TareaService;
import madstodolist.service.TareaServiceException;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
public class TareaController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    TareaService tareaService;

    @Autowired
    ManagerUserSession managerUserSession;

    private void comprobarUsuarioLogeado(Long idUsuario) {
        Long idUsuarioLogeado = managerUserSession.usuarioLogeado();
        if (!idUsuario.equals(idUsuarioLogeado)) {
            throw new TareaServiceException("El usuario " + idUsuario + " no tiene permisos para acceder a datos de otro usuario");
        }
    }

    @GetMapping("/usuarios/{id}/tareas")
    public String listaTareas(@PathVariable(value="id") Long idUsuario, Model model, HttpSession session) {
        
        comprobarUsuarioLogeado(idUsuario);
        
        UsuarioData usuario = usuarioService.findById(idUsuario);
        if (usuario == null) {
            throw new TareaServiceException("Usuario " + idUsuario + " no existe");
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("tareas", tareaService.allTareasUsuario(idUsuario));
        return "listaTareas";
    }

    @GetMapping("/usuarios/{id}/tareas/nueva")
    public String formNuevaTarea(@PathVariable(value="id") Long idUsuario, Model model) {
        
        comprobarUsuarioLogeado(idUsuario);
        
        UsuarioData usuario = usuarioService.findById(idUsuario);
        if (usuario == null) {
            throw new TareaServiceException("Usuario " + idUsuario + " no existe");
        }

        model.addAttribute("usuario", usuario);
        return "formNuevaTarea";
    }

    @PostMapping("/usuarios/{id}/tareas/nueva")
    public String nuevaTarea(@PathVariable(value="id") Long idUsuario, 
                           @RequestParam("titulo") String titulo,
                           RedirectAttributes flash) {
        
        comprobarUsuarioLogeado(idUsuario);
        
        if (titulo.trim().isEmpty()) {
            flash.addFlashAttribute("mensaje", "El título de la tarea no puede estar vacío");
            return "redirect:/usuarios/" + idUsuario + "/tareas/nueva";
        }

        tareaService.nuevaTareaUsuario(idUsuario, titulo);
        flash.addFlashAttribute("mensaje", "Tarea creada correctamente");
        return "redirect:/usuarios/" + idUsuario + "/tareas";
    }

    @GetMapping("/tareas/{id}/editar")
    public String formEditarTarea(@PathVariable(value="id") Long idTarea, Model model) {
        
        TareaData tarea = tareaService.findById(idTarea);
        if (tarea == null) {
            throw new TareaServiceException("Tarea " + idTarea + " no existe");
        }

        // Verificar que el usuario logueado es el propietario de la tarea
        Long idUsuarioLogeado = managerUserSession.usuarioLogeado();
        if (!tarea.getUsuario().getId().equals(idUsuarioLogeado)) {
            throw new TareaServiceException("El usuario no tiene permisos para editar esta tarea");
        }

        model.addAttribute("tarea", tarea);
        return "formEditarTarea";
    }

    @PostMapping("/tareas/{id}/editar")
    public String editarTarea(@PathVariable(value="id") Long idTarea,
                            @RequestParam("titulo") String titulo,
                            RedirectAttributes flash) {
        
        TareaData tarea = tareaService.findById(idTarea);
        if (tarea == null) {
            throw new TareaServiceException("Tarea " + idTarea + " no existe");
        }

        // Verificar que el usuario logueado es el propietario de la tarea
        Long idUsuarioLogeado = managerUserSession.usuarioLogeado();
        if (!tarea.getUsuario().getId().equals(idUsuarioLogeado)) {
            throw new TareaServiceException("El usuario no tiene permisos para editar esta tarea");
        }

        if (titulo.trim().isEmpty()) {
            flash.addFlashAttribute("mensaje", "El título de la tarea no puede estar vacío");
            return "redirect:/tareas/" + idTarea + "/editar";
        }

        tareaService.modificaTarea(idTarea, titulo);
        flash.addFlashAttribute("mensaje", "Tarea modificada correctamente");
        return "redirect:/usuarios/" + tarea.getUsuario().getId() + "/tareas";
    }

    @DeleteMapping("/tareas/{id}")
    @ResponseBody
    public String borrarTarea(@PathVariable(value="id") Long idTarea) {
        
        TareaData tarea = tareaService.findById(idTarea);
        if (tarea == null) {
            throw new TareaServiceException("Tarea " + idTarea + " no existe");
        }

        // Verificar que el usuario logueado es el propietario de la tarea
        Long idUsuarioLogeado = managerUserSession.usuarioLogeado();
        if (!tarea.getUsuario().getId().equals(idUsuarioLogeado)) {
            throw new TareaServiceException("El usuario no tiene permisos para borrar esta tarea");
        }

        tareaService.borraTarea(idTarea);
        return "OK";
    }
}