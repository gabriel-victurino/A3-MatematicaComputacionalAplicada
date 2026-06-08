package modelo;

import java.util.ArrayList;
import java.util.List;

public class ResultadoConsolidado {
    private List<ResultadoCalculo> resultadosPorEmprego = new ArrayList<>();
    private ResultadoCalculo resultadoSomado;  // imposto real sobre total dos salários

    public void adicionarResultado(ResultadoCalculo r) {
        resultadosPorEmprego.add(r);
    }

    public List<ResultadoCalculo> getResultadosPorEmprego() {
        return resultadosPorEmprego;
    }

    public ResultadoCalculo getResultadoSomado() { return resultadoSomado; }
    public void setResultadoSomado(ResultadoCalculo r) { this.resultadoSomado = r; }

    public double getTotalSalarioBruto() {
        return resultadosPorEmprego.stream()
                .mapToDouble(ResultadoCalculo::getSalarioBruto)
                .sum();
    }

    public double getTotalImpostoRetidoPorEmpregador() {
        // Soma dos impostos retidos individualmente (cada empregador reteve separado)
        return resultadosPorEmprego.stream()
                .mapToDouble(ResultadoCalculo::getImpostoFinal)
                .sum();
    }

    public double getDiferencaAjuste() {
        // Positivo = deve pagar a mais na declaração
        // Negativo = tem restituição a receber
        return resultadoSomado.getImpostoFinal() - getTotalImpostoRetidoPorEmpregador();
    }
}