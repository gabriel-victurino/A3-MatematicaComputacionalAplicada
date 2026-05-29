package modelo;

public class Calculo {

    private double salarioMensal;
    private double redutor;

    public Calculo() {
    }

    public Calculo(double salarioMensal, double redutor) {
        this.salarioMensal = salarioMensal;
        this.redutor = redutor;
    }

    public double getSalarioMensal() {
        return salarioMensal;
    }

    public void setSalarioMensal(double salarioMensal) {
        this.salarioMensal = salarioMensal;
    }

    public double getRedutor() {
        return redutor;
    }

    public void setRedutor(double redutor) {
        this.redutor = redutor;
    }

    public void calcularRedutor() {
        if (salarioMensal <= 5000) {
            redutor = 0;
        } else if (salarioMensal <= 7350) {
            redutor = 978.62 - (0.133145 * salarioMensal);
        } else {
            redutor = 0;
        }
    }

    public double calcularImposto() {
        double imposto;
        calcularRedutor();
        double valorBase = salarioMensal - 607.20; // 607,20 desconto simplificado mensal
        if (valorBase <= 4392.80) { //faixa isenta para quem ganha até 5000
            imposto = 0;
            return imposto;
        } else if (valorBase <= 4664.68) {  //faixa 22,5%
            imposto = (valorBase * 0.225) - 675.49 - redutor;
            return imposto;
        } else {    //faixa 27,5%
            imposto = (valorBase * 0.275) - 908.73 - redutor;
            return imposto;
        }
    }
}
