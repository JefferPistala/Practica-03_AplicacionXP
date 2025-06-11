# Documentación técnica de la Práctica XP

Este documento describe los cambios y funcionalidades implementadas en la práctica 2 del proyecto **ToDoList**, como parte del trabajo colaborativo en equipo. Se detalla el trabajo técnico realizado desde la perspectiva de un desarrollador que se incorpora al proyecto.

---

## Nuevas clases y métodos implementados

### Controladores:

- `NavbarFuncionalityTest`: Test de integración para verificar el comportamiento de la barra de navegación.
- `UsuarioController`: Se extendió para manejar la vista de tareas por usuario.
- `LoginController`: Modificado para gestionar la sesión del usuario de forma más explícita y con redirecciones condicionales.

### Servicios:

- `ManagerUserSession`: Servicio auxiliar para manejar el login, logout y verificación de sesiones de usuario.

---

## Plantillas Thymeleaf añadidas o modificadas

- `fragments.html`: Se añadió el fragmento `navbar`, reutilizable en todas las vistas. Este contiene la barra de menú con enlaces condicionales según si el usuario está logueado.
- `formLogin.html`: Se integró la barra de menú en la vista de login.
- `listaTareas.html`: Vista principal de tareas, muestra la barra con el nombre del usuario y el menú desplegable.

---

## Tests implementados

- `NavbarFuncionalityTest`:
  - `testNavbarInAboutPageWhenLoggedOut()`: Verifica que al no estar logueado se muestran los enlaces "Login" y "Registro" y **no** "Cerrar sesión".
- `UsuarioWebTest` y `TareaWebTest` fueron adaptados para reflejar el nuevo menú.

---

## Ejemplo de código relevante

### Fragmento del fragmento `navbar`:

```html
<ul class="navbar-nav ms-auto" th:if="${session.usuario != null}">
    <li class="nav-item dropdown">
        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button"
           data-bs-toggle="dropdown" aria-expanded="false">
            <span th:text="${session.usuario.nombre}">Usuario</span>
        </a>
        <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="navbarDropdown">
            <li><a class="dropdown-item" href="#">Cuenta</a></li>
            <li><a class="dropdown-item" th:href="@{/logout}" th:text="'Cerrar sesión ' + ${session.usuario.nombre}"></a></li>
        </ul>
    </li>
</ul>

## Consideraciones finales

- La funcionalidad fue desarrollada en la rama `002-navbar`, probada mediante tests unitarios e integración.
- Se hizo merge en `main` luego de verificar que el despliegue mediante Docker era exitoso.
- Esta documentación permite al resto del equipo comprender la integración de la navegación contextual.

---

> Elaborado por Jefferson Pistala para la práctica XP del proyecto ToDoList.

