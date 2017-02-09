var app =  angular.module('PendenciaControllerMdl',['ServiceUtilMdl']);

app.controller('pendenciaController',['$scope','logService','contaService', function($scope,logService, contaService) {

	$scope.titulo='Pagamento de Conta';
	$scope.listaContasPendentes = [];
	$scope.msg = 'Pesquisando pendencias';
	$scope.tipoPendencia = {}; 
	logService.loga('Criando pendenciaController');
	
	$scope.listaPendencia = function(tipo) {
		contaService.listaPendencia(tipo).then(
				function(response) {
					logService.loga('Tipo de pendencia '+ $scope.tipoPendencia);
					$scope.listaContasPendentes = response.listaPendencias;
					$scope.msg='';
				},
				function(error) {
					logService.loga('Erro ao lista pendencia conta ' + error);
					$scope.msg='Erro ao pesquisar';
				}
		);
	}
	
	$scope.teste = function () {
		console.log('Acionado ' + $scope.tipoPendencia);
	}

	$scope.totalizaContasPendentes = function() {
		if ($scope.listaContasPendentes != undefined && $scope.listaContasPendentes !=null){
			/** valor total de pagamentos pesquisados */
			var total = 0.0 ;
			var i;
			var pendencia;
			for( i=0; i < $scope.listaContasPendentes.length; i++) {
				pendencia = $scope.listaContasPendentes[i];
				total+= parseFloat(pendencia.valor);
			}
			return total;
		} else {
			return 0.0;
		}
	}

}]);