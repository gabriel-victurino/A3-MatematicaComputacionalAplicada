package visao;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import modelo.Calculo;
import modelo.ResultadoCalculo;
import modelo.ResultadoConsolidado;

public class FrmMenu extends javax.swing.JFrame {

    public FrmMenu() {
        initComponents();
        setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        JBConfirmar = new javax.swing.JButton();
        JBFechar = new javax.swing.JButton();
        JCBNumeroEmprego = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Calculadora de IR - 2026");

        jLabel1.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        jLabel1.setText("Simulador para cálculo sobre imposto de renda 2026");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Quantos vínculos empregatícios você possui?");

        JBConfirmar.setText("Confirmar");
        JBConfirmar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBConfirmarActionPerformed(evt);
            }
        });

        JBFechar.setText("Fechar");
        JBFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBFecharActionPerformed(evt);
            }
        });

        JCBNumeroEmprego.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1 emprego", "Mais de 1 emprego" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(JBFechar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(JBConfirmar)
                .addGap(19, 19, 19))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JCBNumeroEmprego, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)))
                .addGap(38, 38, 38))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel1)
                .addGap(114, 114, 114)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(JCBNumeroEmprego, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JBConfirmar)
                    .addComponent(JBFechar))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void JBConfirmarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBConfirmarActionPerformed
        // TODO add your handling code here:
        Calculo calculo = new Calculo();
        int escolha = JCBNumeroEmprego.getSelectedIndex();
        boolean multiplosEmpregos = (escolha == 1);

        if (!multiplosEmpregos) {
            // ── Fluxo simples: 1 emprego ─────────────────────────────────────
            double salario = lerSalario("Digite seu salário mensal bruto (R$):", "Emprego");
            if (salario < 0) {
                return;
            }

            ResultadoCalculo resultado = calculo.calcularDetalhado(salario);
            JOptionPane.showMessageDialog(null,
                    montarDetalhamento(resultado, null),
                    "Detalhamento do Imposto de Renda",
                    JOptionPane.INFORMATION_MESSAGE);

        } else {
            // ── Fluxo múltiplos empregos ──────────────────────────────────────
            List<String> nomes = new ArrayList<>();
            List<Double> salarios = new ArrayList<>();

            int qtd = 0;
            while (true) {
                qtd++;
                String nomeEmp = JOptionPane.showInputDialog(null,
                        "Nome / descrição do Emprego " + qtd + " (ex: Empresa A):",
                        "Emprego " + qtd,
                        JOptionPane.QUESTION_MESSAGE);
                if (nomeEmp == null) {
                    break;
                }
                if (nomeEmp.trim().isEmpty()) {
                    nomeEmp = "Emprego " + qtd;
                }

                double sal = lerSalario("Salário bruto mensal do " + nomeEmp + " (R$):", nomeEmp);
                if (sal < 0) {
                    break;
                }

                nomes.add(nomeEmp);
                salarios.add(sal);
                calculo.adicionarEmprego(sal);

                Object[] opcoes = {"Sim", "Não"};
                int continuar = JOptionPane.showOptionDialog(null,
                        "Deseja adicionar mais um emprego?",
                        "Mais empregos?",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
                if (continuar != 0) {
                    break;
                }
            }

            if (salarios.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nenhum salário informado.");
                return;
            }

            ResultadoConsolidado cons = calculo.calcularConsolidado();

            // ── Exibe detalhamento individual de cada emprego ─────────────────
            List<ResultadoCalculo> parciais = cons.getResultadosPorEmprego();
            for (int i = 0; i < parciais.size(); i++) {
                parciais.get(i).setNomeEmprego(nomes.get(i));
                JOptionPane.showMessageDialog(null,
                        montarDetalhamento(parciais.get(i), "Emprego " + (i + 1) + ": " + nomes.get(i)),
                        "IR — " + nomes.get(i),
                        JOptionPane.INFORMATION_MESSAGE);
            }

            // ── Exibe consolidado ─────────────────────────────────────────────
            JOptionPane.showMessageDialog(null,
                    montarConsolidado(cons, nomes),
                    "Resumo Consolidado — Múltiplos Empregos",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
    // ── Lê e valida um salário ────────────────────────────────────────────────

    private double lerSalario(String mensagem, String titulo) {
        String entrada = JOptionPane.showInputDialog(null, mensagem, titulo,
                JOptionPane.QUESTION_MESSAGE);
        if (entrada == null || entrada.trim().isEmpty()) {
            return -1;
        }
        try {
            return Double.parseDouble(entrada.replace(",", ".").trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Valor inválido: " + entrada);
            return -1;
        }
    }

    // ── Detalhamento de um único emprego ──────────────────────────────────────
    private static String montarDetalhamento(ResultadoCalculo r, String cabecalho) {
        String sep = "─────────────────────────────────────\n";
        StringBuilder sb = new StringBuilder();

        if (cabecalho != null && !cabecalho.isEmpty()) {
            sb.append("  ").append(cabecalho).append("\n");
        }
        sb.append("       CÁLCULO DO IMPOSTO DE RENDA\n");
        sb.append("          Nova Tabela 2025\n");
        sb.append(sep);

        sb.append("📋  BASE DE CÁLCULO\n");
        sb.append(sep);
        sb.append(String.format("  Salário bruto mensal:       %s\n", fmt(r.getSalarioBruto())));
        sb.append(String.format("  (-) Desconto simplificado:  %s\n", fmt(r.getDescontoSimplificado())));
        sb.append(String.format("  (=) Valor base de cálculo:  %s\n", fmt(r.getValorBase())));
        sb.append("\n");

        sb.append("📊  FAIXA APLICADA\n");
        sb.append(sep);
        sb.append("  ").append(r.getFaixaAplicada()).append("\n");
        if (r.getAliquota() > 0) {
            sb.append(String.format("  Alíquota nominal:           %.1f%%\n", r.getAliquota()));
        }
        sb.append("\n");

        sb.append("🧮  CÁLCULO DO IMPOSTO\n");
        sb.append(sep);

        if (r.getAliquota() == 0 && r.getRedutor() == 0) {
            sb.append("  ✅ Isento pela tabela progressiva.\n");
        } else {
            if (r.getAliquota() > 0) {
                double impostoAntesDeducao = r.getValorBase() * r.getAliquota() / 100;
                double parcelaDeduzir = impostoAntesDeducao - r.getImpostoSemRedutor();
                sb.append(String.format("  Imposto bruto (base × %.1f%%): %s\n",
                        r.getAliquota(), fmt(impostoAntesDeducao)));
                sb.append(String.format("  (-) Parcela a deduzir tabela: %s\n", fmt(parcelaDeduzir)));
                sb.append(String.format("  (=) Imposto após tabela:      %s\n", fmt(r.getImpostoSemRedutor())));
            }
            if (r.getRedutor() > 0) {
                sb.append(String.format("  (-) Redutor nova lei (2025):  %s\n", fmt(r.getRedutor())));
                sb.append(String.format("      (salário bruto ≤ R$ 5.000 → isenção total;\n"));
                sb.append(String.format("       entre R$ 5.001 e R$ 7.000 → isenção parcial)\n"));
            } else if (r.getSalarioBruto() <= 5000) {
                sb.append("  ✅ Isento pela nova lei (salário bruto ≤ R$ 5.000).\n");
            }
        }

        sb.append("\n");
        sb.append(sep);
        sb.append(String.format("💰  IMPOSTO A RECOLHER:      %s\n", fmt(r.getImpostoFinal())));
        sb.append(sep);

        if (r.getImpostoFinal() > 0) {
            double aliqEfetiva = (r.getImpostoFinal() / r.getSalarioBruto()) * 100;
            sb.append(String.format("  Alíquota efetiva:           %.2f%%\n", aliqEfetiva)
                    .replace(".", ","));
        }
        sb.append(String.format("  Salário líquido estimado:   %s\n",
                fmt(r.getSalarioBruto() - r.getImpostoFinal())));

        return sb.toString();
    }

    // ── Consolidado de múltiplos empregos ─────────────────────────────────────
    private static String montarConsolidado(ResultadoConsolidado cons, List<String> nomes) {
        String sep = "─────────────────────────────────────\n";
        String sep2 = "═════════════════════════════════════\n";
        StringBuilder sb = new StringBuilder();

        sb.append("  RESUMO CONSOLIDADO — MÚLTIPLOS EMPREGOS\n");
        sb.append(sep2);

        sb.append("📌  RETENÇÃO POR EMPREGADOR (mensal)\n");
        sb.append(sep);

        List<ResultadoCalculo> parciais = cons.getResultadosPorEmprego();
        for (int i = 0; i < parciais.size(); i++) {
            ResultadoCalculo r = parciais.get(i);
            sb.append(String.format("  %s\n", nomes.get(i)));
            sb.append(String.format("    Salário bruto:  %s\n", fmt(r.getSalarioBruto())));
            sb.append(String.format("    IR retido:      %s\n", fmt(r.getImpostoFinal())));
            sb.append("\n");
        }

        sb.append(sep);
        sb.append(String.format("  Total bruto (soma):          %s\n", fmt(cons.getTotalSalarioBruto())));
        sb.append(String.format("  Total IR retido:             %s\n", fmt(cons.getTotalImpostoRetidoPorEmpregador())));
        sb.append("\n");

        ResultadoCalculo somado = cons.getResultadoSomado();
        sb.append("⚠️  IMPOSTO REAL SOBRE RENDA TOTAL\n");
        sb.append(sep);
        sb.append("  (Na declaração anual, todos os rendimentos\n");
        sb.append("   são somados e tributados em conjunto)\n\n");
        sb.append(String.format("  Base de cálculo total:       %s\n", fmt(somado.getValorBase())));
        sb.append(String.format("  Faixa:  %s\n", somado.getFaixaAplicada()));
        sb.append(String.format("  IR devido sobre total:       %s\n", fmt(somado.getImpostoFinal())));
        sb.append("\n");

        sb.append(sep2);
        double diferenca = cons.getDiferencaAjuste();
        if (Math.abs(diferenca) < 0.01) {
            sb.append("  ✅ Imposto em dia — sem ajuste na declaração.\n");
        } else if (diferenca > 0) {
            sb.append(String.format("  ⚠️  IMPOSTO EXTRA A PAGAR na declaração:\n"));
            sb.append(String.format("       %s\n", fmt(diferenca)));
            sb.append("  (Cada empregador calculou separado,\n");
            sb.append("   mas a Receita soma tudo.)\n");
        } else {
            sb.append(String.format("  ✅ RESTITUIÇÃO ESTIMADA:    %s\n", fmt(Math.abs(diferenca))));
        }
        sb.append(sep2);

        return sb.toString();
    }

    private static String fmt(double valor) {
        return "R$ " + String.format("%.2f", valor).replace(".", ",");
    }//GEN-LAST:event_JBConfirmarActionPerformed

    private void JBFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBFecharActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_JBFecharActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrmMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmMenu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JBConfirmar;
    private javax.swing.JButton JBFechar;
    private javax.swing.JComboBox<String> JCBNumeroEmprego;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables
}
