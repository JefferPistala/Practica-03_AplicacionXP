package madstodolist.dto;

public class TareaData {
    private Long id;
    private String titulo;
    private UsuarioData usuario;

    // Constructor vacío
    public TareaData() {}

    // Constructor con parámetros
    public TareaData(String titulo) {
        this.titulo = titulo;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public UsuarioData getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioData usuario) {
        this.usuario = usuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TareaData tareaData = (TareaData) o;
        return id != null ? id.equals(tareaData.id) : tareaData.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}