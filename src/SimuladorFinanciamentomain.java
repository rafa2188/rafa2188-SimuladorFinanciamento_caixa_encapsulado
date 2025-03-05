import java.util.InputMismatchException;
import java.util.Scanner;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class SimuladorFinanciamentomain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Entrada de dados com validação e tratamento de exceções
        System.out.print("Digite seu nome: ");
        String nomeCliente = scanner.nextLine();
        double valorImovel = lerDouble(scanner, "Digite o valor do imóvel (maior que zero): ", 0, Double.MAX_VALUE);
        double entrada = lerDouble(scanner, "Digite o valor da entrada (deve ser pelo menos 10% do valor do imóvel): ", valorImovel * 0.1, valorImovel);
        int prazoAnos = lerInt(scanner, "Digite o prazo do financiamento (em anos, maior que zero): ", 1, Integer.MAX_VALUE);
        double taxaJurosAnual = lerDouble(scanner, "Digite a taxa de juros anual (% positiva): ", 0, Double.MAX_VALUE);
        double rendaMensal = lerDouble(scanner, "Digite sua renda mensal (maior que zero): ", 0, Double.MAX_VALUE);

        // Cria o objeto Cliente
        Cliente cliente = new Cliente(nomeCliente, rendaMensal);

        // Cria o objeto Financiamento
        Financiamento financiamento = new Financiamento(valorImovel, entrada, prazoAnos, taxaJurosAnual);

        // Cálculo do financiamento
        double[] price = financiamento.calcularSistemaPrice();
        double[] sac = financiamento.calcularSistemaSAC();
        String tabelaSAC = financiamento.gerarTabelaSAC();

        // Validação da parcela com a renda mensal do cliente
        double percentualParcelaPrice = (price[0] / rendaMensal) * 100;
        double percentualParcelaSAC = (sac[0] + (financiamento.calcularValorFinanciado() * (taxaJurosAnual / 100 / 12))) / rendaMensal * 100;

        // Exibição dos resultados no console
        System.out.println("\nResultado da Simulação:");
        System.out.printf("Cliente: %s\n", cliente.getNome());
        System.out.printf("Valor do Imóvel: R$ %.2f\n", valorImovel);
        System.out.printf("Valor Financiado: R$ %.2f\n", financiamento.calcularValorFinanciado());
        System.out.printf("Total pago (Price): R$ %.2f (Juros: R$ %.2f)\n", price[1], price[2]);
        System.out.printf("Total pago (SAC): R$ %.2f (Juros: R$ %.2f)\n", sac[1], sac[2]);

        System.out.println("\nTabela de Parcelas (Price):");
        System.out.printf("Parcela | Valor\n");
        for (int i = 0; i < prazoAnos * 12; i++) {
            System.out.printf("%d | %.2f\n", i + 1, price[0]);
        }

        System.out.println("\nTabela de Parcelas (SAC):");
        System.out.println(tabelaSAC);

        if (percentualParcelaPrice > 30) {
            System.out.println("A parcela do sistema Price ultrapassa 30% da sua renda mensal. Verifique se é viável para você.");
        } else {
            System.out.println("A parcela do sistema Price está dentro de 30% da sua renda mensal.");
        }

        if (percentualParcelaSAC > 30) {
            System.out.println("A parcela inicial do sistema SAC ultrapassa 30% da sua renda mensal. Verifique se é viável para você.");
        } else {
            System.out.println("A parcela inicial do sistema SAC está dentro de 30% da sua renda mensal.");
        }

        // Gera o PDF com os resultados
        GeradorPDF.gerarPDF(
                nomeCliente,
                valorImovel,
                financiamento.calcularValorFinanciado(),
                price[1], price[2],
                sac[1], sac[2],
                tabelaSAC,
                percentualParcelaPrice,
                percentualParcelaSAC
        );

        scanner.close();
    }

    private static double lerDouble(Scanner scanner, String mensagem, double min, double max) {
        double valor;
        while (true) {
            try {
                System.out.print(mensagem);
                valor = scanner.nextDouble();
                if (valor > min && valor <= max) {
                    return valor;
                }
                System.out.println("Valor inválido. Tente novamente.");
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Digite um número válido.");
                scanner.next(); // Limpar buffer do scanner
            }
        }
    }

    private static int lerInt(Scanner scanner, String mensagem, int min, int max) {
        int valor;
        while (true) {
            try {
                System.out.print(mensagem);
                valor = scanner.nextInt();
                if (valor >= min && valor <= max) {
                    return valor;
                }
                System.out.println("Valor inválido. Tente novamente.");
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Digite um número inteiro válido.");
                scanner.next(); // Limpar buffer do scanner
            }
        }
    }
}