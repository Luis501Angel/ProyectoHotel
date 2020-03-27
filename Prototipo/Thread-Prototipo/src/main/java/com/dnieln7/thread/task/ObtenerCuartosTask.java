package com.dnieln7.thread.task;

import com.dnieln7.thread.data.Cuarto;
import com.dnieln7.thread.data.mock.CuartosMock;

import java.util.List;
import java.util.concurrent.Callable;

public class ObtenerCuartosTask implements Callable<List<Cuarto>> {

    private CuartosMock mock;

    public ObtenerCuartosTask(CuartosMock mock) {
        this.mock = mock;
    }

    @Override
    public List<Cuarto> call() throws Exception {
        return mock.findCuartos();
    }
}
