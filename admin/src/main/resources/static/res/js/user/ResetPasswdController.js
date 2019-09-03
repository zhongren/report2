define([] , function(){
	var controller = function( $scope , passwdService ){
		$scope.udata = {} ;
		$scope.uFormMeta = [[{
			field  	:	'passwd' ,
			label 	: 	'原密码' ,
			validation 	:	{
				required 	:	true ,
				express 	: 	'^[0-9a-zA-Z_]{6,20}$'
			},
			className 	: 	'col-xs-12'
		}],[{
			field  	:	'newPasswd' ,
			label 	: 	'新密码' ,
			validation 	:	{
				required 	:	true ,
				express 	: 	'^[0-9a-zA-Z_]{6,20}$'
			},
			className 	: 	'col-xs-12'
		}],[{
			field  	:	'rePasswd' ,
			label 	: 	'确认密码' ,
			validation 	:	{
				required 	:	true ,
				equalTo 	: 	'[name=newPasswd]' ,
				express 	: 	'^[0-9a-zA-Z_]{6,20}$'
			},
			className 	: 	'col-xs-12'
		}]] ;
		var resetHandler = {
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
				return this.resetForm() ;
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
				$scope.$apply() ;
				return this ;
			},
			saveData 	: 	function(){
				if( !this.validator.form() )
					return ;
				var _this = this ;
				passwdService.update( $scope.udata , function(){
					mAlert('密码修改成功!') ;
					return _this.resetForm() ;
				});
			}
		}
		
		//监听式初始化页面.
		!function(){
			var initStack = [ function(){
				return resetHandler.init() ;
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
		this.update = function( param , callback , complete ){
			$.ajax({
				url  	: 	ctx.api + 'user/resetPasswd' ,
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
	app.register.service('passwdService' , service ) ;
	return controller ;
});