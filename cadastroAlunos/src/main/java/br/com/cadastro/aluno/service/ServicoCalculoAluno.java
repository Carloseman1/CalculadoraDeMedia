package br.com.cadastro.aluno.service;

import java.util.List;
import br.com.cadastro.aluno.model.Avaliacao;

public class ServicoCalculoAluno {

    public double calcularMedia(List<Avaliacao> avaliacoes) {
        if (avaliacoes == null || avaliacoes.isEmpty()) {
            return 0.0;
        }
        double somaDasNotas = 0.0;
        for (Avaliacao avaliacao : avaliacoes) {
            somaDasNotas += avaliacao.getNota();
        }
        return somaDasNotas / avaliacoes.size();
    }

    public int calcularTotalFaltas(List<Avaliacao> avaliacoes) {
        int totalFaltas = 0;
        for (Avaliacao avaliacao : avaliacoes) {
            totalFaltas += avaliacao.getFaltas();
        }
        return totalFaltas;
    }

    public double calcularFrequencia(int totalFaltas, int totalAulasDoCurso) {
        if (totalAulasDoCurso == 0) {
            return 100.0; 
        }
        return 100 - ((100.0 * totalFaltas) / totalAulasDoCurso);
    }

    public String determinarResultadoFinal(double media, double frequencia) {
        if (frequencia < 70.0) {
            return "RF (Reprovado por Falta)";
        } else if (media >= 7.0) {
            return "AP (Aprovado)";
        } else {
            return "RM (Reprovado por Média)";
        }
    }
}