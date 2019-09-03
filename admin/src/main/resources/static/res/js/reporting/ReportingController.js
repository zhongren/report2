define(['app/datatable-setting' , 'app/sys/DictService' , 'directive/timepicker/script'] , function( TableConfig ){
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
		mData 	: 	'title' ,
		sTitle 	: 	'标题' ,
		sWidth 	: 	'10%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			return data ;
		}
	},{
		mData 	: 	'startTime' ,
		sTitle 	: 	'开始日期' ,
		sWidth 	: 	'10%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data === '' ? '--' : data ;
			return data ;
		}
	},{
		mData 	: 	'expireTime' ,
		sTitle 	: 	'结束日期' ,
		sWidth 	: 	'10%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data === '' ? '--' : data ;
			return data ;
		}
	},{
		mData 	: 	'createName' ,
		sTitle 	: 	'创建人' ,
		sWidth 	: 	'10%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data === '' ? '--' : data ;
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
	}/*,{
		mData 	: 	'statusName' ,
		sTitle 	: 	'状态' ,
		sWidth 	: 	'5%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			var input = $('<input>').attr({
				'type'  	: 	'checkbox' ,
				'class' 	: 	'switch' ,
				'data-size' : 	'small' ,
				'checked' 	: 	false ,
				'size' 		: 	'small' ,
				'readonly' 	: 	false
 			});
			return data  ;
		}
	}*/,{
		mData 	: 	'id' ,
		sTitle 	: 	'操作' ,
		sWidth 	: 	'5%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			var btnGroup = $('<div></div>').addClass('btn-group') ;
			btnGroup.append("<a href='javascript:;' data-edit='"+data+"' class='btn btn-primary btn-xs glyphicon glyphicon-edit'>修改</a> ");
			return btnGroup.children().length ? btnGroup[0].outerHTML : '暂无操作' ;
		}
	}];
	var controller = function( $scope , ctx , $stateParams , reportingService , dictService ){
		//---mdf:条件检索配置---
		$scope.filterMeta = [{
			'name' 	: 	'title' ,
			'label' : 	'标题'
		}];
		
		var listTable = '#data-list-table' ;
		
		setting.aoColumns = tableMeta ;
		//---mdf:列表数据接口地址---
		setting.ajaxSource = ctx.api + 'reporting' ;
		setting.fnDrawCallback = function(){
			/*$(listTable).find('.switch').bootstrapSwitch({
				'size' 		: 	'small' ,
				'onText'	: 	'发布' ,
				'onColor' 	: 'primary' ,
				'offText' 	: '关闭' ,
				'offColor'	: 'danger' ,
				'onSwitchChange' 	: 	function( event , value ){
					
				}
			});*/
		}
		$scope.filterParam = setting.param ;
		var dataTable = $( listTable ).dataTable( setting ) ;
		$('#filter-group').delegate('[name=filter-btn]' , 'click' , function(){
			dataTable.fnDraw( true ) ;
		});
		
		//---mdf:页面基本参数配置---
		$scope.view = {
			create	: 	{
				positive 	: 	{
					text 	: 	'保存' ,
					disabled: 	false
				},
				title : '创建填报' ,
				style : 	'width:600px'
			},
			edit 	: 	{
				positive 	: 	{
					text 	: 	'保存' ,
					disabled: 	false
				},
				title : '填报详情'
			},
			tools 	: 	{
				search 	: 	true ,
				create	:	true 
			}
		};
		
		$scope.cdata = {} ;
		$scope.cFormMeta = [[{
			field  	:	'title' ,
			label 	: 	'标题' ,
			validation 	:	{
				required 	:	true ,
				maxlength 	: 	30	
			},
			className 	: 	'col-xs-12'
		}],[{
			field  	:	'startTime' ,
			label 	: 	'开始日期' ,
			type 	: 	'timepicker' ,
			format 	: 	'YYYY-MM-DD HH:mm:ss' ,
			validation 	:	{
				required 	:	true 
			},
			className 	: 	'col-xs-12'
		}],[{
			field  	:	'expireTime' ,
			label 	: 	'结束日期' ,
			type 	: 	'timepicker' ,
			format 	: 	'YYYY-MM-DD HH:mm:ss' ,
			validation 	:	{
				required 	:	true 
			},
			className 	: 	'col-xs-12'
		}],[{
			field  	:	'memo' ,
			label 	: 	'描述' ,
			type 	: 	'textarea' ,
			validation 	:	{
				required 	:	false ,
				maxlength	: 	200
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
				$scope.cdata = {} ;
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
				reportingService.create( $scope.cdata , function( data ){
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
			field  	:	'title' ,
			label 	: 	'标题' ,
			validation 	:	{
				required 	:	true ,
				maxlength 	: 	30	
			},
			className 	: 	'col-xs-12'
		}],[{
			field  	:	'startTime' ,
			label 	: 	'开始日期' ,
			type 	: 	'timepicker' ,
			format 	: 	'YYYY-MM-DD HH:mm:ss' ,
			validation 	:	{
				required 	:	true 
			},
			className 	: 	'col-xs-12'
		}],[{
			field  	:	'expireTime' ,
			label 	: 	'结束日期' ,
			type 	: 	'timepicker' ,
			format 	: 	'YYYY-MM-DD HH:mm:ss' ,
			validation 	:	{
				required 	:	true 
			},
			className 	: 	'col-xs-12'
		}],[{
			field  	:	'memo' ,
			label 	: 	'描述' ,
			type 	: 	'textarea' ,
			validation 	:	{
				required 	:	false ,
				maxlength	: 	200
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
				$scope.udata = {} ;
				this.validator.resetForm() ;
				
				this.loadDetailData( function(){
					$(_this.modal).modal('show') ;
				});
			},
			loadDetailData : function( callback ){
				//---mdf:调用接口查询数据---
				reportingService.get( this.id , function( data ){
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
				reportingService.update( _this.id , $scope.udata , function( data ){
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
					reportingService.remove( id , function(){
						mAlert('数据删除成功!');
						dataTable.fnDraw( false ) ;
					});
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
		this.get = function( id , callback , complete ){
			$.ajax({
				url  	: 	ctx.api + 'reporting/'+id ,
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
				url  	: 	ctx.api + 'reporting/'+id ,
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
				url  	: 	ctx.api + 'reporting' ,
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
				url  	: 	ctx.api + 'reporting/'+id ,
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
	app.register.service('reportingService' , service ) ;
	return controller ;
});