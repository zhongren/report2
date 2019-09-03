package com.newproj.core.rest.support;

public class ResultModal {
		private int code;
		private String message;
		private Object extra ;
		private Object data ;
		private long timestamp ;

		public ResultModal(int code, String message) {
			this.code = code;
			this.message = message;
		}
		
		public ResultModal( int code , String message , Object data , Object extra ){
			this.code = code ;
			this.message = message ;
			this.data = data ;
			this.extra = extra ;
		}
		
		public ResultModal(int code, String message , Object extra ) {
			this.code = code;
			this.message = message;
			this.extra = extra ;
		}
		
		public ResultModal() {
			this.code = StatusCode.SUCCESS.getCode();
			this.message = StatusCode.SUCCESS.getMessage();
		}

		public int getCode() {
			return code;
		}

		public Object getExtra() {
			return extra;
		}

		public void setExtra(Object extra) {
			this.extra = extra;
		}

		public long getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(long timestamp) {
			this.timestamp = timestamp;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public Object getData() {
			return data;
		}

		public void setData(Object data) {
			this.data = data;
		}
		
}
