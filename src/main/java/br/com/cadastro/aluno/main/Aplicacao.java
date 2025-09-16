package br.com.cadastro.aluno.main;

import java.util.Scanner;
import br.com.cadastro.aluno.model.Aluno;

public class Aplicacao {

    public static void main(String[] args) {
        
        Scanner scanner = new Scanner(System.in);

        System.out.println("--- Cadastro de Novo Aluno ---");
        
        System.out.print("Digite o nome do aluno: ");
        String nome = scanner.nextLine();

        System.out.print("Digite o código do aluno: ");
        String codigo = scanner.nextLine();

        System.out.print("Digite a turma: ");
        String turma = scanner.nextLine();

        
        Aluno novoAluno = new Aluno(nome, codigo, turma);

        
        System.out.println("\nAluno '" + novoAluno.getNome() + "'cadastrado!");
        

        scanner.close();
    }
}