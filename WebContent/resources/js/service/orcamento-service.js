var modulo = angular.module('OrcamentoServiceMdl',['ServiceUtilMdl','ConstantsServiceMdl']);

modulo.factory('orcamentoService', function($http, PATH_APP, $q, logService, $cacheFactory){
	logService.loga('criando orcamento service ');
	var cache = $cacheFactory('cacheOrcamento');
	
	/** lista de Gastos e Contas pendentes p/ preecnhimento de combo */
	var _carregaOrcamento = function() {
		this.buscaGastosContasResumo()
			.success(function(data,status,headers,config,params){
				cache.put('listaGastos',data.gastos.listaPendencias);
				cache.put('listaContas',data.contas.listaPendencias);
				cache.put('resumo',data.resumo);
			}).error(function(data,status,headers,config,params) {
				console.error('Erro ao buscar gastos ' + data);
			});
	}

	var _buscaGastosContasResumo = function () {
		return $http.get(PATH_APP + 'orcamento' , {params: {acao: 'orcamento'} });
	};

	var _getListaGastos = function() {
		if (cache.get('listaGastos') == null || cache.get('listaGastos') == undefined) {
			return "";
		}
		return cache.get('listaGastos');
	}

	var _getListaContas = function() {
		if (cache.get('listaContas') == null || cache.get('listaContas') == undefined) {
			return "";
		}
		return cache.get('listaContas');
	}

	var _getResumoOrcamento = function() {
		var resumo = cache.get('resumo');
		if (resumo   == undefined || resumo ==null ||
				resumo.valorPendente == undefined ||
				resumo.valorSobrante == undefined || 
				resumo.descOrcamento == undefined ||
				resumo.contaPendente == undefined || 
				resumo.gastoPendente == undefined) {
			return {};
		}
		return resumo;
	}

	var _getIdDescricaoOrcamento = function() {
		var resumo = cache.get('resumo');
		if (resumo == undefined || resumo ==null ||
				resumo.idOrcamento == undefined || resumo.idOrcamento == null) {
			return {};
		}

		var orcamento={
			idOrcamento :resumo.idOrcamento,
			descOrcamento :resumo.descOrcamento };
		return orcamento;
	}

	var _paga = function(gastoSel,data, valor, idOrcamento, descricao) {
		var defer = $q.defer();
		$http({
			method: 'GET',
			url: PATH_APP + 'orcamento?acao=pagaGasto&id='
			+ gastoSel.value+"&data="+ data + 
			'&valor='+valor+"&idOrcamento="+idOrcamento +
			'&descricao='+descricao,
			headers: {'Content-type' : 'application/json'}
		}).success (function(data,status,headers,config,params) {
			defer.resolve(data);
		}).error(function(data,status,headers,config,params) {
			defer.reject(data);
		});

		return defer.promise;
	}
	
	/** verificar se ainda usa */
	var _carregaResumo = function () {
		var defer = $q.defer();
		
		$http.get(PATH_APP + 'orcamento?', {params: {acao: 'resumeOrcamento'}})
			.success ( function(data, status, headers, config, params) {
				var cchResumo = {};
				cchResumo.valorDisponivel = data.valorDisponivel;
				cchResumo.valorPendente   = data.valorPendente;
				cchResumo.valorSobrante   = data.valorSobrante;
				cchResumo.idOrcamento     = data.idOrcamento;
				cchResumo.descOrcamento   = data.descOrcamento;
				cchResumo.contaPendente   = data.contaPendente;
				cchResumo.gastoPendente   = data.gastoPendente;
				cache.put('resumo', cchResumo);
				defer.resolve(data);
			}).error (function (data, status, headers, config, params){
				defer.reject(data);
			});
		return defer.promise;
	}
	
	var _atualizaResumoConta = function (resumo, contas) {
		cache.put('resumo', resumo);
		cache.put('listaContas',contas);
	}
	
	var _atualizaResumoGasto = function (resumo, gastos) {
		cache.put('resumo', resumo);
		cache.put('listaGastos',gastos);
	}
	
	var _listaOrcamento = function() {
		return $http.get(PATH_APP + 'orcamento', {params: {acao: 'listaOrcamento'}} );
	}
	
	var _ativaOrcamento = function(idOrcamento) {
		return $http.put(PATH_APP + 'orcamento',{},{params:{ acao: 'ativaOrcamento',
			idOrcamento: idOrcamento}} );
	}
	
	return {
		carregaOrcamento : _carregaOrcamento,
		buscaGastosContasResumo : _buscaGastosContasResumo,
		getListaGastos : _getListaGastos,
		getListaContas : _getListaContas, 
		getResumoOrcamento : _getResumoOrcamento,
		getIdDescricaoOrcamento : _getIdDescricaoOrcamento,
		paga : _paga,
		carregaResumo : _carregaResumo,
		atualizaResumoConta : _atualizaResumoConta,
		atualizaResumoGasto : _atualizaResumoGasto,
		listaOrcamento : _listaOrcamento, 
		ativaOrcamento : _ativaOrcamento
	}
});