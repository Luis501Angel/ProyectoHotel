package com.dnieln7.thread.data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dnieln7
 */
public class Cuarto {

    private String id;
    private int piso;
    // Luces del cuarto con un booleano indicando si esta encendida.
    private Map<String, Boolean> luces;

    /**
     * Constructor base donde cada luz inicia apagada.
     * @param id - Identificador del cuarto
     * @param piso - Piso del cuarto
     * @param luces - Nombre de las luces en el cuarto, ej. Comedor, Sala, etc.
     */
    public Cuarto(String id, int piso, String... luces) {
        this.id = id;
        this.piso = piso;
        this.luces = new HashMap<>();

        for (String luce : luces) {
            this.luces.put(luce, false);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPiso() {
        return piso;
    }

    public void setPiso(int piso) {
        this.piso = piso;
    }

    public Map<String, Boolean> getLuces() {
        return luces;
    }

    public void setLuces(Map<String, Boolean> luces) {
        this.luces = luces;
    }

    @Override
    public String toString() {
        return "Cuarto{" +
                "id='" + id + '\'' +
                ", piso=" + piso +
                ", luces=" + luces +
                '}';
    }
}
