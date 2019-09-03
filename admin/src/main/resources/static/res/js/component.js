define(['angular' , 'jquery'] , function(){
	
	var directive = function( app ){
		var cmpPath = 'res/html/component/' ;
		var template = function( e ){
			return $( '#' + e[0].tagName.toLowerCase() +'[type=template]' ).html() ;
		}
		// top .
		app.directive('knitTop' , function( ctx ){
			return {
				restrict 	: 	'E' ,
				templateUrl : 	ctx.base + cmpPath + 'public/top.html' ,
				scope 		: 	false ,
				replace 	: 	true
			} 
		})
		.directive('knitSide' , function( ctx ){
			return {
				restrict  	: 	'E' ,
				templateUrl	: 	ctx.base + cmpPath + 'public/side.html' ,
				scope		: 	false ,
				replace 	: 	true
			}
		})
		.directive('knitMain' , function( ctx ){
			return {
				restrict  	: 	'E' ,
				templateUrl	: 	ctx.base + cmpPath + 'public/main.html' ,
				scope		: 	false ,
				replace 	: 	true
			}
		})
		.directive('breadcrumb' , function( ){
			return {
				restrict 	: 	'E' ,
				replace 	: 	true ,
				template 	: 	template ,
				scope 		: 	false
			}
		})
		.directive('knitBottom' , function( ctx ){
			return {
				restrict  	: 	'E' ,
				templateUrl	: 	ctx.base + cmpPath + 'public/bottom.html' ,
				scope		: 	false ,
				replace 	: 	true
			}
		})
		.directive('knitIcon' , function( ctx ){
			return {
				restrict  	: 	'E' ,
				template 	: 	template,
				link 		: 	function( d1 , d2 , d3 ){
					console.log('---', d1 , d2 , d3 )
				},
				scope  		: 	false
			}
		})
		.directive('template' , function(){
			return {
				restrict  	: 	'E' ,
				template 	: 	function( e ){
					return "<script type='template'>"+ e[0].innerHTML+"</script>" ;
				},
				transclude 	: 	true ,
				replace 	: 	true ,
				scope 		: 	{}
			}
		})
		.directive('mAlert' , function( ctx ){
			return {
				restrict  	: 	'E' ,
				templateUrl : 	ctx.base + cmpPath + 'public/modalAlert.html' ,
				replace 	: 	true ,
				scope 		: 	{}
			}
		})
		.directive('mConfirm' , function( ctx ){
			return {
				restrict  	: 	'E' ,
				templateUrl : 	ctx.base + cmpPath + 'public/modalConfirm.html' ,
				replace 	: 	true ,
				scope 		: 	{}
			}
		})
		.directive('modalDialog' , function( ctx ){
			return {
				restrict  	: 	'E' ,
				templateUrl : 	ctx.base + cmpPath + 'public/modalDialog.html' ,
				replace 	: 	true ,
				transclude 	: 	true ,
				scope 		: 	{
					meta 	: 	'=meta' ,
					style 	: 	'@style'
				},
				controller 	: 	function( $scope ){
					var meta = {
						title 	: 	'' ,
						positive: 	{
							text 	: 	'确定' ,
							disabled: 	false
						},
						native	: 	{
							text 	: 	'取消' ,
							disabled: 	false
						}
					}
					$scope.meta = $.extend( {} ,  meta , $scope.meta   ) ;
					if( !$scope.style )
						$scope.style = 'width:900px;' ;
				}
			}
		})
		.directive('knitMenu' , function( ctx ){
			return {
				restrict 	: 	'E' ,
				template 	: 	template ,
				scope 		:	{
					menus 	: 	'=data' ,
					subject : 	'@subject'
				} ,
				replace 	: 	true ,
				link 		: 	function( scope , elem , attrs ){
				},
				controller 	: 	function( $attrs , $scope , $element ){
					$($element).delegate('a' , 'click' , function(){
						var state = $(this).next('ul').attr('data-state') ;
						
						$(this).parent().parent().find('[data-state=open]')
								.attr('data-state' , 'close')
								.stop(true).slideUp();
						if( state == 'open' ){
							$(this).next('ul').attr('data-state' , '')
								.stop(true).slideUp(200) ;
						}else{
							$(this).next('ul').attr('data-state' , 'open')
								.stop(true).slideDown(200) ;
						}
						if( !$(this).next('ul').length ){
							$($element).parent().find('.knit-active').removeClass('knit-active') ;
							$(this).addClass('knit-active') ;
						}
					});
				}
			}
		})
		.directive('dataTable' , function( ctx ){
			return {
				restrict 	: 	'E' ,
				scope 		: 	{} ,
				replace 	: 	true ,
				templateUrl : 	ctx.base + cmpPath + 'public/table.html' 
			}
		})
		.directive('widget' , function( ctx ){
			return {
				restrict 	: 	'E' ,
				scope 		: 	{} ,
				replace 	: 	true ,
				templateUrl : 	ctx.base + cmpPath + 'public/widget.html' ,
				transclude 	: 	true ,
				controller 	: 	function( $scope , $attrs , $element ){
					$scope.title = $attrs.title;
				}
			}
		})
		.directive('multiInput' , function( ctx ){
			return {
				restrict  	: 	'E' ,
				scope 		: 	{
					meta 		: 		'=meta' ,
					data 		: 		'=data' ,
					formContext 	: 	'=formContext'
				} ,
				replace 	: 	true ,
				controller 	: 	function( $scope , $element ){
					if($scope.meta.type=='number'){
						$($element)[0].addEventListener('DOMMouseScroll', MouseWheel, false);
			            function MouseWheel(event) {
			                event = event || window.event;
			                event.preventDefault();
			            }
					}
				},
				templateUrl : 	ctx.base + cmpPath + 'public/multiInput.html' ,
			}
		})
		.directive('fileUpload' , function( ctx ){
			return {
				restrict 	: 	'E' ,
				replace 	: 	true ,
				scope 		: 	{
					data 	: 	'=value' ,
					validation 	: 	'=validation' ,
					formContext	: 	'=formContext'
				},
				controller 	: 	function(  $scope ,$element , $fileUpload  ){
					
					var mediaType = $($element).attr('data-media-type') ,
						module = $($element).attr('data-module');
					mediaType = !mediaType ? 'image' : mediaType ;
					module = !module ? 'upload' : module ;
					$scope.mediaType = mediaType ;
					
					ngUtil.apply( $scope , function(){
						$scope.uploading = false ;
					});
					
					var uploadBuildHandler = {
						init 	: 	function( ){
							this.initValidation().addFieldValid() ;
							ngUtil.apply($scope);
							return this ;
						},
						onupload 		: 	function(){
							ngUtil.apply( $scope , function(){
								$scope.uploading = true ;
							});
						},
						uploadFinish 	: 	function(){
							ngUtil.apply( $scope , function(){
								$scope.uploading = false ;
							});
						},
						addFieldValid 	: 	function(){
							if( !$scope.formContext )
								return this ;
							if( !$scope.formContext['valid'] )
								$scope.formContext['valid'] = [] ;
							$scope.formContext['valid'].push( function(){
								var required = $scope['validation'] && $scope['validation']['rules'] && $scope['validation']['rules']['required'] ;
								if( required && ( !$scope['data'] || !$scope['data'].length ) ){
									var msg , messages;
									if( $scope['validation'] && ( messages = $scope['validation']['messages'] ) ){
										msg = typeof messages == 'string' ? messages : messages['required'] ;
									}
									if( !msg )  msg = '请上传至少一个文件!' ;
									mAlert( msg ) ;
									return false ;
								}
								return true ;
							});
							return this ;
						},
						initValidation 	: function(){
							if( !$scope['validation'] ){
								$scope['validation'] = {} ;
							} else if( !$scope['validation']['rules'] ){
								$scope['validation']['rules'] = {
									maxnum 	: 	1 ,
									file 	: 	"['jpg','png','doc','pdf','xls'].indexOf($ext.toLowerCase()) == -1 ? '文件格式不正确' : true " 
								}
							}
							return this ;
						}
					}.init() ;
					
					console.log( 'file upload>' ,$scope.validation ) ;
					
					if( !$scope.data || ! ( $scope.data instanceof Array )  )
						$scope.data = [] ;
					var file = $('<input>').attr({
						'type'  	: 	'file' ,
						'name' 		: 	'file' ,
						'accept'    :   $scope.validation.rules.accept ,
					});
					file.bind('change' , function(){
						var _this = this ;
						if( /^\s*$/.test( $(this).val() ) )
							return ;
						uploadBuildHandler.onupload() ;
						$fileUpload.upload( {
							module  	: 	module ,
							validation 	: 	$scope.validation.rules.file ,
							file 		: 	file[0]
						}, function( result ){
							console.log('---文件上传成功---' ,  result , '----------' ) ;
							if( $scope.data && $scope.data.indexOf( result.filePath ) != -1 )
								return ;
							var index = $(_this).parents('.knit-img-upload').attr('data-index') ;
							if( typeof index == 'undefined' )
								$scope.data.push( result.filePath ) ;
							else 
								$scope.data[index] = result.filePath ;
							$scope.$apply() ;
						},function( msg ){
							mAlert( msg ) ;
						},function(){ 
							uploadBuildHandler.uploadFinish() ;
						}) ;
					});
					$($element).delegate('.upload-btn' , 'click' , function(){
						file.click() ;
					});
					$($element).delegate('.knit-img-upload-remove' , 'click' , function(){
						var idx = $(this).parents('.knit-img-upload').attr('data-index') ;
						if( typeof idx == 'undefined' )
							return;
						$scope.data.splice( parseInt(idx) , 1 ) ;
						$scope.$apply() ;
					});
					$scope.$watch('data' , function( n ){
						if( !n || !n.length ) return ;
						//去重
						var res = [];
						for( var i = 0 ; i < n.length ; i ++ ){
							if( res.indexOf( n[i] ) == -1 )
								res.push( n[i] ) ;
						}
						$scope.data = res ;
					} , true ) ;
				},
				templateUrl	: 	ctx.base + cmpPath + 'public/fileUpload.html' 
			}
		})
		.directive('filterGroup' , function( ctx ){
			return  {
				restrict  	: 	'E' ,
				replace 	: 	true ,
				templateUrl : 	ctx.base + cmpPath + 'public/filterGroup.html' ,
				scope 		: 	{
					'meta' 	: 	'=meta' ,
					'data' 	: 	'=data'
				},
				controller 	: 	function( $scope ){
					if( !$scope.data )
						$scope.data = {}
				}
			}
		})
		.directive('bootSelect' , function( ctx ){
			return {
				restrict 	: 	'E' ,
				replace 	: 	true ,
				scope 		: 	{
					meta 	: 	'=meta' ,
					data 	: 	'=data' ,
					name 	: 	'=name'
				},
				templateUrl : 	ctx.base + cmpPath + 'public/bootSelect.html' ,
				controller 	: 	function( ctx , $scope , $element ){
					$($element).attr('name' , $scope.name ) ;
					var defMeta = {
						options 	: 	[] ,
						key 		: 	'id' ,
						text 		: 	'name'
					};
					$scope.meta = $.extend( {} , defMeta , $scope.meta ) ;
					var dataLoader = {
						loadData 	: 	function( source , callback ){
							if( typeof source != 'string' )
								return callback( source ) ;
							$.ajax({
								url  	: 	source ,
								success	: 	function( data ){
									if( data.code != 0 )
										return mAlert( data.message) ;
									callback( data.data ) ;
								}
							});
						},
						getFormatData 	: 	function( format , data ){
							var formatMap = undefined , regexp = /(\$[a-zA-Z0-9_]+)/g ;
							while( rs = regexp.exec( format ) ){
								if( !formatMap ) formatMap = {} ;
								formatMap[ rs[0] ] = data[ rs[0].replace('$','') ] ;
							}
							if( !formatMap ) return '@EOF'  ;
							for( var key in formatMap ){
								var value = formatMap[key] ;
								if( typeof value == 'undefined' ){
									value = '' ;
								}
								format = format.replace(key , value ) ;
							}
							format = format.replace(/(\$[a-zA-Z0-9_]+)/g , '') ;
							return format ;
						},
						handleData 	: 	function( data) {
							var _this = this ;
							var result = [{
								key 	: 	'' ,
								text 	: 	'--请选择--'
							}] ;
							if( !data || !data.length )
								return result ;
							if( data.length == 1 )
								result = [] ;
							data.forEach( function( d , k ){
								var text = _this.getFormatData( $scope.meta['text']  , d ) ;
								if( text == '@EOF' )
									text = d[$scope.meta['text']] ;
								var item = {
									key 	: 	d[$scope.meta['key']] ,
									text: 	text
								} ;
								result.push( item ) ;
							});
							return result ;
						},
						viewRend 	: 	function( data ){
							$($element).empty() ;
							var selectedValue = data.length == 1 ? data[0]['key'] : '' ;
							data.forEach( function( d , k ){
								var option = $('<option></option>').attr({
									value 	: 	d['key']
								}).html( d['text']) ;
								if( d['key'] == $scope.data[$scope.name] )
									selectedValue = $scope.data[$scope.name];
								$($element).append( option ) ;
							});
							if( data.length >= 8 )
								$($element).attr('data-live-search' , true ) ;
							$($element).selectpicker( 'val' , selectedValue ) ;
							return this ;
						}
					}
					$scope.$watch('meta.options' , function( n , o ){
						dataLoader.loadData( $scope.meta.options , function( data ){
							dataLoader.viewRend( dataLoader.handleData( data ) ) ;
							$($element).selectpicker('refresh').selectpicker('render') ;
							ngUtil.apply( $scope , function(){
								$scope.data[$scope.name] = $($element).val() ;
							});
						});
					} , true ) ;
					
					$($element).bind('change' , function(){
						var elem = this ;
						ngUtil.apply( $scope , function(){
							$scope.data[$scope.name] = $(elem).val() ;
						});
					});
					$scope.$watch('data' , function( n , o ){
						if( typeof $scope.data[$scope.name] == 'undefined' )
							$scope.data[$scope.name] = $($element).find('option:first').val() ;
						$($element).selectpicker( 'val' , $scope.data[$scope.name] );
						if( $($element).val() == $scope.data[$scope.name] )
							$($element).change() ;
					},true);
				}
			}
		})
		.directive('onLoad' , function(){
			return {
				restrict 	: 	'M' ,
				replace 	: 	true ,
				scope 		: 	false ,
				template 	: 	"<span id='onlad-finish'></span>",
				link 		: 	function( $scope ){
					$scope.$viewLoadFinished = true ;
				}
			}
		})
		.directive('tpSelect' , function( ctx ){
			return {
				restrict  	: 	'E' ,
				replace 	: 	true ,
				scope 		: 	{
					meta 	: 	'=meta' ,
					data 	: 	'=data'
				},
				templateUrl	: 	ctx.base + cmpPath + 'public/tpSelect.html' ,
				controller 	: 	function( $scope , $element ){
					var loadData = function( ajaxSource , param , success , error , complete ){
						$.ajax({
							url  	: 	ajaxSource ,
							type 	: 	'get' ,
							dataType: 	'json' ,
							data 	: 	param ,
							success : 	function( result ){
								if( result.code != 0 )
									return mAlert( result.message ) ;
								success( result.data ) ;
							},
							error 	: 	function( code  ){
								if( typeof error == 'function' )
									return error( code ) ;
								return mAlert('服务器请求出错,请重试!') ;
							},
							complete: 	function( ){
								if( typeof complete == 'function' )
									complete() ;
							}
						});
					};
					var getFormatData 	= 	function( format , data ){
						var formatMap = undefined , regexp = /(\$[a-zA-Z0-9_]+)/g ;
						while( rs = regexp.exec( format ) ){
							if( !formatMap ) formatMap = {} ;
							formatMap[ rs[0] ] = data[ rs[0].replace('$','') ] ;
						}
						if( !formatMap ) return format ;
						for( var key in formatMap ){
							var value = formatMap[key] ;
							if( typeof value == 'undefined' ){
								value = '' ;
							}
							format = format.replace(key , value ) ;
						}
						format = format.replace(/(\$[a-zA-Z0-9_]+)/g , '') ;
						return format ;
					};
					var handleResult = function( data ){
						if( !data || !data.length )
							return [] ;
						if( !$scope.meta.key )
							$scope.meta.key = 'id' ;
						if( !$scope.meta.name )
							$scope.meta.name = '$name' ;
						var options = [] ;
						data.forEach( function( d , v ){
							options .push({
								id  	: 	d[$scope.meta.key] ,
								name 	: 	getFormatData( $scope.meta.name , d ) 
							});
						});
						return options ;
					};
					var loadAndRender = function( callback ){
						loadData( $scope.meta.options , $scope.meta.param , function( data ){
							$scope.meta.options = handleResult( data ) ;
							if ( typeof callback == 'function' )
								callback() ;
							$scope.$apply() ;
						} );
					}
					
					var resetData = function(){
						if( $scope.meta.noHead )
							$scope.data = $scope.meta.options[0]['id'] ;
						else 
							$scope.data = '' ;
					}
					
					$scope.$watch('meta' , function( n ,o ){
						if( typeof $scope.meta.options == 'string' ){
							loadAndRender( function(){
								resetData() ;
							}) ;
						}else{
							if( !$scope.meta.options ) 
								$scope.meta.options = [] ;
							resetData() ;
						}
					},true) ;
					
					if( !$scope.meta ) $scope.meta = {} ;
					if( !$scope.meta.options || !$scope.meta.options.length )
						$scope.meta.noHead = false ;
				}
			}
		})
		.directive('formGroup' , function( ctx ){
			return {
				restrict 	: 	'E' ,
				replace 	: 	true ,
				scope 		: 	{
					meta 	: 	'=meta' ,
					data 	: 	'=data'
				},
				templateUrl	: 	ctx.base + cmpPath + 'public/formGroup.html' 
			}
		})
	}
	
	return {
		init 	: 	function( app ){
			directive( app ) ;
		}
	}
} );