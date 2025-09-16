package br.com.cadastro.aluno.model;

public class Avaliacao {

    private int id; 
    private double nota;
    private int faltas;
    private String alunoCodigo; 


    public Avaliacao(double nota, int faltas, String alunoCodigo) {
        this.nota = nota;
        this.faltas = faltas;
        this.alunoCodigo = alunoCodigo;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public int getFaltas() {
        return faltas;
    }

    public void setFaltas(int faltas) {
        this.faltas = faltas;
    }

    public String getAlunoCodigo() {
        return alunoCodigo;
    }

    public void setAlunoCodigo(String alunoCodigo) {
        this.alunoCodigo = alunoCodigo;
    }
}