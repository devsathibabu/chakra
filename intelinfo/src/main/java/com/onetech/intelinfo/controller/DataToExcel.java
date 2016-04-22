package com.onetech.intelinfo.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.bson.Document;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
@CrossOrigin
@RestController
public class DataToExcel {
	@RequestMapping(value="/download.htm", method=RequestMethod.POST)
	public void handleFileUpload(@RequestParam(value="category") String name,
			HttpServletRequest request, HttpServletResponse response) throws IOException{


		//File file = new File("C:\\Users\\admin\\Desktop\\Building materias.xls");
		MongoClient client = new MongoClient();
		MongoDatabase database = client.getDatabase("NODATA");
		if(database != null){
			System.out.println("Database not Fount");
		}
		
		System.out.println("DataBase Found");
		
		MongoCollection<Document> collection = database.getCollection(name);
		if(collection == null){
			System.out.println("Record Not found");
		}else{
			System.out.println("Record Found");
		}
		Document document = new Document();
		
		//System.out.println("************");
		List<Document> foundRecord = collection.find().into(new ArrayList<Document>());
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

		FileOutputStream writeToExcel = null;
		try {
			writeToExcel = new FileOutputStream(new File("C:/Users/admin/Desktop/Excel.xls"));
			newWorkBook.write(writeToExcel);
			System.out.println("file cereated. check with file path");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	

}
