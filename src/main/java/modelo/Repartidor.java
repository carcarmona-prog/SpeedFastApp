package modelo;

import interfaces.Mostrable;

/**
 * Clase que define los atributos del objeto repartidor.
 * Implementa Mostrable para la utilización de su comportamiento.
 */

public class Repartidor implements Mostrable {


    protected String nombreRepartidor;
    protected boolean disponibilidadInmediata;
    protected boolean mochilaTermica;
    protected String tipoVehiculoRepartidor;

    public Repartidor(String nombreRepartidor) {
        this.nombreRepartidor = nombreRepartidor;
    }

    public Repartidor(String nombreRepartidor, boolean mochilaTermica, String tipoVehiculoRepartidor, boolean disponibilidadInmediata) {

        this.nombreRepartidor = nombreRepartidor;
        this.mochilaTermica = mochilaTermica;
        this.tipoVehiculoRepartidor = tipoVehiculoRepartidor;
        this.disponibilidadInmediata = disponibilidadInmediata;
    }

    public String getNombreRepartidor() {
        return nombreRepartidor;
    }

    public boolean isMochilaTermica() {
        return mochilaTermica;
    }

    public String getTipoVehiculoRepartidor() {
        return tipoVehiculoRepartidor;
    }

    public boolean isDisponibilidadInmediata() {
        return disponibilidadInmediata;
    }


    @Override
    public String toString() {
        String mochila = mochilaTermica ? "Sí tiene" : "No tiene";
        String disponibilidad = disponibilidadInmediata ? "Disponible ahora" : "No disponible";

        return "Nombre Repartidor: " + nombreRepartidor
                + " | Tipo de vehículo: " + tipoVehiculoRepartidor
                + " | Mochila térmica: " + mochila
                + " | Disponibilidad: " + disponibilidad;
    }

    @Override
    public void mostrarInformacion() {
        System.out.println(toString());
    }






}
