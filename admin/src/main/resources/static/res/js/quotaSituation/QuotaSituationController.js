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
		mData 	: 	'name' ,
		sTitle 	: 	'指标名称' ,
		sWidth 	: 	'10%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			return uiUtil.shorthand(data , 35) || '--' ;
		}
	},{
		mData 	: 	'instType' ,
		sTitle 	: 	'机构类型' ,
		sWidth 	: 	'5%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			return data || '--' ;
		}
	},{
		mData 	: 	'schoolType' ,
		sTitle 	: 	'学校类型' ,
		sWidth 	: 	'5%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			return data || '--';
		}
	},{
		mData 	: 	'standardNote' ,
		sTitle 	: 	'标准描述' ,
		sWidth 	: 	'10%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			return uiUtil.shorthand(data , 35) || '--' ;
		}
	},{
		mData 	: 	'calculatedNote' ,
		sTitle 	: 	'计算公式描述' ,
		sWidth 	: 	'10%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			return uiUtil.shorthand(data , 35) || '--' ;
		}
	},{
		mData 	: 	'eligibleNote' ,
		sTitle 	: 	'合格公式描述' ,
		sWidth 	: 	'10%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			return uiUtil.shorthand(data , 35) || '--' ;
		}
	},{
		mData 	: 	'createTime' ,
		sTitle 	: 	'添加日期' ,
		sWidth 	: 	'5%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	true ,
		render 	: 	function( data , st , row ){
			return data || '--';
		}
	},{
		mData 	: 	'createName' ,
		sTitle 	: 	'添加人' ,
		sWidth 	: 	'5%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			return data || '--' ;
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
	var controller = function( $scope , ctx , $stateParams , quotaSituationService , dictService ){
		$scope.filterMeta = [{
			'name' 	: 	'id' ,
			'label' : 	'ID'
		},{
			'name' 	: 	'name' ,
			'label' : 	'监测点名称'
		},{
			'name' 	: 	'instType' ,
			'label' : 	'机构类型' ,
			'type'	: 	'select' ,
			'option' : {
				options 	: 	ctx.api + 'dict/SITUATION_INST_TYPE' ,
				key 		: 	'code'
			}
		},{
			'name' 	: 	'schoolType' ,
			'label' : 	'学校类型' ,
			'type'	: 	'select' ,
			'option' : {
				options 	: 	ctx.api + 'dict/SITUATION_SCHOOL_TYPE' ,
				key 		: 	'code'
			}
		}];
		
		var listTable = '#data-list-table' ;
		
		setting.aoColumns = tableMeta ;
		setting.ajaxSource = ctx.api + 'quotaSituation' ;
		$scope.filterParam = setting.param ;
		var dataTable = $( listTable ).dataTable( setting ) ;
		$('#filter-group').delegate('[name=filter-btn]' , 'click' , function(){
			dataTable.fnDraw( true ) ;
		});
		
		$scope.view = {
			tools 	: 	{
				create 	: 	true ,
				edit 	: 	true ,
				search 	: 	true
			},
			create : {
				style 	: 	'width:1000px;overflow:auto' ,
				title 	: 	'添加监测点' 
			} ,
			edit 	: 	{
				style 	: 	'width:1000px;overflow:auto' ,
				title 	: 	'添加监测点' ,
			} 
		};
		
		
		$scope.udata = {} ;
		$scope.uFormMeta =[[{
			field  	:	'dictId' ,
			label 	: 	'监测点名称' ,
			type 	: 	'select' ,
			option	: 	{
				options 	: 	ctx.api + 'quotaSituation/dict' ,
				key 		: 	'id'
			},
			validation 	:	{
				required 	:	true 
			},
			className 	: 	'col-xs-12'
		}],[{
			field  	:	'instType' ,
			label 	: 	'机构类型' ,
			type 	: 	'select' ,
			option	: 	{
				options 	: 	ctx.api + 'dict/SITUATION_INST_TYPE' ,
				key 		: 	'code'
			},
			validation 	:	{
				required 	:	true 
			},
			className 	: 	'col-xs-6'
		},{
			field  	:	'schoolType' ,
			label 	: 	'学校类型' ,
			type 	: 	'select' ,
			option	: 	{
				options 	: 	ctx.api + 'dict/SITUATION_SCHOOL_TYPE' ,
				key 		: 	'code'
			},
			validation 	:	{
				required 	:	true 
			},
			className 	: 	'col-xs-6'
		}],[{
			field  	:	'calculatedNote' ,
			label 	: 	'统计公式描述' ,
			type 	: 	'textarea' ,
			validation 	:	{
				required 	:	false 
			},
			className 	: 	'col-xs-12' ,
			elementStyle: 	'height:80px'
		}],[{
			field  	:	'calculatedFormula' ,
			label 	: 	'统计计算公式' ,
			type 	: 	'formulaInput' ,
			validation 	:	{
				required 	:	false 
			},
			className 	: 	'col-xs-12' ,
			elementStyle: 	'height:150px'
		}],[{
			field  	:	'standardNote' ,
			label 	: 	'目标描述' ,
			type 	: 	'textarea' ,
			validation 	:	{
				required 	:	false 
			},
			className 	: 	'col-xs-6' ,
			elementStyle: 	'height:80px'
		},{
			field  	:	'eligibleNote' ,
			label 	: 	'统计情况描述' ,
			type 	: 	'textarea' ,
			validation 	:	{
				required 	:	false 
			},
			className 	: 	'col-xs-6' ,
			elementStyle: 	'height:80px'
		}],[{
			field  	:	'eligibleFormula' ,
			label 	: 	'统计情况计算公式' ,
			type 	: 	'formulaInput' ,
			validation 	:	{
				required 	:	false 
			},
			className 	: 	'col-xs-12' ,
			elementStyle: 	'height:150px'
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
				quotaSituationService.get( this.id , function( data ){
					console.log('详情' , data );
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
				
				$scope.view.tools.edit.positive = {
					text 	: 	'保存中...' ,
					disabled: 	true
				}
				$scope.$apply() ;
				//---mdf:调用接口更新数据---
				quotaSituationService.update( _this.id , $scope.udata , function( data ){
					dataTable.fnDraw( false ) ;
					$(_this.modal).modal('hide') ;
					mAlert('数据保存成功!') ;
				} ,function(){
					$scope.view.tools.edit.positive = {
						text 	: 	'确定' ,
						disabled: 	false
					}
					$scope.$apply() ;
				});
			}
		} ;
		
		$scope.cdata = {} ;
		$scope.cFormMeta =[[{
			field  	:	'dictId' ,
			label 	: 	'监测点名称' ,
			type 	: 	'select' ,
			option	: 	{
				options 	: 	ctx.api + 'quotaSituation/dict' ,
				key 		: 	'id'
			},
			validation 	:	{
				required 	:	true 
			},
			className 	: 	'col-xs-12'
		}],[{
			field  	:	'instType' ,
			label 	: 	'机构类型' ,
			type 	: 	'select' ,
			option	: 	{
				options 	: 	ctx.api + 'dict/SITUATION_INST_TYPE' ,
				key 		: 	'code'
			},
			validation 	:	{
				required 	:	true 
			},
			className 	: 	'col-xs-6'
		},{
			field  	:	'schoolType' ,
			label 	: 	'学校类型' ,
			type 	: 	'select' ,
			option	: 	{
				options 	: 	ctx.api + 'dict/SITUATION_SCHOOL_TYPE' ,
				key 		: 	'code'
			},
			validation 	:	{
				required 	:	true 
			},
			className 	: 	'col-xs-6'
		}],[{
			field  	:	'calculatedNote' ,
			label 	: 	'统计公式描述' ,
			type 	: 	'textarea' ,
			validation 	:	{
				required 	:	false 
			},
			className 	: 	'col-xs-12' ,
			elementStyle: 	'height:80px'
		}],[{
			field  	:	'calculatedFormula' ,
			label 	: 	'统计计算公式' ,
			type 	: 	'formulaInput' ,
			validation 	:	{
				required 	:	false 
			},
			className 	: 	'col-xs-12' ,
			elementStyle: 	'height:150px'
		}],[{
			field  	:	'standardNote' ,
			label 	: 	'目标描述' ,
			type 	: 	'textarea' ,
			validation 	:	{
				required 	:	false 
			},
			className 	: 	'col-xs-6' ,
			elementStyle: 	'height:80px'
		},{
			field  	:	'eligibleNote' ,
			label 	: 	'统计情况描述' ,
			type 	: 	'textarea' ,
			validation 	:	{
				required 	:	false 
			},
			className 	: 	'col-xs-6' ,
			elementStyle: 	'height:80px'
		}],[{
			field  	:	'eligibleFormula' ,
			label 	: 	'统计情况计算公式' ,
			type 	: 	'formulaInput' ,
			validation 	:	{
				required 	:	false 
			},
			className 	: 	'col-xs-12' ,
			elementStyle: 	'height:150px'
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
				quotaSituationService.create( param , function( data ){
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
				url  	: 	ctx.api + 'quotaSituation/'+id ,
				success : 	function( result ){
					if( result.code != 0 )
						return mAlert( result.message ) ;
					callback( result.data ) ;
				}
			});
		}
		
		this.update = function( id , param , callback , complete ){
			$.ajax({
				url  	: 	ctx.api + 'quotaSituation/'+id ,
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
				url  	: 	ctx.api + 'quotaSituation' ,
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
	app.register.service('quotaSituationService' , service ) ;
	return controller ;
});