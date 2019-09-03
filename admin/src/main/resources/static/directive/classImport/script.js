define(['app/datatable-setting' ] , function( TableConfig ){
	var setting = new TableConfig().setting ;
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
	}];
	var controller = function($scope , $element , ctx ){
		console.log('collectionId' , $scope.param ) ;
		var listTable = $($element).find('[name=data-list-table]') ;
		setting.aoColumns = tableMeta ;
		setting.paging = false;
		setting.info = false ;
		setting.ajaxSource = ctx.api + 'import/getSchoolClasses';
		var dataTable = $( listTable ).dataTable( setting ) ;
		$($element).find("[name=schoolClassFile]").fileinput({
			uploadUrl:"/import/"+$scope.param+"/schoolClasses",
			allowedFileExtensions: ['xlsx','xls'],
			dropZoneEnabled : false ,
			showPreview :false ,
			language	: 	'zh', //设置语言
	        showUpload	: 	true, //是否显示上传按钮
	        showCaption	:	 true,//是否显示标题
	        browseClass	: 	"btn btn-primary", //按钮样式     
	        dropZoneEnabled	: 	true,//是否显示拖拽区域
	        enctype		: 	'multipart/form-data',
	        validateInitialCount	:	true
		}).on("fileuploaded", function (event, data, previewId, index) {
			 var  code = data.response.code;
		     if(code!=0){
		    	 mAlert(data.response.message);
		    	 return ;
		     }else{
		    	 mAlert("导入成功"); 
		    	 dataTable.fnDraw( true ) ;
		     }
		});
	};
	app.register.directive('classImport' , function( ctx ){
		return {
			restrict  	: 	'E' ,
			replace 	: 	true ,
			scope 		: 	{
				param 	: 	'=param' 
			},
			controller 	: 	controller ,
			templateUrl	: 	ctx.base + 'directive/classImport/template.html' 
		}
	});
});