var app = angularjs.module('RedirectController',[]);

app.controller('redirectController',['$scope','$window',function($scope,$window) {
	$scope.logoff = function() {
		console.log('Executando logoff');
		$window.location.href='../autentica.do?acao=logoff';
	}
	$scope.logoff();
}]);
