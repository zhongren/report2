define(['app/datatable-setting' ,'app/sys/CityService' , 'app/sys/DictService'] , function( TableConfig ){
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
		mData 	: 	'code' ,
		sTitle 	: 	'学校标识' ,
		sWidth 	: 	'5%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			return data ;
		}
	},{
		mData 	: 	'name' ,
		sTitle 	: 	'名称' ,
		sWidth 	: 	'5%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			return data ;
		}
	},{
		mData 	: 	'masterName' ,
		sTitle 	: 	'联系人' ,
		sWidth 	: 	'5%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			if( !data ) return '--' ;
			var group = $('<div></div>').addClass('row').css('margin-right' , '5px')
				.attr({
					'title'  	: 	'点击查看更多联系人' 
				});
			group.append($('<div></div>').addClass('col-xs-6 text-left').text(data) ) ;
			var btnCell = $('<div></div>').addClass('col-xs-6 text-right').appendTo( group ) ;
			btnCell.append($('<a></a>').append("<i class='glyphicon glyphicon-info-sign'></i>")
				.attr({
				'href'   	: 	'javascript:;' ,
				'data-connector-detail' : row['id'] ,
				'class' 	: 	'btn btn-xs btn-primary' ,
			}) ) ;
			return group[0].outerHTML ;
		}
	},{
		mData 	: 	'city' ,
		sTitle 	: 	'所属辖区' ,
		sWidth 	: 	'5%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			var city = row['city'] ? row['city'] : '' ;
			var county = row['county'] ? row['county'] : '' ;
			return city+county ;
		}
	},{
		mData 	: 	'regionType' ,
		sTitle 	: 	'城乡分类' ,
		sWidth 	: 	'5%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			return data ;
		}
	},{
		mData 	: 	'eduinstName' ,
		sTitle 	: 	'所属教育局' ,
		sWidth 	: 	'5%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			return data ;
		}
	},{
		mData 	: 	'typeName' ,
		sTitle 	: 	'学校类型' ,
		sWidth 	: 	'5%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
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
	},{
		mData 	: 	'id' ,
		sTitle 	: 	'操作' ,
		sWidth 	: 	'10%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			var btnGroup = $('<div></div>').addClass('knit-btn-group') ;
			btnGroup.append("<a href='javascript:;' data-edit='"+data+"' class='btn btn-primary btn-xs glyphicon glyphicon-edit'>修改</a> ");
			btnGroup.append("<a href='javascript:;' data-rm='"+data+"' class='btn btn-danger btn-xs glyphicon glyphicon-trash'>删除</a> ");
			return btnGroup.children().length ? btnGroup[0].outerHTML : '暂无操作' ;
		}
	}];
	var controller = function( $scope , ctx , $stateParams , schoolService , cityService , dictService ){
		var setting = new TableConfig().setting ;
		$scope.filterMeta = [{
			'name' 	: 	'name' ,
			'label' : 	'学校名称'
		},{
			'name' 	: 	'code' ,
			'label' : 	'学校标识'
		},{	'name' 	: 	'type' ,
			'type' 	:	'select' ,
			'option': 	{
				options 	: 	ctx.api + 'school/type',
				key 		: 	'id'
			},
			'label' 	: 	'类型'
		}];
		
		var listTable = '#data-list-table' ;
		setting.fnDrawCallback = function(){
		}
		setting.aoColumns = tableMeta ;
		setting.ajaxSource = ctx.api + 'school' ;
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
				imports : 	true
			},
			create	: 	{
				positive 	: 	{
					text 	: 	'保存' ,
					disabled: 	false
				},
				title : '添加学校'
			},
			edit 	: 	{
				positive 	: 	{
					text 	: 	'保存' ,
					disabled: 	false
				},
				title : '学校详情'
			},
			detail 	: 	{
				positive 	: 	{
					text 	: 	'保存' ,
					disabled: 	false ,
					hide 	: 	true
				},
				native 		: 	{
					text 	: 	'关闭窗口'
				},
				title : '学校联系人'
			}
		};
		
		//添加学校 .
		$scope.cdata = {} ;
		$scope.cFormMeta = [[{
			field  	:	'code' ,
			label 	: 	'学校标识码' ,
			validation 	:	{
				required 	:	true ,
				maxlength 	: 	30	
			},
			className 	: 	'col-xs-6'
		},{
			field  	:	'name' ,
			label 	: 	'学校名称' ,
			validation 	:	{
				required 	:	true ,
				maxlength 	: 	30	
			},
			className 	: 	'col-xs-6'
		}],[{
			field  	:	'password' ,
			label 	: 	'登陆密码' ,
			type    :   'password',
			validation 	:	{
				required 	:	true ,
				maxlength 	: 	30 
			},
			className 	: 	'col-xs-6'
		},{
			field  	:	'pwd' ,
			label 	: 	'再次确认密码' ,
			type    :   'password',
			validation 	:	{
				required 	:	true ,
				maxlength 	: 	30	,
				equalTo :  '#data-create-form [name=password]' ,
			},
			className 	: 	'col-xs-6'
		}],[
		{
				field  	:	'regionType' ,
				label 	: 	'城乡分布' ,
				type 	: 	'select' ,
				option	: 	{
					options 	: 	ctx.api + 'dict/REGION_TYPE' ,
					key 		: 	'code'
				},
				validation 	:	{
					required 	:	true 
				},
				className 	: 	'col-xs-6'
		},
		{
			field  	:	'type' ,
			label 	: 	'学校类型' ,
			type 	: 	'select' ,
			option	: 	{
				options 	: 	ctx.api + 'school/type' ,
				key 		: 	'id'
			},
			validation 	:	{
				required 	:	true 
			},
			className 	: 	'col-xs-6'
		}
		],[
			{
				field  	:	'city' ,
				label 	: 	'设区市' ,
				type 	: 	'select' ,
				option	: 	{
					options 	: 	ctx.api + 'city/optionCity' ,
					key 		: 	'id'
				},
				validation 	:	{
					required 	:	true 
				},
				className 	: 	'col-xs-6'
			},
			{
			field  	:	'county' ,
			label 	: 	'区县' ,
			type 	: 	'select' ,
			option	: 	{
				options 	: 	[] ,
				key 		: 	'id'
			},
			validation 	:	{
				required 	:	true 
			},
			className 	: 	'col-xs-6'
		  }
		],
		[
			{
			field  	:	'11' ,
			label 	: 	'新/老城区' ,
			type 	: 	'select' ,
			option	: 	{
				options 	: 	ctx.api + 'dict/XINLAOCHENGQU' ,
				key 		: 	'code'
			},
			validation 	:	{
				required 	:	true 
			},
			className 	: 	'col-xs-6'
		},
		{
			field  	:	'eduinstId' ,
			label 	: 	'学校归属地' ,
			type 	: 	'select' ,
			option	: 	{
				options 	: 	[] ,
				key 		: 	'id'
			},
			validation 	:	{
				required 	:	true 
			},
			className 	: 	'col-xs-6'
		}
	
		] ],
		
		$scope.$watch('cdata.city' , function( n , o ){
			var county ;
			$scope.cFormMeta.forEach( function( d , k ){
				d.forEach( function( sd , k ){
					if( sd.field == 'county' )
						county = sd ;
				});
			});
			if( !county ) return ;
			if( !n ) county.option.options = [] ;
			else county.option.options = ctx.api + 'city/optionCounty?parentId='+n ;
			$scope.cdata['county'] = '' ;
		} , true );
		
		$scope.$watch('cdata.county' , function( n , o ){
			var eduinst ;
			$scope.cFormMeta.forEach( function( d , k ){
				d.forEach( function( sd , k ){
					if( sd.field == 'eduinstId' )
						eduinst = sd ;
				});
			});
			if( !eduinst ) return ;
			if( !n ) eduinst.option.options = [] ;
			else eduinst.option.options = ctx.api + 'eduinst/option?countyId='+n ;
			$scope.cdata['eduinstId'] = '' ;
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
				schoolService.create( param , function( data ){
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
			field  	:	'code' ,
			label 	: 	'学校代码' ,
			validation 	:	{
				required 	:	true ,
				maxlength 	: 	30	
			},
			className 	: 	'col-xs-6'
		},{
			field  	:	'name' ,
			label 	: 	'名称' ,
			validation 	:	{
				required 	:	true ,
				maxlength 	: 	30	
			},
			className 	: 	'col-xs-6'
		}],[{
			field  	:	'city' ,
			label 	: 	'大市' ,
			type 	: 	'select' ,
			option	: 	{
				options 	: 	ctx.api + 'city/optionCity' ,
				key 		: 	'id'
			},
			validation 	:	{
				required 	:	true 
			},
			className 	: 	'col-xs-6'
		},{
			field  	:	'county' ,
			label 	: 	'区县' ,
			type 	: 	'select' ,
			option	: 	{
				options 	: 	[] ,
				key 		: 	'id'
			},
			validation 	:	{
				required 	:	true 
			},
			className 	: 	'col-xs-6'
		}],[{
			field  	:	'eduinstId' ,
			label 	: 	'所属教育局' ,
			type 	: 	'select' ,
			option	: 	{
				options 	: 	[] ,
				key 		: 	'id'
			},
			validation 	:	{
				required 	:	true 
			},
			className 	: 	'col-xs-6'
		},{
			field  	:	'regionType' ,
			label 	: 	'城乡类型' ,
			type 	: 	'select' ,
			option	: 	{
				options 	: 	ctx.api + 'dict/REGION_TYPE' ,
				key 		: 	'code'
			},
			validation 	:	{
				required 	:	true 
			},
			className 	: 	'col-xs-6'
		}],[{
			field  	:	'type' ,
			label 	: 	'学校类型' ,
			type 	: 	'select' ,
			option	: 	{
				options 	: 	ctx.api + 'school/type' ,
				key 		: 	'id'
			},
			validation 	:	{
				required 	:	true 
			},
			className 	: 	'col-xs-6'
		}]];
		
		var fieldMeta = function( meta , field ){
			var sub = undefined ;
			meta.forEach( function( d , k ){
				if( d instanceof Array ){
					d.forEach( function( sd , sk ){
						if( field == sd['field'] )
							sub = sd ;
					});
					if( field == d['field'] )
						sub = d ;
				}
			});
			return sub ;
		}
		
		$scope.$watch('udata.city' , function( n , o ){
			var county = fieldMeta( $scope.uFormMeta , 'county' ) ;
			if( !county ) 
				return ;
			county.option.options = !n ? [] :
				ctx.api + 'city/optionCounty?parentId='+n ;
		} , true );
		
		$scope.$watch('udata.county' , function( n , o ){
			var eduinst = fieldMeta( $scope.uFormMeta , 'eduinstId' ) ;;
			if( !eduinst ) 
				return ;
			eduinst.option.options = !n ? [] :
				ctx.api + 'eduinst/option?countyId='+n ;
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
				schoolService.get( this.id , function( data ){
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
				schoolService.update( _this.id , $scope.udata , function( data ){
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
		
		$scope.idata = {} ;
		$scope.iFormMeta = [[{
			field  	:	'masterName' ,
			label 	: 	'校长姓名' ,
			className 	: 	'col-xs-6'
		},{
			field  	:	'masterPhone' ,
			label 	: 	'校长手机号' ,
			className 	: 	'col-xs-6'
		}],[{
			field  	:	'viceName' ,
			label 	: 	'副校长姓名' ,
			className 	: 	'col-xs-6'
		},{
			field  	:	'vicePhone' ,
			label 	: 	'副校长手机号' ,
			className 	: 	'col-xs-6'
		}],[{
			field  	:	'reportName' ,
			label 	: 	'填报员姓名' ,
			className 	: 	'col-xs-6'
		},{
			field  	:	'reportPhone' ,
			label 	: 	'填报员手机号' ,
			className 	: 	'col-xs-6'
		}] ];
		
		var detailHandler = {
			init 	: 	function(){
				this.modal = '#data-detail-modal' ;
				var _this = this ;
				$( listTable ).delegate('[data-connector-detail]' , 'click' , function(){
					_this.showView( $(this).attr('data-connector-detail') ) ;
				});
				return this ;
			},
			showView : 	function( id ){
				var _this = this ;
				if( !/^[0-9]+$/.test( id ) )
					return mAlert('请选择一条记录!') ;
				this.id = id ;
				ngUtil.apply( $scope , function(){
					$scope.idata = {} ;
				});
				this.loadDetailData( function(){
					$(_this.modal).modal('show') ;
				});
			},
			loadDetailData : function( callback ){
				//---mdf:调用接口查询数据---
				schoolService.getConnector( this.id , function( data ){
					ngUtil.apply( $scope , function(){
						$scope.idata = data ;
					});
					if( typeof callback == 'function' )
						callback() ;
				});
			}
		} ;
		
		//删除学校 .
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
					schoolService.remove( id , function(){
						mAlert('数据删除成功!');
						dataTable.fnDraw( false ) ;
					});
				});
			}
		}
		
		//禁用学校用户 .
		var disableAccountHandler 	= 	{
			init 	: 	function(){
				var _this = this ;
				$( listTable ).delegate('[data-status]' , 'click' , function(){
					_this.disableAccount( $(this).attr('data-status') ) ;
				});
				return this ;
			},
			disableAccount 	: 	function( accountId ){
				if( !accountId || !/^[0-9]+/.test( accountId ) )
					return mAlert('请选择学校!') ;
				schoolService.disabledAccount( accountId , function( data ){
					dataTable.fnDraw( false ) ;
					return mAlert('操作成功!') ;
				});
			}
		}
		
		var importHandler = {
			init 	: 	function(){
				this.modal = '#data-import-modal' ;
				this.fileElem = '#data-import-file' ;
				if( !$(this.fileElem).length )
					return false ;
				_this = this ;
				$('#data-import-btn').bind('click' , function(){
					_this.showView() ;
				});
				$(this.modal).find("button[name='data-positive-btn']").bind('click',function(){
					var formData = new FormData();
					formData.append('file', $('#data-import-file')[0].files[0]);
					schoolService.importSchool( formData,dataTable );
				});

				return this ;
			},
			showView 	: 	function(){
				$(this.modal).modal('show') ;
			}
		}
		
		var initStack = [ function(){
			return createHandler.init() ;
		},function(){
			return editHandler.init() ;
		},function(){
			return rmHandler.init() ;
		},function(){
			return disableAccountHandler.init() ;
		} ,function(){
			return importHandler.init() ;
		},function(){
			return detailHandler.init() ;
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
				url  	: 	ctx.api + 'school/'+id ,
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
		
		this.getConnector = function( id , callback ){
			$.ajax({
				url  	: 	ctx.api + 'school/connector?schoolId='+id ,
				type 	: 	'get' ,
				dataType: 	'json' ,
				success : 	function( result ){
					if( result.code != 0 )
						return mAlert( result.message ) ;
					callback( result.data ) ;
				}
			});
		}
		
		this.importSchool = function( fromData , dataTable , complete ){
			$.ajax({
				url  	: 	ctx.api + 'import/importSchoolUser' ,
				type 	: 	'post' ,
				data : fromData ,
				processData:false, 
				contentType:false,
				cache: false,
				success : 	function( result ){
					if( result.code == 0 ){
						dataTable.fnDraw( false ) ;
						return mAlert( result.data ) ;
					}else{
						return mAlert( result.message ) ;
					}
					
				},
				 error: function (jqXHR, textStatus, errorThrown) {
					 return mAlert( "导入失败" ) ;
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
				url  	: 	ctx.api + 'school/'+id ,
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
				url  	: 	ctx.api + 'school' ,
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
				url  	: 	ctx.api + 'school/'+id ,
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
	app.register.service('schoolService' , service ) ;
	return controller ;
});