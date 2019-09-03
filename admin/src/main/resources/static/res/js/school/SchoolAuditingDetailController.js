define(['app/sys/CityService' , 'app/sys/DictService' , 'directive/collectionDisplay/script' ,
        'directive/classesNums/script'] , function(  ){
	var controller = function( $scope , ctx , $stateParams , schoolAuditingDetailService , cityService , dictService ){
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
				this.schoolId = $stateParams['schoolId'] ;
				if( !/^[0-9]+/.test( this.schoolId ) ){
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
					location.href='#/schoolAuditing'
				});
				
				return this ;
			},
			loadDetailData 	: 	function(){
				var _this = this ;
				schoolAuditingDetailService.getAuditingDetial( this.schoolId , function( data ){
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
				this.handleResult( data['quota'] ) ;
				ngUtil.apply($scope , function(){
					$scope.quotaList = data['quota'] ;
				});
				return this ;
			},
			handleResult : 	function( quotaList ){
				var _this = this ;
				quotaList.forEach( function(d , k){
					if( !d['collections'] || !d['collections'].length )
						return ;
					d['collections'].forEach( function( sd , sk){
						sd['schoolId'] = _this.schoolId ;
						if( ['img' , 'file'].indexOf( sd['valueType']) != -1 )
							sd['content'] = eval('(' + sd['content'] +')') ;
					});
				});
			},
			audit 		: 	function( type ){
				var _this = this ;
				$scope.auditData['status'] = type == 'pass' ? 1 : 2;
				schoolAuditingDetailService.audit( this.schoolId , $scope.auditData , function( ){
					//_this.loadDetailData( ) ;
					mAlert('操作成功!') ;
					location.href='#/schoolAuditing' ;
				});
			},
			submitAuditing 	: 	function(){
				schoolAuditingDetailService.submitAuditing( this.schoolId , function(){
					//_this.loadDetailData( ) ;
					mAlert('操作成功!') ;
					//返回列表页
					location.href='#/schoolAuditing' ;
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
		this.getAuditingDetial = function( schoolId , callback , complete ){
			$.ajax({
				url 	: 	ctx.api +'schoolAuditing/'+schoolId+'/reporting', 
				type 	: 	'get' ,
				success : 	function( result ){
					if( result.code != 0 )
						return mAlert( result.message ) ;
					callback( result.data ) ;
				}
			});
		}
		
		this.audit = function( schoolId , param , callback , complete ){
			$.ajax({
				url 	: 	ctx.api +'schoolAuditing/'+schoolId+'/audit', 
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
		
		this.submitAuditing 	= 	function( schoolId , callback , complete ){
			$.ajax({
				url 	: 	ctx.api +'schoolAuditing/'+schoolId+'/submitAuditing', 
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
	app.register.service('schoolAuditingDetailService' , service ) ;
	return controller ;
});