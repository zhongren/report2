define([] , function(){
	var controller = function( ctx , $scope , multiCitySchoolReportingChartService ){
		var reportingHandler = {
			init 	: 	function(){
				return this.loadCity() ;
			},
			loadCity	: 	function(){
				var _this = this ;
				multiCitySchoolReportingChartService.findCity( function( data ){
					_this.cityViewRend( _this.handlerCityResult( data ) ) ;
				});
				return this ;
			},
			handlerCityResult 	: 	function( data ){
				var nodes = [] , supNode = {
						text 	: 	'选择地区' ,
						selectable 	: 	false ,
						icon 	: 	'glyphicon glyphicon-folder-open' ,
						nodes 	: 	nodes ,
						state 	: 	{
							expanded 	: 	true
						}
					} , supNodes = [ supNode ] ;
				
				if( !data || !data.length )
					return supNodes;
				
				data.forEach( function( d , k ){
					if( typeof supNode['dataId'] == 'undefined' )
						supNode['dataId'] = d['parentId'] ;
					
					var node = {
						text 	: 	d['name'] ,
						icon 	: 	'glyphicon glyphicon-unchecked' ,
						selectable 	: 	true ,
						state 	: 	{
							expanded 	: 	false
						},
						dataId 	: 	d['id'] ,
						dataType: 	'city'
					}
					nodes.push( node ) ;
					
					if( !d['subCitys'] || !d['subCitys'].length )
						return ;
					node['nodes'] = [] ;
					d['subCitys'].forEach( function( sd , sk ){
						var subNode = {
							text 	: 	sd['name'] ,
							icon 	: 	'glyphicon glyphicon-unchecked' ,
							selectable 	: 	true ,
							dataId 	: 	sd['id'] ,
							dataType: 	'county'
						}
						node['nodes'].push( subNode ) ;
					});
				});
				return supNodes ;
			},
			cityViewRend 		: 	function( data ){
				if( !data )
					return $('#city-container').html('暂无数据') ;
				var _this = this ;
				$('#city-container').treeview({
					data  	: 	data  ,
					levels 	: 	3 ,
					showBorder 	: 	false ,
					selectedBackColor 	: 	'#F5F5F5' ,
					selectedColor 		: 	'#000' ,
					selectedIcon 		: 	'glyphicon glyphicon-check' ,
					nodeIcon : 	'' ,
					collapsed 	: 	true ,
					showIcon : 	true, 
					onNodeSelected  : 	function(event , node ){
						_this.loadProcessData( node.dataId ) ;
					},
					onNodeUnselected: 	function(event , node){
						_this.loadProcessData() ;
					}
				});
			},
			loadProcessData 	: 	function( cityId ){
				var _this = this ;
				if( typeof cityId == 'undefined' )
					return _this.processViewRend( ).processCompileViewRend( ) ;
				
				multiCitySchoolReportingChartService.findProcessReporting( cityId , function( data ){
					console.log( 'reportingData:' , data ) ;
					_this.processViewRend( _this.handleProcessResult( data ) ) ;
					_this.processCompileViewRend( _this.handleColumnChartData(data) ) ;
				});
				return this ;
			},
			processViewRend 	: 	function( data ){
				$('#pie-container').empty() ;
				if( !data || !data.length ) {
					$('#pie-container').html('暂无数据!') ;
					return this ;
				}
				data.forEach( function(d , k){
					var container = 'pie-container-'+k ;
					var chartContainer = $('<div></div>').css({
						'display' 	: 	'inline-block' ,
						'width' 	: 	'50%' 
					}).attr({
						'id' 		: 	container
					}).appendTo( $('#pie-container') );
					if( k / 2 != 0 )
						chartContainer.css({
							'border-left' 	: 	'1px solid #eee'
						});
					
					Highcharts.chart( container , d ) ;
				});
				return this ;
			},
			processCompileViewRend 	: 	function( data ){
				if( !data ){
					$('#culumn-container').html('暂无数据!') ;
					return this ;
				}
				Highcharts.chart('culumn-container', data );
				return this ;
			},
			handleColumnChartData 	:	function( data ){
				if( !data || !data.length )
					return ;
				var chartMeta = {
				    chart: {
				        type: 'column'
				    },
				    title: {
				        text: '学校整体填报进度检测'
				    },
				    subtitle: {
				        text: '数据截止(当前)'
				    },
				    xAxis: {
				    	type 	: 	'category' ,
				        labels: {
				            rotation: -45  // 设置轴标签旋转角度
				        }
				    },
				    yAxis: {
				        min: 0,
				        title: {
				            text: '数量'
				        }
				    },
				    legend: {
				        enabled: true
				    },
				    tooltip: {
				        pointFormat: '占比: <b>{point.rate:0.2f}%</b>'
				    },
				    series: [{
				        name: '填报整体进度',
				        data: [ ],
				        dataLabels: {
				            enabled: true,
				            rotation: 0,
				            color: '#FFFFFF',
				            align: 'center',
				            format: '{point.y}所'
				        }
				    }],
				    credits 	: 	{
				    	enabled 	: 	false
				    }
				};
				var  category = [] , series = [] ;
				data.forEach( function ( d , k ){
					if( !d['data'] || !d['data'].length )
						return ;
					category.push( d['name'] ) ;
					d['data'].forEach( function( sd , sk ){
						if( typeof series[sk] == 'undefined' ){
							series[sk] = {
								name	: sd['statusName'],
								data 	: 	[] ,
								dataLabels: {
						            enabled: true,
						            rotation: 0,
						            color: '#FFFFFF',
						            align: 'center',
						            format: '{point.y}所'
						        }
							}
						}
						series[sk]['data'].push({
							'y' 	: 	sd['amount'] ,
							'rate' 	: 	sd['rate']
						});	
					});
				});
				chartMeta['series'] = series ;
				chartMeta['xAxis']['categories'] = category ;
				return chartMeta ;
			},
			handleProcessResult : 	function( data ){
				if( !data || !data.length )
					return ;
				var metas = [] ;
				data.forEach( function( d , k ){
					if( !d || !d['data'] || !d['data'].length )
						return ;
					
					var chartMeta = {
						chart: {
							plotBackgroundColor: null,
							plotBorderWidth: null,
							plotShadow: false,
							type: 'pie'
						},
						title: {
								text: d['name']+'所有学校填报进度监测'
						},
						tooltip: {
								pointFormat: '{series.name}: <b>{point.y}/{point.rate:.1f}%</b>'
						},
						plotOptions: {
								pie: {
									allowPointSelect: true,
									cursor: 'pointer',
									dataLabels: {
										enabled: true
									},
									showInLegend: true
								}
						},
						series: [{
							name: 'Brands',
							colorByPoint: true,
							data: []
						}],
						 credits 	: 	{
					    	enabled 	: 	false
					    }
					};
					metas.push( chartMeta ) ;
					
					var chartData = [] ;
					d['data'].forEach( function( sd , sk ){
						chartData.push({
							name 	: 	sd['statusName'] ,
							y 	: 	sd['amount'] ,
							rate 	: 	sd['rate']
						});
					});
					chartMeta['series'][0]['data'] = chartData ;
					chartMeta['series'][0]['name'] = d['name'] ;
				});
				return metas ;
			}
		}.init() ;
	} 
	var service = function( ctx ){
		this.findCity = function( callback ){
			$.ajax({
				url 	: 	ctx.api + 'schoolDataReporting/groupProcessCity' ,
				success : 	function( result ){
					if( result.code != 0 )
						return mAlert( result.message ) ;
					callback( result.data ) ;
				}
			});
		};
		
		this.findProcessReporting = function( cityId , callback ){
			$.ajax({
				url 	: 	ctx.api + 'schoolDataReporting/cityGroupProcess' ,
				data 	: 	{
					parentId 	: 	cityId 
				},
				success : 	function( result ){
					if( result.code != 0 )
						return mAlert( result.message ) ;
					callback( result.data ) ;
				}
			});
		}
	}
	app.register.service('multiCitySchoolReportingChartService' , service ) ;
	//注册主键 .
	app.register.directive('multiCitySchoolReportingChart' , function( ctx ){
		return {
			restrict 	: 	'E' ,
			replace 	: 	true ,
			scope 		: 	{ } ,
			controller 	: 	controller ,
			templateUrl : 	ctx.base + 'directive/multiCitySchoolReportingChart/template.html' 
		}
	});
});