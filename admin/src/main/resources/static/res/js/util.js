var formUtil = {
	getEditableParam 	: 	function( param , meta ){
		if( !meta ) return param ;
		var editable = {} ;
		meta.forEach( function( d , k ){
			if( !( d instanceof Array) ){
				if( !d['ignore'] && !d['hide'] )
					editable[ d['field'] ] = param[d['field'] ] ;
				return ;
			}
			d.forEach( function( sd , sk ){
				if( !sd['ignore'] && !sd['hide'] )
					editable[ sd['field'] ] = param[sd['field'] ] ;
			});
		});
		return editable ;
	}
};

var ngUtil = {
	apply 	: 	function( $scope , fnc ){
		if( !$scope ) return ;
		setTimeout( function(){
			$scope.$apply( function(){
				typeof fnc == 'function' && fnc() ;
			});
		},1) ;
	},
	syncExec 	: 	function( callback , delay , ttl ){
		delay = delay ? delay : 2000 , ttl = !ttl ? 100 : ttl ;
		if( (delay -= ttl ) > 0 && !callback() )
			setTimeout( function(){
				ngUtil.syncExec( callback , delay , ttl ) ;
			}, ttl );
		return this ;
	},
	meta 	: 	function( meta , field , byKey ){
		var sub = undefined , key = byKey || 'field' ;
		meta.forEach( function( d , k ){
			if( d instanceof Array ){
				d.forEach( function( sd , sk ){
					if( field == sd[ key ] )
						sub = sd ;
				});
				if( field == d[ key ] )
					sub = d ;
			}
		});
		return sub ;
	}
}

var paramUtil = {
	param 	: 	function( name , def ){
		if( typeof def == 'undefined' ) def = {} ;
		var search = location.href.split('?')[1] ;
		if( !search || /^\s*$/.test( search ) ){
			return def ;
		}
		var pairs = search.split('&') ;
		var param = {} ;
		for( var key in pairs ){
			var nameValue = pairs[key].split('=') ;
			if( !nameValue.length ){
				continue ;
			}
			param[nameValue[0]] = nameValue[1] ;
		}
		if( name ) {
			return typeof param[name] == 'undefined' ? def : param[name] ;
		}
		if( ! param ){
			param = {} ;
		}
		return param;
	}
};

