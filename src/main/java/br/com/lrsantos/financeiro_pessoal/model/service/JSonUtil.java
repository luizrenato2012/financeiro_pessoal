package br.com.lrsantos.financeiro_pessoal.model.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class JSonUtil {
	 private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	 public static JsonElement parseDateToJSonElement(Object dateObj) {
		 String dateStr = dateObj!=null? dateFormat.format((Date)dateObj) :"";
		 return new JsonParser().parse(dateStr);
	 }
	 
	 public static JsonElement parseStringToJSonElement(Object strObj) {
		 String str = strObj!=null? ((String)strObj).trim() :"";
		 return new JsonParser().parse(str);
	 }
	 
	 public static JsonElement parseObjectToJSonElement(Object strObj) {
		 return new JsonParser().parse(new Gson().toJson(strObj));
	 }
	 
	 public static String parseDateToString(Object dateObj) {
		 return dateObj!=null? dateFormat.format((Date)dateObj) :"";
	 }

}
