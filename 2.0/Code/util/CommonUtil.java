package com.eka.farmerconnect.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import com.eka.farmerconnect.model.GeneralSettings;

public class CommonUtil {
	
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
}
