package com.newproj.report.file.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.newproj.core.exception.BusinessException;
import com.newproj.core.rest.annotation.Api;
import com.newproj.core.rest.support.RestActionSupporter;
import com.newproj.core.util.ScriptUtil;
import com.newproj.core.util.TimeUtil;
import com.newproj.report.context.Subject;
import com.newproj.report.file.manager.FileManager;

@Api("/file")
public class FileAction extends RestActionSupporter {
	
	@Autowired
	private FileManager fileManager ;

	@RequestMapping( value ="/{module}/upload" , method = RequestMethod.POST , 
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
	public String fileUpload( MultipartFile file , @PathVariable("module") String module ,
			@RequestParam( required = false , value="validScript" ) String validScript ,
			HttpServletRequest request ){
		//获取信息
		if( file == null || file.isEmpty() )
			throw new BusinessException("请选择文件!") ;
		
		String fileName = file.getOriginalFilename() ;
		String ext = fileName.indexOf(".") == -1 ? null : fileName.substring( fileName.lastIndexOf(".")+1 );
		long size = file.getSize() ;
		//校验
		Map<String,Object> validParam = new HashMap<String,Object>() ;
		validParam.put("$ext", ext ) ;
		validParam.put("$size", size ) ;
		String validResult = null ;
		try {
			validResult = ScriptUtil.eval(validScript, validParam);
		} catch (Exception e) {
			logger.error("[文件上传]校验脚本执行出错,script:"+validScript+";args:"+validParam  , e );
			throw new BusinessException("校验脚本执行出错!") ;
		}
		if( !"true".equalsIgnoreCase( validResult ) ){
			throw new BusinessException( validResult ) ;
		}
		String moduleDir = String.format("%s/%s" , module , 
				Subject.getUser() == null ? "0" : Subject.getUser().getId() )  ;
		//保存
		String savePath = fileManager.getUploadDir( moduleDir ) ;
		try{
			fileManager.saveTo(String.format("%s/%s", savePath , fileName ) , file.getInputStream() );
		}catch( BusinessException e ){
			throw e ;
		}catch(Exception e){
			logger.error("文件保存失败!" , e );
			throw new BusinessException("文件保存失败!") ;
		}
		Map<String,Object> result = new HashMap<String,Object>() ;
		result.put("fileName", fileName );
		result.put("filePath", fileManager.getUploadURL( moduleDir , fileName) ) ;
		return success( result );
	}
}
