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
        return repartidorDAO.create(repartidor);
    }

    /** Devuelve todos los repartidores registrados. */
    public List<Repartidor> listarRepartidores() {
        return repartidorDAO.readAll();
    }

    /** Actualiza el nombre de un repartidor existente. */
    public void actualizarRepartidor(Repartidor repartidor) {
        repartidorDAO.update(repartidor);
    }

    /** Elimina un repartidor de la base de datos. */
    public void eliminarRepartidor(int idRepartidor) {
        repartidorDAO.delete(idRepartidor);
    }
}