/*
 * $Id$ Created on Mar 29, 2006
 * 
 * Copyright (C) 2006 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to license terms.
 */
package is.idega.block.family.data;

import is.idega.block.family.business.FamilyConstants;
import is.idega.block.family.business.FamilyLogic;
import is.idega.block.family.business.NoCustodianFound;
import is.idega.block.family.business.NoSiblingFound;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.data.IDOLookupException;
import com.idega.user.data.Gender;
import com.idega.user.data.GenderHome;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.user.data.UserBMPBean;

public class ChildBMPBean extends UserBMPBean implements User, Child {

	public static final String METADATA_GROWTH_DEVIATION = "growth_deviation";
	public static final String METADATA_GROWTH_DEVIATION_DETAILS = "growth_deviation_details";

	public static final String METADATA_ALLERGIES = "allergies";
	public static final String METADATA_ALLERGIES_DETAILS = "allergies_details";

	public static final String METADATA_MULTI_LANGUAGE_HOME = "multi_language_home";
	public static final String METADATA_LANGUAGE = "language";

	public static final String METADATA_OTHER_INFORMATION = "other_information";

	public static final String METADATA_OTHER_CUSTODIAN = "other_custodian";
	public static final String METADATA_RELATIVE_0 = "relative_0";
	public static final String METADATA_RELATIVE_1 = "relative_1";
	public static final String METADATA_RELATIVE_2 = "relative_2";
	public static final String METADATA_RELATION = "relation_";

	public static final String METADATA_FORBIDDEN_RELATIVE = "forbidden_relative";

