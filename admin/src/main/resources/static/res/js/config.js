define( ['prop' , 'component' ,'application', 'angular' , 'jquery' , 'router' ] , function( prop , cmp , appContext ){
	window.app = angular.module('app' , ['ui.router'] ) ;
	app.constant('ctx' , prop.ctx ) ;
	//初始化组件 .
	cmp.init( app ) ;
	//初始化全局context
	appContext.init( app ) ;
	
	var urlConfig = {
		'/' 	: 	{
			source 	: 	'dashboard' , 	//模块资源位置(包括controller以及页面
			template: 	'dashboard.html' ,//模板页面名称
			controller	: 	'DashboardController',
			key 	: 	'/index' , 	//state name
			name 	: 	'首页' 	//描述
		},
		'/school/:quotaId/collection' 	: 	{
			source 	: 	'school' , 	//模块资源位置(包括controller以及页面
			controller 	: 	'SchoolCollectionController' , 	//模型名称
			template: 	'createCollection.html' ,//模板页面名称
			key 	: 	'/school/collection' , 	//state name
			name 	: 	'学校填写采集项' 	//描述
		},
		'/school' 	: 	{
			source 	: 	'school' , 	//模块资源位置(包括controller以及页面
			controller 	: 	'SchoolController' , 	//模型名称
			template: 	'template:generalTemplate.html' ,//模板页面名称
			key 	: 	'/school' , 	//state name
			name 	: 	'学校管理' 	//描述
		},
		'/user' 	: 	{
			source 	: 	'user' , 	//模块资源位置(包括controller以及页面
			controller 	: 	'UserController' , 	//模型名称
			template: 	'user.html' ,//模板页面名称
			key 	: 	'/user' , 	//state name
			name 	: 	'用户管理' 	//描述
		},
		'/eduinst' 	: 	{
			source 	: 	'eduinst' , 	//模块资源位置(包括controller以及页面
			controller 	: 	'EduinstController' , 	//模型名称
			template: 	'template:generalTemplate.html' ,//模板页面名称
			key 	: 	'/eduinst' , 	//state name
			name 	: 	'教育局管理' 	//描述
		},
		'/collection' 	: 	{
			source 	: 	'collection' , 	//模块资源位置(包括controller以及页面
			controller 	: 	'CollectionController' , 	//模型名称
			template: 	'template:generalTemplate.html' ,//模板页面名称
			key 	: 	'/collection' , 	//state name
			name 	: 	'采集项管理' 	//描述
		},
		'/collection/schoolType' 	: 	{
			source 	: 	'collection' , 	//模块资源位置(包括controller以及页面
			controller 	: 	'SchoolTypeCollectionController' , 	//模型名称
			template: 	'schoolTypeCollection.html' ,//模板页面名称
			key 	: 	'/collection/schoolType' , 	//state name
			name 	: 	'学校类型采集项配置' 	//描述
		},
		'/sys/city' 	: 	{
			source 	: 	'sys' , 	//模块资源位置(包括controller以及页面
			controller 	: 	'CityController' , 	//模型名称
			template: 	'template:generalTemplate.html' ,//模板页面名称
			key 	: 	'/sys/city' , 	//state name
			name 	: 	'辖区管理' 	//描述
		},
		'/user/subacc' 	: 	{
			source 	: 	'user' , 	//模块资源位置(包括controller以及页面
			controller 	: 	'SubaccController' , 	//模型名称
			template: 	'subacc.html' ,//模板页面名称
			key 	: 	'/user/subacc' , 	//state name
			name 	: 	'子账号管理' 	//描述
		},
		'/school/schoolType' 	: 	{
			source 	: 	'school' , 	//模块资源位置(包括controller以及页面
			controller 	: 	'SchoolTypeController' , 	//模型名称
			template: 	'template:generalTemplate.html' ,//模板页面名称
			key 	: 	'/school/type' , 	//state name
			name 	: 	'学校类型管理' 	//描述
		},
		'/reporting' 	: 	{
			source 	: 	'reporting' , 	//模块资源位置(包括controller以及页面
			controller 	: 	'ReportingController' , 	//模型名称
			template: 	'template:generalTemplate.html' ,//模板页面名称
			key 	: 	'/reporting' , 	//state name
			name 	: 	'填报年份管理' 	//描述
		},
		'/user/passwd' 	: 	{
			source 	: 	'user' , 	//模块资源位置(包括controller以及页面
			controller 	: 	'ResetPasswdController' , 	//模型名称
			template: 	'resetPasswd.html' ,//模板页面名称
			key 	: 	'/user/passwd' , 	//state name
			name 	: 	'修改密码' 	//描述
		},
		'/school/import' 	: 	{
			source 	: 	'school' , 	//模块资源位置(包括controller以及页面
			controller 	: 	'SchoolClassesImport' , 	//模型名称
			template: 	'classesimport.html' ,//模板页面名称
			key 	: 	'/school/import' , 	//state name
			name 	: 	'绝对班额导入' 	//描述
		},
		'/schoolAuditing' 	: 	{
			source 	: 	'school' , 	//模块资源位置(包括controller以及页面
			controller 	: 	'SchoolAuditingController' , 	//模型名称
			template: 	'template:generalTemplate.html' ,//模板页面名称
			key 	: 	'/schoolAuditing' , 	//state name
			name 	: 	'审核学校列表' 	//描述
		},
		'/eduinstAuditing' 	: 	{
			source 	: 	'eduinst' , 	//模块资源位置(包括controller以及页面
			controller 	: 	'EduinstAuditingController' , 	//模型名称
			template: 	'template:generalTemplate.html' ,//模板页面名称
			key 	: 	'/eduinstAuditing' , 	//state name
			name 	: 	'审核学校列表' 	//描述
		},
		'/eduinstAuditing/reportingData' 	: 	{
			source 	: 	'eduinst' , 	//模块资源位置(包括controller以及页面
			controller 	: 	'EduinstReportingController' , 	//模型名称
			template: 	'template:generalTemplate.html' ,//模板页面名称
			key 	: 	'/eduinstAuditing/reportingData' , 	//state name
			name 	: 	'教育局填报数据' 	//描述
		},
		'/schoolAuditing/reportingData' 	: 	{
			source 	: 	'school' , 	//模块资源位置(包括controller以及页面
			controller 	: 	'SchoolReportingController' , 	//模型名称
			template: 	'template:generalTemplate.html' ,//模板页面名称
			key 	: 	'/schoolAuditing/reportingData' , 	//state name
			name 	: 	'学校填报数据' 	//描述
		},
		'/schoolAuditing/:schoolId/reporting' 	: 	{
			source 	: 	'school' , 	//模块资源位置(包括controller以及页面
			controller 	: 	'SchoolAuditingDetailController' , 	//模型名称
			template: 	'template:reportingDetail.html' ,//模板页面名称
			key 	: 	'/schoolAuditing/reporting' , 	//state name
			name 	: 	'填报详情' 	//描述
		},
		'/schoolAuditing/:schoolId/reportingData' 	: 	{
			source 	: 	'school' , 	//模块资源位置(包括controller以及页面
			controller 	: 	'SchoolReportingDataController' , 	//模型名称
			template: 	'template:reportingDetail.html' ,//模板页面名称
			key 	: 	'/schoolAuditing/reportingDataDetail' , 	//state name
			name 	: 	'填报原数据' 	//描述
		},
		'/eduinstAuditing/:eduinstId/reporting' 	: 	{
			source 	: 	'eduinst' , 	//模块资源位置(包括controller以及页面
			controller 	: 	'EduinstAuditingDetailController' , 	//模型名称
			template: 	'template:reportingDetail.html' ,//模板页面名称
			key 	: 	'/eduinstAuditing/reporting' , 	//state name
			name 	: 	'填报详情' 	//描述
		},
		'/eduinstAuditing/:eduinstId/reportingData' 	: 	{
			source 	: 	'eduinst' , 	//模块资源位置(包括controller以及页面
			controller 	: 	'EduinstReportingDataController' , 	//模型名称
			template: 	'template:reportingDetail.html' ,//模板页面名称
			key 	: 	'/eduinstAuditing/reportingDataDetail' , 	//state name
			name 	: 	'填报原数据' 	//描述
		},
		'/school/connector' 	: 	{
			source 	: 	'school' , 	//模块资源位置(包括controller以及页面
			controller 	: 	'ConnectorController' , 	//模型名称
			template: 	'connector.html' ,//模板页面名称
			key 	: 	'/schoolConnector' , 	//state name
			name 	: 	'学校联系人' 	//描述
		},
		'/bulletin' 	: 	{
			source 	: 	'bulletin' , 	//模块资源位置(包括controller以及页面
			controller 	: 	'BulletinController' , 	//模型名称
			template: 	'bulletin.html' ,//模板页面名称
			key 	: 	'/bulletin' , 	//state name
			name 	: 	'通知公告' 	//描述
		},
		'/bulletin/manage' 	: 	{
			source 	: 	'bulletin' , 	//模块资源位置(包括controller以及页面
			controller 	: 	'BulletinManageController' , 	//模型名称
			template: 	'template:generalTemplate.html' ,//模板页面名称
			key 	: 	'/bulletin/manage' , 	//state name
			name 	: 	'通知公告管理' 	//描述
		},
		'/bulletin/:id/detail' 	: 	{
			source 	: 	'bulletin' , 	//模块资源位置(包括controller以及页面
			controller 	: 	'BulletinDetailController' , 	//模型名称
			template: 	'bulletinDetail.html' ,//模板页面名称
			key 	: 	'/bulletinDetail' , 	//state name
			name 	: 	'通知公告详情' 	//描述
		},
		'/reporting/schoolProcessBoard' 	: 	{
			source 	: 	'school' , 	//模块资源位置(包括controller以及页面
			controller 	: 	'ProcessBoardController' , 	//模型名称
			template: 	'processBoard.html' ,//模板页面名称
			key 	: 	'/schoolProcessBoard' , 	//state name
			name 	: 	'学校整体进度' 	//描述
		},
		'/reporting/countyProcessBoard' 	: 	{
			source 	: 	'dataReporting' , 	//模块资源位置(包括controller以及页面
			controller 	: 	'CountyProcessReportingController' , 	//模型名称
			template: 	'countyProcessReporting.html' ,//模板页面名称
			key 	: 	'/countyProcessBoard' , 	//state name
			name 	: 	'填报进度监控' 	//描述
		},
		'/user/forgetPasswd' 	: 	{
			source 	: 	'user' , 	//模块资源位置(包括controller以及页面
			controller 	: 	'ForgetPasswdController' , 	//模型名称
			template: 	'forgetPasswd.html' ,//模板页面名称
			key 	: 	'/user/forgetPasswd' , 	//state name
			name 	: 	'忘记密码初始化' 	//描述
		},
		'/quotaSituation' 	: 	{
			source 	: 	'quotaSituation' , 	//模块资源位置(包括controller以及页面
			controller 	: 	'QuotaSituationController' , 	//模型名称
			template: 	'template:generalTemplate.html' ,//模板页面名称
			key 	: 	'/quotaSituation' , 	//state name
			name 	: 	'统计指标管理' 	//描述
		},
		'/variable' 	: 	{
			source 	: 	'quotaSituation' , 	//模块资源位置(包括controller以及页面
			controller 	: 	'VariableController' , 	//模型名称
			template: 	'template:generalTemplate.html' ,//模板页面名称
			key 	: 	'/variable' , 	//state name
			name 	: 	'统计指标管理' 	//描述
		},
		'/situationReport' 	: 	{
			source 	: 	'quotaSituation' , 	//模块资源位置(包括controller以及页面
			controller 	: 	'SituationReportController' , 	//模型名称
			template: 	'situationReport.html' ,//模板页面名称
			key 	: 	'/situationReport' , 	//state name
			name 	: 	'数据统计报表下载' 	//描述
		}

 	};
	
	app.service('$fileUpload' , function( ctx ){
		this.upload = function( args , callback , error , complete ){
			if( !args || !args.module || !args.file || !args.validation ){
				error('文件上传参数不正确!') ;
				if( typeof complete == 'function' )
					complete() ;
				return false ;
			}
				
			var formData = new FormData();
			formData.append('file' , args.file.files[0] ) ;
			formData.append('validScript' , args.validation ) ;
			
			$.ajax({
				url  	: 	ctx.api + 'file/'+args.module+'/upload' ,
				type 	: 	'post' ,
				data 	: 	formData ,
				processData 	: 	false ,
				contentType 	: 	false ,
				success 	: 	function( result ){
					console.log( result )
					if( result.code != 0 )
						return mAlert( result.message ) ;
					callback( result.data ) ;
				},
				error 		: 	function( code ){
					if(typeof error == 'function')
						return error( '服务器请求出错!' , code ) ;
					mALert('服务器请求出错,请重试!');
				},
				complete 	: 	function( ){
					if( typeof complete == 'function' )
						complete() ;
				}
			});
		}
	});

	app.config( function( $stateProvider , $urlRouterProvider , 
		$controllerProvider , $compileProvider , $provide , $filterProvider , $locationProvider ) {

		$compileProvider.aHrefSanitizationWhitelist('.*') ;
		$compileProvider.onChangesTtl(2) ;
		$compileProvider.imgSrcSanitizationWhitelist('.*') ;
		app.register = {
			'controller' 	:  	$controllerProvider.register ,
			'service' 		:	$provide.service ,
			'factory' 		: 	$provide.register ,
			'directive' 	: 	$compileProvider.directive ,
			'filter' 		: 	$filterProvider.register	
		};

		$locationProvider.hashPrefix('');

		$urlRouterProvider.when('' , '/') ;

		angular.forEach( urlConfig , function( v , k ) {
			var control = v['controller']; 
			var templateUrl = 'res/html/' ;
			
			var result = /^template:([0-9a-zA-Z_\.]+)$/.exec( v['template'] ) ;
			if(  result && result.length ){
				templateUrl += 'template/' + result[1] ;
			}else {
				templateUrl = 'res/html/'+v['source']+'/'+v['template'] ;
			}
			var jspath = 'app/'+v['source']+'/'+control ;
			var state = {
				url 		: 	k  ,
				views 		: 	{
					'main' 		: 	{
						templateUrl 	: 	templateUrl 
					},
				}
			};
			if( control ){
				state.views.main.controller = control ;
				state['resolve'] ={
					load 	: 	function( $q ){
						var defer = $q.defer() ;
						require([ jspath ] , function( controller ){
							app.register.controller( control  , controller ) ;
							defer.resolve() ;
						});
						return defer.promise ;
					}
				}
			}
			$stateProvider.state( v['key'] , state );
		});
	});
	
	return urlConfig ;
})