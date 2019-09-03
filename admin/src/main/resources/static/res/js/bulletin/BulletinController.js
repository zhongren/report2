define(['app/datatable-setting' ,'app/sys/CityService' , 'app/sys/DictService'] , function( TableConfig ){
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
			return data || '系统发送' ;
		}
	},{
		mData 	: 	'readNum' ,
		sTitle 	: 	'阅读次数' ,
		sWidth 	: 	'10%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		show 	: 	['eduinst','school'] ,
		render 	: 	function( data , st , row ){
			return data ;
		}
	}];
	
	
	var controller = function( $scope , ctx , $stateParams , bulletinService , cityService , dictService ){
		$scope.filterMeta = [{
			'name' 	: 	'title' ,
			'label' : 	'标题'
		},{	'name' 	: 	'type' ,
			'type' 	:	'select' ,
			'option': 	{
				options 	: 	ctx.api + 'dict/BULLETIN_TYPE',
				key 		: 	'code'
			},
			'label' 	: 	'类型'
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
				create 	: 	false ,
				exports : 	false ,
				imports : 	false
			}
		};
		
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
	app.register.service('bulletinService' , service ) ;
	return controller ;
});