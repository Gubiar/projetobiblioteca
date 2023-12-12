package com.br.gustavolazaro;

import java.util.ArrayList;
import java.util.List;
public class Biblioteca {
    private List<Compartilhado> livros;
    private List<Usuario> usuarios;
    public static final int QTD_LIVROS = 2;
    public static final int QTD_USUARIOS = 2;
    public static final int LIMITE_DE_LIVROS_POR_USUARIO = 10;


    public Biblioteca() {
        this.livros = new ArrayList<>();
        this.usuarios = new ArrayList<>();
        inicializarLivros();
    }

    private void inicializarLivros() {
        for (int i = 0; i < QTD_LIVROS; i++) {
            livros.add(new Compartilhado("Livro " + i, "Autor " + i));
        }
    }

    private void inicializarUsuarios(Middleware middleware) {
        for (int i = 0; i < QTD_USUARIOS; i++) {
            usuarios.add(new Usuario(i, "Usuario " + i, middleware));
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
        inicializarUsuarios(middleware);
        // Inicia os hosts (usuários) concorrentes
        middleware.iniciarHosts(usuarios);
    }

    public static void main(String[] args) {
        Biblioteca biblioteca = new Biblioteca();
        biblioteca.executarSistema();
    }
}
