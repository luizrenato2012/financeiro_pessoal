
var app = angular.module('gastoApp',['ngRoute','UtilMdl','ContaControllerMdl','GastoControllerMdl',
                                     'ConfigControllerMdl','PagamentoControllerMdl','PendenciaControllerMdl',
                                     'RedirectControllerMdl','ResumoControllerMdl']);

/** Constantes*/
//app.constant('PATH_APP' ,  'http://lrsantos.com.br/lr_financeiro/' );
//app.constant('PATH_APP' ,'http://10.0.20.185:8080/financeiro_pessoal/' );
app.constant('PATH_APP' ,'http://localhost:8080/financeiro_pessoal/' );
app.constant('APPLICATION_JSON' ,'application/json' );

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
	.when('/redirect', {
		templateUrl: 'redirect.html', controller: 'redirectController' 
	});
//	.otherwise({
//		redirectTo: '/'
//	});		
}]);

