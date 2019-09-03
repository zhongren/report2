define([],function(){
	var controller = function( $scope , $element ){
		$scope.eval = function( script ){
			if( !script || /^\s*$/.test( script ) )
				return ;
			try{ return eval( script ) } catch( e ){}
			return ;
		}
		
		$scope.inputs = [] ;
		var action = {
			isHide 	: 	function( script ){
				var repx = /\$([a-zA-Z0-9_]+)/g ,  repxGroup  ;
				while( ( repxGroup = repx.exec( script ) ) ){
					var value = $scope['data']['value'][repxGroup[1]] && $scope['data']['value'][repxGroup[1]].value ;
					if( typeof value == 'undefined')
						value = "" ;
					value = "'"+value+"'" ;
					script = script.replace( repxGroup[0] , value ) ;
				}
				try{
					if( eval( script ) ) return false ;
				}catch( e ) {
					console.error('脚本'+script+'执行出错!' , e ) ;
				} 
				return true ;
			},
			show 	: 	function( input , script ){
				input['hide'] = this.isHide( script ) ;
				input['fieldMeta']['validation']['rules']['required'] = !input['hide'];
				if( input['hide'] && $scope['data']['value'] &&
						$scope['data']['value'][input['name']] && $scope['data']['value'][input['name']]['value'] )
					ngUtil.apply($scope,function(){
						delete $scope['data']['value'][input['name']]['value'] ;
					});
			}
		}
		
		var groupHandler = {
			events 		: 	[] ,
			splitInput 	: 	function( meta ){
				var _this = this ;
				if( !$scope.data['value'] )
					$scope.data['value'] = {} ;
				var inputs = [] ;
				meta['option'].forEach( function( d , k ){
					if( !$scope.data['value'][d['name']] )
						$scope.data['value'][d['name']] = {value:'',name:d['name']};
					var input = {
						title 	: 	d['title'] ,
						info 	: 	d['info'] ,
						name 	: 	d['name'] ,
						fieldMeta 	: 	{
							name 	: 	meta['name']+'_'+d['name'] ,
							validation 	: 	meta['validation'] ? meta['validation'][d['name']] : {} ,
							type 	: 	d['valueType'] ,
							note    :   d['note'],
							option 	: 	d['valueOpt'] 
						},
						data 		: 	$scope.data['value'][d['name']]	
					}
					
					if( d['show'] ){
						_this.events.push( {
							script 	: 	d['show'] ,
							input 	: 	input
						} ) ;
					}
					
					inputs.push( input ) ;
					ngUtil.syncExec( function(){
						if( $('[name='+input['fieldMeta']['name']+']').length && 
								meta['validation'] && meta['validation'][d['name']] && meta['validation'][d['name']]['rules'] ){
							$('[name='+input['fieldMeta']['name']+']').rules('add' , meta['validation'][d['name']]['rules'] ) ;
							return true ;
						}
						return false ;
					});
				});
				return inputs ;
			},
			getInputs 	: 	function( meta ){
				var _this = this ;
				ngUtil.apply($scope , function(){
					$scope.inputs = _this.splitInput( meta ) ;
					_this.trigger()
				});
				return this ;
			},
			trigger 	: 	function( ){
				if( !this.events || !this.events.length )
					return ;
				var _this = this ;
				$scope.$watch( 'data.value' , function( n ){
					_this.events.forEach( function( d , k ){
						action.show( d['input'] , d ['script'] ) ;
					});
				},true) ;
				return this ;
			}
		}.getInputs( $scope.meta ) ;
	}
	
	app.register.directive('groupInput' , function( ctx ){
		return {
			restrict 	: 	'E' ,
			replace 	: 	true ,
			scope 		: 	{
				meta 	: 	'=meta' ,
				data 	: 	'=data' ,
				formContext 	: 	'=formContext'
			},
			controller 	: 	controller ,
			templateUrl : 	ctx.base + 'directive/groupInput/template.html' 
		}
	});

});