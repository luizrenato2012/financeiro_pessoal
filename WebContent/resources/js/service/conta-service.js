var app = angular.module('ContaServiceMdl', ['ConstantsServiceMdl']);

app.factory('contaService', function($http, $q,PATH_APP) {
	
	var _listaPendencia = function(tipoPendencia) {
		var params = {
			acao: 'listaPendencia'+tipoPendencia,
			tipoGasto: 'FIXO'
		};
		return $http.get(PATH_APP + 'orcamento',{params: params});
	};
	
	var _paga = function(contaSel,data, valor, idOrcamento) {
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
	var _getListaContas = function(idOrcamento) {
			if (cache.get('listaContas') == null || cache.get('listaContas') == undefined) {
				return "";
			}
			return cache.get('listaContas');
	}
	
	return {
		listaPendencia : _listaPendencia,
		paga : _paga,
		getListaContas : _getListaContas
	}
});