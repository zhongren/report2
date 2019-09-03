define(['app/datatable-setting' , 'app/sys/DictService' , 
        'directive/formulaInput/script' ] , function( TableConfig ){
	var setting = new TableConfig().setting ;
	var tableMeta = [{
		mData 	: 	'id' ,
		sTitle 	: 	'ID' ,
		sWidth 	: 	'2%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	true ,
		render 	: 	function( data , st , row ){
			return data || '--' ;
		}
	},{
		mData 	: 	'displayName' ,
		sTitle 	: 	'显示名称' ,
		sWidth 	: 	'10%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			return uiUtil.shorthand(data , 20) || '--' ;
		}
	},{
		mData 	: 	'varName' ,
		sTitle 	: 	'变量名称' ,
		sWidth 	: 	'5%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			return data || '--' ;
		}
	},{
		mData 	: 	'type' ,
		sTitle 	: 	'变量类型' ,
		sWidth 	: 	'5%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			return data || '--';
		}
	},{
		mData 	: 	'target' ,
		sTitle 	: 	'对象类型' ,
		sWidth 	: 	'5%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			return data || '--';
		}
	},{
		mData 	: 	'value' ,
		sTitle 	: 	'值' ,
		sWidth 	: 	'10%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			return uiUtil.shorthand(data , 35) || '--' ;
		}
	},{
		mData 	: 	'note' ,
		sTitle 	: 	'描述' ,
		sWidth 	: 	'10%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			return uiUtil.shorthand(data , 35) || '--' ;
		}
	},{
		mData 	: 	'id' ,
		sTitle 	: 	'操作' ,
		sWidth 	: 	'5%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			var btnGroup = $('<div></div>').addClass('knit-btn-group') ;
			btnGroup.append("<a href='javascript:;' data-edit='"+data+"' class='btn btn-primary btn-xs glyphicon glyphicon-edit'>修改</a> ");
			return btnGroup.children().length ? btnGroup[0].outerHTML : '暂无操作' ;
		}
	}];
	var controller = function( $scope , ctx , $stateParams , variableService , dictService ){
		$scope.filterMeta = [{
			'name' 	: 	'id' ,
			'label' : 	'ID'
		},{
			'name' 	: 	'displayName' ,
			'label' : 	'显示名称'
		},{
			'name' 	: 	'varName' ,
			'label' : 	'变量名'
		},{
			'name' 	: 	'target' ,
			'label' : 	'对象类型' ,
			'type'  : 	'select' ,
			'option' : {
				options 	: 	ctx.api + 'dict/VAR_TARGET_TYPE' ,
				key 		: 	'code'
			}
		}];
		
		var listTable = '#data-list-table' ;
		
		setting.aoColumns = tableMeta ;
		setting.ajaxSource = ctx.api + 'variable' ;
		$scope.filterParam = setting.param ;
		var dataTable = $( listTable ).dataTable( setting ) ;
		$('#filter-group').delegate('[name=filter-btn]' , 'click' , function(){
			dataTable.fnDraw( true ) ;
		});
		
		$scope.view = {
			tools 	: 	{
				create 	: 	true ,
				search 	: 	true
			},
			create 	: 	{
				title 	: 	'添加变量'
			},
			edit 	: 	{
				title 	: 	'变量详情'
			}
		};
		
		
		$scope.udata = {} ;
		$scope.uFormMeta =[[{
			field  	:	'displayName' ,
			label 	: 	'显示名称' ,
			validation 	:	{
				required 	:	true 
			},
			className 	: 	'col-xs-6'
		},{//VAR_TARGET_TYPE
			field  	:	'target' ,
			label 	: 	'对象类型' ,
			type 	: 	'select' ,
			option	: 	{
				options 	: 	ctx.api + 'dict/VAR_TARGET_TYPE' ,
				key 		: 	'code'
			},
			validation 	:	{
				required 	:	true 
			},
			className 	: 	'col-xs-6' 
		}],[{
			field  	:	'type' ,
			label 	: 	'变量类型' ,
			type 	: 	'select' ,
			option	: 	{
				options 	: 	ctx.api + 'dict/VAR_TYPE' ,
				key 		: 	'code'
			},
			validation 	:	{
				required 	:	true 
			},
			className 	: 	'col-xs-6' 
		},{
			field  	:	'varName' ,
			label 	: 	'变量名' ,
			type 	: 	'readonly-text' ,
			validation 	:	{
				required 	:	true ,
				express 	: 	'^\\$[0-9a-zA-Z_]+$'
			},
			info : '变量名必须以$符号开头' ,
			className 	: 	'col-xs-6'
		}],[{
			field  	:	'value' ,
			label 	: 	'变量值' ,
			type 	: 	'formulaInput' ,
			jsBeautify : false ,
			validation 	:	{
				required 	:	false 
			},
			className 	: 	'col-xs-12' ,
			elementStyle: 	'height:80px'
		}],[{
			field  	:	'note' ,
			label 	: 	'描述' ,
			type 	: 	'textarea' ,
			validation 	:	{
				required 	:	false 
			},
			className 	: 	'col-xs-12' ,
			elementStyle: 	'height:80px'
		}]];
		
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
				$scope.$apply();
				this.validator.resetForm() ;
				
				this.loadDetailData( function(){
					$(_this.modal).modal('show') ;
				});
			},
			loadDetailData : function( callback ){
				//---mdf:调用接口查询数据---
				variableService.get( this.id , function( data ){
					ngUtil.apply( $scope , function(){
						$scope.udata = data ;
					});
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
				variableService.update( _this.id , $scope.udata , function( data ){
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
		
		$scope.cdata = {} ;
		$scope.cFormMeta =[[{
			field  	:	'displayName' ,
			label 	: 	'显示名称' ,
			validation 	:	{
				required 	:	true 
			},
			className 	: 	'col-xs-6'
		},{//VAR_TARGET_TYPE
			field  	:	'target' ,
			label 	: 	'对象类型' ,
			type 	: 	'select' ,
			option	: 	{
				options 	: 	ctx.api + 'dict/VAR_TARGET_TYPE' ,
				key 		: 	'code'
			},
			validation 	:	{
				required 	:	true 
			},
			className 	: 	'col-xs-6' 
		}],[{
			field  	:	'type' ,
			label 	: 	'变量类型' ,
			type 	: 	'select' ,
			option	: 	{
				options 	: 	ctx.api + 'dict/VAR_TYPE' ,
				key 		: 	'code'
			},
			validation 	:	{
				required 	:	true 
			},
			className 	: 	'col-xs-6' 
		},{
			field  	:	'varName' ,
			label 	: 	'变量名' ,
			validation 	:	{
				required 	:	true ,
				express 	: 	'^\\$[0-9a-zA-Z_]+$'
			},
			info : '变量名必须以$符号开头' ,
			className 	: 	'col-xs-6'
		}],[{
			field  	:	'value' ,
			label 	: 	'变量值' ,
			type 	: 	'formulaInput' ,
			jsBeautify : false ,
			validation 	:	{
				required 	:	false 
			},
			className 	: 	'col-xs-12' ,
			elementStyle: 	'height:80px'
		}],[{
			field  	:	'note' ,
			label 	: 	'描述' ,
			type 	: 	'textarea' ,
			validation 	:	{
				required 	:	false 
			},
			className 	: 	'col-xs-12' ,
			elementStyle: 	'height:80px'
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
				this.validator.resetForm() ;
				ngUtil.apply( $scope , function(){
					$scope.cdata = {} ;
				});
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
				var param = formUtil.getEditableParam( $scope.cdata , $scope.cFormMeta ) ;
				variableService.create( param , function( data ){
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
		
		var initStack = [function(){
			return editHandler.init() ;
		},function(){
			return createHandler.init() ;
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
	}
	
	var service = function( ctx ){
		this.get = function( id , callback ){
			$.ajax({
				url  	: 	ctx.api + 'variable/'+id ,
				success : 	function( result ){
					if( result.code != 0 )
						return mAlert( result.message ) ;
					callback( result.data ) ;
				}
			});
		}
		
		this.update = function( id , param , callback , complete ){
			$.ajax({
				url  	: 	ctx.api + 'variable/'+id ,
				type 	: 	'put' ,
				data 	: 	$.toJSON( param ) ,
				success : 	function( result ){
					if( result.code != 0 )
						return mAlert( result.message ) ;
					callback( result.data ) ;
				},
				complete : 	function(){
					if ( typeof complete == 'function' ) 
						complete() ;
				},
				contentType 	: 	'application/json;charset=UTF8'
			});
		}
		
		this.create = function( param , callback , complete ){
			$.ajax({
				url  	: 	ctx.api + 'variable' ,
				type 	: 	'post' ,
				data 	: 	$.toJSON( param ) ,
				success : 	function( result ){
					if( result.code != 0 )
						return mAlert( result.message ) ;
					callback( result.data ) ;
				},
				complete : 	function(){
					if ( typeof complete == 'function' ) 
						complete() ;
				},
				contentType 	: 	'application/json;charset=UTF8'
			});
		}
	}
	app.register.service('variableService' , service ) ;
	return controller ;
});