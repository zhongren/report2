define(['app/datatable-setting' ,'app/sys/CityService' , 'app/sys/DictService'] , function( TableConfig ){
	var setting = new TableConfig().setting ;
	var tableMeta = [{
		mData 	: 	'eduinstName' ,
		sTitle 	: 	'教育局名称' ,
		sWidth 	: 	'10%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			return data ;
		}
	},{
		mData 	: 	'code' ,
		sTitle 	: 	'标识' ,
		sWidth 	: 	'5%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			return data ;
		}
	},{
		mData 	: 	'countyId' ,
		sTitle 	: 	'归属地' ,
		sWidth 	: 	'10%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			var cityName = row['cityName'] ? row['cityName'] : '' ;
			var countyName =  row['countyName'] ?  row['countyName'] : '' ;
			return cityName + countyName ;
		}
	},{
		mData 	: 	'submitTime' ,
		sTitle 	: 	'提审日期' ,
		sWidth 	: 	'10%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	true ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			return data ;
		}
	},{
		mData 	: 	'statusName' ,
		sTitle 	: 	'账号审核状态' ,
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
		mData 	: 	'auditTime' ,
		sTitle 	: 	'账号审核日期' ,
		sWidth 	: 	'10%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	true ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			return data ;
		}
	},{
		mData 	: 	'processStatusName' ,
		sTitle 	: 	'填报审核状态' ,
		sWidth 	: 	'5%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			return data ;
		}
	},{
		mData 	: 	'subaccOperation' ,
		sTitle 	: 	'操作' ,
		sWidth 	: 	'10%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			var data = row['eduinstId'] ;
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			var btnGroup = $('<div></div>').addClass('btn-group') ;
			if(row['status'] != 1 && [1,2].indexOf( row['processStatus'] ) != -1 )
				btnGroup.append("<a href='#/eduinstAuditing/"+data+"/reporting' data-audit='"+data+"' class='btn btn-primary btn-xs glyphicon glyphicon-edit'>填报审核</a> ");
			return btnGroup.children().length ? btnGroup[0].outerHTML : '暂无操作' ;
		}
	},{
		mData 	: 	'primaryOperation' ,
		sTitle 	: 	'操作' ,
		sWidth 	: 	'10%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			var data = row['eduinstId'] ;
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			var btnGroup = $('<div></div>').addClass('btn-group') ;
			if([1,2].indexOf( row['processStatus'] ) != -1 )
				btnGroup.append("<a href='#/eduinstAuditing/"+data+"/reporting' data-audit='"+data+"' class='btn btn-primary btn-xs glyphicon glyphicon-edit'>填报审核</a> ");
			return btnGroup.children().length ? btnGroup[0].outerHTML : '暂无操作' ;
		}
	}];
	var controller = function( $scope , ctx , $stateParams , eduinstAuditingService , cityService , dictService ){
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
			'label' : 	'教育局名称'
		},{
			'name' 	: 	'eduinstCode' ,
			'label' : 	'教育局编码'
		},{	'name' 	: 	'status' ,
			'type' 	:	'select' ,
			'option': 	{
				options 	: 	ctx.api + 'dict/AUDIT_STATUS',
				key 		: 	'code'
			},
			'label' 	: 	'账号审核状态'
		},{	'name' 	: 	'processStatus' ,
			'type' 	:	'select' ,
			'option': 	{
				options 	: 	ctx.api + 'dict/EDUINST_PROCESS_STATUS',
				key 		: 	'code'
			},
			'label' 	: 	'教育局填报状态'
		}];
		
		ngUtil.apply( $scope , function(){
			var subaccFilterFields = ['name' , 'eduinstCode' , 'status'] ,
				primaryFilterFields = ['name' , 'eduinstCode' ,'processStatus'] ,
				inFilterFields = $scope.principal['roleCode'] == 'CITY' ? primaryFilterFields : subaccFilterFields ; 
			$scope.filterMeta = handleMeta( $scope.filterMeta , 'name' , inFilterFields ) ;
		});
		
		var subaccTableFields = ['eduinstName','code','countyId','statusName','auditTime','subaccOperation'] ,
			primaryTableFields = ['eduinstName','code','countyId','processStatusName','auditTime','primaryOperation'] ,
			inTableFields = $scope.principal['roleCode'] == 'CITY' ? primaryTableFields : subaccTableFields ;;
		tableMeta = handleMeta( tableMeta , 'mData' , inTableFields ) ;
		
		var listTable = '#data-list-table' ;
		
		setting.aoColumns = tableMeta ;
		setting.ajaxSource = ctx.api + 'eduinstAuditing' ;
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
	app.register.service('eduinstAuditingService' , service ) ;
	return controller ;
});