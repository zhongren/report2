package com.newproj.core.filter;

public class FilterConfigurationProperties {
	private boolean enabledEncrypt = true ; //启用请求响应数据加密 .

	public boolean isEnabledEncrypt() {
		return enabledEncrypt;
	}

	public void setEnabledEncrypt(boolean enabledEncrypt) {
		this.enabledEncrypt = enabledEncrypt;
	}
	
}
