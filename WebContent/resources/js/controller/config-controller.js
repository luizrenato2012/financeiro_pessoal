var app = angular.module('ConfigControllerMdl',[]);

/** carga de dados em cache : resumo, dados do orcamento atual */
/* ---------------- ConfigController ----------------  */
app.controller('configController',['$scope','orcamentoService','gastoService',function($scope,orcamentoService, gastoService) {
	
	$scope.msgCarga='';
	console.log('criando configController');
	
	$scope.carregaResumo = function() {
		$scope.msgCarga='Carregando resumo...';
		orcamentoService.carregaResumo();
		$scope.msgCarga='Resumo carregado.';
	}
	
	$scope.carregaGastos = function() {
		$scope.msgCarga = 'Carregando gastos...';
		gastoService.carregaGastos();
		$scope.msgCarga = 'Gastos carregados.';
	}
	
	$scope.logoff = function() {
		$window.location.href='../autentica.do?acao=logoff';
	}


}]);

