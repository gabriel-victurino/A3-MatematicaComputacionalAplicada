package modelo;

public class ResultadoCalculo {
    private double salarioBruto;
    private double descontoSimplificado;
    private double valorBase;
    private String faixaAplicada;
    private double aliquota;
    private double impostoSemRedutor;
    private double redutor;
    private double impostoFinal;
    private String nomeEmprego = "";   // identificador opcional

    public double getSalarioBruto()          { return salarioBruto; }
    public void   setSalarioBruto(double v)  { this.salarioBruto = v; }

    public double getDescontoSimplificado()         { return descontoSimplificado; }
    public void   setDescontoSimplificado(double v) { this.descontoSimplificado = v; }

    public double getValorBase()          { return valorBase; }
    public void   setValorBase(double v)  { this.valorBase = v; }

    public String getFaixaAplicada()         { return faixaAplicada; }
    public void   setFaixaAplicada(String v) { this.faixaAplicada = v; }

    public double getAliquota()          { return aliquota; }
    public void   setAliquota(double v)  { this.aliquota = v; }

    public double getImpostoSemRedutor()         { return impostoSemRedutor; }
    public void   setImpostoSemRedutor(double v) { this.impostoSemRedutor = v; }

    public double getRedutor()          { return redutor; }
    public void   setRedutor(double v)  { this.redutor = v; }

    public double getImpostoFinal()          { return impostoFinal; }
    public void   setImpostoFinal(double v)  { this.impostoFinal = v; }

    public String getNomeEmprego()         { return nomeEmprego; }
    public void   setNomeEmprego(String v) { this.nomeEmprego = v; }
}