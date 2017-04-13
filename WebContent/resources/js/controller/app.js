
var app = angular.module('financApp',['ngRoute','ServiceUtilMdl','ContaControllerMdl','GastoControllerMdl',
                                     'ConfigControllerMdl','PagamentoControllerMdl','PendenciaControllerMdl',
                                     'RedirectControllerMdl','ResumoControllerMdl','ConstantsServiceMdl',
                                     'OrcamentoServiceMdl','OrcamentoControllerMdl','TesteServiceMdl']);

app.run(function($rootScope,orcamentoService,orcamentoService){
	console.log('Iniciando a aplicacao');
	orcamentoService.carregaResumo();
	orcamentoService.carregaOrcamento();
	console.log('Finalizada inicialização');
	
});


app.controller('testeController',['$scope','dateService','testeService','orcamentoService','logService','PATH_APP','$http', '$q',
                                  function($scope,dateService,testeService,orcamentoService,
                                		  logService, PATH_APP, $http, $q) {
	logService.loga("Criando testeController");

	$scope.testeDataHora = function() {
		$scope.strTeste = dateService.getDataHoraFormatada();
	}

	$scope.testeLog = function() {
		logService.loga('Testando geracao de log');
	}

	$scope.testeOrcamento = function() {
		$http({
			method: 'GET',
			url: PATH_APP + 'orcamento?acao=teste',
			headers: {'Content-type' : "application/json" }
		}).success (function(data,status,headers,config,params) {
			//logService.loga('Mensagem ' + data.mensagem);
			//logService.loga('Resultado ' + data.resultado);
		}).error(function(data,status,headers,config,params) {
			logService.loga('Erro em teste ' + data);
		});  
	}

	$scope.gastosTeste = [];

	$scope.actionCarrega = function() {
		$scope.mensagem = 'Iniciando...';
		testeService.carregaGastos();
		$scope.mensagem = 'Terminado';
	}

	$scope.listaGastos = function() {
		$scope.gastosTeste = testeService.getListaGastos();
	}

	$scope.carregaResumo = function() {
		orcamentoService.carregaResumo();
	}

	$scope.getResumo = function() {
		$scope.resumo = orcamentoService.getResumoOrcamento();
		console.log('getResumo' + $scope.resumo);
	}

	$scope.listaGastos();

}]);

app.config(['$routeProvider','$locationProvider', function($routeProvider,$locationProvider) {
	$locationProvider.hashPrefix('!');

	$routeProvider
	.when('/gasto',{
		templateUrl: 'gasto.html', controller: 'gastoController'
	})
		.when('/conta', {
			templateUrl: 'conta.html', controller: 'contaController'
		})
	.when('/resumo', {
		templateUrl: 'resumo.html',controller: 'resumoController'
	})
	.when('/pagamentos', {
		templateUrl: 'lista_pagamento.html', controller: 'pagamentoController' 
	})
	.when('/configuracoes', {
		templateUrl: 'config.html', controller: 'configController' 
	})
	.when('/pendencias', {
		templateUrl: 'lista_pendencia.html', controller: 'pendenciaController' 
	})
	.when('/orcamentos', {
		templateUrl: 'lista_orcamento.html', controller: 'orcamentoController' 
	})
	.when('/redirect', {
		templateUrl: 'redirect.html', controller: 'redirectController' 
	})
	.otherwise({
		//redirectTo: '/'
		templateUrl: 'teste.html', controller: 'testeController' 
	});		
}]);

