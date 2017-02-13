var app =  angular.module('PendenciaControllerMdl',['ServiceUtilMdl','ContaServiceMdl']);

app.controller('pendenciaController',['$scope','logService','contaService', function($scope,logService, contaService) {

	$scope.titulo='Pagamento de Conta';
	$scope.listaContasPendentes = [];
	$scope.msg = '';
	$scope.tipoPendencia = {}; 
	
	$scope.listaPendencia = function(tipo) {
		contaService.listaPendencia(tipo).success( function(data,status,headers, config, params) {
			$scope.msg='Pesquisando pendencias';
			$scope.listaContasPendentes = data.listaPendencias;
			$scope.msg='';
		}).error( function(data, status, headers, config, params) {
			logService.loga('Erro ao lista pendencia conta ' + error);
			$scope.msg='Erro ao pesquisar';
		});
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