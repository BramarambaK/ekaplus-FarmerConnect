package com.eka.farmerconnect.contract.validator;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.eka.farmerconnect.model.BidToContractEntity;
import com.eka.farmerconnect.util.CommonUtil;

@Component
public class ContractValidator implements Validator {

	@Autowired
	CommonUtil commonUtil;

	private static final String MESSAGE = " field can't be null";

	// Need to add all the mandatory fields below required for contract creation
	private static final List<String> MANDATROY_FIELD = Arrays.asList(
			"corporate", "cpContractRefNo","contractIssueDate","traderName","dealType","cpName","legalEntity"
			,"incoTerms","paymentTerms","itemQuantityUnitId","product");

	@Override
	public boolean supports(Class<?> clazz) {
		return BidToContractEntity.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		// Default implementation is ignored.

		for (String field : MANDATROY_FIELD) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, field, field + MESSAGE);
		}


	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void contractIdNullValidation(Map<String, Object> map, List<String> errors) {

		//		List<String> errorKeys = map.entrySet().stream().filter(predicate -> null == predicate.getValue())
		//				.map(Entry::getKey).collect(Collectors.toList());

		List<Map> list;

		for (Entry<String, Object> entry : map.entrySet()) {

			if (null != entry.getValue() && entry.getValue() instanceof List) {
				list = (List<Map>) entry.getValue();
				contractIdNullValidation((Map<String, Object>) list.get(0), errors);
			} else if (null == entry.getValue()) {
				errors.add(entry.getKey() + " Value not matching with MDM");
			}
		}
	}

}
