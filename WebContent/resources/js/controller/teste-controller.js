var testeApp = angular.module('TesteControllerMdl',['TesteServiceMdl']);

testeApp.controller('testeController',['$scope', function($scope){
	
	$scope.doGet = function doPost() {
		console.log('Testando ...');
	}
		
}]);