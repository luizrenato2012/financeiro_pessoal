
var app = angular.module('gastoApp',['ngRoute','UtilMdl','ServiceMdl','OrcamentoServiceMdl']);

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

/** ----------------  GastoController   ---------------- */
app.controller('gastoController',['$scope', '$http', 'PATH_APP', 'APPLICATION_JSON','gastoService',
                                  'orcamentoService','dateService','logService','$window',
                                  function($scope, $http, PATH_APP, APPLICATION_JSON, gastoService,
                                		  orcamentoService,dateService, logService,$window) {
	
	console.log('Criando gasto controller');
	$scope.init =  function () {
		try {
			console.log('Criando gasto controller');
			var infOrcamento = orcamentoService.getIdDescricaoOrcamento();
			$scope.descOrcamento = infOrcamento.descOrcamento;
			$scope.idOrcamento = infOrcamento.idOrcamento
			$scope.gastos =	   orcamentoService.getListaGastos();
			$scope.data = dateService.getDataFormatada();
		} catch (err) {
			console.log('Erro ao iniciar gasto: ' + err.message);
		}
	}
	//executa init() ao criar o controller 
	$scope.init();

	$scope.paga = function() {
		$scope.mensagemInfo='Enviando...';
		$scope.mensagemErro='';
		var resValidacao = $scope.validaCampos();
		if (resValidacao!='') {
			logService.loga('erro validacao '+ resValidacao);
			$scope.mensagemInfo='';
			$scope.mensagemErro=resValidacao;

		} else {

			gastoService.paga($scope.gastoSel,$scope.data, $scope.valor, $scope.idOrcamento, 
				$scope.descricao).then(
					function(response) {
						$scope.mensagem=response.mensagem;
						logService.loga('Resultado ' + response.tipoMensagem+ ' '+ response.mensagem);
						if (response.tipoMensagem=='OK') {
							$scope.limpa();
							$scope.mensagemInfo=response.mensagem;
							orcamentoService.carregaResumo(response.resumo);
						} else {
							$scope.mensagemInfo='';
							$scope.mensagemErro=response.mensagem;
						}
					},
					function(error) {
						logService.loga('Erro ao pagar ' + error);
						$scope.mensagemInfo='';
						$scope.mensagemErro=response.mensagem;
					}
				);
		}}

	$scope.limpa = function() {
		$scope.data='';
		$scope.valor='';
		$scope.descricao='';
		$scope.mensagemInfo='';
		$scope.mensagemErro='';
		$scope.data = dateService.getDataFormatada();
	}

	$scope.limpaMensagens = function() {
		$scope.mensagemInfo='';
		$scope.mensagemErro='';
	}

	$scope.validaCampos = function() {
		var msg='';
		if ($scope.idOrcamento== undefined || $scope.idOrcamento==null || $scope.idOrcamento==0) {
			msg='Orçamento invalido';
		} else if ($scope.gastoSel== undefined || $scope.gastoSel=='') {
			msg='Selecione um gasto';
		} else if ($scope.data== undefined || $scope.data=='') {
			msg='Digite a data';
		} else if ($scope.valor== undefined || $scope.valor=='' || $scope.valor== 0) {
			msg='Digite um valor';
		}
		return msg;
	}

	//redireciona p/ pagina de login
	$scope.logoff = function() {
		$window.location.href='../autentica.do?acao=logoff';
	}
}]);

