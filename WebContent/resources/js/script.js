/**
 * script da tela de mob/gasto 
 */

var app = angular.module('gastoApp',['ui.mask']);
//app.constant('PATH_APP' ,  'http://lrsantos.com.br/lr_financeiro/' );
app.constant('PATH_APP' ,'http://localhost:8080/financeiro_pessoal/' );
app.constant('APPLICATION_JSON' ,'application/json' );

/** CONTROLLER          **/
app.controller('gastoController',['$scope', '$http', 'PATH_APP', 'APPLICATION_JSON','gastoService',
                                  function($scope, $http, PATH_APP, APPLICATION_JSON, gastoService) {

	$scope.init =  function () {

		gastoService.iniciaOrcamento().then(
			function(response) {
				$scope.descOrcamento = response.descOrcamento;
				$scope.idOrcamento = response.idOrcamento
				$scope.gastos =	   response.gastos;
				$scope.resumeOrcamento(response);
			},
			function (error) {
				console.log('Erro ' + error);
				$scope.mensagemErro = error; 
			}
		);
	}

	$scope.init();

	$scope.paga = function() {
		$scope.mensagemInfo='Enviando...';
		$scope.mensagemErro='';
		var resValidacao = $scope.validaCampos();
		if (resValidacao!='') {
			console.log('erro validacao '+ resValidacao);
			$scope.mensagemInfo='';
			$scope.mensagemErro=resValidacao;

		} else {
			$http({
				method: 'GET',
				url: PATH_APP + 'orcamento?acao=pagaGasto&id='
				+$scope.gastoSel.value+"&data="+ $scope.data + 
				'&valor='+$scope.valor+"&idOrcamento="+$scope.idOrcamento +
				'&descricao='+$scope.descricao,
				headers: {'Content-type' : APPLICATION_JSON }
			}).success (function(data,status,headers,config,params) {
				console.log('Resultado ' + data.tipoMensagem+ ' '+ data.mensagem);
				$scope.mensagem=data.mensagem;
				if (data.tipoMensagem=='OK') {
					$scope.limpa();
					$scope.mensagemInfo=data.mensagem;
					$scope.resumeOrcamento(data);
				} else {
					$scope.mensagemInfo='';
					$scope.mensagemErro=data.mensagem;
				}
			}).error(function(data,status,headers,config,params) {
				$scope.mensagemInfo='';
				$scope.mensagemErro=data.mensagem;
			});
		}
	}

	$scope.limpa = function() {
		$scope.data='';
		$scope.valor='';
		$scope.descricao='';
		$scope.mensagemInfo='';
		$scope.mensagemErro='';
	}

	$scope.limpaMensagens = function() {
		$scope.mensagemInfo='';
		$scope.mensagemErro='';
	}

	$scope.validaCampos = function() {
		var msg='';
		if ($scope.idOrcamento== undefined || $scope.idOrcamento==null || $scope.idOrcamento==0) {
			msg='Orçamento invalido';
		}
		if ($scope.data== undefined || $scope.data=='') {
			msg='Digite a data';
		}
		else if  ($scope.valor== undefined || $scope.valor=='' || $scope.valor== 0) {
			msg='Digite um valor';
		}
		return msg;
	}

	$scope.resumeOrcamento = function(data) {
		$scope.valorDisponivel = data.valorDisponivel;
		$scope.valorPendente = data.valorPendente;
		$scope.valorSobrante = data.valorSobrante;
	}
}]);

/**  ---------- SERVICES ---------------------  */
app.service('gastoService', ['$http', 'PATH_APP', 'APPLICATION_JSON' , '$q',
                     function($http, PATH_APP, APPLICATION_JSON, $q) {

	this.iniciaOrcamento = function() {

	//	console.log('Iniciando orcamento');

		var defer = $q.defer();
		$http({
			method: 'GET',
			url: PATH_APP + 'orcamento?acao=iniciaOrcamento',
			headers: {'Content-type' : APPLICATION_JSON}
		}).success (function(data,status,headers,config,params) {
			defer.resolve(data);
		}).error(function(data,status,headers,config,params) {
			defer.reject(data);
		});

		return defer.promise;
	}

}]);