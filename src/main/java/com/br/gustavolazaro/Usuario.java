// Usuario.java
package com.br.gustavolazaro;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private int idUsuario;
    private String nome;
    private List<Livro> livrosReservados;

    public Usuario(int idUsuario, String nome) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.livrosReservados = new ArrayList<>();
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

    public String getNome() {
        return nome;
    }

    public List<Livro> getLivrosReservados() {
        return livrosReservados;
    }

    public synchronized boolean reservarLivro(Compartilhado recursoCompartilhado) {
        if (recursoCompartilhado.isTrancado() || !recursoCompartilhado.adicionarFilaDeEspera(this)) {
            return false;
        }
        recursoCompartilhado.trancar();
        recursoCompartilhado.reservarLivro();
        System.out.println(this.getNome() + " reservou o livro " + recursoCompartilhado.getLivro().getTitulo());
        return true;
    }

    public synchronized void devolverLivro(Compartilhado recursoCompartilhado) {
        recursoCompartilhado.devolverLivro();
        recursoCompartilhado.destrancar();
        Usuario proximoUsuario = recursoCompartilhado.removerFilaDeEspera();
        if (proximoUsuario != null) {
            proximoUsuario.reservarLivro(recursoCompartilhado);
        }
    }
}