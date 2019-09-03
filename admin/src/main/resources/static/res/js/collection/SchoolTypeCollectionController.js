define(['treeview','app/sys/DictService'] , function(){
	
	var controller = function( $scope , ctx , schoolTypeCollectionService , dictService ){
		$scope.schoolTypes = [] ;
		$scope.typeAll = {
			typeAll 	: 	false
		}
		$scope.cdata = {}
		$scope.$watch( 'cdata.schoolType' , function( n , o ){
			
		},true ) ;
		$scope.$watch( 'typeAll' , function( n , o ){
			if( !$scope.cdata || !$scope.cdata.schoolType ){
				$scope.cdata = {
					schoolType	: {}
				}
			}
			if( $scope.schoolTypes ){
				for( var key in $scope.schoolTypes ){
					if( $scope.schoolTypes[key]['id'] == undefined )
						continue ;
					$scope.cdata.schoolType[ $scope.schoolTypes[key].id ] = n ;
				}
			}
			console.log( $scope.cdata.schoolType , $scope.schoolTypes  )
		},true ) ;
		$scope.typeEditable = false ;
		
		var createHandler = {
			init 	: 	function(){
				this.loadCollectionWithQuota() ;
				this.loadSchoolType() ;
				var _this = this ;
				$('#data-create-btn').bind('click' , function(){
					mConfirm('是否保存更新?' , function(){
						_this.saveData() ;
					});
				});
				$('#data-reset-btn').bind('click' , function(){
					_this.getCollectionType() ;
				});
				return this ;
			},
			loadSchoolType 		: 	function(){
				schoolTypeCollectionService.findSchoolType( function( data ){
					$scope.schoolTypes = data && data.length ? data : [] ;
					$scope.$apply() ;
				});
			},
			loadCollectionWithQuota 	: 	function(){
				var _this = this ;
				schoolTypeCollectionService.findCollectionWithQuota( function( data ){
					_this.rendCollectionView( _this.getCollectionTreeData(data) ) ;
				}) ;
			},
			rendCollectionView 			: 	function( data ){
				var _this = this ;
				$('#left-side-panel').treeview({
					data  	: 	data  ,
					levels 	: 	3 ,
					showBorder 	: 	false ,
					selectedBackColor 	: 	'#F5F5F5' ,
					selectedColor 		: 	'#000' ,
					selectedIcon 		: 	'glyphicon glyphicon-check' ,
					nodeIcon : 	'' ,
					showIcon : 	true, 
					onNodeSelected  : 	function(event , node ){
						_this.collectionId = node.dataId  ;
						_this.getCollectionType( ) ;
					},
					onNodeUnselected: 	function(event , node){
						_this.collectionId = undefined  ;
						_this.getCollectionType( ) ;
					}
				});
			},
			getCollectionTreeData 		: 	function( data ){
				var nodes = [] ;
				data.forEach( function( d , k ){
					var node = {
						text 	: 	d['name']  ,
						icon	:	"glyphicon glyphicon-folder-open",
						backColor	:	"#FFFFFF",
						selectable	:	false
					}
					nodes.push( node ) ;
					if( ! d['subQuota'] || ! d['subQuota'].length )
						return ;
					node['nodes'] = [] ;
					var subQuotas = d['subQuota'] ;
					subQuotas.forEach( function( sd , sk ) {
						var subNode = {
							text 	: 	sd['name'] ,
							icon	:	"glyphicon glyphicon-folder-open",
							backColor	:	"#FFFFFF",
							selectable	:	false
						}
						node['nodes'].push( subNode ) ;
						if( !sd['collections'] ||!sd['collections'].length  )
							return ;
						subNode['nodes'] = [] ;
						var collections = sd['collections'] ;
						collections.forEach(function( cd , ck ){
							var collNode = {
								text 	: 	cd['title'],
								icon	:	"glyphicon glyphicon-unchecked",
								backColor	:	"#FFFFFF",
								selectable	:	true ,
								dataId 		: 	cd['id']
							}
							subNode['nodes'].push( collNode ) ;
						});
					});
				});
				return nodes ;
			},
			getCollectionType 	: 	function( ){
				var collectionId = this.collectionId ;
				$scope.cdata = {} ;
				if( !collectionId ){
					$scope.typeEditable = false ;
					$scope.$apply() ;
					return ;
				}
				$scope.typeEditable = true ;
				
				schoolTypeCollectionService.getCollectionType( collectionId , function( data ){
					if( data && data.length ){
						if( !$scope.cdata.schoolType )
							$scope.cdata.schoolType = {} ;
						data.forEach( function( d , k ){
							$scope.cdata.schoolType[d['schoolType']] = true ;
						});
					}
					$scope.$apply() ;
				});
			},
			saveData 	: 	function( ){
				var collectionId = this.collectionId ;
				var params = [] , _this = this;
				if( $scope.cdata && $scope.cdata.schoolType ){
					for( var key in $scope.cdata.schoolType ){
						if( !$scope.cdata.schoolType[key] )
							continue ;
						params.push({
							schoolType  	: 	key
						});
					}
				}
				schoolTypeCollectionService.createCollectionType( collectionId , params , function(){
					_this.getCollectionType( ) ;
					mAlert('数据保存成功!') ;
				});
			}
		}
		
		var initStack = [ function(){
			return createHandler.init() ;
		}] ;
		function watchInit( exp , wait ){
			setTimeout( function(){
				initStack.forEach( function( d , k ){
					if( d() ) initStack.splice( k , 1 ) ;
				});
				if( initStack.length && exp > 0 ) 
					watchInit( exp -= wait , wait ) ;
			},wait);
		} ;
		watchInit(10000 , 100) ;
	}
	
	var service = function( ctx ){
		this.findCollectionWithQuota = function(  callback , complete ){
			$.ajax({
				url  	: 	ctx.api + 'schoolTypeCollection/collectionWithQuota' ,
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
		
		this.findSchoolType = function(  callback , complete ){
			$.ajax({
				url  	: 	ctx.api + 'school/type?size='+Number.MAX_VALUE ,
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
		
		this.getCollectionType = function( collectionId ,  callback , complete ){
			$.ajax({
				url  	: 	ctx.api + 'schoolTypeCollection/'+collectionId+'/types' ,
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
		
		this.createCollectionType = function( collectionId , params , callback , complete ){
			$.ajax({
				url  	: 	ctx.api + 'schoolTypeCollection/'+collectionId +'/types' ,
				type 	: 	'post' ,
				dataType: 	'json' ,
				data 	: 	$.toJSON( params ) ,
				success : 	function( result ){
					if( result.code != 0 )
						return mAlert( result.message ) ;
					callback( result.data ) ;
				},
				complete: 	function(){
					if( typeof complete == 'function' ){
						complete() ;
					}
				},
				contentType 	: 	'application/json;charset=UTF-8'
			})
		} 
	}
	app.register.service('schoolTypeCollectionService' , service ) ;
	return controller ; 
});