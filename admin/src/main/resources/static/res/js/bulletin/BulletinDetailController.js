define(['app/datatable-setting' ,'app/sys/CityService' , 'app/sys/DictService'] , function( TableConfig ){
	
	var controller = function( $scope , ctx , $sce , $stateParams , bulletinDetailService , cityService , dictService ){
		$scope.notice = {} ;
		var detailHandler = {
			init 	: 	function(){
				return this.loadData( $stateParams['id'] ) ;
			},
			loadData 	: 	function( id ){
				if( !id || /^\s*$/.test( id ) ){
					mAlert('数据不存在!') ;
					//location.href= '#/bulletin' ;
					return this ;
				}
				var _this = this ;
				bulletinDetailService.get( id , function( data ){
					if( !data ) {
						mAlert('数据不存在!') ;
						history.back(-1) ;
						return this ;
					}
					console.log( data ) ;
					_this.viewRend( data ) ;
				});
				return this ;
			},
			viewRend 	:	function( data ){
				switch( data['categoryCode'] ){
				case 	'download' 	: 
					$('#notice-content').empty().append( this.getDownloadContent( data ) ) ;
					break ;
				default :
					$('#notice-content').empty().append( data['content'] ) ;	
				}
				ngUtil.apply($scope , function(){
					$scope.notice = data ;
				});
			},
			getDownloadContent 	: 	function( data ){
				try{
					data['content'] = JSON.parse( data['content'] ) ;
				}catch( e ){
					return '' ;
				}
				var list = $('<ul></ul>').addClass('knit-list');
				data['content'].forEach( function( d , k ){
					var item = $('<li></li>').appendTo( list ) ;
					var fileName = ! d ? '' : d.lastIndexOf('/') == -1
							? d :  d.substring( d.lastIndexOf('/')+1 ) ;
					var downloadLink = $('<a></a>').attr({
						href 	: 	d ,
						style 	: 	'font-size:16px' ,
						title 	: 	'点击下载'
					}).append("<i class='glyphicon glyphicon-download-alt'></i>&nbsp;").append( fileName )
					.appendTo( item );
				});
				console.log( list[0].outerHTML ) ;
				return list ;
			}
		}.init() ;
	}
	
	var service = function( ctx ){
		this.get = function( id , callback , complete ){
			$.ajax({
				url  	: 	ctx.api + 'bulletin/'+id +"?_flat=num",
				type 	: 	'get' ,
				dataType: 	'json' ,
				success : 	function( result ){
					if( result.code != 0 )
						return mAlert( result.message ) ;
					callback( result.data ) ;
				},
				complete: 	function(){
					if( typeof complete == 'function' ){
						complete() ;
					}
				}
			})
		} 
	}
	app.register.service('bulletinDetailService' , service ) ;
	return controller ;
});