define([],function(){
	var controller = function( $scope , $element ){
		var dataHandler = {
			handleGroupData 	: 	function(){
				var items = [] ;
				try{
					$scope.item['content'] = JSON.parse( $scope.item['content'] ) ;
					$scope.item['valueOpt'] = JSON.parse( $scope.item['valueOpt'] ) ;
					$scope.item['valueOpt'].forEach( function( d , k ){
						var item = {
							name 		: 	d['name'] ,
							subTitle 	: 	d['title'] ,
							note 		: 	d['info'] ,
							valueType	: 	d['valueType'],
							valueOpt	: 	d['valueOpt']
						}
						if( $scope.item['content'] && $scope.item['content'][d['name']]  )
							item['content'] = $scope.item['content'][d['name']]['value'] ;
						items.push( item ) ;
					});
					ngUtil.apply( $scope , function(){
						$scope.item['group'] = items ;
					});
				}catch( e ){
					console.error('Handle group data ['+$scope.item['valueOpt']+'] error')  ;
				}
			},
			handleClassImportData 	: 	function(){
				try{
					$scope.item['content'] = JSON.parse( $scope.item['content'] ) ;
				}catch( e ){ }
			},
			handleData 	: 	function(){
				if( !$scope.item )
					return ;
				switch( $scope.item['valueType'] ){
				case 	'group' 	: 	
					this.handleGroupData() ; break ;
				case 	'class-import' 	: 	
					this.handleClassImportData() ; break ;
				}
				return this ;
			}
		}.handleData() ;
	}
	app.register.directive('collectionDisplay' , function( ctx ){
		return {
			restrict 	: 	'E' ,
			replace 	: 	true ,
			scope 		: 	{
				item 	: 	'=item' 
			},
			controller 	: 	controller,
			templateUrl : 	ctx.base + 'directive/collectionDisplay/template.html' 
		}
	});
});