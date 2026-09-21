package modelo;

public class Usuario {

    private int id;
    private String nombre;
    private String correo;
    private String getPassword;
    private String perfil;

    public Usuario(int id, String nombre, String correo, String getPassword, String perfil) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.getPassword = getPassword;
        this.perfil = perfil;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getGetPassword() {
        return getPassword;
    }

    public void setGetPassword(String getPassword) {
        this.getPassword = getPassword;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }
}
