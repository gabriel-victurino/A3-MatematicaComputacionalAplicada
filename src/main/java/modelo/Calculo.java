package modelo;

import java.util.ArrayList;
import java.util.List;

public class Calculo {

    // ── Tabela progressiva 2025 (mensal) ────────────────────────────────────
    // Faixa 1: até R$ 2.259,20            → isento
    // Faixa 2: R$ 2.259,21 – R$ 2.826,65 → 7,5%   parcela R$  182,16
    // Faixa 3: R$ 2.826,66 – R$ 3.751,05 → 15%    parcela R$  394,16
    // Faixa 4: R$ 3.751,06 – R$ 4.664,68 → 22,5%  parcela R$  675,49
    // Faixa 5: acima de R$ 4.664,68      → 27,5%  parcela R$  908,73
    //
    // Isenção total garantida pela nova lei para salário bruto até R$ 5.000,00
    // via redutor progressivo (zerando o imposto em toda essa faixa).
    // ────────────────────────────────────────────────────────────────────────

    private static final double DESCONTO_SIMPLIFICADO = 607.20; // mensal 2025
    private static final double LIMITE_ISENCAO_BRUTO  = 5_000.00;

    // Múltiplos empregos
    private List<double[]> empregos = new ArrayList<>();
    // cada entrada: { salarioBruto }

    public void adicionarEmprego(double salarioBruto) {
        empregos.add(new double[]{ salarioBruto });
    }

    public void limparEmpregos() {
        empregos.clear();
    }

    public int quantidadeEmpregos() {
        return empregos.size();
    }

    // ── Cálculo único (mantém compatibilidade) ───────────────────────────────
    private double salarioMensal;

    public double getSalarioMensal() { return salarioMensal; }
    public void setSalarioMensal(double v) { this.salarioMensal = v; }

    public double calcularImposto() {
        return calcularDetalhado(salarioMensal).getImpostoFinal();
    }

    // ── Cálculo detalhado para UM salário ────────────────────────────────────
    public ResultadoCalculo calcularDetalhado(double salarioBruto) {
        ResultadoCalculo r = new ResultadoCalculo();

        double desconto  = DESCONTO_SIMPLIFICADO;
        double valorBase = salarioBruto - desconto;
        if (valorBase < 0) valorBase = 0;

        r.setSalarioBruto(salarioBruto);
        r.setDescontoSimplificado(desconto);
        r.setValorBase(valorBase);

        // ── Aplica tabela progressiva ────────────────────────────────────────
        double impostoTabela;
        String faixa;
        double aliquota;

        if (valorBase <= 2428.80) {
            impostoTabela = 0;
            faixa   = "Isento — base até R$ 2.428,80";
            aliquota = 0;
        } else if (valorBase <= 2826.65) {
            impostoTabela = (valorBase * 0.075) - 182.16;
            faixa   = "7,5% — base entre R$ 2.259,21 e R$ 2.826,65";
            aliquota = 7.5;
        } else if (valorBase <= 3751.05) {
            impostoTabela = (valorBase * 0.15) - 394.16;
            faixa   = "15% — base entre R$ 2.826,66 e R$ 3.751,05";
            aliquota = 15;
        } else if (valorBase <= 4664.68) {
            impostoTabela = (valorBase * 0.225) - 675.49;
            faixa   = "22,5% — base entre R$ 3.751,06 e R$ 4.664,68";
            aliquota = 22.5;
        } else {
            impostoTabela = (valorBase * 0.275) - 908.73;
            faixa   = "27,5% — base acima de R$ 4.664,68";
            aliquota = 27.5;
        }

        r.setFaixaAplicada(faixa);
        r.setAliquota(aliquota);
        r.setImpostoSemRedutor(Math.max(impostoTabela, 0));

        // ── Redutor da nova lei (isenção progressiva até R$ 5.000 bruto) ─────
        double redutor = calcularRedutor(salarioBruto, impostoTabela);
        r.setRedutor(redutor);

        double impostoFinal = Math.max(impostoTabela - redutor, 0);
        r.setImpostoFinal(impostoFinal);

        return r;
    }

    // ── Redutor progressivo (nova lei — salário bruto até R$ 5.000) ──────────
    // Para bruto ≤ 5.000 → imposto zerado
    // Para bruto entre 5.000 e 7.000 → redutor decresce linearmente até 0
    private double calcularRedutor(double salarioBruto, double impostoTabela) {
        if (salarioBruto <= LIMITE_ISENCAO_BRUTO) {
            // Zera totalmente o imposto
            return Math.max(impostoTabela, 0);
        } else if (salarioBruto <= 7_350.00) {
            // Redutor decresce de forma linear de "imposto pleno em 5.000"
            // até zero em 7.000, garantindo progressividade suave
            double proporcao = 978.62 - (0.133145 * salarioBruto);
            return proporcao;
        }
        return 0;
    }

    // Auxiliar: imposto pela tabela para um valor de salário bruto
    private double calcularImpostoTabela(double salarioBruto) {
        double base = salarioBruto - DESCONTO_SIMPLIFICADO;
        if (base <= 0)       return 0;
        if (base <= 2259.20) return 0;
        if (base <= 2826.65) return Math.max((base * 0.075) - 182.16, 0);
        if (base <= 3751.05) return Math.max((base * 0.15)  - 394.16, 0);
        if (base <= 4664.68) return Math.max((base * 0.225) - 675.49, 0);
        return Math.max((base * 0.275) - 908.73, 0);
    }

    // ── Cálculo consolidado para múltiplos empregos ──────────────────────────
    public ResultadoConsolidado calcularConsolidado() {
        ResultadoConsolidado cons = new ResultadoConsolidado();

        for (double[] emp : empregos) {
            ResultadoCalculo r = calcularDetalhado(emp[0]);
            cons.adicionarResultado(r);
        }

        // Recalcula o imposto sobre a soma dos salários (obrigação legal:
        // rendimentos de múltiplos vínculos são somados na declaração anual)
        double totalBruto = cons.getTotalSalarioBruto();
        ResultadoCalculo resultadoSomado = calcularDetalhado(totalBruto);
        cons.setResultadoSomado(resultadoSomado);

        return cons;
    }
}