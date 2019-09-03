define(['app/datatable-setting' , 'app/sys/DictService'] , function( TableConfig ){
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
		sWidth 	: 	'25%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			return uiUtil.shorthand(data , 35) ;
		}
	},{
		mData 	: 	'quotaId' ,
		sTitle 	: 	'二级指标' ,
		sWidth 	: 	'5%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data == '' ? '--' : row['quotaName'] ;
			return data ;
		}
	},{
		mData 	: 	'valueType' ,
		sTitle 	: 	'类型' ,
		sWidth 	: 	'5%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			return data ;
		}
	},{
		mData 	: 	'note' ,
		sTitle 	: 	'填报说明' ,
		sWidth 	: 	'10%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			return data ;
		}
	},
	
	/*,{
		mData 	: 	'createTime' ,
		sTitle 	: 	'添加日期' ,
		sWidth 	: 	'5%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	true ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			return data ;
		}
	},{
		mData 	: 	'createName' ,
		sTitle 	: 	'添加人' ,
		sWidth 	: 	'5%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			return data ;
		}
	},*/{
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
	var controller = function( $scope , ctx , $stateParams , collectionService , dictService ){
		$scope.filterMeta = [{
			'name' 	: 	'id' ,
			'label' : 	'ID'
		},{
			'name' 	: 	'title' ,
			'label' : 	'标题'
		},{
			'name' 	: 	'quotaId' ,
			'label' : 	'二级指标' ,
			'type' 	: 	'select' ,
			'option' : 	{
				'options' 	: 	ctx.api + 'quota/option?level=2' ,
				'key' 		: 	'id' ,
				'text' 		: 	'[$typeName]$name'
			}
		}];
		
		var listTable = '#data-list-table' ;
		
		setting.aoColumns = tableMeta ;
		setting.ajaxSource = ctx.api + 'collection' ;
		$scope.filterParam = setting.param ;
		var dataTable = $( listTable ).dataTable( setting ) ;
		$('#filter-group').delegate('[name=filter-btn]' , 'click' , function(){
			dataTable.fnDraw( true ) ;
		});
		
		$scope.view = {
			tools 	: 	{
				create 	: 	true ,
				search 	: 	true
			}
		};
		
		
		$scope.udata = {} ;
		$scope.uFormMeta =[[{
			field  	:	'title' ,
			label 	: 	'标题' ,
			validation 	:	{
				required 	:	true 
			},
			className 	: 	'col-xs-12'
		}],[{
			field  	:	'valueOpt' ,
			label 	: 	'值选项(单选|多选需配置该项)' ,
			type 	: 	'textarea' ,
			validation 	:	{
				required 	:	false ,
				json 		: 	true
			},
			className 	: 	'col-xs-12' ,
			elementStyle: 	'height:80px'
		}],[{
			field  	:	'validation' ,
			label 	: 	'校验规则配置' ,
			type 	: 	'textarea' ,
			validation 	:	{
				required 	:	true ,
				json 		: 	true
			},
			className 	: 	'col-xs-12' ,
			info 	: 	"支持校验:required:必填,express:正则,int:整数,lt:小于,gt:大于,max:最大,min:最小,eval:JS表达式,maxlength:字符长度" ,
			elementStyle: 	'height:150px'
		}],[{
			field  	:	'note' ,
			label 	: 	'备注' ,
			type 	: 	'textarea' ,
			validation 	:	{
				required 	:	false 
			},
			className 	: 	'col-xs-12'
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
				$(this.form).find('[name=validation],[name=valueOpt]').on('blur' , function(){
					$(this).val( JSON.stringify( JSON.parse($(this).val()) , null , '\t' ) ).change() ;
				});
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
				collectionService.get( this.id , function( data ){
					ngUtil.apply( $scope , function(){
						$scope.udata = data ;
						if( !data['validation'] || /^\s*$/.test(data['validation']) )
							$scope.udata['validation'] = "{\"rules\":{\"required\":true},\"messages\":\"当前字段必填\"}" ;
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
				collectionService.update( _this.id , $scope.udata , function( data ){
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
			field  	:	'title' ,
			label 	: 	'标题' ,
			validation 	:	{
				required 	:	true 
			},
			className 	: 	'col-xs-12'
		}],[{
			field 	: 	'quotaId' ,
			label : 	'二级指标' ,
			type 	: 	'select' ,
			validation 	: 	{
				required 	: 	true
			},
			option : 	{
				options 	: 	ctx.api + 'quota/option?level=2' ,
				key 		: 	'id' ,
				text 		: 	'[$typeName]$name'
			},
			className 	: 	'col-xs-6'
		},{
			field  	:	'valueType' ,
			label 	: 	'值类型' ,
			type 	: 	'select' ,
			option	: 	{
				options 	: 	ctx.api + 'dict/COLLECTION_TYPE' ,
				key 		: 	'code'
			},
			validation 	:	{
				required 	:	true 
			},
			className 	: 	'col-xs-6'
		}],[{
			field  	:	'valueOpt' ,
			label 	: 	'值选项(单选|多选需配置该项)' ,
			type 	: 	'textarea' ,
			validation 	:	{
				required 	:	false ,
				json 		: 	true
			},
			className 	: 	'col-xs-12' ,
			elementStyle: 	'height:80px'
		}],[{
			field  	:	'validation' ,
			label 	: 	'校验规则配置' ,
			type 	: 	'textarea' ,
			validation 	:	{
				required 	:	true ,
				json 		: 	true
			},
			info 	: 	"支持校验:required:必填,express:正则,int:整数,lt:小于,gt:大于,max:最大,min:最小,eval:JS表达式,maxlength:字符长度" ,
			className 	: 	'col-xs-12' ,
			elementStyle: 	'height:150px'
		}],[{
			field  	:	'note' ,
			label 	: 	'备注' ,
			type 	: 	'textarea' ,
			validation 	:	{
				required 	:	false 
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
				
				$(this.form).find('[name=validation],[name=valueOpt]').on('blur' , function(){
					$(this).val( JSON.stringify( JSON.parse($(this).val()) , null , '\t' ) ).change() ;
				});
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
				$scope.cdata = {"validation":"{\"rules\":{\"required\":true},\"messages\":\"当前字段必填\"}"} ;
				$scope.$apply() ;
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
				var param = formUtil.getEditableParam( $scope.cdata , $scope.cFormMeta ) ;
				collectionService.create( param , function( data ){
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
				url  	: 	ctx.api + 'collection/'+id ,
				success : 	function( result ){
					if( result.code != 0 )
						return mAlert( result.message ) ;
					callback( result.data ) ;
				}
			});
		}
		
		this.update = function( id , param , callback , complete ){
			$.ajax({
				url  	: 	ctx.api + 'collection/'+id ,
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
				url  	: 	ctx.api + 'collection' ,
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
	app.register.service('collectionService' , service ) ;
	return controller ;
});