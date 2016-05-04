package com.onetech.intelinfo.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.web.multipart.MultipartFile;

import com.onetech.intelinfo.Dao.ExcelFileDao;

public class FileParserService {
	
	ExcelFileDao excelFileDao = new ExcelFileDao();
	
	public void parseExcelFile(MultipartFile file) throws Exception{
		String fileName = file.getOriginalFilename();
		String[] fileNameArray = fileName.split("\\.");
		String realFileName = fileNameArray[0];
		
		FileInputStream input = (FileInputStream) file.getInputStream();
		HSSFWorkbook workbook = new HSSFWorkbook(input);
		HSSFSheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();
		HSSFRow headerRow = sheet.getRow(sheet.getFirstRowNum());
		excelFileDao.insertExcelDataToDatabase(rowIterator, headerRow, realFileName);
		workbook.close();
	}
	
	public File fromDbTOExcel(String categoryName) throws Exception{
		File file = null;
		
		file = excelFileDao.fromDBToExcel(categoryName);
		return file;
	}

}
