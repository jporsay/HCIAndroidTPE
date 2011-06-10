package com.grupo3.productConsult.utilities;

import java.util.Locale;

public class PhoneUtils {

	private static final String ENGLISH = "1";
	private static final String SPANISH = "2";

	public static String getLanguageId() {
		String language = Locale.getDefault().getDisplayLanguage();
		return language.contains("English") ? ENGLISH : SPANISH;
	}
}
