define([],function(){
	var controller = function($scope , ctx , forgetPasswdService ){
		var FormValid = function( form , validation ){
			this.rules = {
				onfocusout 	:	function( e ){
					$(e).valid() ;
				},
				errorPlacement 	: 	function( error , e ){
					error.appendTo( $(form).find('[data-for='+$(e).attr('name')+']') ) ;
					$(e).parents('.input-group').removeClass('has-success').addClass('has-error') ;
				},
				success : 	function( error , e ){
					 $(form).find('[data-for='+$(e).attr('name')+']').empty() ;
					$(e).parents('.input-group').removeClass('has-error').addClass('has-success') ;
				},
				rules 	: 	{} 
			}
			if( validation && validation['rules'])
				this.rules['rules'] = validation['rules'] ;
			this.validator = $(form).validate( this.rules ) ;
			this.resetForm = function(){
				if( !$(form).length )
					return this ;
				
				if( this.validator ) 
					this.validator.resetForm() ;
				$(form).find('.has-error').removeClass('.has-error') ;
				$(form).find('.has-success').removeClass('.has-success') ;
				$(form).find('.knit-error').empty() ;
				$(form)[0].reset() ;
				return this ;
			}
			this.resetForm() ;
			this.form = function(){
				if( this.validator && this.validator.form )
					return this.validator.form() ;
				return true ;
			}
		}
		
		$scope.passwdData = {} ;
		var initHandler = {
			init 	: 	function(){
				var _this = this ;
				this.form = '[name=forget-passwd-form]' ;
				$( this.form ).delegate('[data-role=submit-btn]' , 'click' , function(){
					_this.doInit( _this.data['username'] ) ;
				});
				this.data = $scope.passwdData ;
				this.formValid = new FormValid( this.form , {
					rules  	: 	{
						username 	: 	{
							required 	: 	true ,
							maxlength 	: 	50
						}
					}
				});
				return this ;
			},
			resetView 	: 	function(){
				this.formValid.resetForm() ;
				return this ;
			},
			doInit 	: 	function( username ){
				var _this = this ;
				if( !this.formValid.form() )
					return false ;
				mConfirm('是确认否初始化账号['+username+']的密码?' , function(){
					forgetPasswdService.initPasswd( username , function( data ){
						mAlert('密码初始化成功!') ;
						return _this.resetView() ;
					});
				}) ;
			}
		}.init() ;
	}
	
	var service = function( ctx ){
		this.initPasswd = function( username , callback ){
			if( !username || /^\s*$/.test( username ) )
				return mAlert('请输入登陆账号!') ;
			$.ajax({
				url  	: 	ctx.api + 'user/'+username+'/resetPasswd' ,
				type 	: 	'put' ,
				success : 	function( result ){
					if( result.code != 0 )
						return mAlert( result.message ) ;
					callback( result.data ) ;
				},
				contentType 	: 	'application/json;charset=UTF8'
			});
		}
	}
	app.register.service('forgetPasswdService' , service ) ;
	
	return controller ;
});