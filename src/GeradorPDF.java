import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;

public class GeradorPDF {

    public static void gerarPDF(String nomeCliente, double valorImovel, double valorFinanciado,
                                double totalPagoPrice, double jurosTotaisPrice,
                                double totalPagoSAC, double jurosTotaisSAC,
                                String tabelaSAC, double percentualParcelaPrice,
                                double percentualParcelaSAC) {
        Document document = new Document();

        try {
            // Cria o arquivo PDF
            PdfWriter.getInstance(document, new FileOutputStream("SimulacaoFinanciamento.pdf"));
            document.open();

            // Adiciona conteúdo ao PDF
            document.add(new Paragraph("Resultado da Simulação"));
            document.add(new Paragraph("Cliente: " + nomeCliente));
            document.add(new Paragraph("Valor do Imóvel: R$ " + String.format("%.2f", valorImovel)));
            document.add(new Paragraph("Valor Financiado: R$ " + String.format("%.2f", valorFinanciado)));
            document.add(new Paragraph("\nSistema Price:"));
            document.add(new Paragraph("Total pago: R$ " + String.format("%.2f", totalPagoPrice)));
            document.add(new Paragraph("Juros totais: R$ " + String.format("%.2f", jurosTotaisPrice)));
            document.add(new Paragraph("\nSistema SAC:"));
            document.add(new Paragraph("Total pago: R$ " + String.format("%.2f", totalPagoSAC)));
            document.add(new Paragraph("Juros totais: R$ " + String.format("%.2f", jurosTotaisSAC)));
            document.add(new Paragraph("\nTabela de Parcelas (SAC):"));
            document.add(new Paragraph(tabelaSAC));

            document.add(new Paragraph("\nAnálise de Viabilidade:"));
            if (percentualParcelaPrice > 30) {
                document.add(new Paragraph("A parcela do sistema Price ultrapassa 30% da sua renda mensal. Verifique se é viável para você."));
            } else {
                document.add(new Paragraph("A parcela do sistema Price está dentro de 30% da sua renda mensal."));
            }

            if (percentualParcelaSAC > 30) {
                document.add(new Paragraph("A parcela inicial do sistema SAC ultrapassa 30% da sua renda mensal. Verifique se é viável para você."));
            } else {
                document.add(new Paragraph("A parcela inicial do sistema SAC está dentro de 30% da sua renda mensal."));
            }

            System.out.println("PDF gerado com sucesso: SimulacaoFinanciamento.pdf");
        } catch (DocumentException | IOException e) {
            System.err.println("Erro ao gerar o PDF: " + e.getMessage());
        } finally {
            document.close();
        }
    }
}
