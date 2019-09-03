define([],function(){
	var service = function( ctx ){
		this.findDict = function( type , param , success , error , complete ){
			$.ajax({
				url  	: 	ctx.api + 'dict/'+type ,
				type 	: 	'get' ,
				dataType: 	'json' ,
				data 	: 	param ,
				success : 	function( result ){
					if( result.code != 0 )
						return mAlert( result.message ) ;
					success( result.data ) ;
				},
				error 	: 	function( code  ){
					if( typeof error == 'function' )
						return error( code ) ;
					return mAlert('服务器请求出错,请重试!') ;
				},
				complete: 	function( ){
					if( typeof complete == 'function' )
						complete() ;
				}
			})
		}
	}
	app.register.service('dictService' , service ) ;
	return service ;
})