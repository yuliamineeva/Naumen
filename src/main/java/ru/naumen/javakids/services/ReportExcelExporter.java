package ru.naumen.javakids.services;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.naumen.javakids.model.Lecture;
import ru.naumen.javakids.model.User;

public class ReportExcelExporter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<User> users;
    private List<Lecture> lectures;
    private String description;

    public ReportExcelExporter(List<User> listUsers) {
        this.users = listUsers;
        workbook = new XSSFWorkbook();
    }

    public ReportExcelExporter(List<Lecture> lectures, String description) {
        this.lectures = lectures;
        this.description = description;
        workbook = new XSSFWorkbook();
    }

    private void writeHeaderLineUsers() {
        sheet = workbook.createSheet("Users");
        Row row = sheet.createRow(0);
        CellStyle style = getCellStyleHeaderLine();
        createCell(row, 0, "Id пользователя", style);
        createCell(row, 1, "Roles", style);
        createCell(row, 2, "username", style);
        createCell(row, 3, "E-mail", style);
    }

    private void writeHeaderLineLectures() {
        sheet = workbook.createSheet(description);
        Row row = sheet.createRow(0);
        CellStyle style = getCellStyleHeaderLine();
        createCell(row, 0, "Id лекции", style);
        createCell(row, 1, "Тема лекции", style);
    }

    private CellStyle getCellStyleHeaderLine() {
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLinesUsers() {
        int rowCount = 1;
        CellStyle style = getCellStyleDataLines();

        for (User user : users) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, user.getId(), style);
            createCell(row, columnCount++, user.getRoles().toString(), style);
            createCell(row, columnCount++, user.getUsername(), style);
            createCell(row, columnCount++, user.getEmail(), style);
        }
    }

    private void writeDataLinesLectures() {
        int rowCount = 1;
        CellStyle style = getCellStyleDataLines();

        for (Lecture lecture : lectures) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, lecture.getId(), style);
            createCell(row, columnCount++, lecture.getTopic(), style);
        }
    }

    private CellStyle getCellStyleDataLines() {
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setIndention((short) 2);
        return style;
    }

    public void exportUsers(HttpServletResponse response, String report) throws IOException {
        switch (report) {
            case "users":
                writeHeaderLineUsers();
                writeDataLinesUsers();
                break;
            case "lectures":
                writeHeaderLineLectures();
                writeDataLinesLectures();
                break;
        }
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}