var uiUtil = {
	mAlert 	:  	function( msg , title , btnTxt , callback ){
		msg = !msg ? '' : msg ;
		title = !title ? '提示' : title ;
		btnTxt = !btnTxt ? '确认' : btnTxt ;
		var alertModal = $('#alert-dialog-modal') ;
		if(!alertModal || !alertModal.length ){
			//return alert( msg ) ;
		}
		alertModal.find('.modal-body').html( msg ) ;
		alertModal.find('.modal-title').html( title ) ;
		alertModal.find('[name=alert-dialog-position-btn] span').html( btnTxt ) ;
		if( typeof( callback ) == 'function' ){
			alertModal.find('[name=alert-dialog-position-btn]').unbind('click').bind('click' , callback ) ;
		}
		alertModal.modal('show') ;
	},
	link 	: 	function( content , href , attr ){
		var link = $('<a></a>').attr( {
			'href' 	:	href ? href : 'javascript:;' 
		}).append( content ) ;
		if( attr ){
			for( var key in attr ){
				link.attr( key , attr[key] ) ; 
			}
		}
		return link ;
	},
	mConfirm 	: 	function( msg , callback , title , positiveBtn , nativeBtn ){
		var confirm = $('#confirm-dialog-modal') ;
		if( !confirm || !confirm.length ){
			var state = confirm( msg ) ;
			if( state || typeof( callback ) == 'function' ){
				callback() ;
			}
		}
		msg = !msg ? '' : msg ;
		title = !title ? '提示' : title ;
		positiveBtn = !positiveBtn ? '确认' : positiveBtn ;
		nativeBtn 	= !nativeBtn ? '取消' : nativeBtn ;
		confirm.find('.modal-body').html( msg ) ;
		confirm.find('[name=confirm-dialog-position-btn] span').html( positiveBtn ) ;
		confirm.find('[name=confirm-dialog-native-btn] span').html( nativeBtn ) ;
		confirm.find('.modal-title').html( title ) ;
		if( typeof( callback ) == 'function' ){
			confirm.find('[name=confirm-dialog-position-btn]').unbind('click').bind('click' , callback) ;
		}
		confirm.modal('show') ;
	},
	shorthand 	: 	function( text , size ){
		if( !text || /^\s*$/.test( text ) ){
			return '' ;
		}
		size = !size || size <= 0 ? 10 : size ;
		if( text.length <= size ) return text ;
		var warp = $('<div></div>').addClass('shorthand').attr({
			'data-role' 	: 	'shorthand' ,
			'data-text' 	:  	text	
		}).html( text.substring(0,size)+'...') ;
		return warp[0].outerHTML ;
	},
	shorthandActive 	: 	function(){
		$('body').delegate('[data-role=shorthand]' , 'mouseenter' , function(){
			$('[data-role=shorthand] .shorthand-extra').hide() ;
			if( !$(this).find('.shorthand-extra').length ) {
				$(this).append($('<div></div>').addClass('shorthand-extra')
					.html(
						$(this).attr('data-text')
					).bind('mouseout',function(){
						$(this).hide() ;
					}
				)) ;
			}
			$(this).find('.shorthand-extra').show() ;
		});
		return this ;
	},
	switchBtn 	: 	function(){
		var wrap = $('<div></div>').attr({
			'class'  	: 	'switch switch-small'
		});
		var check = $('<input/>').attr({
			'type'  	: 	'checkbox'
		}).appendTo( wrap ) ;
		return wrap ;
	},
	thumbnailActive 	: 	function(){
		$('body').delegate('[data-role=subimg],[data-role=subvideo]' , 'click' , function(){
			var bigImg = $('<div></div>').addClass('modalbg').attr({
				'data-role'  	: 	'modalbg'
			}) ;
			/*var close = $('<div>x</div>').css({
				'width'  	: 	'50px' ,
				'height' 	: 	'50px' ,
				'border-radius':'100%' ,
				'background-color':'rgba(255,255,255,0.8)' ,
				'position' 	: 	'absolute' ,
				'right' 	: 	'0px' ,
				'top' 		: 	'0px' ,
				'font-size' : 	'30px' ,
				'color' 	: 	'#fff' ,
				'display' 	: 	'inline-block' ,
				'cursor' 	: 	'pointer'
			});*/
			$('body').append(bigImg) ;
			bigImg.empty() ;
			var target = '' ;
			if($(this).attr('data-role') == 'subimg' ){
				target = $('<img>').addClass('modalimg').attr('src' , $(this).attr('src') ) ;
			}else if( $(this).attr('data-role') == 'subvideo') {
				var _this = this ;
				target = $('<video>您的浏览器不支持video控件</video>')
				.attr({
					'src' : $(_this).attr('src') ,
					'controls' 	: 	'controls' ,
					'autoplay' 	: 	true
				}).addClass('modalimg');
			}
			bigImg.append(target) ;
			//bigImg.append( close ) ;
			/*close.bind('click',function(){
				$('[data-role=modalbg]').remove() ;
			}) ;*/
			bigImg.bind('click',function(){
				$('[data-role=modalbg]').remove() ;
			}) ;
		});
		return this ;
	},
	
	init 	: 	function(){
		return this.shorthandActive().thumbnailActive() ;
	}
}.init();

var storageUtil = {
	get	:	function( key ) {
		var value ;
		if( !key ) return value ;
		if( localStorage && localStorage.getItem ){
			value = localStorage.getItem( key ) ;
			return value ;
		}
		value = $.cookie( key ) ;
		return value ;
	}	,
	set :	function( key , value ){
		if( !key || !value ) return ;
		if( localStorage && localStorage.setItem ){
			localStorage.setItem( key , value ) ;
			return ;
		}
		$.cookie( key , value , {
			path 	:	'/'
		});
	} 
};

var labelUtil = {
	addLabel 	: 	function( container , labelText , attr , onclose ){
		var _this = this ;
		if( _this.exists( container , labelText ) ){
			return ;
		}
		var label = $('<span></span>').addClass('circle-label') ;
		if( attr ){
			label.attr( attr ) ;
		}
		var text = $('<span></span>').addClass('circle-label-text') ;
		text.html( labelText ) ;
		text.appendTo( label ) ;
		var close = $('<span>&times;</span>').addClass('close') ;
		close.appendTo( label ) ;
		close.bind('click' ,function(){
			if( typeof onclose == 'function' ){
				if( onclose( label , text ) ){
					label.remove() ;
				}
				return ;
			}
			label.remove() ;
		});
		label.appendTo( $( container ) ) ;
	},
	exists 	: 	function( container , labelText ){
		var values = [] ;
		$( container ).find( '.circle-label-text' ).each(function(){
			values.push( $(this).html() ) ;
		}) ;
		console.log( values)
		return values.indexOf( labelText ) > -1 ;
	},
	values 	: 	function( container , attrs ){
		var values = [] ;
		$( container ).find( '.circle-label' ).each( function(){
			var value = {
				value : $(this).find('.circle-label-text').html()
			};
			if( !attrs || !attrs.length ){
				values.push( value ) ;
				return ;
			}
			for( var key in attrs ){
				if( typeof $(this).attr( attrs[key] ) != 'undefined' ){
					value[ attrs[key] ] =  $(this).attr( attrs[key] ) ;
				}
			}
			values.push( value ) ;
		});
		return values ;
	}
}

