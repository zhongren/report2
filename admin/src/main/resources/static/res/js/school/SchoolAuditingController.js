define(['app/datatable-setting' ,'app/sys/CityService' , 'app/sys/DictService'] , function( TableConfig ){
	var controller = function( $scope , ctx , $stateParams , schoolAuditingService , cityService , dictService ){
		var tableMeta = [{
			mData 	: 	'schoolName' ,
			sTitle 	: 	'学校名称' ,
			sWidth 	: 	'10%' ,
			sClass 	: 	'center nowrap' ,
			bSortable	: 	false ,
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
			mData 	: 	'regionTypeName' ,
			sTitle 	: 	'城乡类型' ,
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
			mData 	: 	'statusName' ,
			sTitle 	: 	'审核状态' ,
			sWidth 	: 	'10%' ,
			sClass 	: 	'center nowrap' ,
			bSortable	: 	false ,
			render 	: 	function( data , st , row ){
				if( !row['processStatus'] )
					data = '未提交' ;
				data = typeof data == 'undefined' || data == '' ? '--' : data ;
				return data ;
			}
		},{
			mData 	: 	'processStatusName' ,
			sTitle 	: 	'填报状态' ,
			sWidth 	: 	'10%' ,
			sClass 	: 	'center nowrap' ,
			bSortable	: 	false ,
			render 	: 	function( data , st , row ){
				data = typeof data == 'undefined' || data == '' ? '--' : data ;
				return data ;
			}
		},{
			mData 	: 	'auditTime' ,
			sTitle 	: 	'审核日期' ,
			sWidth 	: 	'10%' ,
			sClass 	: 	'center nowrap' ,
			bSortable	: 	false ,
			render 	: 	function( data , st , row ){
				data = typeof data == 'undefined' || data == '' ? '--' : data ;
				return data ;
			}
		},{
			mData 	: 	'subAccOperation' ,
			sTitle 	: 	'操作' ,
			sWidth 	: 	'10%' ,
			sClass 	: 	'center nowrap' ,
			bSortable	: 	false ,
			render 	: 	function( data , st , row ){
				var data = row['schoolId'] ;
				data = typeof data == 'undefined' || data == '' ? '--' : data ;
				var btnGroup = $('<div></div>').addClass('knit-btn-group') ;
				if( row['status'] != 1 && [1,2].indexOf( row['processStatus'] ) != -1 )
					btnGroup.append("<a href='#/schoolAuditing/"+data+"/reporting' data-audit='"+data+"' class='btn btn-primary btn-xs glyphicon glyphicon-edit'>填报审核</a> ");
				return btnGroup.children().length ? btnGroup[0].outerHTML : '暂无操作' ;
			}
		},
		{
			mData 	: 	'primaryOperation' ,
			sTitle 	: 	'操作' ,
			sWidth 	: 	'10%' ,
			sClass 	: 	'center nowrap' ,
			bSortable	: 	false ,
			render 	: 	function( data , st , row ){
				var data = row['schoolId'] ;
				data = typeof data == 'undefined' || data == '' ? '--' : data ;
				var btnGroup = $('<div></div>').addClass('knit-btn-group') ;
				if( ( [ 'COUNTY','COUNTY_SUBACC'].indexOf($scope.principal['roleCode']) != -1 
						&& [1,2,3].indexOf( row['processStatus'] ) != -1 )
							|| ( 'CITY' == $scope.principal['roleCode'] && row['processStatus'] == 5 ) )
					btnGroup.append("<a href='#/schoolAuditing/"+data+"/reporting' data-audit='"+data+"' class='btn btn-primary btn-xs glyphicon glyphicon-edit'>填报审核</a> ");
				return btnGroup.children().length ? btnGroup[0].outerHTML : '暂无操作' ;
			}
		}];
		var setting = new TableConfig().setting ;
		var handleMeta = function( meta , key , fields ){
			if( !meta || !key || !fields )
				return ;
			var result = [] ;
			meta.forEach( function( d , k ){
				if( fields.indexOf( d[key] ) != -1 )
					result.push( d ) ;
			} );
			return result ;
		}
		$scope.filterMeta = [{
			'name' 	: 	'name' ,
			'label' : 	'学校名称'
		},{
			'name' 	: 	'schoolCode' ,
			'label' : 	'学校编码'
		},{	'name' 	: 	'auditStatus' ,
			'type' 	:	'select' ,
			'option': 	{
				options 	: 	ctx.api + 'dict/AUDIT_STATUS',
				key 		: 	'code'
			},
			'label' 	: 	'账号审核状态'
		},{	'name' 	: 	'processStatus' ,
			'type' 	:	'select' ,
			'option': 	{
				options 	: 	ctx.api + 'dict/SCHOOL_PROCESS_STATUS',
				key 		: 	'code'
			},
			'label' 	: 	'学校填报状态'
		}];
		
		ngUtil.apply( $scope , function(){
			var subaccFields = ['name','schoolCode','auditStatus'] ,
				primaryFields = ['name','schoolCode' , 'processStatus'] , 
				fields =['COUNTY','CITY'].indexOf( $scope.principal['roleCode'] )!=-1 ? primaryFields : subaccFields ;
			$scope.filterMeta = handleMeta( $scope.filterMeta , 'name' , fields ) ;
		});
		
		var subaccTableFields = ['schoolName' , 'code' , 'typeName' , 'regionTypeName' , 'statusName' , 'auditTime' , 'subAccOperation'] ,
			primaryTableFields = ['schoolName' , 'eduinstName' , 'code' , 'typeName' , 'regionTypeName' , 'processStatusName' , 'auditTime' , 'primaryOperation' ] ,
			inTableFields = ['COUNTY','CITY'].indexOf($scope.principal['roleCode']) != -1  ? primaryTableFields : subaccTableFields ;
		tableMeta = handleMeta( tableMeta , 'mData' , inTableFields ) ;
		
		var listTable = '#data-list-table' ;
		
		setting.aoColumns = tableMeta ;
		setting.ajaxSource = ctx.api + 'schoolAuditing' ;
		$scope.filterParam = setting.param ;
		
		var dataTable = $( listTable ).dataTable( setting ) ;
		$('#filter-group').delegate('[name=filter-btn]' , 'click' , function(){
			dataTable.fnDraw( true ) ;
		});
		
		!function(){
			var initStack = [ ] ;
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
	}
	app.register.service('schoolAuditingService' , service ) ;
	return controller ;
});