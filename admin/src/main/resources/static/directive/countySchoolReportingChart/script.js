define([] , function(){
	var service = function( ctx ){
		this.get = function(  callback , complete ){
			$.ajax({
				url  	: 	ctx.api + 'schoolDataReporting/process' ,
				type 	: 	'get' ,
				success : 	function( data ){
					if( data.code != 0 )
						return mAlert( data.message ) ;
					callback( data.data ) ;
				},
				complete 	: 	function(){
					if ( typeof complete == 'function' )
						complete( ) ;
				}
			});
		}
	}
	app.register.service('countySchoolReportingChartService' , service ) ;
	
	var controller = function( $scope , countySchoolReportingChartService ){
		var chartMeta = {
		    chart: {
		        type: 'column' ,
		        height: /^[0-9]+$/.test( $scope.height ) ? $scope.height :undefined ,
		        width : /^[0-9]+$/.test( $scope.width ) ? $scope.width : undefined
		    },
		    title: {
		        text: '区县所有学校整体填报进度监测'
		    },
		    subtitle: {
		        text: '数据截止(当前)'
		    },
		    xAxis: {
		        type: 'category',
		        labels: {
		            rotation: 0  // 设置轴标签旋转角度
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
		var boardHandler = {
			init 	: 	function(){
				return this.loadData() ;
			},
			loadData 	: 	function(){
				var _this = this ;
				countySchoolReportingChartService.get( function( data ){
					_this.chartRend( _this.handleResult( data ) ) ;
				});
				return this ;
			},
			handleResult : 	function( data ){
				var chartData = {
					category 	: 	[] ,
					data 		: 	[]
				} ;
				if( !data || !data.length )
					return  ;
				data.forEach( function( d , k ){
					chartData['category'].push( d['statusName'] ) ;
					chartData['data'].push({
						y  	: 	d['amount'] ,
						rate: 	d['rate']
					});
				});
				return chartData ;
			},
			chartRend 	: 	function( chartData ){
				if( !chartData  ){
					$('#process-chart-container').html("<div class='text-center'>暂无数据</div>") ;
					return this ;
				}
				chartMeta['xAxis']['categories'] = chartData['category'] ;
				chartMeta['series'][0]['data'] = chartData['data'] ;
				this.chart = Highcharts.chart('process-chart-container', chartMeta );
				return this ;
			}
		}.init() ;
	}
	//注册组件。
	app.register.directive('countySchoolReportingChart' , function( ctx ){
		return {
			restrict 	: 	'E' ,
			replace 	: 	true ,
			scope 		: 	{ 
				width 	: 	'=' ,
				height 	: 	'='
			},
			templateUrl : 	ctx.base + 'directive/countySchoolReportingChart/template.html' ,
			controller 	: 	controller 
		}
	});
});