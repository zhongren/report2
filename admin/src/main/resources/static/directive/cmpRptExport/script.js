define([] , function(){
	var controller = function( ctx , $scope , $element ){
		var exportSource = {
			'SCHOOL' 	: 	ctx.api + 'schoolAuditing/reportingData/0/export' ,
			'EDUINST' 	: 	ctx.api + 'eduinstAuditing/reportingData/0/export' ,
		}
		$scope.view = {
			state 	: 	'hide'  
		}
		var submitHandler = {
			init 	:	function(){
				var _this = this ;
				if( $scope.type != 'SCHOOL' )
					return this ;
				
				$($element).bind('click' , function(){
					_this.submit();
				});
				this.gerProcess( function( data ){
					_this.resetStateBtn( data ) ;
				});
				return this ;
			},
			resetStateBtn 	: 	function( data ){
				console.log( data )
				var _this = this ;
				$scope.view['state'] = data ? 'show' : 'hide' ;
				return $scope.$apply() ;
			},
			gerProcess 	: 	function( callback ){
				$.ajax({
					url 	: 	ctx.api + 'collection/getReportingProcess' ,
					type 	: 	'get' ,
					success : 	function( rs ){
						if( rs.code != 0 )
							return mAlert( rs.message ) ;
						callback( rs.data ) ;
					}
				});
			},
			submit :	function( callback ){
				 var url = exportSource[$scope.type] ;
				 if( !url ){
					 return mAlert('数据导出失败!') ;
				 }
	             var a = document.createElement('a');
	             a.href = url;
	             $("body").append(a);    // 修复firefox中无法触发click
	             a.click();
	             $(a).remove();
			}
		}.init() ;
	}
	//注册组件 .
	app.register.directive('cmpRptExport' , function( ctx ){
		return {
			restrict  	: 	'E' ,
			replace 	: 	true ,
			templateUrl : 	ctx.base + 'directive/cmpRptExport/template.html' ,
			scope 		: 	{
				type 	: 	'=type'
			},
			controller 	: 	controller
		}
	})
});