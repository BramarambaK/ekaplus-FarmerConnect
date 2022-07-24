package com.eka.farmerconnect.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SettingDetails {

	
	//TODO:uncomment this annotations when bidsAndOffer App is ready.
	
	//@JsonProperty("marketplace_type")
	private String marketplaceType;
	
	//@JsonProperty("offer_type")
	private String offerType;
	
	private String rolesToPublish;
	
	//@JsonProperty("allow_locking_of_bid_quantity")
	private boolean bidQuantityLocked;
	
	//@JsonProperty("cancellation_of_an_accepted_bid")
	private boolean bidCancellationAllowed;
	
	//@JsonProperty("allow_offer_rating")
	private boolean offerRatingAllowed;
	
	//@JsonProperty("restrict_offeror_information")
	private boolean offerorInfoRestricted;
	
	//@JsonProperty("restrict_sharing_of_personal_information")
	private boolean personalInfoSharingRestricted;
	
	//@JsonProperty("allow_contract_integration")
	private boolean contractIntegrationAllowed;


	// Getter Methods 

	public String getMarketplaceType() {
		return marketplaceType;
	}

	public String getOfferType() {
		return offerType;
	}

	public String getRolesToPublish() {
		return rolesToPublish;
	}

	public boolean getBidQuantityLocked() {
		return bidQuantityLocked;
	}

	public boolean getBidCancellationAllowed() {
		return bidCancellationAllowed;
	}

	public boolean getOfferRatingAllowed() {
		return offerRatingAllowed;
	}

	public boolean getOfferorInfoRestricted() {
		return offerorInfoRestricted;
	}

	public boolean getPersonalInfoSharingRestricted() {
		return personalInfoSharingRestricted;
	}

	public boolean getContractIntegrationAllowed() {
		return contractIntegrationAllowed;
	}

	// Setter Methods 

	public void setMarketplaceType(String marketplaceType) {
		this.marketplaceType = marketplaceType;
	}

	public void setOfferType(String offerType) {
		this.offerType = offerType;
	}

	public void setRolesToPublish(String rolesToPublish) {
		this.rolesToPublish = rolesToPublish;
	}

	public void setBidQuantityLocked(boolean bidQuantityLocked) {
		this.bidQuantityLocked = bidQuantityLocked;
	}

	public void setBidCancellationAllowed(boolean bidCancellationAllowed) {
		this.bidCancellationAllowed = bidCancellationAllowed;
	}

	public void setOfferRatingAllowed(boolean offerRatingAllowed) {
		this.offerRatingAllowed = offerRatingAllowed;
	}

	public void setOfferorInfoRestricted(boolean offerorInfoRestricted) {
		this.offerorInfoRestricted = offerorInfoRestricted;
	}

	public void setPersonalInfoSharingRestricted(boolean personalInfoSharingRestricted) {
		this.personalInfoSharingRestricted = personalInfoSharingRestricted;
	}

	public void setContractIntegrationAllowed(boolean contractIntegrationAllowed) {
		this.contractIntegrationAllowed = contractIntegrationAllowed;
	}

	@Override
	public String toString() {
		return "SettingDetails [marketplaceType=" + marketplaceType + ", offerType=" + offerType + ", rolesToPublish="
				+ rolesToPublish + ", bidQuantityLocked=" + bidQuantityLocked + ", bidCancellationAllowed="
				+ bidCancellationAllowed + ", offerRatingAllowed=" + offerRatingAllowed + ", offerorInfoRestricted="
				+ offerorInfoRestricted + ", personalInfoSharingRestricted=" + personalInfoSharingRestricted
				+ ", contractIntegrationAllowed=" + contractIntegrationAllowed + "]";
	}



}