!function( fn ){
var selectUtil = {
	buildSelect 	: 	function( args ){
		args.select = this ;
		if( args.data ){
			if( ! ( args.data instanceof Array ) ){
				var ary = [] ;
				for( var key in args.data ){
					ary.push({
						name 	: 	args.data[key] ,
						id 		: 	key
					});
				}
				args.data = ary ;
			}
			selectUtil.buildOption( args ) ;
			return this ;
		}
		args.select.empty().append("<option value=''>加载中...</option>") ;
		selectUtil.loadOptionData( args.url , args.param , function( data ){
			args.data = data ;
			selectUtil.buildOption( args ) ;
		});
		return this ;
	},
	buildOption 	: 	function( args ){
		var select = args.select ;
		select.optionList = args.data ;
		select.empty() ;
		if( typeof( args.headKey ) == 'undefined' ){
			args.headKey = '' ;
		}
		if( !args.noHead ){
			select.append("<option value='"+args.headKey+"'>请选择</option>") ;
		}
		if( !args.data || !args.data.length ){
			return ;
		}
		args.key = !args.key ? 'id' : args.key ;
		args.text = !args.text ? 'name' : args.text ;
		var data = args.data ;
		for( var key in data ){
			if( args.except && args.except.indexOf( data[key][args.key] ) != -1 ){
				continue ;
			}
			var option = $("<option value = '"+data[key][args.key]+"'>"+data[key][args.text]+"</option>") ;
			if( typeof( args.value ) != 'undefined' && data[key][args.key] == args.value ){
				option.attr('selected' , true ) ;
			}
			select.append( option ) ;
		}
	},
	loadOptionData 	: 	function( url , param , callback ){
		if( !param ) param = {} ;
		$.ajax({
			url 	: 	url ,
			type 	: 	'get' ,
			data 	: 	param ,
			dataType: 	'json' ,
			success	: 	function( result ){
				if( result.code != 0 ){
					return mAlert( result.message ) ;
				}
				if( typeof(callback) == 'function' ){
					callback( result.data ) ;
				}
			},
			error	: 	function(){
				console.log('from select builder ...') ;
				mAlert('服务器请求失败,请重试!') ;
			}
		});
	}
};
fn.load = selectUtil.buildSelect ;
}($.fn) ;

var jQueryExtend = {
	rpEle 	: 	function( arg ){
		if( typeof arg == 'undefined' || /^\s*$/.test( arg ) )
			return ;
		return $('[name=title_'+arg+']') ;
	},
	resetEle 	: 	function( $){
		$.fn.extend({
			resetEle 	: 	function( value ){
				if( typeof value != 'undefined' )
					$(this).val( value ).change() ;
				var errorClass ;
				if( ( errorClass = $(this).parents('.has-error,.has-success,.has-warning') ).length )
					errorClass.removeClass('has-error has-success has-warning') ;
				var errorMsg , name = $(this).attr('name') ;
				if( (errorMsg = $('[data-for='+name+'],[data-warning='+name+']') ).length )
					errorMsg.empty() ;
				return this ;
			} 
		});
		return this ;
	},
	init 	( $ ){
		var _this = this ;
		$.extend({
			rpEle  	: 	_this.rpEle
		});
		this.resetEle( $ ) ;
		return this ;
	}
}.init( $ ) ;

