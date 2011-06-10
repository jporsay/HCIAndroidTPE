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

import com.grupo3.productConsult.Category;
import com.grupo3.productConsult.Product;
import com.grupo3.productConsult.utilities.PhoneUtils;
import com.grupo3.productConsult.utilities.ServerURLGenerator;
import com.grupo3.productConsult.utilities.XMLParser;

public class CategoriesSearchService {
	private static ServerURLGenerator catalogServer = new ServerURLGenerator(
			"Catalog");
	private static ServerURLGenerator commonServer = new ServerURLGenerator(
			"Common");
	private static ServerURLGenerator securityServer = new ServerURLGenerator(
			"Security");
	private static ServerURLGenerator orderServer = new ServerURLGenerator(
			"Order");

	private final static int BOOKS_ID = 1;
	private final static int MOVIES_ID = 2;

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
		List<Category> subCategories = new LinkedList<Category>();

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
			int subCategoryId) {
		List<Product> products = new LinkedList<Product>();
		products.add(new Product(32, "Wolverine", 19.0));
		products.add(new Product(67, "Thinkerbell", 12.5));
		return products;
	}
}
