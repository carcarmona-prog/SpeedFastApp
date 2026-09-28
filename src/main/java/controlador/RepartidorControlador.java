package controlador;

import dao.RepartidorDAO;
import modelo.Repartidor;

import java.util.List;

/**
 * Controlador de repartidores: intermediario entre las ventanas y
 * RepartidorDAO.
 */
public class RepartidorControlador {

    private final RepartidorDAO repartidorDAO = new RepartidorDAO();

    /** Guarda el repartidor en la base de datos y devuelve su id. */
    public int registrarRepartidor(Repartidor repartidor) {
        return repartidorDAO.guardar(repartidor);
    }

    /** Devuelve todos los repartidores registrados. */
    public List<Repartidor> listarRepartidores() {
        return repartidorDAO.listarTodos();
    }
}
