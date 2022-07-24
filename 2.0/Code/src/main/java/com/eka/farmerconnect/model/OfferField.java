package com.eka.farmerconnect.model;

public enum OfferField {
	
	Product("product"),
	Quality("quality"),
	PriceUnit("priceUnit"),
	Location("location"),
	QuantityUnit("quantityUnit"),
	IncoTerm("incoTerm"),
	CropYear("cropYear"),
	PaymentTerms("paymentTerms"),
	PackingType("packingType"),
	PackingSize("packingSize");
	
	private String id;
	
	OfferField(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
	
	public static OfferField findById(String id) {
		
		for (OfferField field : OfferField.values()) {
			if(field.getId().equals(id))
				return field;
		}
		return null;
	}
}
