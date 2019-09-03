define(['beautify'],function( beautify ){
	var controller = function( $scope , $element , $timeout ){
		var flag = true , cursorPoint = 0 ;
		if( !$scope.config.info )
			$scope.config.info = '' ;
		$scope.config.info += '提示:英文输入法@符号唤醒变量检索框.' ;
		var handler = {
			init 	: 	function(){
				this.element = $($element).find( '[data-element]' ) ;
				this.element.on('blur' , function(){
					if( $scope.config.jsBeautify === false )
						return ;
					$(this).val( beautify.js_beautify( $(this).val() ) ).change() ;
				});
				this.element.on('focus keyup click' , function(){
					cursorPoint = $(this)[0].selectionEnd ;
				});
				var _this = this ;
				this.initFormula( $scope.data ) ;
				return this.build().applyRule().viewType( $scope.config.viewType || 'both' ) ;
			},
			build 	: 	function(){
				var _this = this ;
				this.element.keydown( function( event ){
					if( event.shiftKey && event.keyCode == 229 )
						return false ; //mAlert('请切换到英文状态下输入!') ;
					
					if( event.shiftKey && event.keyCode == 50 ){ 
						var wrap = $($element).find('[data-role=var-filter]') ;
						if( !wrap.length ){
							wrap = _this.createVarTag() ;
							_this.element.after(wrap) ;
						}
						wrap.find('[data-role=formula-var]').focus() ;
						return false ;
					}else if( event.keyCode == 9 ){
						_this.insertAtPosition('    ') ;
						return false ;
					}
					return true ;
				}) ;
				this.element.bind('change blur' , function(){
					_this.getData( $(this).val() );
				});
				$($element).delegate('[data-event]' , 'click' , function(){
					_this.viewType( $(this).attr('data-event') ) ;
				});
				return this ;
			},
			viewType 		: 	function( type ){
				type = type || 'both' ;
				switch( type ){
				case 'edit' :
					$($element).find('[data-view=preview]').hide() ;
					$($element).find('[data-view=edit]').css('width' , '100%').show() ;
					break ;
				case 'preview' : 
					$($element).find('[data-view=edit]').hide() ;
					$($element).find('[data-view=preview]').css('width' , '100%').show()  ;
					break ;
				case 'both' 	: 
					$($element).find('[data-view=edit]').css('width' , '49%').show() ;
					$($element).find('[data-view=preview]').css('width' , '49%').show()  ;
				}
				return this ;
			},
			initFormula 	: 	function( data ){
				this.transFormulate( data ) ;
				if( !this.element )
					return this ;
				this.element.val( data || '' ) ;
				return this ;
			},
			getData 	: 	function( input ){
				ngUtil.apply( $scope , function(){
					$scope.data = input ;
				});
			},
			insertAtPosition 	: 	function( val ){
				if( typeof val == 'undefined' || val == '' )
					return ;
				var value = this.element.val() ;
				value = value.substring( 0 , cursorPoint ) + val + value.substring( cursorPoint ) ;
				this.element.val( value ).change() ;
			},
			createVarTag : function( keyValue ){
				var _this = this ;
				var wrap = $('<div></div>').css({
					'display' 	: 'inline-block' ,
					'padding' 	: '30px 40px' ,
					'cursor' 	: 'move' ,
					'margin' 	: '1px 2px' ,
					'position'	: 'absolute' ,
					'z-index' 	: 	'9999' ,
					'top' 		: 	'5%' ,
					'left' 		: 	'30%' ,
					'background-color': 'rgba(255,255,255,0.5)' ,
					'border' : '1px solid #f2f2f2' ,
					'border-radius' : '3px' ,
					'box-shadow' : '1px 1px 2px gray'
				}).attr({
					'data-role'  	: 	'var-filter' 
				});
				+function( wrap ){
					var move = false , ox = 0 , oy = 0 ;
					wrap.on('mousedown' , function( event ){
						move = true ;
						ox = event.clientX ;
						oy = event.clientY ;
					});
					$('body').off('mousemove.varfilter').on('mousemove.varfilter' , function(event){
						if( !move ) return ;
						$(wrap).css({
							'top' : '+='+(event.clientY-oy)+'px' ,
							'left': '+='+(event.clientX-ox)+'px'
						});
						ox = event.clientX ;
						oy = event.clientY ;
					});
					$('body').off('mouseup.varfilter').on('mouseup.varfilter' , function(){
						move = false ;
					});
				}( wrap  ) ;
				
				var input = $('<input>').attr({
					'type' : 'text',
					'data-role' : 'formula-var' ,
					'placeholder': '关键词格式:关键词 类型' ,
				}).addClass('form-control').css({
					'width'  	: 	'200px' 
				}).appendTo( wrap ) ;
				
				var close = $('<span>&times;</span>').css({
					'position':'absolute',
					'display':'inline-block',
					'padding':'2px' ,
					'cursor':'pointer',
					'top':'-5px' ,
					'right':'0px' ,
					'color':'gray'
				}).attr({
					'title':'关闭'
				}).appendTo(wrap).bind('click',function(){
					wrap.remove() ;
				});
				
				var list = $('<ul></ul>').css({
					'padding' 	: '5px' ,
					'margin'	: '0px' ,
					'list-style': 'none' ,
					'position' 	: 'absolute' ,
					'z-index' 	: 	'9999' ,
					'top' 		: '100%' ,
					'left' 		: '0' ,
					'min-width' : '100%' ,
					'max-height' : '250px' ,
					'overflow' : 'auto' ,
					'background': '#fff' ,
					'border' : '1px solid #f2f2f2' ,
					'border-radius' : '3px' ,
					'box-shadow' : '1px 1px 2px gray' ,
					'border-top' : '0px'
				}).hide().appendTo( wrap ) ;
				var tempKeyWord ;
				input.bind('focus keyup' , function( event ){
					if( $(this).val() != tempKeyWord ){
						_this.reload( list , {
							param 	: 	{
								displayName 	: 	input.val()
							}
						});
					}
					list.show() ;
					tempKeyWord = $(this).val() ;
				});
				
				input.bind('blur' , function(){
					setTimeout(function(){
						list.stop(true).fadeOut() ;
					} , 200 ) ;
				});
				
				list.delegate('li' , 'click' , function(){
					var value = $(this).attr('data-value') ;
					_this.insertAtPosition( value ) ;
					list.hide() ;
					setTimeout(function(){
						list.stop(true).fadeOut();
					} , 200 ) ;
				});
				return wrap ;
			},
			loadVarsData 	: 	function( option , then ){
				var data = $.extend( true , {
					param 	: 	{
						page : 1 ,
						size : 10
					}
				} , option )
				$.ajax({
					url  	: 	'variable' ,
					data 	: 	data['param'] ,
					success : 	function( result ){
						if( result.code != 0 )
							return mAlert( result.message ) ;
						then( result.data ) ;
					}
				});
			},
			getVarsDataByKey 	: 	function( key , then ){
				$.ajax({
					url  	: 	'variable/'+key ,
					success : 	function( result ){
						if( result.code != 0 )
							return mAlert( result.message ) ;
						then( result.data ) ;
					}
				});
			},
			reload 	: 	function( ele , option ){
				var _this = this ;
				ele.empty() ;
				var keyWord ;
				if( option && option.param && ( keyWord = option.param.displayName ) ){
					var p = 0 ;
					if( ( p = keyWord.indexOf(' ') ) != -1 ){
						option.param['target'] = (keyWord.substring( p ) || '').replace(/\s+/g , '' ) ;
						option.param['displayName'] = ( keyWord.substring( 0 , p ) || '' ).replace(/\s+/g , '' ) ; ;
					}
				}
				this.loadVarsData( option , function( data ){
					if( !data || !data.length ){
						return ele.append( _this.createItem('' , '暂无数据') ) ;
					}
					for( var key in data ){
						if( !data[key]['varName'] )
							continue ;
						ele.append( _this.createItem(data[key].varName , '['+data[key].target+']'+data[key].displayName ) ) ;
					}
				});
			},
			createItem : function( key , text ){
				var item = $('<li></li>').attr({
					'data-value'  : 	key  ,
					'title' : text
				}).css({
					'cursor':'pointer' ,
					'display':'block' ,
					'white-space':'nowrap'
				}).html( text && text.substring(0,20) ) ;
				return item ;
			},
			applyRule	: 	function(){
				return this ;
			},
			getVars 		: 	function( formulate ){
				if( !formulate ) return  ;
				var vars = [] , varsExp = /\$[0-9a-zA-Z_]+/g , group ;
				while( ( group = varsExp.exec( formulate ) ) ){
					vars.push( group[0] ) ;
				}
				return vars ;
			},
			transFormulate 	: 	function( formulate ){
				$($element).find('[data-role=formulate-pane]') .html('') ;
				var _this = this ;
				if( !formulate ) return ;
				var vars = this.getVars( formulate ) ;
				if( !vars || !vars.length ){
					$($element).find('[data-role=formulate-pane]') .html( formulate ) ;
					return ;
				}
				$.ajax({
					url  	: 	'variable/byVarName' ,
					data 	: 	{
						'varNames[]' 	: 	vars
					},
					success	:	function( result ){
						if( result.code != 0 )
							return mAlert( result.message ) ;
						var data = result.data || [] , varData = {} ;
						console.log('获取到的变量:' , result.data ) ;
						for( var key = 0 ; key < data.length ; key ++ ){
							varData[data[key].varName] = data[key].displayName ;
						}
						_this.rendFormulate( formulate , varData ) ;
					}
				});
			},
			rendFormulate	: 	function( formulate , varData ){
				var vars = this.getVars( formulate )  , _this=this;
				if( vars && vars.length && varData ){
					for( var key = 0 ; key < vars.length ; key ++ ){
						if( typeof varData[ vars[key] ] == 'undefined' )
							continue ;
						formulate = formulate.replace( vars[key] , _this.substr( varData[vars[key]] ) ) ;
					}
				}
				$($element).find('[data-role=formulate-pane]')
					.html( formulate.replace(/\n/g , '<br>').replace(/\s/g , '&nbsp;') ) ;
			},
			substr 	: 	function( input , size ){
				if( typeof input == 'undefined' || input == '' )
					return input ;
				return input.substring( 0 , size || 20 ) ;
			}
		}
		
		$scope.$watch('data' , function(){
			handler.initFormula( $scope.data ) ;
			flag = true ;
		});
		
		$timeout( function(){
			handler.init() ;
		} , 1 , true ) ;
		
	}
	
	app.register.directive('formulaInput' , function( ctx ){
		return {
			restrict 	: 	'E' ,
			replace 	: 	true ,
			scope 		: 	{
				rules 	: 	'=rules' ,
				name 	: 	'=name' ,
				data 	: 	'=data' ,
				config 	: 	'=config'
			},
			controller 	: 	controller ,
			templateUrl : 	ctx.base + 'directive/formulaInput/template.html' 
		}
	});

});