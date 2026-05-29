package principal;
import modelo.Calculo;
import javax.swing.JOptionPane;

public class Principal {

    public static void main(String[] args) {
        Calculo calculo= new Calculo();
        double salarioMensal= Double.parseDouble(JOptionPane.showInputDialog("Digite seu salário mensal:"));
        calculo.setSalarioMensal(salarioMensal);
        double imposto = calculo.calcularImposto();
        JOptionPane.showMessageDialog(null, "Seu imposto será de: R$ " + String.format("%.2f", imposto).replace(".", ","));
    }
}
