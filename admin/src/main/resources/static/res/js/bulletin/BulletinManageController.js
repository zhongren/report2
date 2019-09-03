define(['app/datatable-setting' ,'app/sys/CityService' , 'app/sys/DictService' 
        , 'directive/fileInput/script'] , function( TableConfig ){
	var setting = new TableConfig().setting ;
	var tableMeta = [{
		mData 	: 	'id' ,
		sTitle 	: 	'ID' ,
		sWidth 	: 	'5%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	true ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			return data ;
		}
	},{
		mData 	: 	'categoryCode' ,
		sTitle 	: 	'分类' ,
		sWidth 	: 	'5%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	true ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			return data ;
		}
	},{
		mData 	: 	'type' ,
		sTitle 	: 	'类型' ,
		sWidth 	: 	'5%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	true ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			return data ;
		}
	},{
		mData 	: 	'title' ,
		sTitle 	: 	'标题' ,
		sWidth 	: 	'60%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			var type = $('<span></span>').html("【"+row['type']+"】") ;
			type.append( data ) ;
			return uiUtil.link( type , '#/bulletin/'+row['id']+'/detail' , {
				title 	: 	data
			} )[0].outerHTML ;
		}
	},{
		mData 	: 	'publishTime' ,
		sTitle 	: 	'发布日期' ,
		sWidth 	: 	'10%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	true ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			return data ;
		}
	},{
		mData 	: 	'publishName' ,
		sTitle 	: 	'发布人' ,
		sWidth 	: 	'10%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			return data ;
		}
	},{
		mData 	: 	'readNum' ,
		sTitle 	: 	'已读' ,
		sWidth 	: 	'10%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		show 	: 	['eduinst','school'] ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			return data ;
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
			btnGroup.append("<a href='javascript:;' data-rm='"+data+"' class='btn btn-danger btn-xs glyphicon glyphicon-trash'>删除</a> ");
			btnGroup.append("<a href='javascript:;' data-edit='"+data+"' class='btn btn-primary btn-xs glyphicon glyphicon-trash'>修改</a> ");
			return btnGroup.children().length ? btnGroup[0].outerHTML : '暂无操作' ;
		}
	}];
	
	var controller = function( $scope , ctx , $stateParams , bulletinManageService , cityService , dictService ){
		$scope.filterMeta = [{
			'name' 	: 	'title' ,
			'label' : 	'标题'
		},{	'name' 	: 	'categoryCode' ,
			'type' 	:	'select' ,
			'option': 	{
				options 	: 	ctx.api + 'dict/BULLETIN_CATEGORY',
				key 		: 	'code'
			},
			'label' 	: 	'分类'
		}];
		
		var listTable = '#data-list-table' ;
		setting.fnDrawCallback = function(){
		}
		setting.aoColumns = tableMeta ;
		setting.ajaxSource = ctx.api + 'bulletin' ;
		$scope.filterParam = setting.param ;
		
		var dataTable = $( listTable ).dataTable( setting ) ;
		$('#filter-group').delegate('[name=filter-btn]' , 'click' , function(){
			dataTable.fnDraw( true ) ;
		});
		
		$scope.view = {
			tools	: 	{
				search 	: 	true ,
				create 	: 	true ,
				exports : 	false ,
				imports : 	false
			}
		};
		
		$scope.cdata = {} ;
		$scope.cFormMeta =[[{
			field  	:	'title' ,
			label 	: 	'公告标题' ,
			validation 	:	{
				required 	:	true 
			},
			className 	: 	'col-xs-6'
		},{
			field  	:	'categoryCode' ,
			label 	: 	'公告分类' ,
			type 	: 	'select' ,
			option	: 	{
				options 	: 	ctx.api + 'dict/BULLETIN_CATEGORY' ,
				key 		: 	'code'
			},
			validation 	:	{
				required 	:	true 
			},
			className 	: 	'col-xs-6'
		}],[{
			field  	:	'content' ,
			label 	: 	'公告内容' ,
			type 	: 	'textarea' ,
			validation 	:	{
				required 	:	true 
			},
			flag 	: 	'text' ,
			className 	: 	'col-xs-12' ,
			elementStyle: 	'height:150px'
		}],[{
			field  	:	'content' ,
			label 	: 	'上传文件' ,
			type 	: 	'fileUpload' ,
			mediaType 	: 	'file' ,
			validation 	:	{
				required 	:	true ,
				rules 	: 	{
					required 	: 	true ,
					file 		: 	"['doc','docx','xls','xlsx','pdf'].indexOf($ext.toLowerCase()) == -1 ? '文件格式不正确,请上传(doc,docx,xls,xlsx,pdf)格式文件!' : true" ,
					maxnum 		: 10
				}
			},
			flag 	: 	'download' ,
			className 	: 	'col-xs-12' 
		}]];
		
		$scope.$watch( 'cdata.categoryCode' , function( n ){
			var textarea = ngUtil.meta( $scope.cFormMeta , 'text' , 'flag' ), 
				fileinput = ngUtil.meta( $scope.cFormMeta , 'download' , 'flag' ) ;
			switch( n ){
			case 	'text' 	: 	
				textarea['hide'] = false ;
				fileinput['hide'] = true ;
				$scope.cdata['content'] = '' ;
				break ;
			case	'download' 	: 	
				textarea['hide'] = true ;
				fileinput['hide'] = false ;
				$scope.cdata['content'] = [] ;
				break ;
			default 	: 
				textarea['hide'] = true ;
				fileinput['hide'] = true ;
				$scope.cdata['content'] = '' ;
			}
			ngUtil.apply( $scope ) ;
		} , true );
		
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
				$scope.cdata = {'categoryCode' : 'text'} ;
				$scope.$apply() ;
				this.validator.resetForm() ;
				
				$(this.modal).modal('show') ;
			},
			validContent 	: 	function(){
				if( ($scope.cdata['content'] instanceof Array && !$scope.cdata['content']).length || 
						( !$scope.cdata['content'] || /^\s*$/.test( $scope.cdata['content'] ) ) )
					return mAlert('公告内容不能为空!') && false ;
				return true ;
			},
			saveData : 		function(){
				var _this = this ;
				var formValid = this.validator.form() ,
					contentValid = this.validContent() ;
				if( !formValid || !contentValid )
					return false ;
				
				if( typeof $scope.cdata['content'] != 'string')
					$scope.cdata['content'] = $.toJSON( $scope.cdata['content'] ) ;
				
				$scope.view.create.positive = {
					text 	: 	'保存中...' ,
					disabled: 	true
				}
				//---mdf:调用接口更新数据---
				var param = formUtil.getEditableParam( $scope.cdata , $scope.cFormMeta ) ;
				bulletinManageService.create( param , function( data ){
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
		
		$scope.udata = {} ;
		$scope.uFormMeta =[[{
			field  	:	'title' ,
			label 	: 	'公告标题' ,
			validation 	:	{
				required 	:	true 
			},
			className 	: 	'col-xs-6'
		},{
			field  	:	'categoryCode' ,
			label 	: 	'公告分类' ,
			type 	: 	'select' ,
			option	: 	{
				options 	: 	ctx.api + 'dict/BULLETIN_CATEGORY' ,
				key 		: 	'code'
			},
			validation 	:	{
				required 	:	true 
			},
			className 	: 	'col-xs-6'
		}],[{
			field  	:	'content' ,
			label 	: 	'公告内容' ,
			type 	: 	'textarea' ,
			validation 	:	{
				required 	:	true 
			},
			flag 	: 	'text' ,
			className 	: 	'col-xs-12' ,
			elementStyle: 	'height:150px'
		}],[{
			field  	:	'content' ,
			label 	: 	'上传文件' ,
			type 	: 	'fileUpload' ,
			mediaType 	: 	'file' ,
			validation 	:	{
				required 	:	true ,
				rules 	: 	{
					required 	: 	true ,
					file 		: 	"['doc','docx','xls','xlsx','pdf'].indexOf($ext.toLowerCase()) == -1 ? '文件格式不正确,请上传(doc,docx,xls,xlsx,pdf)格式文件!' : true" ,
					maxnum 		: 10
				}
			},
			flag 	: 	'download' ,
			className 	: 	'col-xs-12' 
		}]];
		
		$scope.$watch( 'udata.categoryCode' , function( n ){
			var textarea = ngUtil.meta( $scope.uFormMeta , 'text' , 'flag' ), 
				fileinput = ngUtil.meta( $scope.uFormMeta , 'download' , 'flag' ) ;
			switch( n ){
			case 	'text' 	: 	
				textarea['hide'] = false ;
				fileinput['hide'] = true ;
				if( typeof $scope.udata['content'] != 'string')
					$scope.udata['content'] = '' ;
				break ;
			case	'download' 	: 	
				textarea['hide'] = true ;
				fileinput['hide'] = false ;
				if( ! ( $scope.udata['content'] instanceof Array ) )
					$scope.udata['content'] = [] ;
				break ;
			default 	: 
				textarea['hide'] = true ;
				fileinput['hide'] = true ;
				$scope.udata['content'] = '' ;
			}
			ngUtil.apply( $scope ) ;
		} , true );
		
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
				bulletinManageService.get( this.id , function( data ){
					ngUtil.apply( $scope , function(){
						if(data['categoryCode'] == 'download')
							data['content'] = JSON.parse( data['content'] ) ;
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
			validContent 	: 	function(){
				if( ($scope.udata['content'] instanceof Array && !$scope.udata['content']).length || 
						( !$scope.udata['content'] || /^\s*$/.test( $scope.udata['content'] ) ) )
					return mAlert('公告内容不能为空!') && false ;
				return true ;
			},
			saveData : 		function(){
				var _this = this ;
				var formValid = this.validator.form() ,
					contentValid = this.validContent() ;
				if( !formValid || !contentValid )
					return false ;
				
				if( typeof $scope.udata['content'] != 'string')
					$scope.udata['content'] = $.toJSON( $scope.udata['content'] ) ;
				
				$scope.view.edit.positive = {
					text 	: 	'保存中...' ,
					disabled: 	true
				}
				$scope.$apply() ;
				//---mdf:调用接口更新数据---
				bulletinManageService.update( _this.id , $scope.udata , function( data ){
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
		
		var deleteHandler = {
			init 	: 	function(){
				if(! $(listTable).length )
					return false ;
				var _this = this ;
				$(listTable).delegate('[data-rm]' , 'click' , function(){
					var elem = this ;
					mConfirm('是否确认删除?' , function(){
						_this.remove( $(elem).attr('data-rm') ) ;
					});
				});
				return this ;
			},
			remove : 	function( id ){
				bulletinManageService.remove( id , function( data ){
					if( !data ) 
						mAlert('当前数据已删除!') ;
					else 
						mAlert('数据删除成功!') ;
					dataTable.fnDraw( false ) ;
				});
			}
		}
		
		var initStack = [ function(){
			return createHandler.init() ;
		} ,function(){
			return deleteHandler.init() ;
		},function(){
			return editHandler.init() ;
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
		this.get = function( id , callback , complete ){
			$.ajax({
				url  	: 	ctx.api + 'bulletin/'+id ,
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
				url  	: 	ctx.api + 'bulletin/'+id ,
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
				url  	: 	ctx.api + 'bulletin' ,
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
				url  	: 	ctx.api + 'bulletin/'+id ,
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
	app.register.service('bulletinManageService' , service ) ;
	return controller ;
});