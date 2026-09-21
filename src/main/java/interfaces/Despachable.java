package interfaces;

import modelo.Repartidor;

/**
 * Interface que define si un pedido es despachable,
 * si es asi, functional a traves de asiganarRepartidor().
 */

public interface Despachable {

    void asignarRepartidor();

    void setRepartidor(Repartidor repartidor);
}
