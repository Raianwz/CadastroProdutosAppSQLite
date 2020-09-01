package com.example.cadastroprodutosappsqlite;

public class Produtos {
    int id;
    String produto, tipo, dataEntrada;

    //Criando o construtor vazio

    public Produtos() {
    }

    //Criar o construtor

    public Produtos(int id, String produto, String tipo, String dataEntrada) {
        this.id = id;
        this.produto = produto;
        this.tipo = tipo;
        this.dataEntrada = dataEntrada;
    }

    //Criando os getters e setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getProduto() {
        return produto;
    }
    public void setProduto(String produto) {
        this.produto = produto;
    }

    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDataEntrada() {
        return dataEntrada;
    }
    public void setDataEntrada(String dataEntrada) {
        this.dataEntrada = dataEntrada;
    }
}