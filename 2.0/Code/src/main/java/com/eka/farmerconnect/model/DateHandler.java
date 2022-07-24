package com.eka.farmerconnect.model;

import java.io.IOException;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class DateHandler extends StdDeserializer<String> {

	public DateHandler(Class<?> vc) {
		super(vc);
		// TODO Auto-generated constructor stub
	}

	public DateHandler() {
		this(null);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		// TODO Auto-generated method stub
		Number numberValue = p.getNumberValue();
		if (numberValue != null) {
			SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
			return df.format(new java.util.Date(numberValue.longValue()));

		}
		return null;
	}

}
