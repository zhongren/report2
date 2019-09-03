require.config({
	baseUrl 	: 	'/' ,
	waitSeconds 	: 	0 ,
	urlArgs 	: 	new Date().getTime()+'' ,
	paths 		: 	{
		jquery 		: 	'lib/jquery/jquery.min' ,
		cookie 		: 	'lib/jquery/jquery.cookie' ,
		json 		: 	'lib/jquery/jquery.json' ,
		validate 	: 	'lib/jquery/jquery.validate' ,
		message 	: 	'lib/jquery/messages_zh' ,
		chart 		: 	'lib/chart/highcharts' ,
		qrcode 		: 	'lib/jquery/jquery.qrcode.min' ,
		bootstrap 	: 	'lib/bootstrap/js/bootstrap' ,
		bootstrapSelect : 	'lib/bootstrap-select/bootstrap-select' ,
		bootstrapSwitch : 	'lib/bootstrap-switch/bootstrap-switch' ,
		datatable 	: 	'lib/datatable/datatables.min' ,
		timepicker 	: 	'lib/datapicker/bootstrap-datetimepicker' ,
		moment 		: 	'lib/datapicker/moment-with-locales' ,
		angular 	: 	'lib/angular/angular.min' ,
		router 		: 	'lib/angular/angular-ui-router' ,
		app 		: 	'res/js' ,
		prop		: 	'res/js/properties' ,
		util 		: 	'res/js/util' ,
		component 	: 	'res/js/component' ,
		application : 	'res/js/AppController' ,
		treeview 	: 	'lib/treeview/bootstrap-treeview.min' ,
		directive 	: 	'directive',
		fileinput   :   'lib/bootstrap-fileinput/fileinput.min' ,
		beautify	: 	'lib/beautify/beautify'
	},
	shim 		: 	{
		cookie 		: 	{
			deps 	: 	['jquery']
		},
		json 		: 	{
			deps 	: 	['jquery']
		},
		validate 	: 	{
			deps 	: 	['jquery']
		},
		qrcode 		: 	{
			deps 	: 	['jquery']
		},
		timepicker 	: 	{
			deps 	: 	['bootstrap' , 'moment']
		},
		bootstrap 	: 	{
			deps 	: 	['jquery']
		},
		datatable 	: 	{
			deps 	: 	['jquery']
		},
		router 		: 	{
			deps 	: 	['angular']
		},
		util 		: 	{
			deps 	: 	['jquery' , 'cookie' , 'json' , 'validate','bootstrap','timepicker']
		},
		app 		: 	{
			deps 	: 	[ 'angular','cookie' , 'json' , 'validate' , 'qrcode' , 'bootstrap' , 'datatable' , 'router' ,'util'] 
		},
		component 	: 	{
			deps 	: 	['jquery']
		},
		application	: 	{
			deps 	: 	['jquery' , 'cookie' , 'json' , 'validate','bootstrap','timepicker' ]
		},
		message 	:	{
			deps 	: 	['jquery' , 'validate']
		},
		treeview 	: 	{
			deps 	: 	['jquery' , 'bootstrap']
		},
		bootstrapSelect:{
			deps	: 	['bootstrap']
		},
		bootstrapSwitch : {
			deps 	: 	['bootstrap']
		},
		directive 	: 	{
			deps 	: 	['jquery' , 'cookie' , 'json' , 'validate','bootstrap','timepicker','chart' ]
		}
	}
});

//Base lib .
define( [ 'prop' , 'app/config','jquery','util','validate','message','bootstrapSelect' ,'timepicker','moment' ,
          'bootstrapSwitch','chart' ,'treeview','fileinput'] , function( prop , urlConfig ){
	angular.bootstrap( document , ['app'] ) ;
});