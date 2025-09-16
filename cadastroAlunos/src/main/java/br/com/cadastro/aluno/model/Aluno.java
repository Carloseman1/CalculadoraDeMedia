package br.com.cadastro.aluno.model;

public class Aluno {

    private String nome;
    private String codigo;
    private String turma;
    private int totalAulas;

    public Aluno(String nome, String codigo, String turma, int totalAulas) {
        this.nome = nome;
        this.codigo = codigo;
        this.turma = turma;

        this.totalAulas = totalAulas;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTurma() {
        return turma;
    }
   
    public void setTurma(String turma) {
        this.turma = turma;
    }
    
    public int getTotalAulas() {
        return totalAulas;
    }
    
    public void setTotalAulas(int totalAulas) {
        this.totalAulas = totalAulas;
    }
}