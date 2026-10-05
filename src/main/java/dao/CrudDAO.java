package dao;

import java.util.List;

/**
 * Contrato CRUD común para los DAO del sistema (PedidoDAO, RepartidorDAO,
 * EntregaDAO). T es el tipo de dato que representa una fila de la tabla;
 * ID es el tipo de su clave primaria.
 *
 * Cada DAO sigue siendo libre de agregar métodos propios (por ejemplo,
 * listarPorEstado en PedidoDAO) además de estos cuatro, que son los que
 * pide la actividad: create(), readAll(), update() y delete().
 */
public interface CrudDAO<T, ID> {

    /** Inserta la entidad y devuelve el id que le asignó la base de datos. */
    ID create(T entidad);

    /** Devuelve todas las filas de la tabla. */
    List<T> readAll();

    /** Actualiza, por su id, la entidad indicada con sus valores actuales. */
    void update(T entidad);

    /** Elimina la fila con el id indicado. */
    void delete(ID id);
}