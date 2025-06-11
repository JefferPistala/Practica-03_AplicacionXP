package madstodolist.controller;

import madstodolist.dto.UsuarioData;
import madstodolist.service.UsuarioService;
import madstodolist.authentication.ManagerUserSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private ManagerUserSession managerUserSession;

    @Test
    public void getListadoUsuariosDevuelveListaUsuarios() throws Exception {
        // GIVEN
        // Mockeamos la lista de usuarios
        UsuarioData usuario1 = new UsuarioData();
        usuario1.setId(1L);
        usuario1.setEmail("usuario1@test.com");
        
        UsuarioData usuario2 = new UsuarioData();
        usuario2.setId(2L);
        usuario2.setEmail("usuario2@test.com");
        
        List<UsuarioData> usuarios = Arrays.asList(usuario1, usuario2);
        
        when(usuarioService.findAllUsuarios()).thenReturn(usuarios);
        when(managerUserSession.usuarioLogeado()).thenReturn(1L);
        
        // WHEN, THEN
        // Realizamos la petición GET al listado de usuarios
        this.mockMvc.perform(get("/registrados"))
                .andExpect(status().isOk())
                .andExpect(view().name("listaUsuarios"))
                .andExpect(model().attribute("usuarios", usuarios))
                .andExpect(model().attribute("usuarioLogueado", true))
                .andExpect(content().string(allOf(
                    containsString("usuario1@test.com"),
                    containsString("usuario2@test.com"),
                    containsString("Listado de usuarios")
                )));
    }

    @Test
    public void getListadoUsuariosSinUsuarioLogueado() throws Exception {
        // GIVEN
        List<UsuarioData> usuarios = Arrays.asList();
        
        when(usuarioService.findAllUsuarios()).thenReturn(usuarios);
        when(managerUserSession.usuarioLogeado()).thenReturn(null);
        
        // WHEN, THEN
        this.mockMvc.perform(get("/registrados"))
                .andExpect(status().isOk())
                .andExpect(view().name("listaUsuarios"))
                .andExpect(model().attribute("usuarioLogueado", false))
                .andExpect(content().string(containsString("Listado de usuarios")));
    }

    @Test
    public void getAboutConUsuarioLogueado() throws Exception {
        // GIVEN
        when(managerUserSession.usuarioLogeado()).thenReturn(1L);
        
        // WHEN, THEN
        this.mockMvc.perform(get("/about"))
                .andExpect(status().isOk())
                .andExpect(view().name("about"))
                .andExpect(model().attribute("usuarioLogueado", true))
                .andExpect(content().string(allOf(
                    containsString("ToDoList"),
                    containsString("Jefferson Pistala"),
                    containsString("Versión 1.1.0")
                )));
    }

    @Test
    public void getAboutSinUsuarioLogueado() throws Exception {
        // GIVEN
        when(managerUserSession.usuarioLogeado()).thenReturn(null);
        
        // WHEN, THEN
        this.mockMvc.perform(get("/about"))
                .andExpect(status().isOk())
                .andExpect(view().name("about"))
                .andExpect(model().attribute("usuarioLogueado", false))
                .andExpect(content().string(containsString("ToDoList")));
    }
}