var validateUtil = {
	addPhoneMetod 	: 	function(){
		if( !$.validator ){
			return this;
		}
		$.validator.addMethod( 'phone' , function( value , ele , params ){
			return this.optional( ele ) || /^[0-9]{11}$/.test( value ) ;
		} , '手机号码格式不正确!') ;
		return this ;
	},
	addExpressMethod : 	function(){
		if( !$.validator ){
			return this ;
		}
		$.validator.addMethod( 'express' , function( value , ele , params ){
			return  new RegExp( params ).test( value ) ;
		} , '格式不正确!' );
		return this ;
	},
	addLt 	: 	function(){
		if( !$.validator ){
			return this ;
		}
		$.validator.addMethod( 'lt' , function( value , ele , params ){
			return this.optional( ele ) || 
				( /^-?[0-9]+(\.[0-9]+)?$/.test( value ) && parseInt( value ) < parseInt( params ) ) ;
		} , '值不能大于{0}!' );
		return this ;
	},
	addLte 	: 	function(){
		if( !$.validator ) return this ;
		$.validator.addMethod('lte' , function( value , ele , params ){
			if( this.optional( ele ) )
				return true ;
			if(! /^-?[0-9]+(\.[0-9]+)?$/.test( value ) )
				return false ;
			
			var vars , varRepx = /\$([a-zA-Z0-9_]+)/g , group ;
			while( ( group = varRepx.exec( params ) ) ){
				if( !vars ) vars = {} ;
				vars[ group[0] ] = {
					name 	: 	group[1] , 
					element : 	$.rpEle( group[1] ) ,
					value 	:	$.rpEle( group[1] ).length && $.rpEle( group[1] ).val() || null
				}
			}
			if( !vars ) 
				return parseInt( value ) <= parseInt( params ) ;
			var dim = '' ;
			for( var key in vars ){
				var v = vars[key]['value'] ;
				if( typeof v != 'undefined' && v !== null 
						&& !/^-?[0-9]+(\.[0-9]+)?$/.test( v ) 
							&& ['true','false'].indexOf( v ) == -1 )
				v = "'"+v+"'" ;	
				dim +="var ${key}=${value};".replace('${key}' , key ).replace('${value}' , v ) ;
				
				var target = vars[key]['element'] ;
				if( !target || !target.length )
					continue ;
				target.off('.lte').on('blur.lte' , function(){
					$(ele).valid() ;
				});
			}
			console.log('执行表达式' , dim + params , eval( dim + params ) ,
					parseInt(value), parseInt( eval( dim + params ) )	) ;
			return parseInt(value) <= parseInt( eval( dim + params ) ) ;
		});
		return this ;
	},
	addGte 	: 	function(){
		if( !$.validator ) return this ;
		$.validator.addMethod('gte' , function( value , ele , params ){
			if( this.optional( ele ) )
				return true ;
			if(! /^-?[0-9]+(\.[0-9]+)?$/.test( value ) )
				return false ;
			
			var vars , varRepx = /\$([a-zA-Z0-9_]+)/g , group ;
			while( ( group = varRepx.exec( params ) ) ){
				if( !vars ) vars = {} ;
				vars[ group[0] ] = {
					name 	: 	group[1] , 
					element : 	$.rpEle( group[1] ) ,
					value 	:	$.rpEle( group[1] ).length && $.rpEle( group[1] ).val() || null
				}
			}
			if( !vars ) 
				return parseInt( value ) >= parseInt( params ) ;
			var dim = '' ;
			for( var key in vars ){
				var v = vars[key]['value'] ;
				if( typeof v != 'undefined' && v !== null 
						&& !/^-?[0-9]+(\.[0-9]+)?$/.test( v ) 
							&& ['true','false'].indexOf( v ) == -1 )
				v = "'"+v+"'" ;	
				dim +="var ${key}=${value};".replace('${key}' , key ).replace('${value}' , v ) ;
				
				var target = vars[key]['element'] ;
				if( !target || !target.length )
					continue ;
				target.off('.lte').on('blur.lte' , function(){
					$(ele).valid() ;
				});
			}
			console.log('express[gte]:' , dim + params , eval( dim + params ) ,
					parseInt(value), parseInt( eval( dim + params ) )	) ;
			return parseInt(value) >= parseInt( eval( dim + params ) ) ;
		});
		return this ;
	},
	addGt 	: 	function(){
		if( !$.validator ){
			return this ;
		}
		$.validator.addMethod( 'gt' , function( value , ele , params ){
			return this.optional( ele ) || 
				( /^-?[0-9]+(\.[0-9]+)?$/.test( value ) && parseInt( value ) > parseInt( params ) ) ;
		} , '值不能小于{0}!' );
		return this ;
	},
	addInt 	: 	function(){
		if( !$.validator ){
			return this ;
		}
		$.validator.addMethod( 'int' , function( value , ele , params ){
			if( this.optional( ele ) ||  params !== true ) 
				return true ;
			return /^[0-9]+$/.test( value ) ; 
		} , '请输入整数' );
		return this ;
	},
	addEval 	: 	function(){
		if( !$.validator )
			return this ;
		$.validator.addMethod('eval' , function( value , ele , params ){
			if( this.optional( ele ) )
				return true ;
			var result , script = params.replace(/\$this/g , value ),scriptError = false ;
			try{
				result = eval( script ) ;
			}catch( e ){ 
				result = '校验脚本('+script+')执行出错!' ;
				scriptError = true ;
			}
			//显示警示信息 .
			 if( typeof result == 'string' )
				$('[data-warning='+$(ele).attr('name')+']').html( /^\s*$/.test( result ) ? '' :  result ) ;
			 else if( result === true || result === false  )
				$('[data-warning='+$(ele).attr('name')+']').html('') ;
			
			return !scriptError && result !== false  ;
		});
		return this ;
	},
	addScript 	: 	function(){
		if( !$.validator )
			return this ;
		$.validator.addMethod('script' , function( value , ele , params ){
			if( this.optional( ele ) )
				return true ;
			
			var vars = {} , varRepx = /\$([a-zA-Z0-9_]+)/g , group ;
			while( ( group = varRepx.exec( params ) ) ){
				vars[ group[0] ] = {
					name 	: 	group[1] , 
					element : 	$.rpEle( group[1] ) ,
					value 	:	$.rpEle( group[1] ).length && $.rpEle( group[1] ).val() || null
				}
			}
			//如果存在dom并且所有dom都不在本页面则直接跳过校验
			var eleNum = 0 ;;
			for( var key in vars ){
				if( vars[key]['element'].length )
					eleNum ++ ;
			}
			if( eleNum <= 1 )  return true ;
			
			var dim = '' ;
			for( var key in vars ){
				var v = vars[key]['value'] ;
				if( typeof v != 'undefined' && v !== null 
						&& !/^-?[0-9]+(\.[0-9]+)?$/.test( v ) 
							&& ['true','false'].indexOf( v ) == -1 )
				v = "'"+v+"'" ;	
				dim +="var ${key}=${value};".replace('${key}' , key ).replace('${value}' , v ) ;
				
				var target = vars[key]['element'] ;
				if( !target || !target.length )
					continue ;
				target.off('.script').on('blur.script' , function(){
					$(ele).valid() ;
				});
			}
			console.log('express[script]:' , dim+params , eval(dim+params));
			return eval( dim + params ) === true ;
		});
		return this ;
	},
	addJson 	: 	function(){
		if( !$.validator )
			return this ;
		$.validator.addMethod('json' , function( value , ele , params ){
			if( this.optional( ele ) || params !== true )
				return true ;
			try{
				eval('(' + value + ')') ;
			}catch(e){
				return false ;
			}
			return true ;
		},'JSON格式不正确!');
		return this ;
	},
	init	: 	function(){
		return this.addPhoneMetod()
		.addExpressMethod() 
		.addLt() 
		.addGt()
		.addInt()
		.addEval()
		.addLte()
		.addGte()
		.addScript()
		.addJson();
	}
};
validateUtil.init() ;


