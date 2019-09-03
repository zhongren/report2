define(['validate','jquery','message'] , function(){
	var controller = function( $scope , $element ){
		var handler = {
			init 	: 	function(){
				this.element = $($element).find('input[data-role=time-input]') ;
				return this.build() ;//.applyRule() ;
			},
			applyRule: 	function(){
				if( ! $scope.rules )
					return this ;
				var rules = $scope.rules;
				rules['messages'] = '请选择日期!' ;
				if( $scope.rules['messages'] )
					rules['messages'] = $scope.rules['messages'] ;
				this.element.rules('add' , rules ) ;
				return this ;
			},
			build 	: 	function(){
				var _this = this ;
				var format = $scope.format || 'YYYY-MM-DD' ;
				var minDate = undefined , maxDate = undefined ;
				if(  minDate = $(this).attr('data-min') ){
					minDate = new Date( convertDate(minDate) ) ;
				}
				if( maxDate = $(this).attr('data-max') ){
					maxDate = new Date( convertDate(maxDate) ) ;
				}
				
				this.element.datetimepicker({
					format :	format ,
					minDate : 	minDate ,
					maxDate : 	maxDate ,
					locale:  'zh-CN'
				});
				this.element.bind('focus',function(){ 
					ngUtil.apply( $scope , function(){
						$scope.data = '' ;
					});
				}).bind('blur' , function(){
					ngUtil.apply( $scope , function(){
						$scope.data = _this.element.val() ;
					});
				});
				
				return this ;
			}
		}.init() ;
	}
	
	app.register.directive('timepicker' , function( ctx ){
		return {
			restrict 	: 	'E' ,
			replace 	: 	true ,
			scope 		: 	{
				name 	: 	'=name' ,
				rules 	: 	'=rules' ,
				data 	: 	'=data' ,
				format : 	'=format'
			},
			controller 	: 	controller ,
			templateUrl :	ctx.base + 'directive/timepicker/template.html'
		}
	});
});