	public Collection getSiblings() throws NoSiblingFound {
		try {
			return getFamilyLogic().getSiblingsFor(this);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	public Collection getCustodians() throws NoCustodianFound {
		try {
			return getFamilyLogic().getCustodiansFor(this);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	public Custodian getMother() {
		Custodian custodian = getCustodian(FamilyConstants.RELATION_MOTHER);
		if (custodian == null) {
			try {
				GenderHome home = (GenderHome) getIDOHome(Gender.class);
				return getCustodianByGender(home.getFemaleGender());
			}
			catch (FinderException fe) {
				log(fe);
			}
			catch (IDOLookupException ile) {
				throw new IBORuntimeException(ile);
			}
		}

		return custodian;
	}

	public Custodian getFather() {
		Custodian custodian = getCustodian(FamilyConstants.RELATION_FATHER);
		if (custodian == null) {
			try {
				GenderHome home = (GenderHome) getIDOHome(Gender.class);
				return getCustodianByGender(home.getMaleGender());
			}
			catch (FinderException fe) {
				log(fe);
			}
			catch (IDOLookupException ile) {
				throw new IBORuntimeException(ile);
			}
		}

		return custodian;
	}

	public Custodian getCustodian(String relation) {
		// Collection metadataValues = getMetaDataAttributes().values();
		Map metadataAttributes = getMetaDataAttributes();
		Collection keys = metadataAttributes.keySet();
		// if (metadataValues != null) {
		if (keys != null) {
			// Iterator iter = metadataValues.iterator();
			Iterator iter = keys.iterator();
			while (iter.hasNext()) {
				// Object value = iter.next();
				String key = (String) iter.next();
				if (key.startsWith(METADATA_RELATION)) {
					String value = (String) metadataAttributes.get(key);
					if (value.equals(relation)) {
						try {
							String custPk = key.substring(METADATA_RELATION.length(), key.length());
							Integer custId = new Integer(custPk);

							return getCustodianByPrimaryKey(custId);
						}
						catch (FinderException fe) {
							log(fe);
						}
					}
				}
			}
		}

		return null;
	}

	public void setRelation(Custodian custodian, String relation) {
		setMetaData(METADATA_RELATION + custodian.getPrimaryKey().toString(), relation, "java.lang.String");
	}

	public String getRelation(Custodian custodian) {
		return getMetaData(METADATA_RELATION + custodian.getPrimaryKey().toString());
	}

	public Custodian getExtraCustodian() {
		String custodianPK = this.getMetaData(METADATA_OTHER_CUSTODIAN);
		if (custodianPK != null) {
			try {
				return getCustodianByPrimaryKey(new Integer(custodianPK));
			}
			catch (FinderException fe) {
				fe.printStackTrace();
			}
		}
		return null;
	}

	public void setExtraCustodian(Custodian custodian) {
		setExtraCustodian(custodian, null);
	}

	public void setExtraCustodian(Custodian custodian, String relation) {
		setMetaData(METADATA_OTHER_CUSTODIAN, custodian.getPrimaryKey().toString(), "com.idega.user.data.User");
		if (relation != null && relation.length() > 0) {
			setRelation(custodian, relation);
		}
	}

	public List getRelatives() {
		return getRelatives("");
	}

	public List getRelatives(String prefix) {
		List relatives = new ArrayList();

		for (int a = 1; a <= 2; a++) {
			String name = getMetaData(prefix + (a == 1 ? METADATA_RELATIVE_1 : METADATA_RELATIVE_2) + "_name");
			String relation = getMetaData(prefix + (a == 1 ? METADATA_RELATIVE_1 : METADATA_RELATIVE_2) + "_relation");
			String homePhone = getMetaData(prefix + (a == 1 ? METADATA_RELATIVE_1 : METADATA_RELATIVE_2) + "_homePhone");
			String workPhone = getMetaData(prefix + (a == 1 ? METADATA_RELATIVE_1 : METADATA_RELATIVE_2) + "_workPhone");
			String mobilePhone = getMetaData(prefix + (a == 1 ? METADATA_RELATIVE_1 : METADATA_RELATIVE_2) + "_mobilePhone");
			String email = getMetaData(prefix + (a == 1 ? METADATA_RELATIVE_1 : METADATA_RELATIVE_2) + "_email");

			if (name != null) {
				Relative relative = new Relative();
				relative.setName(name);
				relative.setRelation(relation);
				relative.setHomePhone(homePhone);
				relative.setWorkPhone(workPhone);
				relative.setMobilePhone(mobilePhone);
				relative.setEmail(email);

				relatives.add(relative);
			}
		}

		return relatives;
	}

	public void storeMainRelative(String prefix, String name, String relation, String homePhone, String workPhone, String mobilePhone, String email) {
		setMetaData(prefix + METADATA_RELATIVE_0 + "_name", name, "java.lang.String");
		setMetaData(prefix + METADATA_RELATIVE_0 + "_relation", relation, "java.lang.String");
		setMetaData(prefix + METADATA_RELATIVE_0 + "_homePhone", homePhone, "java.lang.String");
		setMetaData(prefix + METADATA_RELATIVE_0 + "_workPhone", workPhone, "java.lang.String");
		setMetaData(prefix + METADATA_RELATIVE_0 + "_mobilePhone", mobilePhone, "java.lang.String");
		setMetaData(prefix + METADATA_RELATIVE_0 + "_email", email, "java.lang.String");
		store();
	}

	public Relative getMainRelative(String prefix) {
		String name = getMetaData(prefix + METADATA_RELATIVE_0 + "_name");
		String relation = getMetaData(prefix + METADATA_RELATIVE_0 + "_relation");
		String homePhone = getMetaData(prefix + METADATA_RELATIVE_0 + "_homePhone");
		String workPhone = getMetaData(prefix + METADATA_RELATIVE_0 + "_workPhone");
		String mobilePhone = getMetaData(prefix + METADATA_RELATIVE_0 + "_mobilePhone");
		String email = getMetaData(prefix + METADATA_RELATIVE_0 + "_email");

		if (name != null) {
			Relative relative = new Relative();
			relative.setName(name);
			relative.setRelation(relation);
			relative.setHomePhone(homePhone);
			relative.setWorkPhone(workPhone);
			relative.setMobilePhone(mobilePhone);
			relative.setEmail(email);

			return relative;
		}

		return null;
	}

	public void storeRelative(String name, String relation, int number, String homePhone, String workPhone, String mobilePhone, String email) {
		storeRelative("", name, relation, number, homePhone, workPhone, mobilePhone, email);
	}

	public void storeRelative(String prefix, String name, String relation, int number, String homePhone, String workPhone, String mobilePhone, String email) {
		if (number > 2 || number < 1) {
			return;
		}

		setMetaData(prefix + (number == 1 ? METADATA_RELATIVE_1 : METADATA_RELATIVE_2) + "_name", name, "java.lang.String");
		setMetaData(prefix + (number == 1 ? METADATA_RELATIVE_1 : METADATA_RELATIVE_2) + "_relation", relation, "java.lang.String");
		setMetaData(prefix + (number == 1 ? METADATA_RELATIVE_1 : METADATA_RELATIVE_2) + "_homePhone", homePhone, "java.lang.String");
		setMetaData(prefix + (number == 1 ? METADATA_RELATIVE_1 : METADATA_RELATIVE_2) + "_workPhone", workPhone, "java.lang.String");
		setMetaData(prefix + (number == 1 ? METADATA_RELATIVE_1 : METADATA_RELATIVE_2) + "_mobilePhone", mobilePhone, "java.lang.String");
		setMetaData(prefix + (number == 1 ? METADATA_RELATIVE_1 : METADATA_RELATIVE_2) + "_email", email, "java.lang.String");
		store();
	}

	public void storeForbiddenRelative(String name, String personalID, String details) {
		setMetaData(METADATA_FORBIDDEN_RELATIVE + "_name", name, "java.lang.String");
		setMetaData(METADATA_FORBIDDEN_RELATIVE + "_personalID", personalID, "java.lang.String");
		setMetaData(METADATA_FORBIDDEN_RELATIVE + "_details", details, "java.lang.String");
		store();
	}

	public Relative getForbiddenRelative() {
		String name = getMetaData(METADATA_FORBIDDEN_RELATIVE + "_name");
		String personalID = getMetaData(METADATA_FORBIDDEN_RELATIVE + "_personalID");
		String details = getMetaData(METADATA_FORBIDDEN_RELATIVE + "_details");

		if (name != null && name.length() > 0) {
			Relative relative = new Relative();
			relative.setName(name);
			relative.setPersonalID(personalID);
			relative.setDetails(details);

			return relative;
		}

		return null;
	}

	public Boolean hasGrowthDeviation() {
		return hasGrowthDeviation("");
	}

	public Boolean hasGrowthDeviation(String prefix) {
		String meta = getMetaData(prefix + METADATA_GROWTH_DEVIATION);
		if (meta != null && meta.length() > 0) {
			return new Boolean(meta);
		}
		return null;
	}

	public void setHasGrowthDeviation(Boolean hasGrowthDeviation) {
		setHasGrowthDeviation("", hasGrowthDeviation);
	}

	public void setHasGrowthDeviation(String prefix, Boolean hasGrowthDeviation) {
		if (hasGrowthDeviation != null) {
			setMetaData(prefix + METADATA_GROWTH_DEVIATION, hasGrowthDeviation.toString());
		}
		else {
			removeMetaData(prefix + METADATA_GROWTH_DEVIATION);
		}
	}

	public String getGrowthDeviationDetails() {
		return getGrowthDeviationDetails("");
	}

	public String getGrowthDeviationDetails(String prefix) {
		return getMetaData(prefix + METADATA_GROWTH_DEVIATION_DETAILS);
	}

	public void setGrowthDeviationDetails(String details) {
		setGrowthDeviationDetails("", details);
	}

	public void setGrowthDeviationDetails(String prefix, String details) {
		if (details != null && details.length() > 0) {
			setMetaData(prefix + METADATA_GROWTH_DEVIATION_DETAILS, details);
		}
		else {
			removeMetaData(prefix + METADATA_GROWTH_DEVIATION_DETAILS);
		}
	}

	public Boolean hasAllergies() {
		return hasAllergies("");
	}

	public Boolean hasAllergies(String prefix) {
		String meta = getMetaData(prefix + METADATA_ALLERGIES);
		if (meta != null && meta.length() > 0) {
			return new Boolean(meta);
		}
		return null;
	}

	public void setHasAllergies(Boolean hasAllergies) {
		setHasAllergies("", hasAllergies);
	}

	public void setHasAllergies(String prefix, Boolean hasAllergies) {
		if (hasAllergies != null) {
			setMetaData(prefix + METADATA_ALLERGIES, hasAllergies.toString());
		}
		else {
			removeMetaData(prefix + METADATA_ALLERGIES);
		}
	}

	public String getAllergiesDetails() {
		return getAllergiesDetails("");
	}

	public String getAllergiesDetails(String prefix) {
		return getMetaData(prefix + METADATA_ALLERGIES_DETAILS);
	}

	public void setAllergiesDetails(String details) {
		setAllergiesDetails("", details);
	}

	public void setAllergiesDetails(String prefix, String details) {
		if (details != null && details.length() > 0) {
			setMetaData(prefix + METADATA_ALLERGIES_DETAILS, details);
		}
		else {
			removeMetaData(prefix + METADATA_ALLERGIES_DETAILS);
		}
	}

	public boolean hasMultiLanguageHome() {
		String meta = getMetaData(METADATA_MULTI_LANGUAGE_HOME);
		if (meta != null) {
			return new Boolean(meta).booleanValue();
		}
		return false;
	}

	public void setHasMultiLanguageHome(boolean hasMultiLanguageHome) {
		setMetaData(METADATA_MULTI_LANGUAGE_HOME, String.valueOf(hasMultiLanguageHome), "java.lang.Boolean");
	}

	public String getLanguage() {
		return getMetaData(METADATA_LANGUAGE);
	}

	public void setLanguage(String language) {
		if (language != null && language.length() > 0) {
			setMetaData(METADATA_LANGUAGE, language, "java.lang.String");
		}
		else {
			removeMetaData(METADATA_LANGUAGE);
		}
	}

	public String getOtherInformation() {
		return getOtherInformation("");
	}

	public String getOtherInformation(String prefix) {
		return getMetaData(prefix + METADATA_OTHER_INFORMATION);
	}

	public void setOtherInformation(String otherInformation) {
		setOtherInformation("", otherInformation);
	}

	public void setOtherInformation(String prefix, String otherInformation) {
		if (otherInformation != null && otherInformation.length() > 0) {
			setMetaData(prefix + METADATA_OTHER_INFORMATION, otherInformation);
		}
		else {
			removeMetaData(prefix + METADATA_OTHER_INFORMATION);
		}
	}

	private Custodian getCustodianByPrimaryKey(Object primaryKey) throws FinderException {
		try {
			CustodianHome home = (CustodianHome) getIDOHome(Custodian.class);
			return home.findByPrimaryKey(new Integer(primaryKey.toString()));
		}
		catch (IDOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	private Custodian getCustodianByGender(Gender gender) {
		try {
			Collection custodians = getCustodians();
			Iterator iter = custodians.iterator();
			while (iter.hasNext()) {
				Custodian custodian = (Custodian) iter.next();
				if (custodian.getGender().equals(gender)) {
					return custodian;
				}
			}
		}
		catch (NoCustodianFound ncf) {
			// No custodians found;
		}

		Custodian extraCustodian = getExtraCustodian();
		if (extraCustodian != null && extraCustodian.getGender().equals(gender)) {
			return extraCustodian;
		}

		return null;
	}

	private FamilyLogic getFamilyLogic() {
		try {
			return (FamilyLogic) IBOLookup.getServiceInstance(getIWMainApplication().getIWApplicationContext(), FamilyLogic.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	public Integer ejbFindUserForUserGroup(Group group) throws FinderException {
		return super.ejbFindUserForUserGroup(group);
	}
}
