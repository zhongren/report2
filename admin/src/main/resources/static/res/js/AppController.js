//页面全局处理，菜单，用户信息等加载 .
define([] , function(){
	var application = function( app ){
		app.controller('appCtrl' ,function( $scope , appService ){
			$scope.context = {
				'path' 	: 	'/'
			}
			$scope.eventHandler = {
				collectionSubmitTrigger 	: 	true  ,
				finishQuota 	:	function( quotaId ){
					if( !$scope.quotas || !$scope.quotas.length )
						return ;
					$scope.quotas.forEach( function( d , k ){
						if( ( !d.subMenus || !d.subMenus.length ) && d['id'] == quotaId  )
							d['dot'] = '已完成' ;
						else
							d.subMenus.forEach( function( sd , sk ){
								if( sd['id'] == quotaId )
									sd['dot'] = '已完成' ;
							});
					});
					ngUtil.apply( $scope ) ;
				}
			}
			$scope.appEvent = {
				//保存填报,触发填报提交按钮刷新 .
				onSaveCollection 	: 	function(){
					$scope.eventHandler['collectionSubmitTrigger'] = !$scope.eventHandler['collectionSubmitTrigger'] ;
				}
			}
			//用户登陆信息 .
			$scope.principal = {} ; 
			$scope.noticeMenu=[{
				name 	: 	'消息列表' ,
				url 	: 	'#/bulletin' ,
				dot 	: 	10
			}];
			$scope.menus = [];
			
			$scope.fixedCollection = [{
				name  	: 	'上传自评' 
			},{
				name 	: 	'绝对班额'
			}];
			
			var quotaHandler = {
				init 	: 	function(){
					return this ;
				},
				loadData: 	function( ){
					var _this = this ;
					appService.getQuota( function( data ){
						$scope.quotas = _this.handleData( data ) ;
						$scope.$apply() ;
					});
					return this ;
				},
				handleData 	: 	function( data ){
					if( !data || !data.length )
						return ;
					var quotas = [] ;
					data.forEach( function( v , k ){
						var quota1 = {
							id 		: 	v['id'] ,
							name 	: 	v['name'] 
						};
						if( v.subQuota && v.subQuota.length ){
							quota1['subMenus'] = [] ;
							v.subQuota.forEach( function( sv , sk ){
								var subQuota = {
									id 		: 	sv['id'] ,
									name 	: 	sv['name'] ,
									url	: 	'#/school/'+sv.id+'/collection?name='+sv['name'] 
								};
								if( sv['finished'] )
									subQuota['dot'] = '已完成' ;
								quota1['subMenus'].push( subQuota ) ;
							});
						}
						quotas.push( quota1 ) ;
					});
					return quotas ;
				}
			}.init().loadData() ;
			
			var menuHandler = 	{
				init 	: 	function(){
					return this ;
				},
				loadData	: 	function(){
					var _this = this ;
					appService.getMenu( function( data ){
						$scope.menus = _this.handleData( data ) ;
						$scope.$apply() ;
					});
				},
				handleData : 	function( data ){
					return data ;
				}
			}.init().loadData() ;
			
			var logoutHander = {
				init 	: 	function(){
					var _this = this ;
					$('body').delegate('#logout-btn' , 'click' , function(){
						_this.logout() ;
					});
					return this ;
				},
				logout 	: 	function(){
					var _this = this ;
					mConfirm('是否退出系统?' , function(){
						appService.logout( function(){
						},function(){
							location.href='login.html' ;
						});
					});
				}
			}.init() ;
			
			var principalHandler = {
				init 	: 	function(){
					return this.loadData() ;
				},
				loadData : 	function(){
					appService.getPrincipal( function( data ){
						$scope.principal = data ;
						$scope.$apply() ;
					});
					return this ;
				}
			}.init() ;
		});
		//请求接口数据 .
		app.service('appService' , ['ctx' , function( ctx ){
			this.getQuota = function( callback , complete ){
				$.ajax({
					url  	: 	ctx.api + 'collection/quotas?process=true' ,
					type 	: 	'get' ,
					cached	: 	false ,
					success : 	function( result ){
						console.log( result )
						if( result.code != 0 )
							return mAlert( result.message ) ;
						callback( result.data ) ;
					},
					complete: 	function(){
						if( typeof complete == 'function' )
							complete() ;
					}
				})
			}
			
			this.getPrincipal = function( callback , complete ){
				$.ajax({
					url  	: 	ctx.api + 'user/principal' ,
					type 	: 	'get' ,
					cached	: 	false ,
					success : 	function( result ){
						if( result.code != 0 )
							return mAlert( result.message ) ;
						callback( result.data ) ;
					},
					complete: 	function(){
						if( typeof complete == 'function' )
							complete() ;
					}
				})
			}
			
			this.getMenu = function( callback , complete ){
				$.ajax({
					url  	: 	ctx.api + 'menu' ,
					type 	: 	'get' ,
					cached	: 	false ,
					success : 	function( result ){
						console.log( result )
						if( result.code != 0 )
							return mAlert( result.message ) ;
						callback( result.data ) ;
					},
					complete: 	function(){
						if( typeof complete == 'function' )
							complete() ;
					}
				})
			}
			
			this.logout = function( callback , complete ){
				$.ajax({
					url  	: 	ctx.api + 'user/logout' ,
					type 	: 	'get' ,
					cached	: 	false ,
					success : 	function( result ){
						callback( result.data ) ;
					},
					complete: 	function(){
						if( typeof complete == 'function' )
							complete() ;
					}
				})
			}
		}]);
	}
	return {
		init 	: 	function( app ){
			application.call( this , app ) ;
		}
	}
});