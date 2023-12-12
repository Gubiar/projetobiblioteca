// Livro.java
package com.br.gustavolazaro;

public class Livro {
    private String titulo;
    private String autor;

    private Boolean isReservado;

    public Livro(String titulo, String autor) {
        this.titulo = titulo;
        this.autor = autor;
        this.isReservado = false;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Boolean getReservado() {
        return isReservado;
    }

    public void setReservado(Boolean reservado) {
        isReservado = reservado;
    }
}