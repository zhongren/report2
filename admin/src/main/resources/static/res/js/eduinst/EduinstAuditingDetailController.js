define(['app/sys/CityService' , 'app/sys/DictService'] , function(  ){
	var controller = function( $scope , ctx , $stateParams , eduinstAuditingDetailService , cityService , dictService ){
		$scope.view = {
			audit 	:  	false ,
			submit 	: 	false 
		}
		$scope.quotaList = [] ;
		$scope.auditData = {
			'auditNote' 	: 	''
		} ;
		var detailHandler = {
			init 	: 	function(){
				var _this = this ;
				this.view = '#reporting-audit-detail' ;
				this.eduinstId = $stateParams['eduinstId'] ;
				if( !/^[0-9]+/.test( this.eduinstId ) ){
					return this.viewRend() ;
				}
				this.loadDetailData( ) ;
				 
				$( this.view).delegate('[data-audit]' , 'click' , function(){
					var type = $(this).attr('data-audit') ;
					mConfirm('是否'+(type == 'pass' ? '通过审核' : '驳回修改') + '?' , function(){
						_this.audit( type ) ;
					});
				});
				
				$( this.view).delegate('[data-submit]' , 'click' , function(){
					mConfirm('是否确认提交审核?' , function(){
						_this.submitAuditing( ) ;
					});
				});
				
				$(this.view).delegate('[data-back]' , 'click' , function(){
					location.href='#/eduintAuditing'
				});
				
				return this ;
			},
			loadDetailData 	: 	function(){
				var _this = this ;
				eduinstAuditingDetailService.getAuditingDetial( this.eduinstId , function( data ){
					console.log( data ) ;
					_this.viewRend( data ) ;
				});
			},
			viewRend 		: 	function( data ){
				data = !data ? {} : data ;
				$scope.view.audit = data['audit'] ;
				$scope.view.submit = data['submit'] ;
				if( !data['quota'] || ! (data['quota'] instanceof Array) )
					data['quota'] = [] ;
				$scope.quotaList = data['quota'] ;
				$scope.$apply() ;
				return this ;
			},
			audit 		: 	function( type ){
				var _this = this ;
				$scope.auditData['status'] = type == 'pass' ? 1 : 2;
				eduinstAuditingDetailService.audit( this.eduinstId , $scope.auditData , function( ){
					//_this.loadDetailData( ) ;
					mAlert('操作成功!') ;
					location.href='#/eduinstAuditing' ;
				});
			},
			submitAuditing 	: 	function(){
				eduinstAuditingDetailService.submitAuditing( this.eduinstId , function(){
					//_this.loadDetailData( ) ;
					mAlert('操作成功!') ;
					location.href='#/eduinstAuditing'
				});
			}
		}
		
		!function(){
			var initStack = [ function(){
				return detailHandler.init() ;
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
		this.getAuditingDetial = function( eduinstId , callback , complete ){
			$.ajax({
				url 	: 	ctx.api +'eduinstAuditing/'+eduinstId+'/reporting', 
				type 	: 	'get' ,
				success : 	function( result ){
					if( result.code != 0 )
						return mAlert( result.message ) ;
					callback( result.data ) ;
				}
			});
		}
		
		this.audit = function( eduinstId , param , callback , complete ){
			$.ajax({
				url 	: 	ctx.api +'eduinstAuditing/'+eduinstId+'/audit', 
				type 	: 	'put' ,
				data 	: 	$.toJSON( param ) ,
				success : 	function( result ){
					if( result.code != 0 )
						return mAlert( result.message ) ;
					callback( result.data ) ;
				},
				contentType 	: 	'application/json;charset=UTF-8'
			});
		}
		
		this.submitAuditing 	= 	function( eduinstId , callback , complete ){
			$.ajax({
				url 	: 	ctx.api +'eduinstAuditing/'+eduinstId+'/submitAuditing', 
				type 	: 	'put' ,
				success : 	function( result ){
					if( result.code != 0 )
						return mAlert( result.message ) ;
					callback( result.data ) ;
				},
				contentType 	: 	'application/json;charset=UTF-8'
			});
		}
	}
	app.register.service('eduinstAuditingDetailService' , service ) ;
	return controller ;
});