package com.newproj.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.commons.lang3.StringUtils;

public class ScriptUtil {
	private static ScriptEngine scriptEngine ;
	static{
		scriptEngine = new ScriptEngineManager().getEngineByName("javascript") ;
	}
	
	private static String appendJavaImport( String script ){
		if( script == null )
			script = "" ;
		script = "load(\"nashorn:mozilla_compat.js\");"
				+ "importPackage('com.newproj.report.sys.dal.dao','java.util');" + script ;
		return script ;
	}
	
	private static String o( String script ){
		if( script == null )
			script = "" ;
		script = "function o( input ){ "
					+ "var rs = {}; "
					+ "try{"
						+ "rs=JSON.parse(input);"
					+ "} catch(e){} "
					+ "return rs; "
				+ "}\r\n" + script ;
		return script ;
	}
	
	private static String toInt( String script ){
		if( script == null )
			script = "" ;
		script = "function toInt( input , def ){ "
					+ "var rs = def; "
					+ "if( isNaN(input) ) return def ;"
					+ "try{"
						+ "rs=parseInt(input);"
					+ "} catch(e){"
						+ "rs = def;"
					+ "} "
					+ "return rs; "
				+ "}\r\n" + script ;
		return script ;
	}
	private static String toFloat( String script ){
		if( script == null )
			script = "" ;
		script = "function toFloat( input , def ){ "
					+ "var rs = def; "
					+ "try{"
						+ "rs=parseFloat(input);"
					+ "} catch(e){} "
					+ "return rs; "
				+ "}\r\n" + script ;
		return script ;
	}
	
	public static String eval( String script , Map<String,Object> args )throws Exception {
		if( script == null || script.trim().length() == 0 )
			throw new IllegalArgumentException("script is not null!") ;
		
		//内置函数 .
		script = appendJavaImport( script ) ;
		script = o(script);
		script = toInt( script );
		script = toFloat( script );
		
		Map<String , Object> useArgs = new HashMap<String,Object>() ;
		if( args != null && !args.isEmpty() ){
			useArgs.putAll( args );
		}
		handleScriptUndefinedParam( script , useArgs ) ;
		CompiledScript compiledScript = ( ( Compilable )scriptEngine ).compile( script ) ;
		Bindings binding = scriptEngine.createBindings() ;
		if( useArgs != null && !useArgs.isEmpty() ){
			for( Map.Entry<String, Object> entry : useArgs.entrySet() ){
				binding.put( entry.getKey(), entry.getValue() ) ;
			}
		}
		Object result = compiledScript.eval( binding ) ;
		return result == null ? null : result.toString() ;
	}
	
	public static List<String> scriptVars ( String script ){
		if( StringUtils.isEmpty( script ) ) 
			return null ;
		List<String> vars = new ArrayList<String>() ;
		Matcher matcher = Pattern.compile("\\$[0-9a-zA-Z_]+").matcher( script ) ;
		while( matcher.find() )
			vars.add( matcher.group( 0 ) ) ;
		return vars.isEmpty() ? null : vars ;
	}
	
	private static void handleScriptUndefinedParam( String script , Map<String,Object> param ){
		Set<String> vars = scriptVariables( script ) ;
		if( vars == null || vars.isEmpty() )
			return ;
		for( String var : vars ){
			if( param.containsKey( var ) )
				continue ;
			param.put( var , null ) ;
		}
	}
	
	private static Set<String> scriptVariables( String script ){
		if( script == null )
			return null ;
		Pattern pattern = Pattern.compile("\\$[0-9a-zA-Z_]+") ;
		Matcher matcher = pattern.matcher( script )  ;
		Set<String> vars = new HashSet<String>() ;
		while( matcher.find() )
			for( int i = 0 ;i <= matcher.groupCount() ; i ++ )
				vars.add( matcher.group(0) ) ;
		return vars ;
	}
	
	public static void main(String [] args ) throws Exception{
		/*Map<String,Object> param = new HashMap<String,Object>() ;
		Student stu = new Student() ;
		stu.setName("stuName1");
		stu.setAge(20);
		param.put("$student", "20") ;
		System.out.println(ScriptUtil.eval( "'合格'" , param ) );*/
		//select( columns , table , where , gropy )
		Map<String,Object> param = new HashMap<String,Object>() ;
		param.put("$arg", 100 ) ;
		System.out.println(ScriptUtil.eval("var o = {a:1,b:2} ; JSON.stringify(result)" , param ));
	}
	
}

class Student {
	private String name;
	private int age ;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	
}
