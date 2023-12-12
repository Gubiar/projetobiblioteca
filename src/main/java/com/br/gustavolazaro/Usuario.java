package com.br.gustavolazaro;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import static com.br.gustavolazaro.Biblioteca.LIMITE_DE_LIVROS_POR_USUARIO;

public class Usuario implements Runnable {
    private int idUsuario;
    private String nome;
    private List<Livro> livrosReservados;
    private Middleware middleware;

    public Usuario(int idUsuario, String nome, Middleware middleware) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.livrosReservados = new ArrayList<>();
        this.middleware = middleware;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setLivrosReservados(List<Livro> livrosReservados) {
        this.livrosReservados = livrosReservados;
    }

    public void addLivroReservado(Livro livro) {
        livrosReservados.add(livro);
    }

    public String getNome() {
        return nome;
    }

    public List<Livro> getLivrosReservados() {
        return livrosReservados;
    }

    public boolean reservarLivro(Compartilhado recursoCompartilhado) {
        synchronized (recursoCompartilhado) {
            if (!recursoCompartilhado.getLivro().getReservado() && !recursoCompartilhado.isTrancado()) {
                recursoCompartilhado.trancar();
                recursoCompartilhado.reservarLivro();
                System.out.println(this.getNome() + " reservou o livro " + recursoCompartilhado.getLivro().getTitulo());
                recursoCompartilhado.destrancar();
                return true;
            } else {
                // Adiciona à fila de espera se não puder reservar imediatamente
                return recursoCompartilhado.adicionarFilaDeEspera(this);
            }
        }
    }

    public void devolverLivro(Compartilhado recursoCompartilhado) {
        synchronized (recursoCompartilhado) {
            recursoCompartilhado.trancar();
            recursoCompartilhado.devolverLivro();
            recursoCompartilhado.destrancar();
            Usuario proximoUsuario = recursoCompartilhado.removerFilaDeEspera();
            if (proximoUsuario != null) {
                proximoUsuario.reservarLivro(recursoCompartilhado);
            }
        }
    }


    @Override
    public void run() {
        Random random = new Random();

        while (getLivrosReservados().size() < LIMITE_DE_LIVROS_POR_USUARIO) {
            // Tenta reservar um livro aleatório
            int livroIndex = random.nextInt(middleware.getBiblioteca().getLivros().size());
            Compartilhado recursoCompartilhado = middleware.getBiblioteca().getLivros().get(livroIndex);

            if (reservarLivro(recursoCompartilhado)) {
                addLivroReservado(recursoCompartilhado.getLivro());
                System.out.println(getNome() + " reservou o livro: " + recursoCompartilhado.getLivro().getTitulo());

                // Aguarda por um período (2 segundos) simulando o tempo que o usuário possui o livro
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    // Devolve o livro
                    devolverLivro(recursoCompartilhado);
                    System.out.println(getNome() + " devolveu o livro: " + recursoCompartilhado.getLivro().getTitulo());
                }
            } else {
                System.out.println("--");
                System.out.println(getNome() + " não conseguiu reservar o " + recursoCompartilhado.getLivro().getTitulo());
                System.out.println(recursoCompartilhado.getLivro().getTitulo() + (recursoCompartilhado.isTrancado() ? " está trancado" : " não está trancado" ));
                System.out.println(recursoCompartilhado.getLivro().getTitulo() + (recursoCompartilhado.getLivro().getReservado() ? " está reservado" : " não está reservado" ));
                System.out.println("--");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        if (getLivrosReservados().size() >= LIMITE_DE_LIVROS_POR_USUARIO) {
            System.out.println(getNome() + " atingiu o limite de reserva de livros, desligando usuário...");
            middleware.desligarHost(this);
        }
    }
}