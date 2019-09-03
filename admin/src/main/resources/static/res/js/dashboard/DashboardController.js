define(['directive/countySchoolReportingChart/script' , 'directive/cmpNotice/script'] , function(){
	var controller = function( ctx , $scope  ){
		$scope.module = {
			'reportingChart' 	: 	false 
		}
		$scope.$watch('principal' , function( n , o ){
			$scope.module['reportingChart'] = ['COUNTY_SUBACC','COUNTY'].indexOf( n['roleCode'] ) != -1 ;
		});
	}
	return controller ;
});