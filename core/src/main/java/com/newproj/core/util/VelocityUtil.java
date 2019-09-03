package com.newproj.core.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class VelocityUtil {
	static {
		Properties p = new Properties();
		// 设置输入输出编码类型。和这次说的解决的问题无关
		p.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
		p.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
		// 文件缓存
		p.setProperty(Velocity.FILE_RESOURCE_LOADER_CACHE, "false");
		// 这里加载类路径里的模板而不是文件系统路径里的模板
		p.setProperty("file.resource.loader.class",
				"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		Velocity.init(p);
	}

	public static Template getTemplateInstance(String templtePath) {

		Template template = Velocity.getTemplate(templtePath);
		return template;
	}

	/**
	 * 下载文件
	 * @param fileName    文件名
	 * @param templatePath   模板路径
	 * @param context   VelocityContext
	 * @param response  
	 * @throws IOException
	 */
	public static void download(String fileName, String templatePath, Context context,HttpServletRequest request , HttpServletResponse response)
			throws IOException {
		Template template = VelocityUtil.getTemplateInstance(templatePath);
		Writer writer = new StringWriter();
		template.merge(context, writer);
		response.reset();
		// 设置response的Header
		byte[] result = writer.toString().getBytes("UTF-8") ;
		response.addHeader("Content-Disposition", "attachment;filename=" + new String( fileName.getBytes("gb2312"), "ISO8859-1" ));
		response.addHeader("Content-Length", "" + result.length);
		response.setContentType("application/octet-stream;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
		toClient.write( result );
		toClient.flush();
		toClient.close();
		
	}

}
