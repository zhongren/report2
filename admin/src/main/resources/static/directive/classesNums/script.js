define(['app/datatable-setting' ] , function( TableConfig ){
	var tableMeta = [{
		mData 	: 	'name' ,
		sTitle 	: 	'年级' ,
		sWidth 	: 	'10%' ,
		sClass 	: 	'center nowrap' ,
		bSortable	: 	true ,
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
		bSortable	: 	true ,
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
		bSortable	: 	true ,
		render 	: 	function( data , st , row ){
			data = typeof data == 'undefined' || data == '' ? '--' : data ;
			return data ;
		}
	}];
	var controller = function($scope , $element , ctx ){
		var setting = new TableConfig().setting ;
		var listTable = $($element).find('[name=data-list-table]') ;
		setting.aoColumns = tableMeta ;
		setting.paging = false;
		setting.info = false ;
		setting.ajaxSource = ctx.api + 'schoolClass/'+$scope.schoolId+'/classes' ;
		var dataTable = $( listTable ).dataTable( setting ) ;
	};
	app.register.directive('classesNums' , function( ctx ){
		return {
			restrict  	: 	'E' ,
			replace 	: 	true ,
			scope 		: 	{
				schoolId 	: 	'=schoolId' 
			},
			controller 	: 	controller ,
			templateUrl	: 	ctx.base + 'directive/classesNums/template.html' 
		}
	});
});