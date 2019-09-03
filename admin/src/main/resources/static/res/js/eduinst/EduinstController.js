define(['app/datatable-setting' , 'app/sys/DictService'] , function( TableConfig ){
	var setting = new TableConfig().setting ;
	var tableMeta = [{
		mData 	: 	'id' ,
		sTitle 	: 	'教育局ID' ,
		sWidth 	: 	'5%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	true ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			return data ;
		}
	},{
		mData 	: 	'code' ,
		sTitle 	: 	'教育局标识' ,
		sWidth 	: 	'5%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	true ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			return data ;
		}
	},{
		mData 	: 	'name' ,
		sTitle 	: 	'教育局名称' ,
		sWidth 	: 	'5%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			return data ;
		}
	},{
		mData 	: 	'typeName' ,
		sTitle 	: 	'教育局类型' ,
		sWidth 	: 	'5%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			return data ;
		}
	},{
		mData 	: 	'cityName' ,
		sTitle 	: 	'所属辖区' ,
		sWidth 	: 	'5%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			var city = row['cityName'] ? row['cityName'] : '' ;
			var county = row['countyName'] ? row['countyName'] : '' ;
			var result = city + county  ;
			return result ? result : '--' ;
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
	}/*,{
		mData 	: 	'createTime' ,
		sTitle 	: 	'添加日期' ,
		sWidth 	: 	'5%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	true ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			return data ;
		}
	}/*,{
		mData 	: 	'accountStatusName' ,
		sTitle 	: 	'账号状态' ,
		sWidth 	: 	'5%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	true ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data === '' ? '--' : data ;
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
			btnGroup.append("<a href='javascript:;' data-edit='"+data+"' class='btn btn-primary btn-xs glyphicon glyphicon-edit'>修改</a> ");
			btnGroup.append("<a href='javascript:;' data-rm='"+data+"' class='btn btn-danger btn-xs glyphicon glyphicon-trash'>删除</a> ");
			return btnGroup.children().length ? btnGroup[0].outerHTML : '暂无操作' ;
		}
	}];
	var controller = function( $scope , ctx , $stateParams , eduinstService , dictService ){
		$scope.filterMeta = [{
			'name' 	: 	'code' ,
			'label' : 	'教育局标识'
		},{
			'name' 	: 	'name' ,
			'label' : 	'教育局名称'
		},{	'name' 	: 	'type' ,
			'type' 	:	'select' ,
			'option': 	{
				options 	: 	ctx.api + 'dict/INST_TYPE',
				key 		: 	'code'
			},
			'label' 	: 	'类型'
		}];
		
		var listTable = '#data-list-table' ;
		
		setting.aoColumns = tableMeta ;
		setting.ajaxSource = ctx.api + 'eduinst' ;
		$scope.filterParam = setting.param ;
		var dataTable = $( listTable ).dataTable( setting ) ;
		$('#filter-group').delegate('[name=filter-btn]' , 'click' , function(){
			dataTable.fnDraw( true ) ;
		});
		
		$scope.view = {
			tools 	: 	{
				search 	: 	true ,
				create	:	true ,
				imports 	: 	true
			},
			create	: 	{
				positive 	: 	{
					text 	: 	'保存' ,
					disabled: 	false
				},
				title : ''
			},
			edit 	: 	{
				positive 	: 	{
					text 	: 	'保存' ,
					disabled: 	false
				},
				title : ''
			},
		};
		
		$scope.cdata = {} ;
		$scope.cFormMeta = [[{
			field  	:	'code' ,
			label 	: 	'教育局标识' ,
			validation 	:	{
				required 	:	true ,
				maxlength 	: 	30	
			},
			className 	: 	'col-xs-6'
		},{
			field  	:	'name' ,
			label 	: 	'教育局名称' ,
			validation 	:	{
				required 	:	true ,
				maxlength 	: 	30	
			},
			className 	: 	'col-xs-6'
		}],[{
			field  	:	'passwd' ,
			label 	: 	'登陆密码' ,
			type 	: 	'password' ,
			validation 	:	{
				required 	:	true ,
				maxlength 	: 	30	
			},
			className 	: 	'col-xs-6'
		},{
			field  	:	'repasswd' ,
			label 	: 	'确认密码' ,
			type 	: 	'password' ,
			validation 	:	{
				required 	:	true ,
				maxlength 	: 	30	,
				equalTo 	: 	'[name=passwd]'
			},
			className 	: 	'col-xs-6'
		}],[{
			field  	:	'type' ,
			label 	: 	'教育局类型' ,
			type 	: 	'select' ,
			option	: 	{
				options 	: 	ctx.api + 'dict/INST_TYPE' ,
				key 		: 	'code'
			},
			validation 	:	{
				required 	:	true 
			},
			className 	: 	'col-xs-6'
		},{
			field  	:	'cityId' ,
			label 	: 	'设区市' ,
			type 	: 	'select' ,
			option	: 	{
				options 	: 	ctx.api + 'city/optionCity' ,
				key 		: 	'id'
			},
			validation 	:	{
				required 	:	true 
			},
			className 	: 	'col-xs-6' ,
			hide 	:	false 
		}],[{
			field  	:	'countyId' ,
			label 	: 	'区县' ,
			type 	: 	'select' ,
			option	: 	{
				options 	: 	[] ,
				key 		: 	'id'
			},
			validation 	:	{
				required 	:	true 
			},
			className 	: 	'col-xs-6' ,
			hide 	:	false 
		},{
			field  	:	'parentId' ,
			label 	: 	'归属教育局' ,
			type 	:   'select' ,
			option 	: 	{
				options 	: 	{
					data 	: 	[]
				}
			},
			hide 	: 	true ,
			validation 	:	{
				required 	:	true ,
				maxlength 	: 	30	
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
		
		$scope.$watch('cdata.cityId' , function( n , o ){
			var county = fieldMeta( $scope.cFormMeta , 'countyId') ,
				parent = fieldMeta( $scope.cFormMeta , 'parentId' );
			if( !n ) {
				county.option.options = [] ;
				parent.option.options = [] ;
			} else{
				parent['option']['options'] = ctx.api + 'eduinst/option?type=CITY&cityId='+n ;
				county.option.options = ctx.api + 'city/optionCounty?parentId='+n ;
			}
		} , true );
		
		$scope.$watch('cdata.type' , function( n ,o ){
			var city = fieldMeta($scope.cFormMeta , 'cityId') , county = fieldMeta($scope.cFormMeta ,'countyId'),
				parent = fieldMeta( $scope.cFormMeta , 'parentId' );
			if( n == 'CITY'){
				city.hide = false ;
				county.hide = true ;
				parent.hide = false ;
				parent['option']['options'] = ctx.api + 'eduinst/option?type=PROVINCE' ;
			}else if( n == 'COUNTY' ){
				city.hide = false ;
				county.hide = false ;
				parent.hide = false ;
				parent['option']['options'] = !$scope.cdata['cityId'] ? [] :  
					ctx.api + 'eduinst/option?type=CITY&cityId='+$scope.cdata['cityId'] ;
			}else{
				city.hide = true ;
				county.hide = true ;
				parent.hide = true ;
			}
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
				console.log($scope.cdata);
				var _this = this ;
				if( !this.validator.form() )
					return false ;
				
				$scope.view.create.positive = {
					text 	: 	'保存中...' ,
					disabled: 	true
				}
				var param = formUtil.getEditableParam( $scope.cdata , $scope.cFormMeta ) ;
				console.log('--------ppp>' , param ) ;
				//---mdf:调用接口更新数据---
				eduinstService.create( param , function( data ){
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
		$scope.uFormMeta = [[{
			field  	:	'code' ,
			label 	: 	'教育局标识' ,
			validation 	:	{
				required 	:	true ,
				maxlength 	: 	30	
			},
			className 	: 	'col-xs-6' 
		},{
			field  	:	'name' ,
			label 	: 	'教育局名称' ,
			validation 	:	{
				required 	:	true ,
				maxlength 	: 	30	
			},
			className 	: 	'col-xs-6'
		}],[{
			field  	:	'type' ,
			label 	: 	'教育局类型' ,
			type 	: 	'select' ,
			option	: 	{
				options 	: 	ctx.api + 'dict/INST_TYPE' ,
				key 		: 	'code'
			},
			validation 	:	{
				required 	:	true 
			},
			className 	: 	'col-xs-6'
		},{
			field  	:	'cityId' ,
			label 	: 	'大市' ,
			type 	: 	'select' ,
			option	: 	{
				options 	: 	ctx.api + 'city/optionCity' ,
				key 		: 	'id'
			},
			validation 	:	{
				required 	:	true 
			},
			className 	: 	'col-xs-6' ,
			hide 	:	false 
		}],[{
			field  	:	'countyId' ,
			label 	: 	'区县' ,
			type 	: 	'select' ,
			option	: 	{
				options 	: 	[] ,
				key 		: 	'id'
			},
			validation 	:	{
				required 	:	true 
			},
			className 	: 	'col-xs-6' ,
			hide 	:	false 
		},{
			field  	:	'parentId' ,
			label 	: 	'归属教育局' ,
			type 	:   'select' ,
			option 	: 	{
				options 	: 	{
					data 	: 	[]
				}
			},
			hide 	: 	true ,
			validation 	:	{
				required 	:	true 
			},
			className 	: 	'col-xs-6'
		}]];
		
		$scope.$watch('udata.cityId' , function( n , o ){
			var county = fieldMeta( $scope.uFormMeta , 'countyId') ,
				parent = fieldMeta( $scope.uFormMeta , 'parentId'); 
			if( !n ) {
				parent['option']['options'] = [] ;
				county.option.options = [] ;
			}else{
				parent['option']['options'] = ctx.api + 'eduinst/option?type=CITY&cityId='+n ;
				county.option.options = ctx.api + 'city/optionCounty?parentId='+n ;
			}
		} , true );
		
		$scope.$watch('udata.type' , function( n ,o ){
			var city = fieldMeta($scope.uFormMeta , 'cityId') , 
				county = fieldMeta($scope.uFormMeta ,'countyId') ,
				parent = fieldMeta( $scope.uFormMeta , 'parentId'); 
			if( n == 'CITY'){
				city.hide = false ;
				county.hide = true ;
				parent.hide = false ;
				parent['option']['options'] = ctx.api + 'eduinst/option?type=PROVINCE' ;
			}else if( n == 'COUNTY' ){
				city.hide = false ;
				county.hide = false ;
				parent.hide = false ;
				parent['option']['options'] = !$scope.udata['cityId'] ? [] :  
					ctx.api + 'eduinst/option?type=CITY&cityId='+$scope.udata['cityId'] ;
			}else{
				city.hide = true ;
				county.hide = true ;
				parent.hide = true ;
			}
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
				eduinstService.get( this.id , function( data ){
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
				eduinstService.update( _this.id , $scope.udata , function( data ){
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
					eduinstService.remove( id , function(){
						mAlert('数据删除成功!');
						dataTable.fnDraw( false ) ;
					});
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
					eduinstService.importEdunist( formData,dataTable );
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
			return importHandler.init() ;
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
				url  	: 	ctx.api + 'eduinst/'+id ,
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
		
		this.importEdunist = function( fromData , dataTable , complete ){
			$.ajax({
				url  	: 	ctx.api + 'import/importEduinstlUser' ,
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
				url  	: 	ctx.api + 'eduinst/'+id ,
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
				url  	: 	ctx.api + 'eduinst' ,
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
				url  	: 	ctx.api + 'eduinst/'+id ,
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
	app.register.service('eduinstService' , service ) ;
	return controller ;
});