var degistUtil = {
	uuid 	: 	function() {
	  var s = [];
	  var hexDigits = "0123456789abcdef";
	  for (var i = 0; i < 36; i++) {
	    s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
	  }
	  s[14] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
	  s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
	  s[8] = s[13] = s[18] = s[23] = "-";
	  var uuid = s.join("");
	  return uuid;
	}
};

var fileUploadBuilder = {
	buildContainer 	: 	function( container , args , events ){
		var _this = this ;
		if( !$( container ) || !$( container ).length ){
			return ;
		}
		var fileItem = $('<span></span>').addClass('file-item file-pre') ;
		var style = $(container).attr('data-style') ;
		if( !style ) style = 'file-item-md' ;
		fileItem.addClass( style ) ;
		
		var img = $('<img/>').attr({ 
			src 	: 	'res/images/new.png'
		});
		if( args.src ){
			img.attr({
				'src' : args.baseUrl + '/' + args.src ,
				'data-src' : args.src
			} ) ;
		}
		img.appendTo( fileItem ) ;
		
		var fieldRandomId = degistUtil.uuid()  ;
		var fileField = $('<input />').attr({
			type 	: 	'file' ,
			id 		: 	fieldRandomId
		});
		fileField.appendTo( fileItem ) ;
		
		fileField.bind('change',function(){
			if( /^\s*$/.test( $(this).val() ) ){
				return ;
			}
			var hasEmpty = false ;
			$(container).find('input:file').each(function(){
				if( /^\s*$/.test( $(this).val() ) ){
					hasEmpty = true ;
				}
			});
			if( !hasEmpty && args.max && args.max > $(container).find('img').length ){
				fileUploadBuilder.buildContainer( container , args ) ;
			}
			fileUploadUtil.upload({
				url 	: 	args.url ,
				file 	: 	fileField 
			} , function( data) {
				if( data.code != 0 ){
					fileField.empty() ;
					return mAlert( data.message ) ;
				}
				img.attr({
					'src' : args.baseUrl + '/' + data.data ,
					'data-src' : data.data
				} ) ;
				img.css('opacity' , 1) ;
			});
		});
		
		var close = $('<span>&times;</span>').addClass('file-item-close') ;
		close.appendTo( fileItem ) ;
		close.bind('click',function(){
			if( !img.attr('data-src') || img.attr('data-src') == '' ) return ;
			if( events && events['onclose'] ){
				events['onclose']( fileItem , img , fileField ) ;
			}
			fileItem.remove() ;
			if( args.max && args.max >  $(container).find('img').length ){
				fileUploadBuilder.buildContainer( container , args ) ;
			}
		});
		
		img.bind('click',function(){
			console.log( fileField )
			if( fileField.length ) fileField.click() ;
		});
		
		fileItem.appendTo( $(container) ) ;
		return fileItem ;
	},
	imageList 	: 	function( container ){
		if( !$(container).find('img').length ){
			return [] ;
		}
		var images = [] ;
		$(container).find('img[data-src]').each(function(){
			images.push( $(this).attr('data-src') ) ;
		});
		return images ;
	}
}

