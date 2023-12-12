package com.br.gustavolazaro;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Biblioteca {
    private List<Compartilhado> livros;
    private List<Usuario> usuarios;

    private static final int QTD_LIVROS = 10;
    private static final int QTD_USUARIOS = 3;

    public Biblioteca() {
        this.livros = new ArrayList<>();
        this.usuarios = new ArrayList<>();
        inicializarLivros(QTD_LIVROS);
        inicializarUsuarios(QTD_USUARIOS);
    }

    private void inicializarLivros(int quantidade) {
        for (int i = 0; i < quantidade; i++) {
            livros.add(new Compartilhado("Livro " + i, "Autor " + i));
        }
    }

    private void inicializarUsuarios(int quantidade) {
        for (int i = 0; i < quantidade; i++) {
            usuarios.add(new Usuario(i, "Usuario " + i));
        }
    }

    public void setLivros(List<Compartilhado> livros) {
        this.livros = livros;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public List<Compartilhado> getLivros() {
        return livros;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void executarSistema() {
        Middleware middleware = new Middleware(QTD_USUARIOS, this);

        // Inicia os hosts (usuários) concorrentes
        middleware.iniciarHosts(usuarios);

        // Simula algumas operações no sistema
        realizarOperacoes();
    }

    private void realizarOperacoes() {
        // Simula algumas operações de reserva e devolução
        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            Usuario usuario = usuarios.get(random.nextInt(usuarios.size()));
            Compartilhado compartilhado = livros.get(random.nextInt(livros.size()));

            // Realiza a tentativa de reserva
            usuario.reservarLivro(compartilhado);

            // Aguarda por um curto período para simular a posse do livro
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Realiza a devolução
            usuario.devolverLivro(compartilhado);
        }
    }

    public static void main(String[] args) {
        Biblioteca biblioteca = new Biblioteca();
        biblioteca.executarSistema();
    }
}
