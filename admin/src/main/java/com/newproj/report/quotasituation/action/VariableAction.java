package com.newproj.report.quotasituation.action;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.newproj.core.exception.BusinessException;
import com.newproj.core.page.RemotePage;
import com.newproj.core.rest.annotation.Api;
import com.newproj.core.rest.annotation.Get;
import com.newproj.core.rest.annotation.Post;
import com.newproj.core.rest.annotation.Put;
import com.newproj.core.rest.support.ParamModal;
import com.newproj.core.rest.support.RestActionSupporter;
import com.newproj.core.util.ScriptUtil;
import com.newproj.report.quotasituation.dto.Variable;
import com.newproj.report.quotasituation.dto.VariableParam;
import com.newproj.report.quotasituation.service.VariableService;
@Api("variable")
public class VariableAction extends RestActionSupporter{

	@Autowired
	private VariableService variableService ;
	
	@Get
	public String findList( ParamModal modal ){
		RemotePage<Variable> pageData = variableService.findList( modal.getParam("?displayName" , "?varName" , "type" , "?target"), 
				modal.getPageParam() , Variable.class ) ;
		return success( pageData , pageData == null ? 0 : pageData.getTotal() ) ;
	}
	
	@Get("/{key}")
	public String get( @PathVariable("key") String key  ){
		String by = Pattern.matches("^[0-9]+$", key ) ? "id" : "varName" ;
		return success( variableService.findBy( by , key , Variable.class )  ) ; 
	}
	
	@Get("/byVarName")
	public String getByVarName( @RequestParam("varNames[]") String[] varNames  ){
		if( varNames == null || varNames.length == 0 )
			return success( null ) ;
		return success( variableService.findListBy( "varName" , 
				Arrays.asList(varNames) , Variable.class  , "varName" , "displayName")  ) ; 
	}
	
	@Post
	public  String create( @RequestBody VariableParam param  ){
		if( variableService.findBy("varName", param.getVarName() , Variable.class , "id") != null )
			throw new BusinessException("变量名["+param.getVarName()+"]已存在!") ;
		if( "sql".equalsIgnoreCase( param.getType() ) && !validateScriptVars( param.getValue() ) )
			throw new BusinessException("SQL中存在无效变量!") ;
		
		return success( variableService.create( param , VariableParam.class ) ) ;
	}
	
	private boolean validateScriptVars( String script ){
		if( StringUtils.isEmpty( script ) )
			return true ;
		List<String> vars = ScriptUtil.scriptVars( script ) ;
		if( vars == null || vars.isEmpty() )
			return true ;
		List<Variable> variables = variableService.findListBy("varName", vars , Variable.class , "varName" ) ;
		return variables != null && variables.size() == vars.size() ;
	}
	
	@Put("/{id}")
	public String update( @PathVariable("id") int id , 
			@RequestBody VariableParam param  ){
		if( variableService.findBy("id",  id , Variable.class , "id") == null )
			throw new BusinessException("数据不存在!") ;
		
		if( "sql".equalsIgnoreCase( param.getType() ) && !validateScriptVars( param.getValue() ) )
			throw new BusinessException("SQL中存在无效变量!") ;
		
		param.setVarName( null ); //不能修改变量名
		variableService.update("id",  id , param ) ;
		return success( null ) ;
	}
	
	
}
