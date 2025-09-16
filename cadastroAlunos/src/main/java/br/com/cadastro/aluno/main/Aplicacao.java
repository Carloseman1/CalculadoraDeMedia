package br.com.cadastro.aluno.main;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import br.com.cadastro.aluno.model.Aluno;
import br.com.cadastro.aluno.model.Avaliacao;
import br.com.cadastro.aluno.repository.AlunoRepositorio;
import br.com.cadastro.aluno.service.ServicoCalculoAluno; 

public class Aplicacao {

    private static final Scanner scanner = new Scanner(System.in);
    private static final AlunoRepositorio repositorio = new AlunoRepositorio();
    private static final com.google.gson.Gson gson = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
    
    private static final ServicoCalculoAluno servicoCalculo = new ServicoCalculoAluno();

    public static void main(String[] args) {
        
        repositorio.criarTabelaSeNaoExistir();
        repositorio.criarTabelaAvaliacoes();

        while (true) {
            exibirMenu();
            
            try {
                int opcao = Integer.parseInt(scanner.nextLine());
                switch (opcao) {
                    case 1:
                        cadastrarNovoAluno();
                        break;
                    case 2:
                        buscarAlunoEExibirRelatorio();
                        break;
                    case 3:
                        lancarAvaliacao();
                        break;
                    case 4:
                        buscarAlunoEExibirJson();
                        break;
                    case 0:
                        System.out.println("Saindo do sistema...");
                        return;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Erro: Por favor, digite um número válido.");
            }
        }
    }

    private static void exibirMenu() {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("1 - Cadastrar Novo Aluno");
        System.out.println("2 - Exibir Relatório do Aluno");
        System.out.println("3 - Lançar Avaliação para Aluno");
        System.out.println("4 - Exibir Relatório do Aluno (JSON)");
        System.out.println("0 - Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static void cadastrarNovoAluno() {
        System.out.println("\n--- Cadastro de Novo Aluno ---");
        System.out.print("Digite o nome do aluno: ");
        String nome = scanner.nextLine();
        System.out.print("Digite o código do aluno: ");
        String codigo = scanner.nextLine();
        System.out.print("Digite a turma: ");
        String turma = scanner.nextLine();
        System.out.print("Digite o total de aulas do curso: ");
        int totalAulas = Integer.parseInt(scanner.nextLine());

        Aluno novoAluno = new Aluno(nome, codigo, turma, totalAulas);
        repositorio.salvar(novoAluno);
    }
    
    private static void buscarAlunoEExibirRelatorio() {
        System.out.println("\n--- Relatório do Aluno ---");
        System.out.print("Digite o código do aluno: ");
        String codigo = scanner.nextLine();

        Optional<Aluno> alunoOptional = repositorio.buscarPorCodigo(codigo);

        if (alunoOptional.isPresent()) {
            Aluno aluno = alunoOptional.get();
            List<Avaliacao> avaliacoes = repositorio.buscarAvaliacoesPorAluno(aluno.getCodigo());

            double media = servicoCalculo.calcularMedia(avaliacoes);
            int totalFaltas = servicoCalculo.calcularTotalFaltas(avaliacoes);
            double frequencia = servicoCalculo.calcularFrequencia(totalFaltas, aluno.getTotalAulas());
            String resultadoFinal = servicoCalculo.determinarResultadoFinal(media, frequencia);


            System.out.println("\n----------------------------------------");
            System.out.println("Relatório Final do Aluno");
            System.out.println("----------------------------------------");
            System.out.println("Nome: " + aluno.getNome());
            System.out.print("Notas: ");
            avaliacoes.forEach(av -> System.out.print(av.getNota() + " | "));
            System.out.println("\nTotal de Faltas: " + totalFaltas);
            System.out.printf("Média Final: %.2f\n", media);
            System.out.printf("Frequência: %.1f%%\n", frequencia);
            System.out.println("Resultado: " + resultadoFinal);
            System.out.println("----------------------------------------");
            
        } else {
            System.out.println("Aluno com o código '" + codigo + "' não foi encontrado.");
        }
    }
    
    private static void buscarAlunoEExibirJson() {
        System.out.println("\n--- Gerar Relatório JSON do Aluno ---");
        System.out.print("Digite o código do aluno: ");
        String codigo = scanner.nextLine();

        repositorio.buscarPorCodigo(codigo).ifPresent(aluno -> {
            List<Avaliacao> avaliacoes = repositorio.buscarAvaliacoesPorAluno(aluno.getCodigo());
            double media = servicoCalculo.calcularMedia(avaliacoes);
            int totalFaltas = servicoCalculo.calcularTotalFaltas(avaliacoes);
            double frequencia = servicoCalculo.calcularFrequencia(totalFaltas, aluno.getTotalAulas());
            String resultadoFinal = servicoCalculo.determinarResultadoFinal(media, frequencia);

           
            java.util.Map<String, Object> relatorio = new java.util.LinkedHashMap<>();
            relatorio.put("codigo", aluno.getCodigo());
            relatorio.put("nome", aluno.getNome());
            relatorio.put("turma", aluno.getTurma());
            relatorio.put("total_aulas", aluno.getTotalAulas());
            relatorio.put("avaliacoes", avaliacoes);
            relatorio.put("media_final", media);
            relatorio.put("frequencia_percentual", frequencia);
            relatorio.put("resultado_final", resultadoFinal);

            
            String json = gson.toJson(relatorio);
            System.out.println(json);
        });
    }
    
    private static void lancarAvaliacao() {
        System.out.println("\n--- Lançar Avaliação ---");
        System.out.print("Digite o código do aluno: ");
        String codigo = scanner.nextLine();

        if (repositorio.buscarPorCodigo(codigo).isPresent()) {
            String continuar;
            
            do {
                System.out.print("Digite a NOTA: ");
                double nota = Double.parseDouble(scanner.nextLine());
                System.out.print("Digite a quantidade de faltas nessa matéria : ");
                int faltas = Integer.parseInt(scanner.nextLine());

                Avaliacao novaAvaliacao = new Avaliacao(nota, faltas, codigo);
                repositorio.salvarAvaliacao(novaAvaliacao);

                System.out.print("\nDeseja lançar outra avaliação para este aluno? (s/n): ");
                continuar = scanner.nextLine();
            } while (continuar.equalsIgnoreCase("s"));
        } else {
            System.out.println("Aluno não encontrado. Não foi possível lançar a avaliação.");
        }
    }
}