var fileUploadUtil = {
	upload 	: 	function( args , callback ){
		var _this = this ;
		if( !args.file || !$( args.file ).length ){
			return ;
		}
		var randomName = degistUtil.uuid() ;
		var form ;
		_this.doUpload( form = _this.buildForm( args  ) , function( data ){
			callback( data ) ;
		});
	},
	buildForm : function( args , randomName ){
		var _this = this ;
		var form = $('<form></form>').attr({
			enctype : 	'multipart/form-data' ,
			name 	: 	randomName ,
			target 	: 	randomName ,
			action 	: 	args.url ,
			method 	: 	'POST'
		}).hide() ;
		
		var fileField = args.file ;
		fileField.attr({
			'name' 	: 	'image'
		} ) ;
		fileField.appendTo( form ) ;
		return form ;
	},
	doUpload : function( form , callback) {
		console.log('upload url:' , form.attr('action') ) ;
		var formData = new FormData( form[0] ) ;
		$.ajax({
			url 	: 	form.attr('action') ,
			data 	: 	formData ,
			dataType: 	'json' ,
			type 	: 	'POST' ,
			processData : false ,
			success : 	function( result ){
				callback( result ) ;
			},
			error	: 	function(){
				mAlert('服务器请求失败,请重试!') ;
			},
			contentType : false 
		});
	}
}

function exportMethod( name , func ){
	var method = {} ;
	method [name] = func ;
	$.extend(method) ;
}

Array.prototype.newInstance = function( n , def ){
	var size = 0 ;
	try{
		size = parseInt( n ) ;
		if( size < 0 ) throw '' ;
	}catch(e){
		return [].newInstance( def , 0 ) ;
	}
	var ary = [] ;
	for( var i = 0 ; i < size ; i ++ )
		ary.push( i ) ;
	return ary ;
}

!function(){
	var invoke = $.ajax ;
	$.extend({
		ajax 	: 	function( args ){
			var error = args.error ;
			var errorWrap = function(xmlHttp , msg ){
				if( xmlHttp.status == 404 )
					return mAlert('请求资源不存在!') ;
				if( xmlHttp.status == 401 ){
					mAlert('登录超时请重新登录!') ;
					setTimeout(function(){
						location.href='login.html' ;
					},2000) ;
					return ;
				}
				if( typeof error == 'function' )
					error( xmlHttp , msg ) ;
			}
			args.error = errorWrap ;
			var defOption = {
				type 	: 	'get' ,
				dataType: 	'json' ,
				cache	: 	false 
			}
			args = $.extend({} , defOption , args ) ;
			invoke( args ) ;
		}
	});
}() ;

$(document).ready(function(){
	window.mAlert = uiUtil.mAlert ;
	window.mConfirm = uiUtil.mConfirm ;
	$('body').bind('hashchange' , function( e ){
		console.log( e )
	});
});
