define([],function(  ){
	var service = function( ctx ){
		this.findBulletin = function( callback ){
			$.ajax( {
				url  	: 	ctx.api + 'bulletin?page=1&size=5&status=1&orderField=publishTime&orderType=DESC' ,
				success : 	function( result ){
					if( result.code != 0 )
						return mAlert( result.message ) ;
					callback( result.data ) ;
				}
			});
		}
	}
	app.register.service('noticeService' , service ) ;
	
	var controller = function( ctx , $scope , noticeService ){
		$scope.notice = [] ;
		$scope.process = '数据加载中...' ;
		var dashboardHandler = {
			init 	: 	function(){
				return this.loadBulletin() ;
			},
			loadBulletin 	: 	function(){
				$scope.notice = [] ;
				noticeService.findBulletin( function( result ){
					ngUtil.apply( $scope , function(){
						$scope.notice = result ;
						if( !result || !result.length )
							$scope.process = '暂无数据' ;
					});
				});
				return this ;
			}
		}.init() ;
	}
	
	//注册组件。
	app.register.directive('cmpNotice' , function( ctx ){
		return {
			restrict 	: 	'E' ,
			replace 	: 	true ,
			scope 		: 	{ },
			templateUrl : 	ctx.base + 'directive/cmpNotice/template.html' ,
			controller 	: 	controller 
		}
	});
	
});