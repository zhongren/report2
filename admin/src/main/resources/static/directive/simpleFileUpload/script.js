define([],function(){
	var controller = function( $scope , $element ){
		
	}
	app.register.directive('simpleFileUpload' , function( ctx ){
		return {
			restrict 	: 	'E' ,
			replace 	: 	true ,
			scope 		: 	{
				meta 	: 	'=meta' ,
				data 	: 	'=data'
			},
			controller 	: 	controller ,
			templateUrl : 	ctx.base + 'directive/simpleFileUpload/template.html' 
		}
	});
});