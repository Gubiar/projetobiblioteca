package com.br.gustavolazaro;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

public class Compartilhado {
    private Livro livro;
    private Queue<Usuario> filaDeEspera;
    private ReentrantLock lock;

    public Compartilhado(String titulo, String autor) {
        this.livro = new Livro(titulo, autor);
        this.filaDeEspera = new LinkedList<>();
        this.lock = new ReentrantLock();
    }

    public boolean isTrancado() {
        return lock.isLocked();
    }

    public synchronized void trancar() {
        lock.lock();
    }

    public synchronized void destrancar() {
        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }

    public Livro getLivro() {
        return livro;
    }

    // Métodos adicionados para manipulação da fila de espera
    public synchronized boolean adicionarFilaDeEspera(Usuario usuario) {
        if (!filaDeEspera.contains(usuario)) {
            filaDeEspera.add(usuario);
            System.out.println(usuario.getNome() + " adicionado à fila de espera para o livro " + livro.getTitulo());
            exibirEstadoFilaEspera();
            return true;
        }
        return false;
    }

    public synchronized Usuario removerFilaDeEspera() {
        if (!filaDeEspera.isEmpty()) {
            Usuario usuario = filaDeEspera.poll();
            System.out.println(usuario.getNome() + " removido da fila de espera para o livro " + livro.getTitulo());
            exibirEstadoFilaEspera();
            return usuario;
        }
        return null;
    }

    private void exibirEstadoFilaEspera() {
        StringBuilder stringBuilder = new StringBuilder("[ ");
        for (Usuario cada: filaDeEspera) {
            stringBuilder.append(cada.getNome());
            stringBuilder.append(", ");
        }
        if(!filaDeEspera.isEmpty()) {
            stringBuilder.setLength(stringBuilder.length() - 2);
        }
        stringBuilder.append("]");
        System.out.println("Estado atual da fila de espera para o livro " + livro.getTitulo() + ": " + stringBuilder);
    }

    public void reservarLivro() {
        livro.setReservado(true);
        System.out.println(livro.getTitulo() + " reservado.");
    }

    public void devolverLivro() {
        livro.setReservado(false);
        System.out.println(livro.getTitulo() + " devolvido.");
    }
}
