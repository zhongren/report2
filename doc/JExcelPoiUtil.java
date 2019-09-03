package com.edu.lebao.framework.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.edu.lebao.framework.core.json.JsonSerializer;

public class JExcelPoiUtil {

	
	public static <T> List<T> readWorkBook(String suffix ,
			InputStream input , Class<T> clazz , int sheetIndex , int rowOffset  , String [] fields ,
			Integer [] skip ) throws Exception {
		if( input == null || fields == null || fields.length == 0 ){
			return null ;
		}
		if( !"xls,xlsx".contains(suffix) ) {
			throw new IllegalArgumentException("文件格式错误！") ;
		}
		Workbook workBook = null ;
		if( "xls".equalsIgnoreCase( suffix ) ){
			workBook = new HSSFWorkbook( input ) ;
		}else if( "xlsx".equalsIgnoreCase( suffix ) ){
			workBook = new XSSFWorkbook( input ) ;
		} 
		Sheet sheet = workBook.getSheetAt( sheetIndex ) ;
		int allRows = sheet.getLastRowNum() ;
		if(  allRows < rowOffset ){
			return null ;
		}
		int allColumns = sheet.getDefaultColumnWidth() ;
		allColumns = allColumns <= fields.length ? allColumns : fields.length ;
		List<T> dataList = new ArrayList<T>() ;
		for( Row row : sheet  ){
			if( row.getRowNum() < rowOffset ) continue ;
			T item = clazz.newInstance() ;
			int index = 0 , emptyRows = 0 ;
			System.out.println(row.getLastCellNum());
			for( int i = 0 ; i <= row.getLastCellNum() ; i ++  ){
				if( index >= fields.length ){
					continue ;
				}
				if( skip != null && skip.length > 0 ){
					boolean isSkip = false ;
					for( int idx : skip ){
						if( i == idx )  isSkip = true ; 
					}
					if( isSkip ) continue ;
				}
				Cell cell = row.getCell(i) ;
				String value = "" ;
				if( cell != null ){ 
					switch( cell.getCellType() ){
					case HSSFCell.CELL_TYPE_NUMERIC :
						if( HSSFDateUtil.isCellDateFormatted( cell ) ){
							value = String.valueOf( cell.getDateCellValue().getTime() / 1000 ) ; 
							break ;
						}
						cell.setCellType( Cell.CELL_TYPE_STRING );
						value = cell.getStringCellValue() ;
						break ;
					default : value = cell.toString() ;
					}
				}
				if(StringUtils.isEmpty( value ) ){
					emptyRows ++ ;
				}
				BeanUtils.setProperty( item , fields[index++] , value );
			}
			if( emptyRows == fields.length ) continue ;
			dataList.add( item ) ;
		}
		return dataList ;
	}
	
	/**
	 * 根据SHEET 名称读取Excel数据表 .
	 * 
	 * @param input 		文件流
	 * @param sheetName		SHEET名称
	 * @param clazz			转换类型
	 * @param fieldMapping 	字段名和工作表标题映射
	 * @param offset 		偏移量
	 * @return
	 */
	@SuppressWarnings("resource")
	public static List<Map<String,Object>> readWorkBookf( String type , InputStream input , String sheetName , 
			String ... fieldNames  ){
		Workbook workBook = null ;
		try{
			if( type.equalsIgnoreCase("xls") )
				workBook = new HSSFWorkbook( input ) ;
			else if( type.equalsIgnoreCase("xlsx") )
				workBook = new XSSFWorkbook( input ) ;
			else throw new IllegalArgumentException("文件格式不正确!") ;
		}catch(Exception e){
			throw new RuntimeException("文件解析失败!") ;
		}
		
		Sheet sheet = workBook.getSheet( sheetName ) ;
		if( sheet == null ) throw new RuntimeException("工作表名称【"+sheetName+"】不存在!") ;
		
		int firstRowNum = 0 ;
		Row fieldRow = sheet.getRow( firstRowNum = sheet.getFirstRowNum() ) ;
		Map<String,Integer> columnIndex = getExcelColumnNameIndex( fieldRow , fieldNames ) ;
		
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>() ;
		for( int i = firstRowNum+1 ; i <= sheet.getLastRowNum() ; i ++ ){
			Map<String,Object> rowValue = new HashMap<String,Object>() ;
			for( Map.Entry<String, Integer> item : columnIndex.entrySet() ){
				Row row = sheet.getRow( i ) ;
				if( row == null ) continue ;
				Cell cell = row.getCell(item.getValue() ) ;
				if( cell == null ) continue ;
				cell.setCellType( CellType.STRING );
				String cellValue = cell.getStringCellValue().trim()  ;
				if( cellValue.equals("") ) continue ;
				rowValue.put(item.getKey() , cellValue ) ;
			}
			if( rowValue.isEmpty() ) continue ;
			rowValue.put("xlsLine", i+1 ) ;
			result.add( rowValue ) ;
		}
		
		return result ;
	}
	
	public static void main( String [] args ) throws FileNotFoundException{
		System.out.println( new JsonSerializer() .toJson(readWorkBookf( "xlsx" , new FileInputStream( new File("C:\\Users\\10147\\Desktop\\report\\test.xlsx") ) ,
				"参保人员信息" )) ) ;
	}
	
	private static Map<String,Integer> getExcelColumnNameIndex( Row row , String ...columns   ){
		int lastCellNum = row.getLastCellNum() , firstCellNum = row.getFirstCellNum() ;
		List<String> columnsList = null ;
		if( columns != null && columns.length > 0 ){
			columnsList = Arrays.asList( columns ) ;
		}
		Map<String,Integer> tempIndex = new LinkedHashMap<String,Integer>() ;
		for( int i = firstCellNum ; i < lastCellNum ; i ++ ){
			Cell cell = row.getCell( i ) ;
			if( cell == null ) continue ;
			String value = cell.getStringCellValue() ;
			if( columnsList != null && !columnsList.isEmpty() && columnsList.contains( value ) ){
				tempIndex.put( value , i ) ;
				continue ;
			}
			tempIndex.put( value , i ) ;
		}
		return tempIndex ;
	}
	
}
