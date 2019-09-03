define(['app/datatable-setting' , 'app/sys/DictService' , 'treeview'] , function( TableConfig ){
	var setting = new TableConfig().setting ;
	var tableMeta = [{
		mData 	: 	'id' ,
		sTitle 	: 	'ID' ,
		sWidth 	: 	'2%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	true ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			return data ;
		}
	},{
		mData 	: 	'username' ,
		sTitle 	: 	'账号' ,
		sWidth 	: 	'5%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			return data ;
		}
	},{
		mData 	: 	'realName' ,
		sTitle 	: 	'姓名' ,
		sWidth 	: 	'5%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			return data ;
		}
	},{
		mData 	: 	'tokenTime' ,
		sTitle 	: 	'上次登陆日期' ,
		sWidth 	: 	'5%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	true ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			return data ;
		}
	},{
		mData 	: 	'createTime' ,
		sTitle 	: 	'创建日期' ,
		sWidth 	: 	'5%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	true ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			return data ;
		}
	},{
		mData 	: 	'createName' ,
		sTitle 	: 	'创建人' ,
		sWidth 	: 	'5%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			return data ;
		}
	}/*,{
		mData 	: 	'statusName' ,
		sTitle 	: 	'账号状态' ,
		sWidth 	: 	'5%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			return data ;
		}
	}*/,{
		mData 	: 	'id' ,
		sTitle 	: 	'操作' ,
		sWidth 	: 	'5%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			var btnGroup = $('<div></div>').addClass('knit-btn-group') ;
//			btnGroup.append("<a href='javascript:;' data-edit='"+data+"' class='btn btn-primary btn-xs glyphicon glyphicon-edit'>修改</a> ");
			btnGroup.append("<a href='javascript:;' data-quota='"+data+"' class='btn btn-default btn-xs glyphicon glyphicon-cog'>审核指标</a> ");
			btnGroup.append("<a href='javascript:;' data-rm='"+data+"' class='btn btn-danger btn-xs glyphicon glyphicon-trash'>删除</a> ");
			return btnGroup.children().length ? btnGroup[0].outerHTML : '暂无操作' ;
		}
	}];
	var controller = function( $scope , ctx , $stateParams , subaccService , dictService ){
		//---mdf:条件检索配置---
		$scope.filterMeta = [{
			'name' 	: 	'username' ,
			'label' : 	'账号'
		},{
			'name' 	: 	'realName' ,
			'label' : 	'姓名'
		}];
		
		var listTable = '#data-list-table' ;
		
		setting.aoColumns = tableMeta ;
		setting.fnDrawCallback = function(){
			$('.switch').bootstrapSwitch({
				
			});
		}
		//---mdf:列表数据接口地址---
		setting.ajaxSource = ctx.api + 'subaccount' ;
		$scope.filterParam = setting.param ;
		var dataTable = $( listTable ).dataTable( setting ) ;
		$('#filter-group').delegate('[name=filter-btn]' , 'click' , function(){
			dataTable.fnDraw( true ) ;
		});
		
		//---mdf:页面基本参数配置---
		$scope.view = {
			tools 	: 	{
				create 	: 	true
			},
			create	: 	{
				positive 	: 	{
					text 	: 	'保存' ,
					disabled: 	false
				},
				title : '添加子账号' ,
				style : 	'width:600px'
			},
			edit 	: 	{
				positive 	: 	{
					text 	: 	'保存' ,
					disabled: 	false
				},
				title : '子账号详情'
			},
			quota 	: 	{
				positive 	: 	{
					text 	: 	'保存' ,
					disabled: 	false
				},
				title : '配置审核指标项'
			}
		};
		
		$scope.cdata = {} ;
		$scope.cFormMeta = [[{
			field  	:	'username' ,
			label 	: 	'登录账号' ,
			validation 	:	{
				required 	:	true ,
				maxlength 	: 	30	
			},
			className 	: 	'col-xs-12'
		}],[{
			field  	:	'realName' ,
			label 	: 	'用户姓名' ,
			validation 	:	{
				required 	:	true ,
				maxlength 	: 	30	
			},
			className 	: 	'col-xs-12'
		}]];
		
		var createHandler = {
			
			init 	: 	function(){
				this.modal = '#data-create-modal' ;
				this.form='#data-create-form' ;
				if( !$(this.form).length )
					return false ;
				
				var _this = this ;
				$('#data-create-btn').bind('click' , function(){
					_this.showView() ;
				});
				$(this.modal).find('[name=data-positive-btn]').bind('click' , function(){
					_this.saveData() ;
				});
				//---mdf:初始化校验规则---
				this.validator = $(this.form).validate( this.getFormRule( $scope.cFormMeta ) ) ;
				return this ;
			},
			getFormRule 	:	function( meta ){
				var rule = 	{
					onfocusout 	: 	function( e ){
						$(e).valid() ;
					},
					errorPlacement 	: 	function( error , e ){
						console.log( error ) ;
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
			showView : 	function(){
				ngUtil.apply($scope , function(){
					$scope.cdata = {} ;
				});
				this.validator.resetForm() ;
				
				$(this.modal).modal('show') ;
			},
			saveData : 		function(){
				var _this = this ;
				if( !this.validator.form() )
					return false ;
				
				$scope.view.create.positive = {
					text 	: 	'保存中...' ,
					disabled: 	true
				}
				//---mdf:调用接口更新数据---
				subaccService.create( $scope.cdata , function( data ){
					dataTable.fnDraw( true ) ;
					$(_this.modal).modal('hide') ;
					mAlert('数据保存成功!') ;
				} ,function(){
					$scope.view.create.positive = {
						text 	: 	'保存' ,
						disabled: 	false
					}
				});
			}
		} ;
		
		$scope.uFormMeta = [[{
			field  	:	'username' ,
			label 	: 	'登录账号' ,
			validation 	:	{
				required 	:	true ,
				maxlength 	: 	30	
			},
			className 	: 	'col-xs-12'
		}],[{
			field  	:	'realName' ,
			label 	: 	'用户姓名' ,
			validation 	:	{
				required 	:	true ,
				maxlength 	: 	30	
			},
			className 	: 	'col-xs-12'
		}]] ;
		$scope.udata 	= {} ;
		
		var editHandler = {
			init 	: 	function(){
				this.modal = '#data-edit-modal' ;
				this.form='#data-edit-form' ;
				if( !$(this.form).length )
					return false ;
				
				var _this = this ;
				$( listTable ).delegate('[data-edit]' , 'click' , function(){
					_this.showView( $(this).attr('data-edit') ) ;
				});
				$(this.modal).find('[name=data-positive-btn]').bind('click' , function(){
					_this.saveData() ;
				});
				//---mdf:初始化校验规则---
				this.validator = $(this.form).validate( this.getFormRule( $scope.uFormMeta ) ) ;
				return this ;
			},
			showView : 	function( id ){
				var _this = this ;
				if( !/^[0-9]+$/.test( id ) )
					return mAlert('请选择一条记录!') ;
				this.id = id ;
				ngUtil.apply($scope , function(){
					$scope.udata = {} ;
				});
				this.validator.resetForm() ;
				
				this.loadDetailData( function(){
					$(_this.modal).modal('show') ;
				});
			},
			loadDetailData : function( callback ){
				//---mdf:调用接口查询数据---
				subaccService.get( this.id , function( data ){
					$scope.udata = data ;
					$scope.$apply() ;
					if( typeof callback == 'function' )
						callback() ;
				});
			},
			getFormRule 	:	function( meta ){
				var rule = 	{
					onfocusout 	: 	function( e ){
						$(e).valid() ;
					},
					errorPlacement 	: 	function( error , e ){
						console.log( error ) ;
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
			saveData : 		function(){
				var _this = this ;
				if( !this.validator.form() )
					return false ;
				
				$scope.view.edit.positive = {
					text 	: 	'保存中...' ,
					disabled: 	true
				}
				$scope.$apply() ;
				//---mdf:调用接口更新数据---
				subaccService.update( _this.id , $scope.udata , function( data ){
					dataTable.fnDraw( false ) ;
					$(_this.modal).modal('hide') ;
					mAlert('数据保存成功!') ;
				} ,function(){
					$scope.view.edit.positive = {
						text 	: 	'确定' ,
						disabled: 	false
					}
					$scope.$apply() ;
				});
			}
		} ;
		
		var rmHandler = {
			init 	: 	function(){
				var _this = this ;
				$(listTable).delegate('[data-rm]' , 'click' , function(){
					_this.remove( $(this).attr('data-rm')) ;
				})
				return this ;
			},
			remove : 	function( id ){
				mConfirm('是否确认删除当前数据?' , function(){
					//---mdf:调用接口更新数据---
					subaccService.remove( id , function(){
						mAlert('数据删除成功!');
						dataTable.fnDraw( false ) ;
					});
				});
			}
		}
		
		var quotaHandler = {
			init 	: 	function(){
				var _this = this ;
				this.modal = '#data-quota-modal' ;
				$( listTable ).delegate('[data-quota]' , 'click' , function(){
					_this.showView( $(this).attr('data-quota') ) ;
				});
				$(this.modal).find('[name=data-positive-btn]').bind('click' , function(){
					_this.saveData() ;
				});
				return this ;
			},
			showView 	: 	function( userId ){
				var _this = this ;
				this.userId = userId ;
				this.param = [] ;
				this.loadQuota( function(data){
					_this.viewRend( _this.getTreeNode( data ) ) ;
					$(_this.modal).modal('show') ;
				});
			},
			loadQuota 	:	function( callback ){
				var _this = this ;
				subaccService.findQuota( this.userId , function( data ){
					data.forEach( function( d , k ){
						if( !d['subaccChecked'] )
							return ;
						_this.spliceParam( d['id'] , true ) ;
					});
					callback( data ) ;
				});
			},
			spliceParam : 	function( quotaId , type ){
				var quotaIdx;
				for( var key in this.param ){
					if( this.param[key]['quotaId'] != quotaId )
						continue ;
					quotaIdx = key ;
					break ;
				}
				if( type && typeof quotaIdx == 'undefined' )
					this.param.push({
						quotaId  	: 	quotaId
					});
				else if( ( type && typeof quotaIdx != 'undefined') 
							|| !type && typeof quotaIdx == 'undefined' )
					return ;
				else if ( !type && typeof quotaIdx != 'undefined' )
					this.param.splice( quotaIdx , 1 ) ;
			},
			getTreeNode :	function( data ){
				var nodes = [] , _this = this;
				data.forEach( function( d , k ){
					var node = {
						text 	: 	d['name']  ,
						icon	:	"glyphicon glyphicon-menu-hamburger",
						backColor	:	"#FFFFFF",
						selectable	:	false
					}
					nodes.push( node ) ;
					if( ! d['subQuota'] || ! d['subQuota'].length )
						return ;
					node['nodes'] = [] ;
					var subQuotas = d['subQuota'] ;
					subQuotas.forEach( function( sd , sk ) {
						var subNode = {
							text 	: 	sd['name'] ,
							icon	:	sd['disabledCheck'] ? 'glyphicon glyphicon-ban-circle' : "glyphicon glyphicon-unchecked",
							backColor	:	"#FFFFFF",
							selectable	:	!sd['disabledCheck'] ,
							state: {
								selected:sd['subaccChecked']
							},
							dataId	: 	sd['id']
						}
						if( sd['subaccChecked'] )
							_this.spliceParam( sd['id'] , true ) ;
						
						node['nodes'].push( subNode ) ;
					});
				});
				return nodes ;
			},
			viewRend 	: 	function( data ){
				var _this = this ;
				$('#quota-tree-view').treeview({
					data  	: 	data  ,
					levels 	: 	2 ,
					showBorder 	: 	false ,
					selectedBackColor 	: 	'#F5F5F5' ,
					selectedColor 		: 	'#000' ,
					selectedIcon 		: 	'glyphicon glyphicon-check' ,
					nodeIcon : 	'' ,
					showIcon : 	true ,
					multiSelect	: 	true ,
					onNodeSelected 	: 	function( event , node ){
						_this.spliceParam( node.dataId , true ) ;
					},
					onNodeUnselected: 	function( event , node ){
						_this.spliceParam( node.dataId , false ) ;
					}
				});
			},
			saveData 	: 	function(){
				var _this = this ;
				$scope.view.quota.positive = {
					text 	: 	'保存中...' ,
					disabled: 	true
				}
				$scope.$apply() ;
				
				subaccService.updateQuota( this.userId , this.param , function(){
					mAlert('数据保存成功!') ;
					$(_this.modal).modal('hide') ;
				},function(){
					$scope.view.quota.positive = {
						text 	: 	'保存' ,
						disabled: 	false
					}
					$scope.$apply() ;
				});
			}
		}
		
		//监听式初始化页面.
		!function(){
			var initStack = [ function(){
				return createHandler.init() ;
			},function(){
				return editHandler.init() ;
			},function(){
				return rmHandler.init() ;
			},function(){
				return quotaHandler.init() ;
			} ] ;
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
		this.get = function( id , callback , complete ){
			$.ajax({
				url  	: 	ctx.api + 'subaccount/'+id ,
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
		
		this.remove = function( id , callback , complete ){
			$.ajax({
				url  	: 	ctx.api + 'subaccount/'+id ,
				type 	: 	'delete' ,
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
		
		this.create = function( param , callback , complete ){
			$.ajax({
				url  	: 	ctx.api + 'subaccount' ,
				type 	: 	'post' ,
				dataType: 	'json' ,
				data 	: 	$.toJSON( param ) ,
				success : 	function( result ){
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
		
		this.update = function( id , param , callback , complete ){
			$.ajax({
				url  	: 	ctx.api + 'subaccount/'+id ,
				type 	: 	'put' ,
				dataType: 	'json' ,
				data 	: 	$.toJSON( param ) ,
				success : 	function( result ){
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
		
		this.findQuota 	= function( userId , callback , complete ){
			$.ajax({
				url  	: 	ctx.api + 'subaccount/'+userId+'/quotas' ,
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
		
		this.updateQuota = function( userId , param , callback , complete ){
			$.ajax({
				url  	: 	ctx.api + 'subaccount/'+userId +"/quotas" ,
				type 	: 	'put' ,
				dataType: 	'json' ,
				data 	: 	$.toJSON( param ) ,
				success : 	function( result ){
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
	}
	app.register.service('subaccService' , service ) ;
	return controller ;
});