package com.onetech.intelinfo.Dao;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.bson.Document;

import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;

public class ExcelFileDao {
public void insertExcelDataToDatabase(Iterator<Row> rowIterator, HSSFRow headerRow, String fileName) {
		
		HSSFRow row;
		MongoClient mongoClient = new MongoClient();
		MongoDatabase mongoDatabase = mongoClient.getDatabase("NODATA");
		MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(fileName);
		Document document = null;
		
		String key = null;
		String value = null;
		Object obj = null;
		Iterator<Cell> cellIterator = null;
		Iterator<Cell> headerRowCell = headerRow.cellIterator();
		
		rowIterator.next();
		while(rowIterator.hasNext()){
			document = new Document();
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
				case Cell.CELL_TYPE_BLANK : value = " ";
												break;
				default : System.out.println("Unknown value");

				}

				System.out.println( key+ ":"+" "+ value );
				document.append(key, value);
			}
			
			mongoCollection.insertOne(document);
			
		}
		
		mongoClient.close();
		
	}
	
	
	
	public File fromDBToExcel(String categoryName) throws InterruptedException, Exception{
		MongoClient client = new MongoClient();
		MongoDatabase database = client.getDatabase("NODATA");
		if(database != null){
			System.out.println("Database Created successful");
		}else{
			System.out.println("Failed to create database");
		}
		MongoCollection<Document> collection = database.getCollection(categoryName);
		if(collection == null){
			System.out.println("Record Not found");
		}else{
			System.out.println("Record Found");
		}
		Document document = new Document();
		/*document.append("1", "value").append("2", "value").append("3", "value");
		collection.insertOne(document);*/

		System.out.println("************");
		List<Document> foundRecord = collection.find().into(new ArrayList<Document>());
		//System.out.println(foundRecord);
		Iterator<Document> dbIterator = foundRecord.iterator();
		HSSFWorkbook newWorkBook = new HSSFWorkbook();
		HSSFSheet newSheet = newWorkBook.createSheet("Sheet1");

		Map<String, Object[]> myMap = null;
		String data = null;
		DBObject dbObject = null;
		Iterator<String > key = null;
		Set<String> keySet = null;
		Object[] obj;
		int rowCount = 0;
		int headerRowCount = 0;
		while(dbIterator.hasNext()){

			

			Row row = newSheet.createRow(rowCount++);

			data = dbIterator.next().toJson();
			dbObject = (DBObject) JSON.parse(data);


			myMap = dbObject.toMap();
			//System.out.println(myMap);*/

			myMap.remove("_id");
			//System.out.println(myMap);

			keySet = myMap.keySet();
			//System.out.println(keySet);*/
			
			
			Object object1;
			int cellCount = 0;
			if(headerRowCount == 0){
				for(String keySetvalue : keySet){
					Cell cell = row.createCell(cellCount++);
					object1 = keySetvalue;
					if(object1 instanceof String){
						System.out.println("object is of type string"+object1);
						cell.setCellValue((String)object1);
					}else if(object1 instanceof Integer){
						System.out.println("Object is of type Integer"+object1);
						cell.setCellValue((Integer)object1);
					}else if(object1 == null ){
						cell.setCellValue("");
					}
				}
				
				headerRowCount = 1;
				
			}else{
				for(String keySetvalue : keySet){
					Cell cell = row.createCell(cellCount++);
					object1 = myMap.get(keySetvalue);
					if(object1 instanceof String){
						System.out.println("object is of type string"+object1);
						cell.setCellValue((String)object1);
					}else if(object1 instanceof Integer){
						System.out.println("Object is of type Integer"+object1);
						cell.setCellValue((Integer)object1);
					}else if(object1 == null ){
						cell.setCellValue("");
					}
				}
			}
		}
		
		String parentPath = System.getProperty("catalina.home");
		String pathToFileDirectory = parentPath+File.separator+"tmpFiles";
		File tempFiles = new File(pathToFileDirectory);
		
		if(!tempFiles.exists()){
			if(!tempFiles.mkdir()){
				System.out.println("Directory created successfully");
			}
			else{
				System.out.println("Failed to create Directory");
			}
		}
		
		File excelFileToDownload = new File(pathToFileDirectory+File.separator+categoryName+".xls");
		System.out.println(excelFileToDownload.getName());

		FileOutputStream writeToExcel = null;
		try {
			writeToExcel = new FileOutputStream(excelFileToDownload);
			newWorkBook.write(writeToExcel);
			System.out.println("file cereated. check with file path");
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			client.close();
			writeToExcel.close();
			newWorkBook.close();
		}
		return excelFileToDownload;
	}
}
