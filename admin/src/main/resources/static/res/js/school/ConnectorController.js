define([] , function(){
	var controller = function( $scope , connectorService ){
		$scope.udata = {} ;
		$scope.uFormMeta = [[{
			field  	:	'masterName' ,
			label 	: 	'主管校长姓名' ,
			validation 	:	{
				required 	:	true ,
				maxlength 	: 	20
			},
			className 	: 	'col-xs-12'
		}],[{
			field  	:	'masterPhone' ,
			label 	: 	'主管校长手机号' ,
			validation 	:	{
				required 	:	true ,
				express 	: 	'^[0-9]{11}$'
			},
			className 	: 	'col-xs-12'
		}],[{
			field  	:	'viceName' ,
			label 	: 	'副主管校长姓名' ,
			validation 	:	{
				required 	:	true ,
				maxlength 	: 	20
			},
			className 	: 	'col-xs-12'
		}],[{
			field  	:	'vicePhone' ,
			label 	: 	'副主管校长手机号' ,
			validation 	:	{
				required 	:	true ,
				express 	: 	'^[0-9]{11}$'
			},
			className 	: 	'col-xs-12'
		}],[{
			field  	:	'reportName' ,
			label 	: 	'填报员姓名' ,
			validation 	:	{
				required 	:	true ,
				maxlength 	: 	20
			},
			className 	: 	'col-xs-12'
		}],[{
			field  	:	'reportPhone' ,
			label 	: 	'填报员手机号' ,
			validation 	:	{
				required 	:	true ,
				express 	: 	'^[0-9]{11}$'
			},
			className 	: 	'col-xs-12'
		}]] ;
		var connectorHandler = {
			init 	:	 function(){
				var _this = this ;
				this.modal = '#data-edit-modal' ;
				this.form='#data-edit-form' ;
				if( !$(this.form).length )
					return false ;
				this.validator = $(this.form).validate( this.getFormRule( $scope.uFormMeta ) ) ;
				$(this.modal).delegate('[name=data-positive-btn]' , 'click' , function(){
					_this.saveData() ;
				});
				$(this.modal).delegate('[name=data-native-btn]' , 'click' , function(){
					_this.resetForm() ;
				});
				this.viewRend() ;
				return this.resetForm() ;
			},
			viewRend 		: 	function(){
				var _this = this ;
				this.loadDetail( function(){
					$scope.$apply() ;
				});
				return this ;
			},
			loadDetail 		: 	function( callback ){
				connectorService.get( function( data ){
					$scope.udata = data ;
					callback() ;
				});
			},
			getFormRule 	:	function( meta ){
				var rule = 	{
					onfocusout 	: 	function( e ){
						$(e).valid() ;
					},
					errorPlacement 	: 	function( error , e ){
						error.appendTo( $(e).parents('form').find('[data-for='+$(e).attr('name')+']')) ;
					},
					success: 	function( error , e ){
					},
					rules 		: 	{}
				};
				if( !meta ) return rule ;
				meta.forEach( function( d , k ){
					d.forEach( function( sd , sk ){
						rule.rules[sd.field] =  sd.validation ;
					});
				});
				return rule ;
			},
			resetForm : 	function(){
				this.validator.resetForm() ;
				$scope.udata = {} ;
				return this.viewRend() ;
			},
			saveData 	: 	function(){
				if( !this.validator.form() )
					return ;
				var _this = this ;
				connectorService.update( $scope.udata , function(){
					mAlert('联系人信息更新成功!') ;
					return _this.resetForm() ;
				});
			}
		}
		
		//监听式初始化页面.
		!function(){
			var initStack = [ function(){
				return connectorHandler.init() ;
			}] ;
			function watchInit( exp , wait ){
				setTimeout( function(){
					initStack.forEach( function( d , k ){
						if( d() ) initStack.splice( k , 1 ) ;
					});
					if( initStack.length && exp > 0 ) 
						watchInit( exp -= wait , wait ) ;
				},wait);
			} ;
			watchInit(10000 , 100) ;
		}() ;
	}
	
	var service = function( ctx ){
		this.get = function(  callback , complete ){
			$.ajax({
				url  	: 	ctx.api + 'school/connector' ,
				type 	: 	'get' ,
				success : 	function( data ){
					if( data.code != 0 )
						return mAlert( data.message ) ;
					callback( data.data ) ;
				},
				complete 	: 	function(){
					if ( typeof complete == 'function' )
						complete( ) ;
				}
			});
		}
		
		this.update = function( param , callback , complete ){
			$.ajax({
				url  	: 	ctx.api + 'school/connector' ,
				type 	: 	'put' ,
				data 	: 	$.toJSON( param ) ,
				success : 	function( data ){
					if( data.code != 0 )
						return mAlert( data.message ) ;
					callback( data.data ) ;
				},
				complete 	: 	function(){
					if ( typeof complete == 'function' )
						complete( ) ;
				},
				contentType 	: 	'application/json;charset=UTF-8'
			});
		}
	}
	app.register.service('connectorService' , service ) ;
	return controller ;
});