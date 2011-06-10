package com.grupo3.productConsult.utilities;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.util.Log;

public class XMLParser {
	private Document xml;
	
	public XMLParser(HttpResponse response) throws
		ParseException, IOException, ParserConfigurationException, SAXException {
		this.xml = this.createDocument(response);
	}
	
	private Document createDocument(HttpResponse r) throws
		ParseException, IOException, ParserConfigurationException, SAXException {
		
		HttpEntity r_entity = r.getEntity();
		String xmlString = EntityUtils.toString(r_entity);
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		InputSource inStream = new InputSource();
		inStream.setCharacterStream(new StringReader(xmlString));
		
		return db.parse(inStream);
	}
	
	public Document getXML() {
		return this.xml;
	}
	
	public NodeList getElements(String element) {
		return this.xml.getElementsByTagName(element);
	}
	
	public NamedNodeMap getAttributes(Element e) {
		return e.getAttributes();
	}
	
	public String getErrorMessage() {
		if (!this.serverOk()) {
			return this.getAttribute((Element) this.getElements("error").item(0), "message");
		}
		return null;
	}
	
	public boolean serverOk() {
		NodeList resp = this.getElements("response");
		String status = this.getAttribute((Element) resp.item(0), "status");
		return !status.equals("fail");
	}
	
	public String getStringFromSingleElement(String tag) {
		Element e = (Element) this.xml.getElementsByTagName(tag).item(0);
		return e.getFirstChild().getNodeValue();
	}
	
	public String getAttribute(Element element, String attribute) {
		return element.getAttribute(attribute);
	}
}
