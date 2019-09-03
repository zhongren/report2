define(['app/datatable-setting' , 'directive/classImport/script' ] , function( TableConfig ){
	/*var setting = new TableConfig().setting ;
	var tableMeta = [{
		mData 	: 	'name' ,
		sTitle 	: 	'年级' ,
		sWidth 	: 	'10%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			return data ;
		}
	}
	,{
		mData 	: 	'classes' ,
		sTitle 	: 	'班级' ,
		sWidth 	: 	'10%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			return data ;
		}
	}
	,{
		mData 	: 	'nums' ,
		sTitle 	: 	'人数' ,
		sWidth 	: 	'10%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	false ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			return data ;
		}
	}];*/
	var controller = function( ctx ){
		/*var listTable = '#data-list-table' ;
		setting.aoColumns = tableMeta ;
		setting.paging = false;
		setting.info = false ;
		setting.ajaxSource = ctx.api + 'import/getSchoolClasses' ;
		var dataTable = $( listTable ).dataTable( setting ) ;
		$("#input-id").fileinput({
			uploadUrl:"/import/schoolClasses",
			allowedFileExtensions: ['xlsx','xls'],
		}).on("fileuploaded", function (event, data, previewId, index) {
			 var  code = data.response.code;
		     if(code!=0){
		    	 mAlert(data.response.message);
		    	 return ;
		     }else{
		    	 mAlert("导入成功"); 
		    	 dataTable.fnDraw( true ) ;
		     }
		     
		});*/
	}
	return controller ;
});