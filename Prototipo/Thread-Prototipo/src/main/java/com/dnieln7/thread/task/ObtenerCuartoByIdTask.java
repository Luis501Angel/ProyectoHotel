package com.dnieln7.thread.task;

import com.dnieln7.thread.data.Cuarto;
import com.dnieln7.thread.data.mock.CuartosMock;

import java.util.List;
import java.util.concurrent.Callable;

public class ObtenerCuartoByIdTask implements Callable<Cuarto> {

    private CuartosMock mock;
    private String id;

    public ObtenerCuartoByIdTask(CuartosMock mock, String id) {
        this.mock = mock;
        this.id = id;
    }

    @Override
    public Cuarto call() throws Exception {
        return mock.findCuartoById(id);
    }
}
