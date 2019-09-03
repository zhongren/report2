package com.edu.lebao.framework.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class JExcelUtil {
	
	public static void generateExcelWithData( OutputStream output ,  List<String[]> data , String[] fields ) throws Exception{
		WritableWorkbook workbook = createWorkBook( output ) ;
		WritableSheet sheet = workbook.createSheet("sheet", 0 ) ;
		int offset = 0 ;
		if( fields != null && fields.length > 0 ){
			for( int i = 0 ; i < fields.length ; i ++ ){
				Label label = new Label( i , 0 , fields[ i ] ) ; 
				sheet.addCell( label ) ; 
			}
			offset += 1 ;
		}
		if( data == null || data.isEmpty() ){
			return ; 
		}
		for( int i = 0 ; i < data.size() ; i ++ ){
			for( int j = 0 ; j < data.get( i ).length ; j ++ ){
				Label label = new Label( j , i + offset ,  data.get( i )[j] ) ;
				sheet.addCell( label ) ;
			}
		}
		workbook.write();
		workbook.close();
	}
	
	private static WritableWorkbook createWorkBook( OutputStream output ) throws IOException{
		WritableWorkbook workbook = Workbook.createWorkbook( output ) ;
		return workbook ;
	}
	
	public static <T> List<T> readWorkBook(
			InputStream input , Class<T> clazz , int sheetIndex , int rowOffset  , String [] fields ) throws Exception {
		if( input == null || fields == null || fields.length == 0 ){
			return null ;
		}
		Workbook workBook = Workbook.getWorkbook( input ) ;
		Sheet sheet = workBook.getSheet( sheetIndex ) ;
		int allRows = sheet.getRows() ;
		if(  allRows < rowOffset ){
			return null ;
		}
		int allColumns = sheet.getColumns() ;
		allColumns = allColumns <= fields.length ? allColumns : fields.length ;
		List<T> dataList = new ArrayList<T>() ;
		for( int i = rowOffset ; i < allRows ; i ++  ){
			T item = clazz.newInstance() ;
			for( int j = 0 ; j < allColumns ; j ++  ){
				if( StringUtils.isEmpty( fields[j] ) ){
					continue ;
				}
				BeanUtils.setProperty( item , fields[j] , sheet.getCell( i ,  j ) );
			}
			dataList.add( item ) ;
		}
		return dataList ;
	}
	
}
