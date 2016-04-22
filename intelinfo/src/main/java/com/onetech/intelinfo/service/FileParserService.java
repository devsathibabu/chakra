package com.onetech.intelinfo.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.onetech.intelinfo.Dao.ExcelFileDao;

public class FileParserService {
	
	ExcelFileDao excelFileDao = new ExcelFileDao();
	
	public void parseExcelFile(File file) throws IOException{
		
		String fileName = file.getName();
		FileInputStream input = new FileInputStream(new File(file.getAbsolutePath()));
		HSSFWorkbook workbook = new HSSFWorkbook(input);
		Sheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();
		Row headerRow = sheet.getRow(sheet.getFirstRowNum());
		excelFileDao.insertExcelDataToDatabase(rowIterator, headerRow, fileName);
		workbook.close();
	}
	
	public boolean insertIntoExcel(Iterator<Row> rowIterator, String fileName) throws IOException{
		
		HSSFRow row;
		HSSFWorkbook newWorkbook = new HSSFWorkbook();
		HSSFSheet newSheet = newWorkbook.createSheet("MySheet");
		int rowCount = 0;
		while(rowIterator.hasNext()){
			row = (HSSFRow) rowIterator.next();
			Row newRow = newSheet.createRow(rowCount++);
			Iterator<Cell> cellIterator = row.cellIterator();
			int cellCount = 0;
			while(cellIterator.hasNext()){
				Cell newCell = newRow.createCell(cellCount++);
				Cell data = cellIterator.next();
				switch(data.getCellType()){
				case Cell.CELL_TYPE_BLANK : newCell.setCellValue(" ");
											break;
				case Cell.CELL_TYPE_NUMERIC : newCell.setCellValue(data.getNumericCellValue());
											break;
				case Cell.CELL_TYPE_STRING : newCell.setCellValue(data.getRichStringCellValue());
											break;
				/*default:newCell.setCellValue(data.getRichStringCellValue());
									break;*/
				}
				
			}
			System.out.println("**********");
			
		}
		FileOutputStream writeToExcel = null;
		try {
			writeToExcel = new FileOutputStream(new File("C:/Users/admin/Desktop/"+fileName));
			newWorkbook.write(writeToExcel);
			System.out.println("Excel file created successfully");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			writeToExcel.close();
			newWorkbook.close();
		}
	}

}
