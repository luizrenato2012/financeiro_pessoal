/**  ---------- SERVICES ---------------------  */
var app = angular.module("ServiceMdl",[])

app.service('gastoService', ['$http', 'PATH_APP', 'APPLICATION_JSON' , '$q','logService','$cacheFactory',
                             'resumoService',
                             function($http, PATH_APP, APPLICATION_JSON, $q, logService, $cacheFactory,
                             resumoService) {
	logService.loga('criando gastoService ');
	var cache = $cacheFactory('cacheGasto');
	
	
	this.init = function() {
		resumoService.carregaResumo();
		this.carregaGastos();
	}
	
	this.carregaGastos = function() {
		logService.loga('Carregando gastos');
		this.buscaGastos().then(
			function(response) {
				cache.put('listaGastos',response.gastos);
			}, 
			function(error) {
				console.error(error);
			}
		);
	}
	
	this.iniciaOrcamento = function() {
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
		//	logService.loga('Retornando inicio orcamento');
		return defer.promise;
	}

	this.listaPagamento = function(dataInicial, dataFinal, tipoPagamento) {
		var defer = $q.defer();
		$http({
			method: 'GET',
			url: PATH_APP + 'orcamento?acao=listaPagamento&dataInicial='+ dataInicial +
			'&dataFinal='+ dataFinal + '&tipo='+ tipoPagamento,
			headers: {'Content-type': APPLICATION_JSON}	
		}).success(function(data,status,headers,config,params) {
			defer.resolve(data);
		}).error(function(data,status,headers,config,params) {
			defer.reject(data);
		});

		return defer.promise;
	}

	this.paga = function(gastoSel,data, valor, idOrcamento, descricao) {
		var defer = $q.defer();
		$http({
			method: 'GET',
			url: PATH_APP + 'orcamento?acao=pagaGasto&id='
			+ gastoSel.value+"&data="+ data + 
			'&valor='+valor+"&idOrcamento="+idOrcamento +
			'&descricao='+descricao,
			headers: {'Content-type' : APPLICATION_JSON}
		}).success (function(data,status,headers,config,params) {
			defer.resolve(data);
		}).error(function(data,status,headers,config,params) {
			defer.reject(data);
		});

		return defer.promise;
	}
	
	this.buscaGastos = function () {
		var defer = $q.defer();
		$http({
			method: 'GET',
			url: PATH_APP + 'orcamento?acao=listaGasto'
			//	headers: {'Content-type' : APPLICATION_JSON}
		}).success(function(data,status,headers,config,params){
			//console.log(data.gastos);
			defer.resolve(data);
		}).error(function(data,status,headers,config,params) {
			console.error('Erro ao testar ' + data);
			defer.reject(data);
		});
		return defer.promise;
	};
	
	this.getListaGastos = function() {
		if (cache.get('listaGastos') == null || cache.get('listaGastos') == undefined) {
			return "";
		}
		return cache.get('listaGastos');
	}
	
	this.init();

	
}]);

// ------------------------ contaService ---------------------------------//
app.service('contaService', ['$http','$q','PATH_APP','APPLICATION_JSON', function($http, $q,
		PATH_APP,APPLICATION_JSON) {

	this.listaPendencia = function() {
		var defer = $q.defer();

		$http({
			method: 'GET',
			url: PATH_APP + 'orcamento?acao=listaPendenciaConta',
			headers: {'Content-type' : APPLICATION_JSON}
		}).success( function(data,status,headers, config, params) {
			defer.resolve(data);
		}).error( function(data, status, headers, config, params) {
			defer.reject(data);
		});

		return defer.promise;
	};
}]);

