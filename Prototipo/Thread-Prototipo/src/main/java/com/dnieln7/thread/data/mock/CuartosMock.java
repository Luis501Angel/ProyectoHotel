package com.dnieln7.thread.data.mock;

import com.dnieln7.thread.data.Cuarto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CuartosMock {

    private List<Cuarto> data;

    public CuartosMock() {
        data = new ArrayList<>(Arrays.asList(
                new Cuarto("A", 1, "Recamara", "Baño", "Comedor", "Sala"),
                new Cuarto("B", 1, "Recamara", "Baño", "Comedor", "Sala"),
                new Cuarto("C", 1, "Recamara", "Baño", "Comedor", "Sala"),
                new Cuarto("D", 2, "Recamara", "Baño", "Comedor", "Sala"),
                new Cuarto("E", 2, "Recamara", "Baño", "Comedor", "Sala"),
                new Cuarto("F", 2, "Recamara", "Baño", "Comedor", "Sala"),
                new Cuarto("G", 3, "Recamara", "Baño", "Comedor", "Sala"),
                new Cuarto("H", 3, "Recamara", "Baño", "Comedor", "Sala"),
                new Cuarto("I", 3, "Recamara", "Baño", "Comedor", "Sala"),
                new Cuarto("J", 4, "Recamara", "Baño", "Comedor", "Sala")
        ));
    }

    public List<Cuarto> findCuartos() {
        return data;
    }

    public Cuarto findCuartoById(String id) {
        Cuarto cuarto = null;

        for (Cuarto cuarto1 : data) {
            if (cuarto1.getId().equals(id)) {
                cuarto = cuarto1;
            }
        }

        return cuarto;
    }
}
