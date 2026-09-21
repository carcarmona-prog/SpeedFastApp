package data;

import modelo.Usuario;

import java.util.ArrayList;

public class UsuarioData {

    public static ArrayList<Usuario> usuarios = new ArrayList<>();



    public static void cargarUsuario() {
        usuarios.add(new Usuario(1, "Pablo","pablito@gmail.com","123","ADMIN"));
        usuarios.add(new Usuario(2,"Ramon","ramoncito@gmail.com", "123","USER"));
        usuarios.add(new Usuario(2,"Juan","juancito@gmail.com", "123","USER"));
        usuarios.add(new Usuario(2,"Jaqueline","jaquesita@gmail.com", "123","USER"));
        usuarios.add(new Usuario(2,"Tamara","tamarita@gmail.com", "123","USER"));
    }
}
