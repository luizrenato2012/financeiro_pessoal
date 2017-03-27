package br.com.lrsantos.financeiro_pessoal.model.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JSonUtilRetorno {
	
	private JsonObject objJson;
	
	public JSonUtilRetorno() {
		objJson = new JsonObject();
	}

	public JSonUtilRetorno adicionaRetorno(String label, Object objeto) {
		objJson.add(label, new JsonParser().parse(new Gson().toJson(objeto)));
		return this;
	}

	public JsonObject getObjJson() {
		return objJson;
	}
	
}
