define(['directive/cmpRptExport/script' ,'directive/groupInput/script', 'directive/classImport/script' ,'directive/cmpRptSubmit/script'] , function(){
	var controller = function( $scope , ctx , $stateParams , schoolCollectionService , $location ){
		$scope.view = {
			editable 	: 	true ,
			positiveBtn 	: 	{
				text 		: 	'保存数据' ,
				disabled 	: 	false ,
			},
			nativeBtn		: 	{
				text 		: 	'重置数据',
				disabled 	: 	false,
			},
			quotaTitle 	: 	$location.search()['name']
		};
		
		$scope.formContext = {} ;
		var collectionHandler = {
			rule 	: 	{
				onfocusout 	: 	function( e ){
					$(e).valid() ;
				},
				errorPlacement 	: 	function( error , e ){
					$(e).parents('.form-group').removeClass('has-success').addClass('has-error') ;
					error.appendTo( $(e).parents('form').find('[data-for='+$(e).attr('name')+']')) ;
				},
				success: 	function( error , e ){
					error.remove();
					$(e).parents('.form-group').removeClass('has-error').addClass('has-success') ;
				},
				rules 		: 	{},
				messages 	: 	{}
			},
			init 	: 	function( ){
				this.quotaId = $stateParams.quotaId ;
				this.form = '#data-create-form' ;
				var _this = this ;
				_this.loadCollection( function( data ){
					_this.initValidation( data ) ;
				});
				$('#data-save-btn').bind('click' , function(){
					_this.saveCollection() ;
				});
				$('#data-clear-btn').bind('click' , function(){
					_this.loadCollection() ;
				});
				return this ;
			},
			initValidation 	: 	function( data ){
				var _this = this ;
				data.forEach( function( v , k ){
					if( !v['fieldMeta'] || !v['fieldMeta'].validation || !v['fieldMeta'].validation.rules ){
						return ;
					}
					_this.rule.rules [v['fieldMeta']['name']] = v['fieldMeta'].validation.rules ;
					_this.rule.messages [v['fieldMeta']['name']] = v['fieldMeta'].validation.messages ;
				});
				this.validation = $(this.form).validate( this.rule ) ;
				this.validation.resetForm() ;
				$(this.form)[0].reset() ;
				return this ;
			},
			loadCollection 	: 	function( callback ){
				var _this = this ;
				schoolCollectionService.loadCollection( this.quotaId , function( data ){
					$scope.collections = _this.handleResult( data ) ; 
					setTimeout( function(){ //解决数据无法及时渲染到页面问题 .
						$scope.$apply() ;
					},1);
					if( typeof callback == 'function')
						callback( $scope.collections ) ;
				});
				return this ;
			},
			handleResult 	: 	function( data ){
				var _this = this ;
				var collections = [] ;
				if( !data || !data.length )
					return collections ;
				data.forEach( function( v , k ){
					var validation = v['validation'] ? eval('(' + v['validation'] + ')') : {}  , 
							option = v['valueOpt'] ? eval('(' + v['valueOpt'] + ')') : {}  , 
					value = null ;
					try{
						value =  $.parseJSON( v['content'] ) ;
					}catch(e){
						value = v['content'] ;
					};
					
					//Script validation was server validate .
					/*if( validation && validation['rules'] && validation['rules']['script'] )
						delete validation['rules']['script'] ;
					
					if( validation && validation['rules']  ){
						for( var key in validation['rules'] ){
							if( typeof validation['rules'][key] != 'string' )
								continue ;
							if( /^(@[0-9]+)(,@[0-9]+)*,?$/.test( validation['rules'][key] ) )
								delete validation['rules'][key] ;
						}
					}*/
					var collection = {
						title 	: 	v['title'] ,
						id 		: 	v['id'] ,
						quotaId	: 	_this.quotaId ,
						fieldMeta 	: 	{
							name 	: 	'title_'+v['id'] ,
							validation 	: 	validation ,
							type 	: 	v['valueType'] ,
							note    :   v['note'],
							option 	: 	option 
						},
						data 		: 	{
							value 	: 	value ,
							id 		: 	v['id'] ,
						}
					}
					collections.push( collection ) ;
				});
				return collections ;
			},
			saveCollection 	: 	function(){
				var _this = this ;
				
				var extraValid = true , formValid = this.validation.form() ;
				if( $scope.formContext['valid'] )
					$scope.formContext['valid'].forEach( function( d , k ){
						if( !d() )
							extraValid = false ;
					});
				if( !extraValid || !formValid )
					return ;
				
				var dataList = [] ;
				$scope.collections.forEach( function( v , k ){
					dataList.push({
						content 	: 	typeof v.data['value'] == 'object' || v.data['value'] instanceof Array ? $.toJSON( v.data['value'] ) : v.data['value'],
						collectionId: 	v.data['id']
					});
				});
				$scope.view.positiveBtn = {
					text 	: 	'数据保存中...' 	,
					disabled: 	true
				}
				
				schoolCollectionService.saveSchoolCollection( _this.quotaId , dataList , function(){
					_this.loadCollection() ;
					processHandler.loadData() ;
					$scope.appEvent.onSaveCollection() ;//刷新提交按钮状态.
					$scope.eventHandler.finishQuota( _this.quotaId ) ; //更新菜单
					return mAlert('数据保存成功!') ;
				},function(){
					$scope.view.positiveBtn = {
						text 	: 	'保存数据' 	,
						disabled: 	false
					}
				});
			}
		};
		
		
		$scope.process = {
			total 	: 	1 ,
			finish	: 	0 ,
			finishRate 	: 	0 ,
			status 	: 	0
		}
		
		var processHandler = {
			init 	: 	function(){
				this.loadData() ;
				return this ;
			},
			loadData 	: 	function( ){
				var _this = this ;
				schoolCollectionService.getReportingProcess( function( data ){
					_this.viewRend( data ) ;
				});
				return this ;
			},
			viewRend 	: 	function( data ){
				console.log( data )
				$scope.process['total'] = data['totalNum'] ;
				$scope.process['finish'] = data['finishNum']  ;
				$scope.process['finishRate'] = (data['finishNum']/data['totalNum'] * 100).toFixed(2) ;
				$scope.process['status'] = data['status'] ;
				$scope.view.editable = [0,4].indexOf( data['status'] ) != -1 ;
				$scope.$apply() ;
			}
		}
		
		//监听式初始化页面.
		!function(){
			var initStack = [ function(){
				return processHandler.init() ;
			},function(){
				return collectionHandler.init() ;
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
		this.loadCollection = function( quotaId , callback , complete ){
			$.ajax({
				url  	: 	ctx.api + 'collection/'+quotaId+'/quotaCollection/' ,
				type 	: 	'get' ,
				dataType: 	'json' ,
				success : 	function( result ){
					if( result.code != 0 )
						return mAlert( result.message ) ;
					callback( result.data ) ;
				},
				complete: 	function(){
					if( typeof complete == 'function' ){
						complete() ;
					}
				}
			})
		} 
		
		this.saveSchoolCollection = function( quotaId , param , callback , complete , invalid ){
			$.ajax({
				url  	: 	ctx.api + 'collection/'+quotaId+'/saveCollection' ,
				type 	: 	'post' ,
				dataType: 	'json' ,
				data 	: 	$.toJSON( param ) ,
				success : 	function( result ){
					//Other error .
					if( result.code != 0 )
						return mAlert( result.message ) ;
					callback( result.data ) ;
				},
				complete: 	function(){
					if( typeof complete == 'function' ){
						complete() ;
					}
				},
				contentType 	: 	'application/json;charset=UTF-8'
			})
		}
		
		this.getReportingProcess = function( callback , complete ){
			$.ajax({
				url  	: 	ctx.api + 'collection/getReportingProcess' ,
				type 	: 	'get' ,
				dataType: 	'json' ,
				success : 	function( result ){
					if( result.code != 0 )
						return mAlert( result.message ) ;
					callback( result.data ) ;
				},
				complete: 	function(){
					if( typeof complete == 'function' ){
						complete() ;
					}
				}
			})
		}
	}
	app.register.service('schoolCollectionService' , service ) ;
	return controller ;
});