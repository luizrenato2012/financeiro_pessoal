var app = angular.module('ContaServiceMdl', ['ConstantsServiceMdl']);

app.service('contaService', ['$http','$q','PATH_APP', function($http, $q,PATH_APP) {

	this.listaPendencia = function(tipoPendencia) {
		var defer = $q.defer();
		$http({
			method: 'GET',
		//	url: PATH_APP + 'orcamento?acao=listaPendenciaConta',
			url: PATH_APP + 'orcamento?acao=listaPendencia'+tipoPendencia,
			headers: {'Content-type' : application/json}
		}).success( function(data,status,headers, config, params) {
			defer.resolve(data);
		}).error( function(data, status, headers, config, params) {
			defer.reject(data);
		});

		return defer.promise;
	};
	
	this.paga = function(gastoSel,data, valor, idOrcamento, descricao) {
		var defer = $q.defer();
		$http({
			method: 'GET',
			url: PATH_APP + 'orcamento?acao=pagaConta&id='
			+ gastoSel.value+"&data="+ data + 
			'&valor='+valor+"&idOrcamento="+idOrcamento +
			'&descricao='+descricao,
			headers: {'Content-type' : application/json}
		}).success (function(data,status,headers,config,params) {
			defer.resolve(data);
		}).error(function(data,status,headers,config,params) {
			defer.reject(data);
		});

		return defer.promise;
	}
	
	// lista de contas a serem pagas
	this.getListaContas = function(idOrcamento) {
			if (cache.get('listaContas') == null || cache.get('listaContas') == undefined) {
				return "";
			}
			return cache.get('listaContas');
		
	}
}]);