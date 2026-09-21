package controlador;

import data.UsuarioData;
import modelo.Usuario;

import java.util.ArrayList;

public class UsuarioControlador {

public ArrayList<Usuario> listaUsuarios(){
    return UsuarioData.usuarios;
}

public Usuario validarCredenciales(String email, String password){
    for(Usuario usuario : UsuarioData.usuarios){
        if(usuario.getCorreo().equals(email) && usuario.getGetPassword().equals(password)){
            return usuario;
        }
    }

    return null;
}
public Usuario buscarPorId(int id){
    for(Usuario usuario : UsuarioData.usuarios){
        if(usuario.getId()==id){
            return usuario;
        }

    }
    return null;
}

}
