define([],function(){
	var controller = function( $scope , $element , ctx ){
		var fileinputHandler = {
			init 	: 	function(){
				$($element).find('[name=file]').fileinput({
					uploadUrl	:	ctx.api + "file/bulletin/upload",
					allowedFileExtensions	: 	['doc','docx','xls','xlsx','pdf'],
					language	: 	'zh', //设置语言
			        showUpload	: 	true, //是否显示上传按钮
			        showCaption	:	 true,//是否显示标题
			        browseClass	: 	"btn btn-primary", //按钮样式     
			        dropZoneEnabled	: 	true,//是否显示拖拽区域
			        maxFileSize	: 	1024 * 20,//单位为kb，如果为0表示不限制文件大小
			        maxFileCount	: 	10, //表示允许同时上传的最大文件个数
			        enctype		: 	'multipart/form-data',
			        validateInitialCount	:	true
				}).on('fileuploaded' , function( event , data ){
					console.log( '文件上传完成' , data ) ;
				});
				return this ;
			}
		}.init() ;
	}
	app.register.directive('fileInput' , function( ctx ){
		return {
			restrict 	: 	'E' ,
			replace 	: 	true ,
			scope 		: 	{
				data 	: 	'=data'
			},
			controller 	: 	controller ,
			templateUrl :  	ctx.base + 'directive/fileInput/template.html' 
		}
	});
});