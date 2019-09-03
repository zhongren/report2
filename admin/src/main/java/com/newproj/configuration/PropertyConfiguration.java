package com.newproj.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties( prefix="api.config" , ignoreInvalidFields=false , ignoreUnknownFields = true )
public class PropertyConfiguration {

	//文件服务器根路径（保存路径:根路径/相对路径/文件目录文件名）
	private String fileUploadPath 	= "D:/workspace/tools/nginx11111/nginx_1.11.11/html/" ;
	//文件保存相对路径
	private String fileUploadMapping ="/upload/server/" ;
	//文件访问域名(访问路径:域名/相对路径/文件目录文件名)
	private String fileServerDomain = "http://localhost/" ;
	
	public String getFileUploadPath() {
		return fileUploadPath;
	}
	public void setFileUploadPath(String fileUploadPath) {
		this.fileUploadPath = fileUploadPath;
	}
	public String getFileUploadMapping() {
		return fileUploadMapping;
	}
	public void setFileUploadMapping(String fileUploadMapping) {
		this.fileUploadMapping = fileUploadMapping;
	}
	public String getFileServerDomain() {
		return fileServerDomain;
	}
	public void setFileServerDomain(String fileServerDomain) {
		this.fileServerDomain = fileServerDomain;
	}
	
}
