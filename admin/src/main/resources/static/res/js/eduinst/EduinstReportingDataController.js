define(['app/sys/CityService' , 'app/sys/DictService' , 'directive/collectionDisplay/script'] , function(  ){
	var controller = function( $scope , ctx , $stateParams , eduinstReportingDataService , cityService , dictService ){
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
				return this ;
			},
			loadDetailData 	: 	function(){
				var _this = this ;
				eduinstReportingDataService.getReportingData( this.eduinstId , function( data ){
					console.log( data ) ;
					_this.handleResult( data['quota'] ) ;
					_this.viewRend( data ) ;
				});
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
			viewRend 		: 	function( data ){
				data = !data ? {} : data ;
				$scope.view.audit = data['audit'] ;
				$scope.view.submit = data['submit'] ;
				if( !data['quota'] || ! (data['quota'] instanceof Array) )
					data['quota'] = [] ;
				$scope.quotaList = data['quota'] ;
				console.log('指标列表:' , $scope.quotaList ) ;
				$scope.$apply() ;
				return this ;
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
		this.getReportingData = function( eduinstId , callback , complete ){
			$.ajax({
				url 	: 	ctx.api +'eduinstAuditing/'+eduinstId+'/reportingData', 
				type 	: 	'get' ,
				success : 	function( result ){
					if( result.code != 0 )
						return mAlert( result.message ) ;
					callback( result.data ) ;
				}
			});
		}
	}
	app.register.service('eduinstReportingDataService' , service ) ;
	return controller ;
});