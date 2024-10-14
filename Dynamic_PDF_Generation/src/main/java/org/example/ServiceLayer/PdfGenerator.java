package org.example.ServiceLayer;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.example.Dtos.InvoiceRequest;

import java.io.ByteArrayOutputStream;

public class PdfGenerator {

    public static byte[] generatePdf(InvoiceRequest request) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();

            // Add title
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("Invoice", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            // Create a main table for everything (2 columns: Seller and Buyer, then item details)
            PdfPTable mainTable = new PdfPTable(2);
            mainTable.setWidthPercentage(100); // Full width
            mainTable.setSpacingBefore(10f);
            mainTable.setSpacingAfter(10f);
            mainTable.setWidths(new float[]{1f, 1f}); // Equal widths for both columns

            // Add seller and buyer information in the first row
            PdfPTable sellerTable = new PdfPTable(1); // Single column table for seller data
            sellerTable.addCell(createCell("Seller:", Font.BOLD));
            sellerTable.addCell(createCell(request.getSeller(), Font.NORMAL));
            sellerTable.addCell(createCell("Seller GSTIN:", Font.BOLD));
            sellerTable.addCell(createCell(request.getSellerGstin(), Font.NORMAL));
            sellerTable.addCell(createCell("Seller Address:", Font.BOLD));
            sellerTable.addCell(createCell(request.getSellerAddress(), Font.NORMAL));

            PdfPTable buyerTable = new PdfPTable(1); // Single column table for buyer data
            buyerTable.addCell(createCell("Buyer:", Font.BOLD));
            buyerTable.addCell(createCell(request.getBuyer(), Font.NORMAL));
            buyerTable.addCell(createCell("Buyer GSTIN:", Font.BOLD));
            buyerTable.addCell(createCell(request.getBuyerGstin(), Font.NORMAL));
            buyerTable.addCell(createCell("Buyer Address:", Font.BOLD));
            buyerTable.addCell(createCell(request.getBuyerAddress(), Font.NORMAL));

            // Add seller and buyer tables to the main table's first row
            mainTable.addCell(new PdfPCell(sellerTable));
            mainTable.addCell(new PdfPCell(buyerTable));

            // Add a second row for item headers spanning across 2 columns
            PdfPTable itemHeaderTable = new PdfPTable(4); // 4 columns for the item details
            itemHeaderTable.setWidthPercentage(100);
            itemHeaderTable.setWidths(new float[]{2f, 1f, 1f, 1f}); // Column widths
            itemHeaderTable.addCell(createCell("Item", Font.BOLD));
            itemHeaderTable.addCell(createCell("Quantity", Font.BOLD));
            itemHeaderTable.addCell(createCell("Rate", Font.BOLD));
            itemHeaderTable.addCell(createCell("Amount", Font.BOLD));

            // Add the item header table to the main table spanning 2 columns
            PdfPCell itemHeaderCell = new PdfPCell(itemHeaderTable);
            itemHeaderCell.setColspan(2);
            mainTable.addCell(itemHeaderCell);

            // Add a third row for the actual item data spanning across 2 columns
            PdfPTable itemDataTable = new PdfPTable(4); // 4 columns for the item data
            itemDataTable.setWidthPercentage(100);
            itemDataTable.setWidths(new float[]{2f, 1f, 1f, 1f});
            for (InvoiceRequest.Item item : request.getItems()) {
                itemDataTable.addCell(createCell(item.getName(), Font.NORMAL));
                itemDataTable.addCell(createCell(item.getQuantity(), Font.NORMAL));
                itemDataTable.addCell(createCell(item.getRate().toString(), Font.NORMAL));
                itemDataTable.addCell(createCell(item.getAmount().toString(), Font.NORMAL));
            }

            // Add the item data table to the main table spanning 2 columns
            PdfPCell itemDataCell = new PdfPCell(itemDataTable);
            itemDataCell.setColspan(2);
            mainTable.addCell(itemDataCell);

            // Add the main table to the document
            document.add(mainTable);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    }

    // Utility method to create a PdfPCell with specified text and font style
    private static PdfPCell createCell(String text, int fontStyle) {
        Font font = new Font(Font.FontFamily.HELVETICA, 10, fontStyle);
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(5);
        cell.setBorder(Rectangle.NO_BORDER); // No border for cleaner look
        return cell;
    }
}
