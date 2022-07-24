package com.eka.farmerconnect.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.eka.farmerconnect.model.GeneralSettings;

@Component
public class CommonUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);

	private static final String DATEFORMAT = "dd-MMM-yyyy";
	private static final String DATEPATTERN = "yyyy-MM-dd'T'HH:mm:ss.mmmZ";
	private static final String ISO_DATE_FORMAT  = "yyyy-MM-dd'T'HH:mm:ss";
	
	public static GeneralSettings parseGeneralSettings(Map<FieldInfo, Object> fieldsInfoMap) {
		
		Iterator<FieldInfo> keyItr = fieldsInfoMap.keySet().iterator();
		Iterator<Object> valuesItr = fieldsInfoMap.values().iterator();
		GeneralSettings gs = new GeneralSettings(parseValue(keyItr.next(), valuesItr.next()), 
												 parseValue(keyItr.next(), valuesItr.next()), 
												 parseValue(keyItr.next(), valuesItr.next()), 
												 parseValue(keyItr.next(), valuesItr.next()), 
												 parseValue(keyItr.next(), valuesItr.next()), 
												 parseValue(keyItr.next(), valuesItr.next()),
												 parseValue(keyItr.next(), valuesItr.next()),
												 parseValue(keyItr.next(), valuesItr.next()),
												 parseValue(keyItr.next(), valuesItr.next()));
		return gs;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T parseValue(FieldInfo fieldInfo, Object value) {
		switch (fieldInfo.getFieldType()) {
		case CHECKBOX:
			return (T) new Boolean(new Integer(1).equals(value));
		case TEXTFIELD:
		case RADIO:
			return (T) (Objects.nonNull(value) ?  String.valueOf(value) : "");
		default:
			return null;
		}
	}
	
	public HttpHeaders getHeaders(HttpServletRequest request) {

		HttpHeaders headers = new HttpHeaders();

		Enumeration<?> names = request.getHeaderNames();

		while (names.hasMoreElements()) {

			String name = (String) names.nextElement();
			headers.add(name, request.getHeader(name));
		}
		return headers;

	}
	
	public static String getFormattedDate(Date inputDate) {

		String formattedDate = null;
		String isoDatePattern = DATEPATTERN;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(isoDatePattern);
		formattedDate = simpleDateFormat.format(inputDate);

		return formattedDate;
	}

	
	public static String getFormattedDate(String inputDate) {

		String formattedDate = null;

		SimpleDateFormat dateFormat = new SimpleDateFormat(DATEFORMAT);
		String isoDatePattern = DATEPATTERN;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(isoDatePattern);
		Date date = null;
		try {
			date = dateFormat.parse(inputDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.error("Error in parsing date " + e);
		}
		formattedDate = simpleDateFormat.format(date);

		return formattedDate;
	}
}
