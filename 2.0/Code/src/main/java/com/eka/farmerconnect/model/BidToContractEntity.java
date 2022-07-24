package com.eka.farmerconnect.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class BidToContractEntity {

	private String corporate;

	private String cpContractRefNo;
	
	@JsonDeserialize(using = DateHandler.class)
	private String contractIssueDate;

	private String traderName;

	private String dealType;

	private String cpName;
	
	private String purchaseContractCPName;
	
	private String salesContractCPName;

	private String legalEntity;	

	private String incoTerms;

	private String paymentTerms;

	private String itemQuantityUnitId; //contractQuantityUnit

	private String product;

	private String origin;

	private String cropYear;

	private String quality;

	private String profitCenter;

	private String strategy;

	private String itemQuantity;

	private String packingType;

	private String packingSize;

	private String tolerance;

	private String toleranceType;

	private String toleranceLevel;

	private String plannedShipmentQuantity; // need to check

	private String payInCurrency;

	private String priceType;

	private String priceDf;  // contractPrice

	private String priceUnitId;

	private String loadingType;

	private String loadingCountry;

	private String loadingLocation;

	private String destinationType;

	private String destinationCountry;

	private String destinationLocation;

	private String periodType;

	@JsonDeserialize(using = DateHandler.class)
	private String deliveryFromDate;

	@JsonDeserialize(using = DateHandler.class)
	private String deliveryToDate;

	@JsonDeserialize(using = DateHandler.class)
	private String paymentDueDate;

	private String invoiceDocumentPriceUnit;

	private String taxScheduleApplicableCountry;

	private String taxSchedule;

	private String termsAndConditions; // Applicable law contract

	private String arbitration;

	private String weightFinalAt;

	private String qualityFinalAt;

	private String approvalStatus;

	private String offerType;


	@Override
	public String toString() {
		return "BidToContractEntity [corporate=" + corporate + ", cpContractRefNo=" + cpContractRefNo
				+ ", contractIssueDate=" + contractIssueDate + ", traderName=" + traderName + ", dealType=" + dealType
				+ ", cpName=" + cpName + ", purchaseContractCPName=" + purchaseContractCPName + ", salesContractCPName="
				+ salesContractCPName + ", legalEntity=" + legalEntity + ", incoTerms=" + incoTerms + ", paymentTerms="
				+ paymentTerms + ", itemQuantityUnitId=" + itemQuantityUnitId + ", product=" + product + ", origin="
				+ origin + ", cropYear=" + cropYear + ", quality=" + quality + ", profitCenter=" + profitCenter
				+ ", strategy=" + strategy + ", itemQuantity=" + itemQuantity + ", packingType=" + packingType
				+ ", packingSize=" + packingSize + ", tolerance=" + tolerance + ", toleranceType=" + toleranceType
				+ ", toleranceLevel=" + toleranceLevel + ", plannedShipmentQuantity=" + plannedShipmentQuantity
				+ ", payInCurrency=" + payInCurrency + ", priceType=" + priceType + ", priceDf=" + priceDf
				+ ", priceUnitId=" + priceUnitId + ", loadingType=" + loadingType + ", loadingCountry=" + loadingCountry
				+ ", loadingLocation=" + loadingLocation + ", destinationType=" + destinationType
				+ ", destinationCountry=" + destinationCountry + ", destinationLocation=" + destinationLocation
				+ ", periodType=" + periodType + ", deliveryFromDate=" + deliveryFromDate + ", deliveryToDate="
				+ deliveryToDate + ", paymentDueDate=" + paymentDueDate + ", invoiceDocumentPriceUnit="
				+ invoiceDocumentPriceUnit + ", taxScheduleApplicableCountry=" + taxScheduleApplicableCountry
				+ ", taxSchedule=" + taxSchedule + ", termsAndConditions=" + termsAndConditions + ", arbitration="
				+ arbitration + ", weightFinalAt=" + weightFinalAt + ", qualityFinalAt=" + qualityFinalAt
				+ ", approvalStatus=" + approvalStatus + ", offerType=" + offerType + "]";
	}

	public String getCorporate() {
		return corporate;
	}

	public void setCorporate(String corporate) {
		this.corporate = corporate;
	}

	public String getCpContractRefNo() {
		return cpContractRefNo;
	}

	public void setCpContractRefNo(String cpContractRefNo) {
		this.cpContractRefNo = cpContractRefNo;
	}

	public String getContractIssueDate() {
		return contractIssueDate;
	}

	public void setContractIssueDate(String contractIssueDate) {
		this.contractIssueDate = contractIssueDate;
	}

	public String getTraderName() {
		return traderName;
	}

	public void setTraderName(String traderName) {
		this.traderName = traderName;
	}

	public String getDealType() {
		return dealType;
	}

	public void setDealType(String dealType) {
		this.dealType = dealType;
	}

	public String getCpName() {
		return cpName;
	}

	public void setCpName(String cpName) {
		this.cpName = cpName;
	}

	public String getLegalEntity() {
		return legalEntity;
	}

	public void setLegalEntity(String legalEntity) {
		this.legalEntity = legalEntity;
	}

	public String getIncoTerms() {
		return incoTerms;
	}

	public void setIncoTerms(String incoTerms) {
		this.incoTerms = incoTerms;
	}

	public String getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(String paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

	public String getItemQuantityUnitId() {
		return itemQuantityUnitId;
	}

	public void setItemQuantityUnitId(String itemQuantityUnitId) {
		this.itemQuantityUnitId = itemQuantityUnitId;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getCropYear() {
		return cropYear;
	}

	public void setCropYear(String cropYear) {
		this.cropYear = cropYear;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getProfitCenter() {
		return profitCenter;
	}

	public void setProfitCenter(String profitCenter) {
		this.profitCenter = profitCenter;
	}

	public String getStrategy() {
		return strategy;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	public String getItemQuantity() {
		return itemQuantity;
	}

	public void setItemQuantity(String itemQuantity) {
		this.itemQuantity = itemQuantity;
	}

	public String getPackingType() {
		return packingType;
	}

	public void setPackingType(String packingType) {
		this.packingType = packingType;
	}

	public String getPackingSize() {
		return packingSize;
	}

	public void setPackingSize(String packingSize) {
		this.packingSize = packingSize;
	}

	public String getTolerance() {
		return tolerance;
	}

	public void setTolerance(String tolerance) {
		this.tolerance = tolerance;
	}

	public String getToleranceType() {
		return toleranceType;
	}

	public void setToleranceType(String toleranceType) {
		this.toleranceType = toleranceType;
	}

	public String getToleranceLevel() {
		return toleranceLevel;
	}

	public void setToleranceLevel(String toleranceLevel) {
		this.toleranceLevel = toleranceLevel;
	}

	public String getPlannedShipmentQuantity() {
		return plannedShipmentQuantity;
	}

	public void setPlannedShipmentQuantity(String plannedShipmentQuantity) {
		this.plannedShipmentQuantity = plannedShipmentQuantity;
	}

	public String getPayInCurrency() {
		return payInCurrency;
	}

	public void setPayInCurrency(String payInCurrency) {
		this.payInCurrency = payInCurrency;
	}

	public String getPriceType() {
		return priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}

	public String getPriceDf() {
		return priceDf;
	}

	public void setPriceDf(String priceDf) {
		this.priceDf = priceDf;
	}

	public String getPriceUnitId() {
		return priceUnitId;
	}

	public void setPriceUnitId(String priceUnitId) {
		this.priceUnitId = priceUnitId;
	}

	public String getLoadingType() {
		return loadingType;
	}

	public void setLoadingType(String loadingType) {
		this.loadingType = loadingType;
	}

	public String getLoadingCountry() {
		return loadingCountry;
	}

	public void setLoadingCountry(String loadingCountry) {
		this.loadingCountry = loadingCountry;
	}

	public String getLoadingLocation() {
		return loadingLocation;
	}

	public void setLoadingLocation(String loadingLocation) {
		this.loadingLocation = loadingLocation;
	}

	public String getDestinationType() {
		return destinationType;
	}

	public void setDestinationType(String destinationType) {
		this.destinationType = destinationType;
	}

	public String getDestinationCountry() {
		return destinationCountry;
	}

	public void setDestinationCountry(String destinationCountry) {
		this.destinationCountry = destinationCountry;
	}

	public String getDestinationLocation() {
		return destinationLocation;
	}

	public void setDestinationLocation(String destinationLocation) {
		this.destinationLocation = destinationLocation;
	}

	public String getPeriodType() {
		return periodType;
	}

	public void setPeriodType(String periodType) {
		this.periodType = periodType;
	}

	public String getDeliveryFromDate() {
		return deliveryFromDate;
	}

	public void setDeliveryFromDate(String deliveryFromDate) {
		this.deliveryFromDate = deliveryFromDate;
	}

	public String getDeliveryToDate() {
		return deliveryToDate;
	}

	public void setDeliveryToDate(String deliveryToDate) {
		this.deliveryToDate = deliveryToDate;
	}

	public String getPaymentDueDate() {
		return paymentDueDate;
	}

	public void setPaymentDueDate(String paymentDueDate) {
		this.paymentDueDate = paymentDueDate;
	}

	public String getInvoiceDocumentPriceUnit() {
		return invoiceDocumentPriceUnit;
	}

	public void setInvoiceDocumentPriceUnit(String invoiceDocumentPriceUnit) {
		this.invoiceDocumentPriceUnit = invoiceDocumentPriceUnit;
	}

	public String getTaxScheduleApplicableCountry() {
		return taxScheduleApplicableCountry;
	}

	public void setTaxScheduleApplicableCountry(String taxScheduleApplicableCountry) {
		this.taxScheduleApplicableCountry = taxScheduleApplicableCountry;
	}

	public String getTaxSchedule() {
		return taxSchedule;
	}

	public void setTaxSchedule(String taxSchedule) {
		this.taxSchedule = taxSchedule;
	}

	public String getTermsAndConditions() {
		return termsAndConditions;
	}

	public void setTermsAndConditions(String termsAndConditions) {
		this.termsAndConditions = termsAndConditions;
	}

	public String getArbitration() {
		return arbitration;
	}

	public void setArbitration(String arbitration) {
		this.arbitration = arbitration;
	}

	public String getWeightFinalAt() {
		return weightFinalAt;
	}

	public void setWeightFinalAt(String weightFinalAt) {
		this.weightFinalAt = weightFinalAt;
	}

	public String getQualityFinalAt() {
		return qualityFinalAt;
	}

	public void setQualityFinalAt(String qualityFinalAt) {
		this.qualityFinalAt = qualityFinalAt;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public String getOfferType() {
		return offerType;
	}

	public void setOfferType(String offerType) {
		this.offerType = offerType;
	}
	
	public String getPurchaseContractCPName() {
		return purchaseContractCPName;
	}

	public void setPurchaseContractCPName(String purchaseContractCPName) {
		this.purchaseContractCPName = purchaseContractCPName;
	}

	public String getSalesContractCPName() {
		return salesContractCPName;
	}

	public void setSalesContractCPName(String salesContractCPName) {
		this.salesContractCPName = salesContractCPName;
	}



	@JsonAutoDetect(fieldVisibility = Visibility.ANY)
	public class MDMServiceKeyDetails {

		private String traderName = "userListByRole";
		private String contractType = "contractType";
		private String dealType = "dealType";
		private String cpName = "businessPartnerCombo";
		private String purchaseContractCPName = "businessPartnerCombo";
		private String salesContractCPName = "businessPartnerCombo";
		private String product = "productComboDropDrown";
		private String quality = "qualityComboDropDrown";
		private String itemQuantityUnitId = "physicalproductquantitylist";
		private String priceType = "productpricetypelist";
		private String incoTerms = "incoterm";
		private String paymentTerms = "paytermlist_phy";
		private String arbitration = "ContractRulesAndArbitrationList";
		private String strategy = "corporateStrategy";
		private String payInCurrency = "productCurrencyList";
		private String destinationCountry = "countriesComboDataFromDB";
		private String loadingCountry = "countriesComboDataFromDB";
		private String taxScheduleApplicableCountry = "taxScheduleCountrystate";
		private String termsAndConditions = "applicableLaw";
		private String profitCenter = "userProfitCenterList";
		private String loadingType = "incoTermLocationGroupType";
		private String destinationType = "incoTermLocationGroupType";
		private String loadingLocation = "cityComboDataFromDB";
		private String destinationLocation = "cityComboDataFromDB";
		private String taxSchedule = "listOfTaxSchedules";
		private String priceUnitId = "productPriceUnit";
		// Added defaulted fields below
		private String origin = "productorigin"; 
		private String cropYear ="cropyearComboDropDrown"; 
		private String packingType ="phyPackingtypesizeByCItemOrProductList"; 
		//private String packingSize ="phyPackingtypesizeByCItemOrProductList"; 
		private String legalEntity ="legalEntityList"; 
		private String weightFinalAt = "PortOperations"; 
		private String qualityFinalAt = "PortOperations";

		public String getTraderName() {
			return traderName;
		}
		public String getContractType() {
			return contractType;
		}
		public String getDealType() {
			return dealType;
		}
		public String getCpName() {
			return cpName;
		}
		public String getProduct() {
			return product;
		}
		public String getQuality() {
			return quality;
		}
		public String getItemQuantityUnitId() {
			return itemQuantityUnitId;
		}
		public String getPriceType() {
			return priceType;
		}
		public String getIncoTerms() {
			return incoTerms;
		}
		public String getPaymentTerms() {
			return paymentTerms;
		}
		public String getArbitration() {
			return arbitration;
		}
		public String getStrategy() {
			return strategy;
		}
		public String getPayInCurrency() {
			return payInCurrency;
		}
		public String getDestinationCountry() {
			return destinationCountry;
		}
		public String getLoadingCountry() {
			return loadingCountry;
		}
		public String getTaxScheduleApplicableCountry() {
			return taxScheduleApplicableCountry;
		}
		public String getTermsAndConditions() {
			return termsAndConditions;
		}
		public String getProfitCenter() {
			return profitCenter;
		}
		public String getLoadingType() {
			return loadingType;
		}
		public String getDestinationType() {
			return destinationType;
		}
		public String getLoadingLocation() {
			return loadingLocation;
		}
		public String getDestinationLocation() {
			return destinationLocation;
		}
		public String getTaxSchedule() {
			return taxSchedule;
		}
		public String getPriceUnitId() {
			return priceUnitId;
		}
		public String getOrigin() {
			return origin;
		}
		public String getCropYear() {
			return cropYear;
		}
		public String getPackingType() {
			return packingType;
		}

		/*
		 * public String getPackingSize() { return packingSize; }
		 */
		public String getLegalEntity() {
			return legalEntity;
		}
		public String getWeightFinalAt() {
			return weightFinalAt;
		}
		public String getQualityFinalAt() {
			return qualityFinalAt;
		}
		public String getPurchaseContractCPName() {
			return purchaseContractCPName;
		}
		
		public String getSalesContractCPName() {
			return salesContractCPName;
		}
		
		
		
	}

	@JsonAutoDetect(fieldVisibility = Visibility.ANY)
	public class TRMPayloadKeyDetails {

		private String traderName = "traderUserId";
		private String contractType = "contractType";
		private String dealType = "dealType";
		private String cpName = "cpProfileId";
		private String purchaseContractCPName = "purchasecpProfileId";
		private String salesContractCPName = "salescpProfileId";
		private String product = "productId";
		private String quality = "quality";
		private String itemQuantityUnitId = "itemQtyUnitId";
		private String priceType = "priceTypeId";
		private String incoTerms = "incotermId";
		private String paymentTerms = "paymentTermId";
		private String arbitration = "arbitrationId";
		private String strategy = "strategyAccId";
		private String payInCurrency = "payInCurId";
		private String destinationCountry = "destinationCountryId";
		private String loadingCountry = "originationCountryId";
		private String taxScheduleApplicableCountry = "taxScheduleCountryId";
		private String termsAndConditions = "applicableLawId";
		private String profitCenter = "profitCenterId";
		private String loadingType = "loadingLocationGroupTypeId";
		private String destinationType = "destinationLocationGroupTypeId";
		private String loadingLocation = "originationCityId";
		private String destinationLocation = "destinationCityId";
		private String taxSchedule = "taxScheduleId";
		private String contractIssueDate = "issueDate";
		private String itemNo = "itemNo";
		private String itemQuantity = "itemQty";
		private String tolerance = "tolerance";
		
		//Commenting below code now as TRM Version is not upgraded to 9.1.4
		
		private String toleranceMin = "toleranceMin";
		private String toleranceMax = "toleranceMax";
		
		private String toleranceType = "toleranceType";
		private String toleranceLevel = "toleranceLevel";
		private String shipmentMode = "shipmentMode";
		private String deliveryFromDate = "deliveryFromDate";
		private String deliveryToDate = "deliveryToDate";
		private String paymentDueDate = "paymentDueDate";
		private String qualityFinalAt = "qualityFinalAt";
		private String weightFinalAt = "weightFinalAt";
		private String priceDf = "priceDf";
		private String fxBasisToPayin = "fxBasisToPayin";
		private String priceUnitId = "priceUnitId";

		private String cpContractRefNo = "cpRefNo"; 
		private String periodType = "periodType";

		private String legalEntity = "legalEntityId"; 
		private String origin ="originId"; 
		private String cropYear = "cropYearId"; 
		private String packingType = "packingTypeId"; 
		private String packingSize = "packingSizeId";

		private String productSpecs = "productSpecs";
		
		//CFC-977
		private String totalQtyUnitId = "totalQtyUnitId";

		//Fields to be added later when i get the trmkey
		/*
		private String corporate;
		private double plannedShipmentQuantity;
		private String invoiceDocumentPriceUnit;

		 */

		public String getTotalQtyUnitId() {
			return totalQtyUnitId;
		}
		public String getTraderName() {
			return traderName;
		}
		public String getContractType() {
			return contractType;
		}
		public String getDealType() {
			return dealType;
		}
		public String getCpName() {
			return cpName;
		}
		public String getProduct() {
			return product;
		}
		public String getQuality() {
			return quality;
		}
		public String getItemQuantityUnitId() {
			return itemQuantityUnitId;
		}
		public String getPriceType() {
			return priceType;
		}
		public String getIncoTerms() {
			return incoTerms;
		}
		public String getPaymentTerms() {
			return paymentTerms;
		}
		public String getArbitration() {
			return arbitration;
		}
		public String getStrategy() {
			return strategy;
		}
		public String getPayInCurrency() {
			return payInCurrency;
		}
		public String getDestinationCountry() {
			return destinationCountry;
		}
		public String getLoadingCountry() {
			return loadingCountry;
		}
		public String getTaxScheduleApplicableCountry() {
			return taxScheduleApplicableCountry;
		}
		public String getTermsAndConditions() {
			return termsAndConditions;
		}
		public String getProfitCenter() {
			return profitCenter;
		}
		public String getLoadingType() {
			return loadingType;
		}
		public String getDestinationType() {
			return destinationType;
		}
		public String getLoadingLocation() {
			return loadingLocation;
		}
		public String getDestinationLocation() {
			return destinationLocation;
		}
		public String getTaxSchedule() {
			return taxSchedule;
		}
		public String getContractIssueDate() {
			return contractIssueDate;
		}
		public String getItemNo() {
			return itemNo;
		}
		public String getItemQuantity() {
			return itemQuantity;
		}
	
		public String getTolerance() {
			return tolerance;
		}
	
		public String getToleranceMin() {
			return toleranceMin;
		}
		public String getToleranceMax() {
			return toleranceMax;
		}
	
	
		public String getToleranceType() {
			return toleranceType;
		}
		public String getToleranceLevel() {
			return toleranceLevel;
		}
		public String getShipmentMode() {
			return shipmentMode;
		}
		public String getDeliveryFromDate() {
			return deliveryFromDate;
		}
		public String getDeliveryToDate() {
			return deliveryToDate;
		}
		public String getPaymentDueDate() {
			return paymentDueDate;
		}
		public String getQualityFinalAt() {
			return qualityFinalAt;
		}
		public String getWeightFinalAt() {
			return weightFinalAt;
		}
		public String getPriceDf() {
			return priceDf;
		}
		public String getFxBasisToPayin() {
			return fxBasisToPayin;
		}
		public String getPriceUnitId() {
			return priceUnitId;
		}
		public String getCpContractRefNo() {
			return cpContractRefNo;
		}
		public String getPeriodType() {
			return periodType;
		}
		public String getLegalEntity() {
			return legalEntity;
		}
		public String getOrigin() {
			return origin;
		}
		public String getCropYear() {
			return cropYear;
		}
		public String getPackingType() {
			return packingType;
		}
		public String getPackingSize() {
			return packingSize;
		}
		public String getProductSpecs() {
			return productSpecs;
		}
		public String getPurchaseContractCPName() {
			return purchaseContractCPName;
		}
		public String getSalesContractCPName() {
			return salesContractCPName;
		}	
		
	}

}
