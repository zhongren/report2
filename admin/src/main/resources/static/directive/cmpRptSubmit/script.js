define([] , function(){
	var controller = function( $scope , $element , ctx ){
		$scope.view = {
			state 	: 	'hide' ,
			text 	: 	'加载中...' ,
		} 
		var submitHandler = {
			init 	:	function(){
				var _this = this ;
				$($element).unbind('click' ) ;
				this.gerProcess( function( data ){
					_this.resetStateBtn( data ) ;
				});
				return this ;
			},
			resetStateBtn 	: 	function( data ){
				var _this = this ;
				if( !data ) return ;
				var totalNum = data['totalNum'] ? data['totalNum'] : 0 ,
						finishNum = data['finishNum'] ? data['finishNum'] : 0 ;
				if( 0 == data['status'] && data['totalNum'] > data['finishNum'] ){
					$scope.view = {
						state 	: 	'show' ,
						text 	: 	'填报中('+totalNum+'/'+finishNum+')' 
					}
					return $scope.$apply() ;
				}else if( [0,4].indexOf( data['status'] ) != -1  && data['totalNum'] <= data['finishNum'] ){
					$scope.view = {
						state 	: 	'show' ,
						text 	: 	'提交审核('+totalNum+'/'+finishNum+')'
					}
					$($element).bind('click' , function(){
						mConfirm('是否确认提交审核?' , function(){
							_this.submit( function(){
								mAlert('提交成功!') ;
								//刷新状态
								submitHandler.init() ;
							});
						});
					});
					return $scope.$apply() ;
				}else if( 1 == data['status'] ){
					$scope.view = {
						state 	: 	'show' ,
						text 	: 	'已提交审核'
					}
					return $scope.$apply() ;
				}else if( 2 == data['status'] ){
					$scope.view = {
						state 	: 	'show' ,
						text 	: 	'审核中'
					}
					return $scope.$apply() ;
				}else if( 3 == data['status'] ){
					$scope.view = {
						state 	: 	'show' ,
						text 	: 	'审核完成'
					}
					return $scope.$apply() ;
				}
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
			invalid : 	function( error ){
				var format = "<p style='font-size:13px'>指标:[{quota1}/{quota2}]<br>采集项:{title}<br><font color=red>错误信息:“{message}”</font><br></p>" ;
				var messages = '' ;
				error.forEach( function( d , k ){
					var regxp = /\{\s*([a-zA-Z0-9_]+)\s*\}/g , group , cpFormat = format ;
					while( ( group = regxp.exec( format ) ) ){
						var value = typeof d[group[1]] == 'undefined' ? '' :  d[group[1]] ;
						cpFormat = cpFormat.replace( group[0] , value ) ;
					}
					messages += cpFormat ;
				});
				return mAlert( messages ) ;
			},
			submit	:	function( callback ){
				var _this = this ;
				$.ajax({
					url 	: 	ctx.api + 'collection/submitAudit' ,
					type 	: 	'put' ,
					success : 	function( rs ){
						if( rs.code == 2 )
							return _this.invalid( eval('(' +rs.message+ ')') ) ;
						
						if( rs.code != 0 )
							return mAlert( rs.message ) ;
						callback() ;
					},
					complete : 	function(){
						
					},
					contentType 	: 	'application/json;charset=UTF-8'
				});
			}
		};
		//监听加载事件
		$scope.$watch('trigger' , function(){
			submitHandler.init() ;
		},true);
	};
	app.register.directive('cmpRptSubmit' , function( ctx ){
		return {
			restrict  	: 	'E' ,
			replace 	: 	true ,
			templateUrl : 	ctx.base + 'directive/cmpRptSubmit/template.html' ,
			controller 	: 	controller ,
			scope 		: 	{
				trigger 	: 	'=trigger'
			}
		}
	});
	
});