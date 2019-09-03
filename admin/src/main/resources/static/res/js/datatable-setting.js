define(['jquery' , 'datatable'] , function(){
	var dtoUtil = {
		postDT 	:　	function( sSource , param , aoData , fnCallback ){
			dtoUtil.request = aoData ;
			var standardParam = dtoUtil.standardReqDT( aoData ) ;
			if( param ){
				for( var key in param ){
					standardParam[ key ] = param[key] ;
				};
			}
			console.log('------search param----------\n' , standardParam , param , !param ) ;
			$.ajax({
				url 	: 	sSource ,
				type 	: 	'GET' ,
				async 	: 	true ,
				data 	: 	standardParam ,
				success : 	function( result , status , xmlHttp ){
					if( result.code != 0 ){
						fnCallback( dtoUtil.handleRsult( {} ) ) ;
						return mAlert( result.message ) ;
					}
					console.log( result.data )
					fnCallback( dtoUtil.handleRsult( result ) ) ;
				},
				error	: 	function( xmlHttp , status ){
					mAlert('服务器请求出错!') ;
					fnCallback({}) ;
				}
			});
		},
		standardReqDT 	: 	function( aoData ){
			if( !aoData || !aoData.length ){
				return aoData ;
			}
			var temp = {} ;
			for( var key in aoData ){
				temp[aoData[key].name] = aoData[key].value ;
			}
			var standardParam = {
				size 	: 	temp['iDisplayLength'] ,
				page  	:　	temp['iDisplayStart'] / temp['iDisplayLength'] + 1,
				orderField 	: 	temp['mDataProp_'+temp['iSortCol_0']] ,
				orderType 	: 	temp['sSortDir_0']
			};
			return standardParam ;
		},
		handleRsult 	: 	function( result ){
			var preview = dtoUtil.request ;
			var aoData = {
				sEcho 	: 	preview.sEcho + 1,
				iDisplayStart 	: 	preview.iDisplayStart ,
				iDisplayLength 	: 	preview.iDisplayLength ,
				iTotalRecords 	: 	result.extra ? result.extra : 0 ,
				iTotalDisplayRecords 	: 	result.extra ? result.extra : 0 ,
				aaData 	: 	result.data ? result.data : [] 
			};
			return aoData ;
		}
	};
	var TableConfig = function(){
		var _this = this;
		this.setting = {
			oLanguage 	: 	{
				"sLengthMenu" : "每页显示 _MENU_ 条记录",
				"sZeroRecords" : "没有检索到数据",
				"sInfo" : "共有 _TOTAL_ 条记录",
				"sInfoEmtpy" : "没有数据",
				"sProcessing" : "加载中...请稍等",
				"search" : "查询",
				"sInfoEmpty" : "共 0 条记录",
				"bStateSave" : false,
				"oPaginate" : {
					"sFirst" : "首页",
					"sPrevious" : "前页",
					"sNext" : "后页",
					"sLast" : "尾页"
				}
			},
			bPaginate 			: 	true ,
			sPaginationType 	: 	'full_numbers' ,
			bJQueryUI 			: 	false ,
			bAutoWidth			: 	false ,
			bProcessing			: 	true ,
			sServerMethod 		: 	'POST' ,
			bServerSide			: 	true ,
			bFilter				: 	false ,
			bStateSave 			: 	true ,
			aLengthMenu 		: 	[ [ 10, 25, 50 ], [ 10, 25, 50 ] ] ,
			aaSorting 			: 	[[0,'desc']] ,
			fnDrawCallback 		: 	function(){
				//console.log('Data Table CallBack ...') ;
				
			},
			fnInitComplete		: 	function( oSetting , json ){
				//console.log('Data Table Init Complete ...') ;
			},
			fnRowCallback 		: 	function( nRow , aData , iDisplayIndex ){
				//console.log( 'Data Table Row Call Back ...') ;
			},
			fnServerData 		: 	function( sSource , aoData , fnCallback ){
				console.log('---settingparam---' , _this.setting )
				dtoUtil.postDT( sSource  , _this.setting.param , aoData , fnCallback ) ;
			},
			param : 	{}
		} ;
	}
	return TableConfig ;
});