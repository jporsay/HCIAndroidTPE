package com.grupo3.productConsult.services;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.IntentService;
import android.content.Intent;

import com.grupo3.productConsult.Category;
import com.grupo3.productConsult.Product;
import com.grupo3.productConsult.utilities.PhoneUtils;
import com.grupo3.productConsult.utilities.ServerURLGenerator;
import com.grupo3.productConsult.utilities.XMLParser;

public class CategoriesSearchService extends IntentService {

	private static ServerURLGenerator catalogServer = new ServerURLGenerator(
			"Catalog");
	private static ServerURLGenerator commonServer = new ServerURLGenerator(
			"Common");
	private static ServerURLGenerator securityServer = new ServerURLGenerator(
			"Security");
	private static ServerURLGenerator orderServer = new ServerURLGenerator(
			"Order");

	public CategoriesSearchService(String name) {
		super(name);
	}

	public static List<Category> fetchCategories()
			throws ClientProtocolException, IOException {
		catalogServer.clearParameters();
		catalogServer.addParameter("method", "GetCategoryList");
		catalogServer.addParameter("language_id", PhoneUtils.getLanguageId());
		HttpResponse response = catalogServer.getServerResponse();
		try {
			List<Category> categories = new LinkedList<Category>();
			XMLParser parser = new XMLParser(response);
			NodeList catNodes = parser.getElements("category");
			for (int i = 0; i < catNodes.getLength(); i++) {
				categories.add(parseCategory(parser, catNodes.item(i)));
			}
			return categories;
		} catch (ParseException e) {
		} catch (ParserConfigurationException e) {
		} catch (SAXException e) {
		}
		return null;
	}

	private static Category parseCategory(XMLParser parser, Node node) {
		int id = Integer.parseInt(node.getAttributes().getNamedItem("id")
				.getNodeValue());
		String name = parser.getStringFromSingleElement("name", (Element) node);
		return new Category(name, id);
	}

	public static List<Category> fetchSubCategories(int categoryId)
			throws ClientProtocolException, IOException {
		catalogServer.clearParameters();
		catalogServer.addParameter("method", "GetSubcategoryList");
		catalogServer.addParameter("language_id", PhoneUtils.getLanguageId());
		catalogServer.addParameter("category_id", categoryId + "");
		HttpResponse response = catalogServer.getServerResponse();
		try {
			List<Category> categories = new LinkedList<Category>();
			XMLParser parser = new XMLParser(response);
			NodeList subCatNodes = parser.getElements("subcategory");
			for (int i = 0; i < subCatNodes.getLength(); i++) {
				categories.add(parseCategory(parser, subCatNodes.item(i)));
			}
			return categories;
		} catch (ParseException e) {
		} catch (ParserConfigurationException e) {
		} catch (SAXException e) {
		}
		return null;
	}

	public static List<Product> fetchProductsBySubcategory(int categoryId,
			int subCategoryId) throws IOException {
		catalogServer.clearParameters();
		catalogServer.addParameter("method", "GetProductListBySubcategory");
		catalogServer.addParameter("language_id", PhoneUtils.getLanguageId());
		catalogServer.addParameter("category_id", categoryId + "");
		catalogServer.addParameter("subcategory_id", subCategoryId + "");
		HttpResponse response = catalogServer.getServerResponse();
		try {
			List<Product> products = new LinkedList<Product>();
			XMLParser parser = new XMLParser(response);
			NodeList productNodes = parser.getElements("product");
			for (int i = 0; i < productNodes.getLength(); i++) {
				products.add(parseProduct(parser, productNodes.item(i)));
			}
			return products;
		} catch (ParseException e) {
		} catch (ParserConfigurationException e) {
		} catch (SAXException e) {
		}
		return null;
	}

	private static Product parseProduct(XMLParser parser, Node node) {
		int id = Integer.parseInt(node.getAttributes().getNamedItem("id")
				.getNodeValue());
		String name = parser.getStringFromSingleElement("name", (Element) node);
		double price = Double.parseDouble(parser.getStringFromSingleElement(
				"price", (Element) node));
		return new Product(id, name, price);
	}

	@Override
	protected void onHandleIntent(Intent intent) {

	}
}
