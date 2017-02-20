var app = angular.module('ContaServiceMdl', ['ConstantsServiceMdl']);

app.service('contaService', ['$http','$q','PATH_APP', function($http, $q,PATH_APP) {

	this.listaPendencia = function(tipoPendencia) {
		var params = {
			acao: 'listaPendencia'+tipoPendencia,
			tipoGasto: 'FIXO'
		};
		return $http.get(PATH_APP + 'orcamento',{params: params});
	};
	
	this.paga = function(contaSel,data, valor, idOrcamento) {

		var params = {
			acao: 'pagaConta',
			id: contaSel.id,
			data: data,
			valor: valor,
			idOrcamento: idOrcamento
		};
		
		return $http.put(PATH_APP + 'orcamento',{},{params:params});
	}
	
	// lista de contas a serem pagas
	this.getListaContas = function(idOrcamento) {
			if (cache.get('listaContas') == null || cache.get('listaContas') == undefined) {
				return "";
			}
			return cache.get('listaContas');
		
	}
}]);