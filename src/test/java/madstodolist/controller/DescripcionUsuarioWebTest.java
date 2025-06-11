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

import java.util.Date;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class DescripcionUsuarioWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private ManagerUserSession managerUserSession;

    @Test
    public void getDescripcionUsuarioMuestraDetalles() throws Exception {
        // GIVEN
        UsuarioData usuario = new UsuarioData();
        usuario.setId(1L);
        usuario.setEmail("usuario@test.com");
        usuario.setNombre("Usuario Prueba");
        usuario.setFechaNacimiento(new Date());

        when(usuarioService.findById(1L)).thenReturn(usuario);
        when(managerUserSession.usuarioLogeado()).thenReturn(2L);

        // WHEN, THEN
        this.mockMvc.perform(get("/registrados/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("descripcionUsuario"))
                .andExpect(model().attribute("usuario", usuario))
                .andExpect(model().attribute("usuarioLogueado", true))
                .andExpect(content().string(containsString("usuario@test.com")))
                .andExpect(content().string(containsString("Usuario Prueba")))
                .andExpect(content().string(containsString("Descripción del Usuario")));
    }

    @Test
    public void getDescripcionUsuarioSinLoguearLanzaExcepcion() throws Exception {
        // GIVEN
        when(managerUserSession.usuarioLogeado()).thenReturn(null);

        // WHEN, THEN
        this.mockMvc.perform(get("/registrados/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void getDescripcionUsuarioInexistenteLanzaExcepcion() throws Exception {
        // GIVEN
        when(managerUserSession.usuarioLogeado()).thenReturn(1L);
        when(usuarioService.findById(999L)).thenReturn(null);

        // WHEN, THEN
        this.mockMvc.perform(get("/registrados/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getDescripcionUsuarioConDatosIncompletos() throws Exception {
        // GIVEN
        UsuarioData usuario = new UsuarioData();
        usuario.setId(1L);
        usuario.setEmail("usuario@test.com");
        // No establecemos nombre ni fecha de nacimiento

        when(usuarioService.findById(1L)).thenReturn(usuario);
        when(managerUserSession.usuarioLogeado()).thenReturn(2L);

        // WHEN, THEN
        this.mockMvc.perform(get("/registrados/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("descripcionUsuario"))
                .andExpect(content().string(containsString("usuario@test.com")))
                .andExpect(content().string(containsString("No especificado")))
                .andExpect(content().string(containsString("No especificada")));
    }
}