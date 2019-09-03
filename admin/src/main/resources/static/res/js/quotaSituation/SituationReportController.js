define([] , function(){
	var controller = function( $scope , $timeout ){
		$scope.showIfRole = function( roles ){
			if( $scope.principal.roleCode == 'SYS' )
				return true ;
			if( !roles || !roles.length )
				return false ;
			return roles.indexOf($scope.principal.roleCode) != -1 ;
		}
		var handler = {
			data: {
				
			},
			init 	: 	function(){
				return this.selectPicker() ;
			},
			selectPicker 	: 	function(){
				var _this = this ;
				$('select[data-ajax]').each( function(){
					var elem = this ;
					$(elem).on('change' , function( e ){
						$(this).trigger('selectpicker.change' , [$(this).val()]) ;
						_this.validate( this ) ;
					});
					$( elem ).on('selectpicker.onload selectpicker.change' , function( e , data ){
						var group = $(this).parents('[data-group]').attr('data-group') ;
						if( !group ) return ;
						if( !_this.data[group] )
							_this.data[group] = {} ;
						_this.data[group][ $(this).attr('name') ] = $(this).val() ;
					}) ;
					if( !$(elem).attr('data-parent') ){
						_this.buildSelect( elem , function(){
							$(elem).attr('data-live-search' , true ).selectpicker('refresh') ;
							$(elem).trigger('selectpicker.onload' , [$(elem).val()] ) ;
						} );
						return ;
					}
					!function( elem ){
						$( $(elem).attr('data-parent') ).on('selectpicker.onload selectpicker.change' , function( e , data ){
							var paramKey = $(elem).attr('data-param') || 'parentId' , paramData = {};
							paramData[paramKey] = data || 0;
							_this.buildSelect( elem , function(){
								$(elem).attr('data-live-search' , true ).selectpicker('refresh') ;
								$(elem).trigger('selectpicker.onload') ;
							} , paramData );
						});
					}( elem ) ;
				});
				$('[data-download-btn]').on('click' , function(){
					var validated = true;
					$(this).parents('[data-group]').find('select').each( function(){
						var tv = _this.validate( this ) ;
						validated = validated && tv ;
					});
					if( !validated ) return ;
					var group = $(this).parents('[data-group]').attr('data-group') ,
					exportUrl = $(this).attr('data-export-url');
					if( !group || !exportUrl ) return ;
					downloadHandler.download( exportUrl , _this.data[group] ) ;
				});
				return this ;
			},
			validate 		: 	function( element ){
				if( $(element).attr('data-required') == 'false' )
					return true ;
				if( !$(element).val() ){
					$(element).parents('[data-error]').addClass('error').attr('title' , '请选择条件!') ;
					return false ;
				}
				$(element).parents('[data-error]').removeClass('error').attr('title' , '') ; ;
				return true ;
			},
			loadOption 		: 	function( url , data , then ){
				$.ajax( {
					url  	: 	url ,
					data 	: 	data || {} ,
					success	: 	function( result ){
						if( result.code != 0 )
							return mAlert( result.message ) ;
						then( result.data ) ;
					}
				});
			},
			buildSelect 	: 	function( elem , then , paramData ){
				var url = $(elem).attr('data-ajax') , 
					keyName = $(elem).attr('data-key') || 'id' ,
					text = $(elem).attr('data-text') || 'name' , 
					title= $(elem).attr('data-title') || '请选择' , 
					_this = this ;
				$(elem).empty().append("<option value=''>数据加载中,请稍后...</option>") ;
				this.loadOption( url , paramData , function( data ){
					$(elem).empty() ;
					if( !data || !data.length ){
						$(elem).append("<option value=''>暂无数据</option>") ;
						return then() ;
					}
					if( data.length > 1 && title )
						$(elem).append("<option value=''>"+title+"</option>") ;
					
					for( var key = 0 ; key < data.length ; key ++ ){
						$(elem).append( _this.createOption( {
							id 	: 	data[key][keyName] ,
							name: 	_this.getFormatData( text , data[key] ) 
						} ) ) ;
					}
					if( $(elem).find('option').length == 1 ){
						$(elem).find('option:first').attr('selected' , true ) ;
						$(elem).parent().hide() ;
					}else{
						$(elem).parent().show() ;
					}
					return then() ;
				});
			},
			createOption 	: 	function( data ){
				return $('<option></option>').attr('value' , data['id'] ).text( data['name'] ) ;
			},
			getFormatData 	: 	function( format , data ){
				var formatMap = undefined , regexp = /(\$[a-zA-Z0-9_]+)/g ;
				while( rs = regexp.exec( format ) ){
					if( !formatMap ) formatMap = {} ;
					formatMap[ rs[0] ] = data[ rs[0].replace('$','') ] ;
				}
				if( !formatMap ) {
					formatMap = {
						format 	: 	data[format]	
					}
				}
				for( var key in formatMap ){
					var value = formatMap[key] ;
					if( typeof value == 'undefined' ){
						value = '' ;
					}
					format = format.replace(key , value ) ;
				}
				format = format.replace(/(\$[a-zA-Z0-9_]+)/g , '') ;
				return format ;
			}
		} ;
		
		var downloadHandler = {
			download 	: 	function( url , param ){
				url = this.handlePath( url , param ) || 'javascript:;';
				console.log('开始下载' , url , param ) ;
				var request = '' ;
				for( var key in param ){
					request += ( key + '=' + param[key] + '&' ) ;
				}
				request = request.replace(/&\s*$/g , '' ) ;
				console.log('请求地址:' , url + '?' + request ) ;
				location.href = url + '?' + request ;
			},
			handlePath 	:	function( path , param ){
				if( !param || !path ) return path ;
				var group , exp = /\{\s*([a-zA-Z0-9_]+)\s*\}/g , vars = [];
				while( ( group = exp.exec( path ) ) )
					vars.push([group[0] , group[1]]) ;
				if( !vars || !vars.length )
					return path ;
				for( var i = 0 ; i < vars.length ; i ++ ){
					if( typeof param[ vars[i][1] ] == 'undefined' )
						continue ;
					path = path.replace( vars[i][0] , param[ vars[i][1] ] )  ;
				}
				console.log( path ) ;
				return this.handlePath( path , param ) ;
			}
		}
		
		$timeout( function(){
			handler.init() ;
		} , 1 , true ) ;
		
	}
	return controller ;
});