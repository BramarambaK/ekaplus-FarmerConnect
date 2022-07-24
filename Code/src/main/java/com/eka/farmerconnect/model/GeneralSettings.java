package com.eka.farmerconnect.model;

public class GeneralSettings {
	
	private final String marketplaceType;
	private final String offerType;
	private final boolean isContractIntegrationAllowed;
	private final boolean isBidQuantityLocked;
	private final boolean isBidCancellationAllowed;
	private final boolean isOfferRatingAllowed;
	private final boolean isOfferorInfoRestricted;
	private final boolean isPersonalInfoSharingRestricted;
	private final String rolesToPublish;
	

	public GeneralSettings(String marketplaceType, String offerType, boolean isContractIntegrationAllowed,
			boolean isBidQuantityLocked, boolean isBidCancellationAllowed, boolean isOfferRatingAllowed,
			boolean isOfferorInfoRestricted, boolean isPersonalInfoSharingRestricted, String rolesToPublish) {
		super();
		this.marketplaceType = marketplaceType;
		this.offerType = offerType;
		this.isContractIntegrationAllowed = isContractIntegrationAllowed;
		this.isBidQuantityLocked = isBidQuantityLocked;
		this.isBidCancellationAllowed = isBidCancellationAllowed;
		this.isOfferRatingAllowed = isOfferRatingAllowed;
		this.isOfferorInfoRestricted = isOfferorInfoRestricted;
		this.isPersonalInfoSharingRestricted = isPersonalInfoSharingRestricted;
		this.rolesToPublish = rolesToPublish;
	}

	public boolean isBidQuantityLocked() {
		return isBidQuantityLocked;
	}

	public String getRolesToPublish() {
		return rolesToPublish;
	}

	public String getMarketplaceType() {
		return marketplaceType;
	}

	public String getOfferType() {
		return offerType;
	}

	public boolean isContractIntegrationAllowed() {
		return isContractIntegrationAllowed;
	}

	public boolean isBidCancellationAllowed() {
		return isBidCancellationAllowed;
	}

	public boolean isOfferRatingAllowed() {
		return isOfferRatingAllowed;
	}

	public boolean isOfferorInfoRestricted() {
		return isOfferorInfoRestricted;
	}

	public boolean isPersonalInfoSharingRestricted() {
		return isPersonalInfoSharingRestricted;
	}
}
