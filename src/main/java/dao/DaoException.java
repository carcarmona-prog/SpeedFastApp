package dao;

/**
 * Error al acceder a la base de datos desde un DAO. Es "unchecked" para que
 * las ventanas la capturen y muestren el mensaje sin llenar todo el código
 * de "throws".
 */
public class DaoException extends RuntimeException {

    public DaoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
