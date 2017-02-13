var app = angular.module('ConfigControllerMdl',[]);

/** carga de dados em cache : resumo, dados do orcamento atual */
/* ---------------- ConfigController ----------------  */
app.controller('configController',['$scope','orcamentoService','gastoService',function($scope,orcamentoService, gastoService) {
	
	$scope.msgCarga='';
//	console.log('criando configController');
	
	$scope.carregaOrcamento = function() {
		$scope.msgCarga='Carregando resumo...';
		orcamentoService.carregaResumo().then(
			function(response) {
				$scope.msgCarga='Resumo carregado.';
			},
			function(error) {
				scope.msgCarga='Erro ao carregar dados...';
				console.log('Erro ao carregar orcamento '+ error);
			}
		);
	}
	
//	$scope.carregaGastos = function() {
//		$scope.msgCarga = 'Carregando gastos...';
//		gastoService.carregaGastos();
//		$scope.msgCarga = 'Gastos carregados.';
//	}
	
	$scope.logoff = function() {
		$window.location.href='../autentica.do?acao=logoff';
	}


}]);

