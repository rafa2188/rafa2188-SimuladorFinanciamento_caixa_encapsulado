public class Financiamento {
    private double valorImovel;
    private double entrada;
    private int prazoAnos;
    private double taxaJurosAnual;

    public Financiamento(double valorImovel, double entrada, int prazoAnos, double taxaJurosAnual) {
        this.valorImovel = valorImovel;
        this.entrada = entrada;
        this.prazoAnos = prazoAnos;
        this.taxaJurosAnual = taxaJurosAnual;
    }

    public double calcularValorFinanciado() {
        return valorImovel - entrada;
    }

    public double[] calcularSistemaPrice() {
        double valorFinanciado = calcularValorFinanciado();
        int prazoMeses = prazoAnos * 12;
        double taxaJurosMensal = (taxaJurosAnual / 100) / 12;

        double parcelaPrice = (valorFinanciado * taxaJurosMensal) /
                (1 - Math.pow(1 + taxaJurosMensal, -prazoMeses));
        double totalPagoPrice = parcelaPrice * prazoMeses;
        double jurosTotaisPrice = totalPagoPrice - valorFinanciado;

        return new double[]{parcelaPrice, totalPagoPrice, jurosTotaisPrice};
    }

    public double[] calcularSistemaSAC() {
        double valorFinanciado = calcularValorFinanciado();
        int prazoMeses = prazoAnos * 12;
        double taxaJurosMensal = (taxaJurosAnual / 100) / 12;
        double amortizacao = valorFinanciado / prazoMeses;
        double totalPagoSAC = 0;

        for (int i = 0; i < prazoMeses; i++) {
            double saldoDevedor = valorFinanciado - (i * amortizacao);
            double jurosMensal = saldoDevedor * taxaJurosMensal;
            double parcelaSAC = amortizacao + jurosMensal;
            totalPagoSAC += parcelaSAC;
        }

        double jurosTotaisSAC = totalPagoSAC - valorFinanciado;

        return new double[]{amortizacao, totalPagoSAC, jurosTotaisSAC};
    }

    public String gerarTabelaSAC() {
        double valorFinanciado = calcularValorFinanciado();
        int prazoMeses = prazoAnos * 12;
        double taxaJurosMensal = (taxaJurosAnual / 100) / 12;
        double amortizacao = valorFinanciado / prazoMeses;
        StringBuilder tabelaSAC = new StringBuilder();
        tabelaSAC.append("Parcela | Valor | Amortização | Juros\n");

        for (int i = 0; i < prazoMeses; i++) {
            double saldoDevedor = valorFinanciado - (i * amortizacao);
            double jurosMensal = saldoDevedor * taxaJurosMensal;
            double parcelaSAC = amortizacao + jurosMensal;
            tabelaSAC.append(String.format("%d | %.2f | %.2f | %.2f\n", i + 1, parcelaSAC, amortizacao, jurosMensal));
        }

        return tabelaSAC.toString();
    }
}
