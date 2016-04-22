package com.onetech.intelinfo.Dao;

import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.json.JSONObject;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.util.JSON;

public class ExcelFileDao {
	
	public void insertExcelDataToDatabase(Iterator<Row> rowIterator,Row headerRow,String tableName){
		HSSFRow row = null;
		JSONObject json = new JSONObject();
		Iterator<Cell> cellIterator = null;
		String key = null;
		String value = null;
		Iterator<Cell> tableHeader = headerRow.cellIterator();
		Iterator<Cell> headerRowCell = headerRow.cellIterator();
		rowIterator.next();
		
		Mongo myMongo = new Mongo("localhost",27017);
		DB myDB = myMongo.getDB("NODATA");
		DBCollection collection = myDB.getCollection(tableName);
		DBObject mongoObject = null;
		
		while(rowIterator.hasNext()){
			row = (HSSFRow) rowIterator.next();
			cellIterator = row.iterator();
			headerRowCell = headerRow.cellIterator();
			while(headerRowCell.hasNext()){
				
				Cell cell = cellIterator.next();
				Cell headerCell = headerRowCell.next();
				key = headerCell.getStringCellValue();
				switch(cell.getCellType()){
				case Cell.CELL_TYPE_NUMERIC : value = new Double(cell.getNumericCellValue()).toString();
				break;
				case Cell.CELL_TYPE_STRING : value = cell.getStringCellValue();
				break;
				case Cell.CELL_TYPE_BLANK : value = "";
												break;
				default : value="";
						break; 
				}

				json.put(key, value);
			}
			String data = json.toString();
			mongoObject = (DBObject)JSON.parse(data);
			System.out.println(json.toString());
			collection.insert(mongoObject);
			
		}
	}

}
