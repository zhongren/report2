define(['app/datatable-setting' ,'app/sys/CityService' , 'app/sys/DictService'] , function( TableConfig ){
	var setting = new TableConfig().setting ;
	var tableMeta = [{
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
		mData 	: 	'eduinstId' ,
		sTitle 	: 	'填报进度(总/完成/进度)' ,
		sWidth 	: 	'10%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			var totalNum = row['totalNum'] ? row['totalNum'] : 0 ;
			var finishNum = row['finishNum'] ? row['finishNum'] : 0 ;
			var rate = totalNum == 0 ? 0 : ( ( finishNum / totalNum ) * 100 ).toFixed( 2 ) ;
			return totalNum + '/' + finishNum + '/' + rate +'%';
		}
	},{
		mData 	: 	'auditTime' ,
		sTitle 	: 	'审核日期' ,
		sWidth 	: 	'10%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	true ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			return data ;
		}
	},{
		mData 	: 	'processStatusName' ,
		sTitle 	: 	'审核状态' ,
		sWidth 	: 	'5%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			return data ;
		}
	},{
		mData 	: 	'eduinstId' ,
		sTitle 	: 	'操作' ,
		sWidth 	: 	'10%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			var btnGroup = $('<div></div>').addClass('knit-btn-group') ;
			if([1,2,3,4,5].indexOf( row['processStatus'] ) != -1 ){
				btnGroup.append("<a href='#/eduinstAuditing/"+data+"/reportingData' data-audit='"+data+"' class='btn btn-primary btn-xs glyphicon glyphicon-edit'>填报数据</a> ");
				btnGroup.append("<a href='javascript:;' data-dowload='"+data+"' class='btn btn-primary btn-xs glyphicon glyphicon-edit'>下载</a> ");
			}
			return btnGroup.children().length ? btnGroup[0].outerHTML : '暂无操作' ;
		}
	}];
	var controller = function( $scope , ctx , $stateParams , eduinstReportingService , cityService , dictService ){
		$scope.filterMeta = [{
			'name' 	: 	'eduinstName' ,
			'label' : 	'教育局名称'
		},{
			'name' 	: 	'code' ,
			'label' : 	'教育局标识'
		},{	'name' 	: 	'processStatus' ,
			'type' 	:	'select' ,
			'option': 	{
				options 	: 	ctx.api + 'dict/PROCESS_STATUS',
				key 		: 	'code'
			},
			'label' 	: 	'填报状态'
		}];
		
		var listTable = '#data-list-table' ;
		
		setting.aoColumns = tableMeta ;
		setting.ajaxSource = ctx.api + 'eduinstAuditing/reportingData' ;
		$scope.filterParam = setting.param ;
		
		var dataTable = $( listTable ).dataTable( setting ) ;
		$('#filter-group').delegate('[name=filter-btn]' , 'click' , function(){
			dataTable.fnDraw( true ) ;
		});
		
		var dowloadHandler = {
			init 	: 	function(){
				$( listTable ).delegate('[data-dowload]' , 'click' , function(){
					location.href=ctx.api + 'eduinstAuditing/reportingData/'+$(this).attr('data-dowload')+'/export' ;
				});
				return this ;
			}
		}
		
		!function(){
			var initStack = [ function(){
				return dowloadHandler.init() ;
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
	}
	app.register.service('eduinstReportingService' , service ) ;
	return controller ;
});