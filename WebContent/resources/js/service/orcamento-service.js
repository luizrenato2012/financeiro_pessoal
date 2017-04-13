var modulo = angular.module('OrcamentoServiceMdl',['ServiceUtilMdl','ConstantsServiceMdl']);

modulo.service('orcamentoService', ['$http', 'PATH_APP',  '$q','logService','$cacheFactory',
                                    function($http, PATH_APP, $q, logService, $cacheFactory) {
//	logService.loga('criando orcamento service ');
	var cache = $cacheFactory('cacheOrcamento');

	/** lista de Gastos e Contas pendentes p/ preecnhimento de combo */
	this.carregaOrcamento = function() {
		this.buscaGastosContasResumo()
			.success(function(data,status,headers,config,params){
				cache.put('listaGastos',data.gastos.listaPendencias);
				cache.put('listaContas',data.contas.listaPendencias);
				cache.put('resumo',data.resumo);
			}).error(function(data,status,headers,config,params) {
				console.error('Erro ao buscar gastos ' + data);
			});
	}

	this.buscaGastosContasResumo = function () {
		var params = {acao: 'orcamento'};
		return $http.get(PATH_APP + 'orcamento' , {params: params});
	};

	this.getListaGastos = function() {
		if (cache.get('listaGastos') == null || cache.get('listaGastos') == undefined) {
			return "";
		}
		return cache.get('listaGastos');
	}

	this.getListaContas = function() {
		if (cache.get('listaContas') == null || cache.get('listaContas') == undefined) {
			return "";
		}
		return cache.get('listaContas');
	}

	this.getResumoOrcamento = function() {
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

	this.getIdDescricaoOrcamento = function() {
		var resumo = cache.get('resumo');
		if (resumo == undefined || resumo ==null ||
				resumo.idOrcamento == undefined || resumo.idOrcamento == null) {
			return {};
		}

		var orcamento={};
		orcamento.idOrcamento =   resumo.idOrcamento;
		orcamento.descOrcamento = resumo.descOrcamento;
		return orcamento;
	}

	this.paga = function(gastoSel,data, valor, idOrcamento, descricao) {
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
	this.carregaResumo = function () {
		var defer = $q.defer();
		var params = {acao: 'resumeOrcamento'}; 
		
		$http.get(PATH_APP + 'orcamento?', {params: params})
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
	
	this.atualizaResumoConta = function (resumo, contas) {
		cache.put('resumo', resumo);
		cache.put('listaContas',contas);
	}
	
	this.atualizaResumoGasto = function (resumo, gastos) {
		cache.put('resumo', resumo);
		cache.put('listaGastos',gastos);
	}
	
	this.listaOrcamento = function() {
		var params = {
				acao: 'listaOrcamento'
			};
		return $http.get(PATH_APP + 'orcamento', {params: params});
	}
	
	this.ativaOrcamento = function(idOrcamento) {
		var params = {
			acao: 'ativaOrcamento',
			idOrcamento: idOrcamento
		};
		
		return $http.put(PATH_APP + 'orcamento',{},{params:params});
	}
}]);