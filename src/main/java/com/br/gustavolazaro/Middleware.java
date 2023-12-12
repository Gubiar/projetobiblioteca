package com.br.gustavolazaro;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.Map;
import java.util.HashMap;

public class Middleware {
    private ExecutorService executorService;
    private Map<Usuario, Future<?>> tarefasUsuarios;
    private Biblioteca biblioteca;

    public Middleware(int numHosts, Biblioteca biblioteca) {
        this.executorService = Executors.newFixedThreadPool(numHosts);
        this.tarefasUsuarios = new HashMap<>();
        this.biblioteca = biblioteca;
    }

    public Biblioteca getBiblioteca() {
        return biblioteca;
    }

    public void iniciarHosts(List<Usuario> usuarios) {
        for (int i = 0; i < usuarios.size(); i++) {
            Usuario usuario = usuarios.get(i);
            Future<?> tarefa = executorService.submit(new Host(usuario, this));
            tarefasUsuarios.put(usuario, tarefa);
        }
    }

    public void desligarHost(Usuario usuario) {
        Future<?> tarefa = tarefasUsuarios.get(usuario);
        if (tarefa != null) {
            tarefa.cancel(true); // Interrompe a thread do usuário.
            System.out.println("O " + usuario.getNome() + " foi desligado após atingir o limite de reservas.");
        }
    }
}
