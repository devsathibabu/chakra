package com.onetech.intelinfo.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import com.onetech.intelinfo.Dao.ExcelFileDao;

public class FileParserService {
	
	ExcelFileDao excelFileDao = new ExcelFileDao();
	
	public void parseExcelFile(File file) throws Exception{
		String fileName = file.getName();
		FileInputStream input = new FileInputStream(new File(file.getAbsolutePath()));
		HSSFWorkbook workbook = new HSSFWorkbook(input);
		HSSFSheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();
		HSSFRow headerRow = sheet.getRow(sheet.getFirstRowNum());
		excelFileDao.insertExcelDataToDatabase(rowIterator, headerRow, fileName);
		workbook.close();
	}
	
	public File fromDbTOExcel() throws Exception{
		File file = null;
		
		file = excelFileDao.fromDBToExcel();
		return file;
	}

}
