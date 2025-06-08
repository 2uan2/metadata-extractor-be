package org.example.schemaextractor.services.impl;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.example.schemaextractor.domain.entities.*;
import org.example.schemaextractor.services.ReportDownloadService;
import org.example.schemaextractor.services.ReportEditorService;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service
public class ReportDownloadImpl implements ReportDownloadService {

//    private final String OUTPUT_PATH = "src/main/resources/reports/test.docx";
    private final String TEMPLATE_PATH = "reports/template.docx";

    private final ReportEditorService reportEditorService;

    public ReportDownloadImpl(ReportEditorService reportEditorService) {
        this.reportEditorService = reportEditorService;
    }

    @Override
    public byte[] createDocxReportFile(Long id) {
        try (
                InputStream templateInputStream = new ClassPathResource(TEMPLATE_PATH).getInputStream();
                XWPFDocument document = new XWPFDocument(templateInputStream);
                ByteArrayOutputStream bis = new ByteArrayOutputStream();
        ) {
            List<TableEntity> reportTableData = reportEditorService.getReportTables(id);

            List<XWPFTable> tables = document.getTables();

            XWPFTable tableTableTemplate = tables.get(3);
            XWPFTable columnTableTemplate = tables.get(4);
            XWPFTable constraintTableTemplate = tables.get(5);
            XWPFTable indexTableTemplate = tables.get(6);

            // get Style of big heading for table and smaller heading for constraint, index and trigger
            List<XWPFParagraph> paragraphs = document.getParagraphs();

            XWPFParagraph tableStyleParagraph = null;
            String tableStyle = "";
            XWPFParagraph tableSubStyleParagraph = null;
            String tableSubStyle = "";
            for (int i = 0; i < paragraphs.size(); i++) {
                if (paragraphs.get(i).getText().contains("Chi tiết bảng cơ sở dữ liệu")) {
                    tableStyle = paragraphs.get(i+2).getStyle();
                    tableStyleParagraph = paragraphs.get(i+2);
                } else if (paragraphs.get(i).getText().contains("Constraint")) {
                    tableSubStyle = paragraphs.get(i).getStyle();
                    tableSubStyleParagraph = paragraphs.get(i);
                }
            }

            setTables(tableTableTemplate, reportTableData);

            XmlCursor cursor = indexTableTemplate.getCTTbl().newCursor();

//            for (int i = 0; i < 5; i++) {
//                cursor.toEndToken();
//                cursor.toNextToken();
//            }

            XWPFTableRow secondRowColumnTable = columnTableTemplate.getRow(1);
            CTRow ctrowColumn = CTRow.Factory.parse(secondRowColumnTable.getCtRow().newInputStream());
            columnTableTemplate.removeRow(1);
            XWPFTableRow secondRowConstraintTable = constraintTableTemplate.getRow(1);
            CTRow ctrowConstraint = CTRow.Factory.parse(secondRowConstraintTable.getCtRow().newInputStream());
            constraintTableTemplate.removeRow(1);
            XWPFTableRow secondRowIndexTable = indexTableTemplate.getRow(1);
            CTRow ctrowIndex = CTRow.Factory.parse(secondRowIndexTable.getCtRow().newInputStream());
            indexTableTemplate.removeRow(1);

            for (TableEntity table : reportTableData) {
                System.out.println("inserting for table: " + table.getName());
                addTableNameAndColumnTable(table, document, cursor, tableStyle, columnTableTemplate, ctrowColumn);

                addConstraintHeadingAndTable(table, document, cursor, tableSubStyle, constraintTableTemplate, ctrowConstraint);

                addIndexHeadingAndTable(table, document, cursor, tableSubStyle, indexTableTemplate, ctrowIndex);

                addTrigger(document, cursor, tableSubStyle);
            }

            document.removeBodyElement(document.getPosOfTable(columnTableTemplate));
            document.removeBodyElement(document.getPosOfTable(constraintTableTemplate));
            document.removeBodyElement(document.getPosOfTable(indexTableTemplate));

            document.removeBodyElement(document.getPosOfParagraph(tableSubStyleParagraph));
            document.removeBodyElement(document.getPosOfParagraph(tableStyleParagraph));

//            FileOutputStream out = new FileOutputStream(OUTPUT_PATH);
            document.write(bis);
//            document.write(out);
//            out.close();

            return bis.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (XmlException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] createPdfReportFile(Long id) {
        byte[] bytes = createDocxReportFile(id);
        try (
                InputStream inputStream = new ByteArrayInputStream(bytes);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ) {
            IConverter converter = LocalConverter.builder().build();
            converter.convert(inputStream).as(DocumentType.DOCX).to(outputStream).as(DocumentType.PDF).execute();

            return outputStream.toByteArray();

        } catch (IOException  e) {
            throw new RuntimeException(e);
        }
    }

    private void writeReportData(Long id, XWPFDocument document) throws XmlException, IOException {
    }

    private static void addTableNameAndColumnTable(TableEntity table, XWPFDocument document, XmlCursor cursor, String tableStyle, XWPFTable columnTableTemplate, CTRow ctrowColumn) {
        // setting header with table name
        XWPFParagraph tableNameHeading = document.insertNewParagraph(cursor);
        tableNameHeading.setStyle(tableStyle);
        tableNameHeading.createRun().setText(table.getName());
        cursor.toNextToken();

        // setting table of columns metadata
        XWPFTable columnTable = document.insertNewTbl(cursor);
        columnTable.getCTTbl().setTblPr(columnTableTemplate.getCTTbl().getTblPr());
        columnTable.getCTTbl().setTrArray(columnTableTemplate.getCTTbl().getTrArray());

        for (ColumnEntity column : table.getColumns()) {
            XWPFTableRow newRow = new XWPFTableRow(ctrowColumn, columnTableTemplate);
            List<XWPFTableCell> cells = newRow.getTableCells();
            cells.get(0).setText(column.getFieldName());
            cells.get(1).setText(column.getDataType() + "(" + column.getDataLength() + ")");
            cells.get(2).setText( column.getNullable() ? "x" : "" );
            cells.get(3).setText( column.getAutoIncrement() ? "x" : "" );
            cells.get(4).setText(column.getKeyType());
            cells.get(5).setText(column.getDefaultValue());
            cells.get(6).setText(column.getDescription());
            columnTable.addRow(newRow);
        }
        // add additional row so looks less empty i guess
        XWPFTableRow finalRow = new XWPFTableRow(ctrowColumn, columnTableTemplate);
        finalRow.getTableCells().forEach(cell -> {
            cell.setText(null);
        });
        columnTable.addRow(finalRow);
        cursor.toNextToken();
    }

    private static void addConstraintHeadingAndTable(TableEntity table, XWPFDocument document, XmlCursor cursor, String tableSubStyle, XWPFTable constraintTableTemplate, CTRow ctrowConstraint) {
        // sub heading for constraint
        XWPFParagraph constraintHeading = document.insertNewParagraph(cursor);
        constraintHeading.setStyle(tableSubStyle);
        constraintHeading.createRun().setText("Constraint");
        cursor.toNextToken();

        XWPFTable constraintTable = document.insertNewTbl(cursor);
        constraintTable.getCTTbl().setTblPr(constraintTableTemplate.getCTTbl().getTblPr());
        constraintTable.getCTTbl().setTrArray(constraintTableTemplate.getCTTbl().getTrArray());

        for (ConstraintEntity constraint : table.getConstraints()) {
            XWPFTableRow newRow = new XWPFTableRow(ctrowConstraint, constraintTableTemplate);
            List<XWPFTableCell> cells = newRow.getTableCells();
            cells.get(0).setText(constraint.getKeyName());
            cells.get(1).setText(constraint.getColumnName());
            cells.get(2).setText(constraint.getKeyType());
            cells.get(3).setText(constraint.getReferencedTableName());
            cells.get(4).setText(constraint.getReferencedColumnName());
            constraintTable.addRow(newRow);
        }
        XWPFTableRow finalRow = new XWPFTableRow(ctrowConstraint, constraintTableTemplate);
        finalRow.getTableCells().forEach(cell -> {
            cell.setText(null);
        });
        constraintTable.addRow(finalRow);
        cursor.toNextToken();
    }

    private static void addIndexHeadingAndTable(TableEntity table, XWPFDocument document, XmlCursor cursor, String tableSubStyle, XWPFTable indexTableTemplate, CTRow ctrowIndex) {
        XWPFParagraph indexHeading = document.insertNewParagraph(cursor);
        indexHeading.setStyle(tableSubStyle);
        indexHeading.createRun().setText("Index");
        cursor.toNextToken();

        XWPFTable indexTable = document.insertNewTbl(cursor);
        indexTable.getCTTbl().setTblPr(indexTableTemplate.getCTTbl().getTblPr());
        indexTable.getCTTbl().setTrArray(indexTableTemplate.getCTTbl().getTrArray());

        for (IndexEntity index : table.getIndexes()) {
            XWPFTableRow newRow = new XWPFTableRow(ctrowIndex, indexTableTemplate);
            List<XWPFTableCell> cells = newRow.getTableCells();
            cells.get(0).setText(index.getName());
            cells.get(1).setText(index.getReferencedColumnName());
            indexTable.addRow(newRow);
        }
        XWPFTableRow finalRow = new XWPFTableRow(ctrowIndex, indexTableTemplate);
        finalRow.getTableCells().forEach(cell -> {
            cell.setText(null);
        });
        indexTable.addRow(finalRow);
        cursor.toNextToken();
    }

    private static void addTrigger(XWPFDocument document, XmlCursor cursor, String tableSubStyle) {
        XWPFParagraph triggerHeading = document.insertNewParagraph(cursor);
        triggerHeading.setStyle(tableSubStyle);
        triggerHeading.createRun().setText("Trigger");
        cursor.toNextToken();

        XWPFParagraph triggerBody = document.insertNewParagraph(cursor);
        triggerBody.createRun().setText("N/A");
        cursor.toNextToken();
        
        XWPFParagraph padding = document.insertNewParagraph(cursor);
        cursor.toNextToken();
    }

    private static void setTables(XWPFTable tableTableTemplate, List<TableEntity> reportTableData) throws XmlException, IOException {
        XWPFTableRow tableRowTemplate = tableTableTemplate.getRow(1);
        CTRow tableCtRow = CTRow.Factory.parse(tableRowTemplate.getCtRow().newInputStream());
        tableTableTemplate.removeRow(1);
        for (TableEntity table : reportTableData) {
            XWPFTableRow newRow = new XWPFTableRow(tableCtRow, tableTableTemplate);
            String tableName = table.getName();
            newRow.getCell(0).setText(tableName);
            String tableDescription = table.getDescription();
            newRow.getCell(1).setText(tableDescription);

            tableTableTemplate.addRow(newRow);
        }
    }
}
