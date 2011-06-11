package com.grupo3.productConsult;

import java.util.Locale;

public class PhoneUtils {

	public static final String ENGLISH = "1";
	public static final String SPANISH = "2";

	public static String getLanguageId() {
		String language = Locale.getDefault().getDisplayLanguage();
		return language.contains("English") ? ENGLISH : SPANISH;
	}
}
