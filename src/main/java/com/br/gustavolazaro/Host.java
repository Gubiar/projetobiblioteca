package com.br.gustavolazaro;

import java.util.Random;

public class Host implements Runnable {
    private Usuario usuario;
    private static final int LIMITE_DE_LIVROS = 5;
    private Middleware middleware;

    public Host(Usuario usuario, Middleware middleware) {
        this.usuario = usuario;
        this.middleware = middleware;
    }

    @Override
    public void run() {
        Random random = new Random();

        while (usuario.getLivrosReservados().size() < LIMITE_DE_LIVROS) {
            // Tenta reservar um livro aleatório
            int livroIndex = random.nextInt(middleware.getBiblioteca().getLivros().size());
            Compartilhado recursoCompartilhado = middleware.getBiblioteca().getLivros().get(livroIndex);

            if (usuario.reservarLivro(recursoCompartilhado)) {
                System.out.println(usuario.getNome() + " reservou o livro: " + recursoCompartilhado.getLivro().getTitulo());

                // Aguarda por um período (5 segundos) simulando o tempo que o usuário possui o livro
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Devolve o livro
                usuario.devolverLivro(recursoCompartilhado);
                System.out.println(usuario.getNome() + " devolveu o livro: " + recursoCompartilhado.getLivro().getTitulo());
            } else {
                System.out.println("--");
                System.out.println(usuario.getNome() + " não conseguiu reservar o " + recursoCompartilhado.getLivro().getTitulo());
                System.out.println(recursoCompartilhado.getLivro().getTitulo() + (recursoCompartilhado.isTrancado() ? " está trancado" : " não está trancado" ));
                System.out.println("--");
//                try {
//                    Thread.sleep(10000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        }

        if (usuario.getLivrosReservados().size() >= LIMITE_DE_LIVROS) {
            System.out.println(usuario.getNome() + " atingiu o limite de reserva de livros, desligando usuário...");
            middleware.desligarHost(usuario);
        }
    }
}