// ----------------------------------resumoService
app.service('resumoService',['$http','$q','PATH_APP','APPLICATION_JSON','$cacheFactory','logService',
                             function($http, $q,PATH_APP,APPLICATION_JSON, $cacheFactory,logService) {
	
	logService.loga('criando resumoService ');
	
	var f = {};
	var cacheResumo = $cacheFactory('cacheResumo');
	
	f.init = function() {
		logService.loga('carregando resumo')
		f.carregaResumo();
	}
	
	f.getResumoOrcamento = function() {
		
		if (cacheResumo.get('objResumo') ==undefined || cacheResumo.get('objResumo') ==null) {
			return {};
		}
		
		var cchResumo = cacheResumo.get('objResumo');
		var resumo={};
		resumo.valorDisponivel= cchResumo.valorDisponivel;
		resumo.valorPendente  = cchResumo.valorPendente;
		resumo.valorSobrante =  cchResumo.valorSobrante;
		resumo.idOrcamento =    cchResumo.idOrcamento;
		resumo.descOrcamento =  cchResumo.descOrcamento;
		resumo.contaPendente =  cchResumo.contaPendente;
		resumo.gastoPendente =  cchResumo.gastoPendente;
		return resumo;
	}
	
	f.getResumoOrcamento = function() {
		var cchResumo = cacheResumo.get('objResumo');
		if (cchResumo   == undefined || cchResumo ==null ||
				cchResumo.valorPendente == undefined ||
				cchResumo.valorSobrante == undefined || 
				cchResumo.descOrcamento == undefined ||
				cchResumo.contaPendente == undefined || 
				cchResumo.gastoPendente == undefined) {
			return {};
		}
		
		var resumo={};
		resumo.valorDisponivel= cchResumo.valorDisponivel;
		resumo.valorPendente  = cchResumo.valorPendente;
		resumo.valorSobrante =  cchResumo.valorSobrante;
		resumo.idOrcamento =    cchResumo.idOrcamento;
		resumo.descOrcamento =  cchResumo.descOrcamento;
		resumo.contaPendente =  cchResumo.contaPendente;
		resumo.gastoPendente =  cchResumo.gastoPendente;
		return resumo;
	}
	
	f.getIdDescricaoOrcamento = function() {
		var cchResumo = cacheResumo.get('objResumo');
		if (cchResumo == undefined || cchResumo ==null ||
				cchResumo.idOrcamento == undefined || cchResumo.idOrcamento == null) {
			return {};
		}
		
		var orcamento={};
		orcamento.idOrcamento =   cchResumo.idOrcamento;
		orcamento.descOrcamento = cchResumo.descOrcamento;
		return orcamento;
	}
	
	f.carregaResumo = function (resumo) {
		var cchResumo = {};
		cchResumo.valorDisponivel = resumo.valorDisponivel;
		cchResumo.valorPendente   = resumo.valorPendente;
		cchResumo.valorSobrante   = resumo.valorSobrante;
		cchResumo.idOrcamento     = resumo.idOrcamento;
		cchResumo.descOrcamento   = resumo.descOrcamento;
		cchResumo.contaPendente   = resumo.contaPendente;
		cchResumo.gastoPendente   = resumo.gastoPendente;
		cacheResumo.put('objResumo', cchResumo);
	}
	
	f.carregaResumo = function () {
		this.findResumo().then(
			function(response) {
				var cchResumo = {};
				cchResumo.valorDisponivel = response.valorDisponivel;
				cchResumo.valorPendente   = response.valorPendente;
				cchResumo.valorSobrante   = response.valorSobrante;
				cchResumo.idOrcamento     = response.idOrcamento;
				cchResumo.descOrcamento   = response.descOrcamento;
				cchResumo.contaPendente   = response.contaPendente;
				cchResumo.gastoPendente   = response.gastoPendente;
				cacheResumo.put('objResumo', cchResumo);
			},
			function (error) {
				console.error ('Erro ao resumir ' + error);
			}
		);
	}
	
	f.findResumo = function(){
		var defer = $q.defer();

		$http({
			method: 'GET',
			url: PATH_APP + 'orcamento?acao=resumeOrcamento',
			headers: {'Content-type': APPLICATION_JSON}
		}).success ( function(data, status, headers, config, params) {
			defer.resolve(data);
		}).error (function (data, status, headers, config, params){
			defer.reject(data);
		});

		return defer.promise;
	};
	
	return f;
	
}]);

//------------------------ contaService ---------------------------------//
/*app.service('testeService',['$http','$q','PATH_APP','APPLICATION_JSON','$cacheFactory', function($http, $q,
		PATH_APP,APPLICATION_JSON,$cacheFactory) {

	var f = {};
	var cacheResumo = $cacheFactory('cacheResumoOrcamento');
	f.carregaGastos = function() {
		this.buscaGastos().then(
			function(response) {
				var listaGastos = response.gastos;
				cacheResumo.put('listaGasto',listaGastos);
			}, 
			function(error) {
				console.error(error);
			}
		);
	}
	
	f.getListaGastos = function() {
		var cchResumo = cacheResumo.get('listaGasto');
		if (cchResumo == undefined || cchResumo ==null ||
				cchResumo.idOrcamento == undefined || cchResumo.idOrcamento == null) {
			return {};
		}
		return cchResumo.gastos;
	}


	f.buscaGastos = function () {

		var defer = $q.defer();
		$http({
			method: 'GET',
			url: PATH_APP + 'orcamento?acao=teste'
			//	headers: {'Content-type' : APPLICATION_JSON}
		}).success(function(data,status,headers,config,params){
			//console.log(data.gastos);
			defer.resolve(data);
		}).error(function(data,status,headers,config,params) {
			console.error('Erro ao testar ' + data);
			defer.reject(data);
		});
		return defer.promise;
	};

	return f;	


}]);  */