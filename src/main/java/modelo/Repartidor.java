package modelo;

import interfaces.Mostrable;

public class Repartidor implements Mostrable {

    protected String nombreRepartidor;
    protected boolean mochilaTermica;
    protected String tipoVehiculoRepartidor;
    protected boolean disponibilidadInmediata;

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
    public void mostrarInformacion() {
        String mochila = mochilaTermica ? "Sí tiene" : "No tiene";
        String disponibilidad = disponibilidadInmediata ? "Disponible ahora" : "No disponible";

        System.out.println("Nombre Repartidor: " + nombreRepartidor
                + " Tipo de vehículo: " + tipoVehiculoRepartidor
                + " Mochila térmica: " + mochila
                + " Disponibilidad: " + disponibilidad);
    }


}