/** ---------------------- ContaController ---------------------- */
app.controller('contaController',['$scope', '$http', 'PATH_APP', 'APPLICATION_JSON','gastoService',
                                  'orcamentoService', 'dateService','logService','$window',
                                  function($scope, $http, PATH_APP, APPLICATION_JSON, gastoService,
                                		  orcamentoService,dateService, logService,$window) {
	
	//console.log('Criando conta controller');
	$scope.init =  function () {
		try {
		//	console.log('Criando gasto controller');
			var infOrcamento = orcamentoService.getIdDescricaoOrcamento();
			$scope.descOrcamento = infOrcamento.descOrcamento;
			$scope.idOrcamento = infOrcamento.idOrcamento
			$scope.contas =	   orcamentoService.getListaContas($scope.idOrcamento);
			$scope.data = dateService.getDataFormatada();
		} catch (err) {
			logService.loga('Erro ao iniciar conta: ' + err.message);
		}
	}
	//executa init() ao criar o controller 
	$scope.init();

	$scope.paga = function() {
		$scope.mensagemInfo='Enviando...';
		$scope.mensagemErro='';
		var resValidacao = $scope.validaCampos($scope.idOrcamento, $scope.contaSel, $scope.data, $scope.valor);
		if (resValidacao!='') {
			logService.loga('erro validacao '+ resValidacao);
			$scope.mensagemInfo='';
			$scope.mensagemErro=resValidacao;

		} else {
			contaService.paga($scope.gastoSel,$scope.data, $scope.valor, $scope.idOrcamento, 
				$scope.descricao).then(
					function(response) {
						$scope.mensagem=response.mensagem;
						logService.loga('Resultado paga conta:' + response.tipoMensagem+ ' '+ response.mensagem);
						if (response.tipoMensagem=='OK') {
							$scope.limpa();
							$scope.mensagemInfo=response.mensagem;
							orcamentoService.carregaResumo(response.resumo);
						} else {
							$scope.mensagemInfo='';
							$scope.mensagemErro=response.mensagem;
						}
					},
					function(error) {
						logService.loga('Erro ao pagar ' + error);
						$scope.mensagemInfo='';
						$scope.mensagemErro=response.mensagem;
					}
				);
		}}

	$scope.limpa = function() {
		$scope.data='';
		$scope.valor='';
		$scope.descricao='';
		$scope.mensagemInfo='';
		$scope.mensagemErro='';
		$scope.data = dateService.getDataFormatada();
	}

	$scope.limpaMensagens = function() {
		$scope.mensagemInfo='';
		$scope.mensagemErro='';
	}

	$scope.validaCampos = function(idOrcamento, contaSel, data, valor) {
		var msg='';
		if (idOrcamento== undefined || idOrcamento==null || idOrcamento==0) {
			msg='Orçamento invalido';
		} else if (contaSel== undefined || contaSel=='') {
			msg='Selecione um gasto';
		} else if (data== undefined || data=='') {
			msg='Digite a data';
		} else if (valor== undefined || valor=='' || valor== 0) {
			msg='Digite um valor';
		}
		return msg;
	}

	//redireciona p/ pagina de login
	$scope.logoff = function() {
		$window.location.href='../autentica.do?acao=logoff';
	}


}]);

/** ----------------  PagamentoController ----------------  */
app.controller('pagamentoController',['$scope','gastoService','dateService','logService', function($scope, 
		gastoService, dateService, logService) {

	logService.loga("Criando pagmentoController");

	$scope.titulo = 'Lista de Pagamentos';
	$scope.dataInicial=dateService.getDataFormatada();
	$scope.dataFinal=dateService.getDataFormatada();
	$scope.mensagemPesquisaPag='';

	$scope.listaPagamento = function() {
		$scope.mensagemPesquisaPag='Pesquisando...';
		//logService.loga("Listando pagamentos")
		if($scope.tipoPagamento==undefined || $scope.tipoPagamento==null || $scope.tipoPagamento=='Todos') {
			$scope.tipoPagamento='';
		}
		gastoService.listaPagamento($scope.dataInicial, $scope.dataFinal, $scope.tipoPagamento).then(
				function(response) {
					$scope.pagamentos = response.pagamentos;
					$scope.mensagemPesquisaPag='';
				},
				function(error) {
					logService.loga('Erro ao listar pagamentos ' +error);
					$scope.mensagemErro = error;
					$scope.mensagemPesquisaPag='Erro ao pesquisar';
					console.error(error);	
				}
		);
	}

	/** valor total de pagamentos pesquisados */
	$scope.resumePagamentos = function() {
		if($scope.pagamentos !=undefined && $scope.pagamentos!=null) {
			var total = 0.0 ;
			var i;
			var pagamento;
			for( i=0; i < $scope.pagamentos.length; i++) {
				pagamento = $scope.pagamentos[i];
				total+= parseFloat(pagamento.valor);
			}
			return total;
		} else {
			return 0.0;
		}
	}

}]);

/** ----------------  PendenciaController ---------------- */
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

/** ---------------- ResumoController ---------------- */
app.controller('resumoController',['$scope','orcamentoService','logService','$window', 
                                   function($scope,orcamentoService, logService, $window) {

	//logService.loga('Criando resumeController');

	$scope.resumeOrcamento = function() {
		var resumo = orcamentoService.getResumoOrcamento();

		$scope.valorDisponivel = resumo.valorDisponivel;
		$scope.valorPendente   = resumo.valorPendente;
		$scope.valorSobrante   = resumo.valorSobrante;
		$scope.descOrcamento   = resumo.descOrcamento;
		$scope.idOrcamento   = resumo.idOrcamento;
		$scope.contaPendente   = resumo.contaPendente;
		$scope.gastoPendente   = resumo.gastoPendente;

//		);

	}

	$scope.logoff = function() {
		$window.location.href='../autentica.do?acao=logoff';
	}

	$scope.resumeOrcamento();
}]);

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

/**----------------  RedirectController ---------------- */
app.controller('redirectController',['$scope','$window',function($scope,$window) {
	$scope.logoff = function() {
		console.log('Executando logoff');
		$window.location.href='../autentica.do?acao=logoff';
	}
	$scope.logoff();
}]);


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

