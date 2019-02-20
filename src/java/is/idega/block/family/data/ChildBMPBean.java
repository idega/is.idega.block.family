/*
 * $Id$ Created on Mar 29, 2006
 *
 * Copyright (C) 2006 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf. Use is subject to license terms.
 */
package is.idega.block.family.data;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWMainApplication;
import com.idega.user.data.Gender;
import com.idega.user.data.GenderHome;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.user.data.UserBMPBean;
import com.idega.user.data.UserHome;
import com.idega.util.ListUtil;
import com.idega.util.StringHandler;
import com.idega.util.StringUtil;

import is.idega.block.family.business.FamilyConstants;
import is.idega.block.family.business.FamilyLogic;
import is.idega.block.family.business.NoCustodianFound;

public class ChildBMPBean extends UserBMPBean implements User, Child {

	private static final long serialVersionUID = 8582495738354609902L;

	public static final String METADATA_BRIEF_DISABILITIES_DESCRIPTION = "disabilities_description";

	public static final String METADATA_REGULAR_MEDICATION = "regular_medication";
	public static final String METADATA_REGULAR_MEDICATION_DETAILS = "regular_medication_details";

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

	/*
	 * (non-Javadoc)
	 * @see is.idega.block.family.data.Child#getSiblings()
	 */
	@Override
	public Collection<User> getSiblings() {
		return getFamilyLogic().getSiblingsFor(this);
	}

	@Override
	public Collection<Custodian> getCustodians() throws NoCustodianFound {
		try {
			FamilyLogic familyLogic = getFamilyLogic();
			return familyLogic.getConvertedUsersAsCustodians(familyLogic.getCustodiansFor(this));
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	@Override
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

	@Override
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

	@Override
	public Custodian getCustodian(String relation) {
		// Collection metadataValues = getMetaDataAttributes().values();
		Map<String, String> metadataAttributes = getMetaDataAttributes();
		Collection<String> keys = metadataAttributes.keySet();
		// if (metadataValues != null) {
		if (keys != null) {
			// Iterator iter = metadataValues.iterator();
			Iterator<String> iter = keys.iterator();
			while (iter.hasNext()) {
				// Object value = iter.next();
				String key = iter.next();
				if (key.startsWith(METADATA_RELATION)) {
					String value = metadataAttributes.get(key);
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

	@Override
	public void setRelation(Custodian custodian, String relation) {
		storeRelation(custodian, relation, false);
	}

	@Override
	public void storeRelation(Custodian custodian, String relation) {
		storeRelation(custodian, relation, true);
	}

	private void storeRelation(Custodian custodian, String relation, boolean store) {
		setMetaData(METADATA_RELATION + custodian.getPrimaryKey().toString(), relation, "java.lang.String");
		if (store) {
			try {
				store();
			} catch (Exception e) {
				getLogger().log(Level.WARNING, "Error storing child " + this + " after relation (" + relation + ") with " + custodian + " added");
			}
		}
	}

	@Override
	public String getRelation(Custodian custodian) {
		return getMetaData(METADATA_RELATION + custodian.getPrimaryKey().toString());
	}

	private boolean isCustodianFor(User custodian, User child) {
		if (custodian == null || child == null) {
			return false;
		}

		try {
			FamilyLogic familyLogic = IBOLookup.getServiceInstance(IWMainApplication.getDefaultIWApplicationContext(), FamilyLogic.class);
			Collection<User> custodians = null;
			try {
				custodians = familyLogic.getCustodiansFor(child, false);
			} catch (Exception e) {}
			if (ListUtil.isEmpty(custodians)) {
				return false;
			}

			String id = custodian.getId();
			for (User childCustodian: custodians) {
				if (childCustodian.getId().equals(id)) {
					return true;
				}
			}
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error while checking if " + custodian + " is in custody of child " + child, e);
		}

		return false;
	}

	@Override
	public Custodian getExtraCustodian() {
		String custodianPK = this.getMetaData(METADATA_OTHER_CUSTODIAN);
		if (StringHandler.isNumeric(custodianPK)) {
			try {
				Custodian custodian = getCustodianByPrimaryKey(new Integer(custodianPK));
				if (custodian == null) {
					return null;
				}

				//	Double checking if found custodian is currently a custodian of a child
				if (isCustodianFor(custodian, this)) {
					return custodian;
				}

				try {
					removeMetaData(METADATA_OTHER_CUSTODIAN);
				} catch (Exception e) {
					getLogger().log(Level.WARNING, "Error removing metadata '" + METADATA_OTHER_CUSTODIAN + "' for " + this, e);
				}
				return null;
			} catch (FinderException fe) {
				fe.printStackTrace();
			} catch (Exception e) {
				getLogger().log(Level.WARNING, "Error getting extra custodian (ID: " + custodianPK + ") for " + this, e);
			}
		}
		return null;
	}

	@Override
	public void setExtraCustodian(Custodian custodian) {
		setExtraCustodian(custodian, null);
	}

	@Override
	public void setExtraCustodian(Custodian custodian, String relation) {
		setMetaData(METADATA_OTHER_CUSTODIAN, custodian.getPrimaryKey().toString(), "com.idega.user.data.User");
		if (relation != null && relation.length() > 0) {
			setRelation(custodian, relation);
		}
	}

	@Override
	public void removeExtraCustodian() {
		this.removeMetaData(METADATA_OTHER_CUSTODIAN);
		this.store();
	}

	@Override
	public List<Relative> getRelatives() {
		return getRelatives("");
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.block.family.data.Child#getRelatives(java.lang.String)
	 */
	@Override
	public List<Relative> getRelatives(String prefix) {
		List<Relative> relatives = new ArrayList<Relative>();

		for (int a = 1; a <= 2; a++) {
			String name = getMetaData(prefix +
					(a == 1 ? METADATA_RELATIVE_1 : METADATA_RELATIVE_2) +
					"_name");
			String relation = getMetaData(prefix +
					(a == 1 ? METADATA_RELATIVE_1 : METADATA_RELATIVE_2) +
					"_relation");
			String homePhone = getMetaData(prefix +
					(a == 1 ? METADATA_RELATIVE_1 : METADATA_RELATIVE_2) +
					"_homePhone");
			String workPhone = getMetaData(prefix +
					(a == 1 ? METADATA_RELATIVE_1 : METADATA_RELATIVE_2) +
					"_workPhone");
			String mobilePhone = getMetaData(prefix +
					(a == 1 ? METADATA_RELATIVE_1 : METADATA_RELATIVE_2) +
					"_mobilePhone");
			String email = getMetaData(prefix +
					(a == 1 ? METADATA_RELATIVE_1 : METADATA_RELATIVE_2) +
					"_email");
			String personalID = getMetaData(prefix +
					(a == 1 ? METADATA_RELATIVE_1 : METADATA_RELATIVE_2) +
					"_personalID");

			if (!StringUtil.isEmpty(name)) {
				Relative relative = new Relative();
				if (!StringUtil.isEmpty(personalID)) {
					if (personalID.indexOf("_") != -1) {
						String userPK = personalID.substring(personalID.indexOf("_") + 1);
						try {
							User user = ((UserHome) IDOLookup.getHome(User.class))
									.findByPrimaryKey(new Integer(userPK));
							if (user != null) {
								personalID = user.getPersonalID();
							}
						} catch (Exception ile) {
							getLogger().log(Level.WARNING,
									"Failed to get " + User.class.getSimpleName() +
									" by primary key: " + userPK);
						}
					}
				}

				relative.setPersonalID(personalID);
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

	@Override
	public void storeMainRelative(String prefix, String name, String relation, String homePhone, String workPhone, String mobilePhone, String email) {
		setMetaData(prefix + METADATA_RELATIVE_0 + "_name", name, "java.lang.String");
		setMetaData(prefix + METADATA_RELATIVE_0 + "_relation", relation, "java.lang.String");
		setMetaData(prefix + METADATA_RELATIVE_0 + "_homePhone", homePhone, "java.lang.String");
		setMetaData(prefix + METADATA_RELATIVE_0 + "_workPhone", workPhone, "java.lang.String");
		setMetaData(prefix + METADATA_RELATIVE_0 + "_mobilePhone", mobilePhone, "java.lang.String");
		setMetaData(prefix + METADATA_RELATIVE_0 + "_email", email, "java.lang.String");
		store();
	}

	@Override
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

	@Override
	public void storeRelative(String name, String relation, int number, String homePhone, String workPhone, String mobilePhone, String email) {
		storeRelative("", null, name, relation, number, homePhone, workPhone, mobilePhone, email);
	}

	@Override
	public void storeRelative(String personalID, String name, String relation, int number, String homePhone, String workPhone, String mobilePhone, String email) {
		storeRelative("", personalID, name, relation, number, homePhone, workPhone, mobilePhone, email);
	}

	@Override
	public void storeRelative(String prefix, String personalID, String name, String relation, int number, String homePhone, String workPhone, String mobilePhone, String email) {
		if (number > 2 || number < 1) {
			return;
		}

		if (personalID != null) {
			setMetaData(prefix + (number == 1 ? METADATA_RELATIVE_1 : METADATA_RELATIVE_2) + "_personalID", personalID, "java.lang.String");
		}
		setMetaData(prefix + (number == 1 ? METADATA_RELATIVE_1 : METADATA_RELATIVE_2) + "_name", name, "java.lang.String");
		setMetaData(prefix + (number == 1 ? METADATA_RELATIVE_1 : METADATA_RELATIVE_2) + "_relation", relation, "java.lang.String");
		if (StringUtil.isEmpty(homePhone)) {
			removeMetaData(prefix + (number == 1 ? METADATA_RELATIVE_1 : METADATA_RELATIVE_2) + "_homePhone");
		} else {
			setMetaData(prefix + (number == 1 ? METADATA_RELATIVE_1 : METADATA_RELATIVE_2) + "_homePhone", homePhone, "java.lang.String");
		}
		if (StringUtil.isEmpty(workPhone)) {
			removeMetaData(prefix + (number == 1 ? METADATA_RELATIVE_1 : METADATA_RELATIVE_2) + "_workPhone");
		} else {
			setMetaData(prefix + (number == 1 ? METADATA_RELATIVE_1 : METADATA_RELATIVE_2) + "_workPhone", workPhone, "java.lang.String");
		}
		if (StringUtil.isEmpty(mobilePhone)) {
			removeMetaData(prefix + (number == 1 ? METADATA_RELATIVE_1 : METADATA_RELATIVE_2) + "_mobilePhone");
		} else {
			setMetaData(prefix + (number == 1 ? METADATA_RELATIVE_1 : METADATA_RELATIVE_2) + "_mobilePhone", mobilePhone, "java.lang.String");
		}
		if (StringUtil.isEmpty(email)) {
			removeMetaData(prefix + (number == 1 ? METADATA_RELATIVE_1 : METADATA_RELATIVE_2) + "_email");
		} else {
			setMetaData(prefix + (number == 1 ? METADATA_RELATIVE_1 : METADATA_RELATIVE_2) + "_email", email, "java.lang.String");
		}
		store();
	}

	@Override
	public void removeRelative(int number) {
		removeRelative("", number);
	}

	@Override
	public void removeRelative(String prefix, int number) {
		if (number > 2 || number < 1) {
			return;
		}

		removeMetaData(prefix + (number == 1 ? METADATA_RELATIVE_1 : METADATA_RELATIVE_2) + "_personalID");
		removeMetaData(prefix + (number == 1 ? METADATA_RELATIVE_1 : METADATA_RELATIVE_2) + "_name");
		removeMetaData(prefix + (number == 1 ? METADATA_RELATIVE_1 : METADATA_RELATIVE_2) + "_relation");
		removeMetaData(prefix + (number == 1 ? METADATA_RELATIVE_1 : METADATA_RELATIVE_2) + "_homePhone");
		removeMetaData(prefix + (number == 1 ? METADATA_RELATIVE_1 : METADATA_RELATIVE_2) + "_workPhone");
		removeMetaData(prefix + (number == 1 ? METADATA_RELATIVE_1 : METADATA_RELATIVE_2) + "_mobilePhone");
		removeMetaData(prefix + (number == 1 ? METADATA_RELATIVE_1 : METADATA_RELATIVE_2) + "_email");


		//FIXME: This is not removing, but rather creating a new METADATA/RELATIVE!!!
//		setMetaData(prefix + (number == 1 ? METADATA_RELATIVE_1 : METADATA_RELATIVE_2) + "_personalID", "", "java.lang.String");
//		setMetaData(prefix + (number == 1 ? METADATA_RELATIVE_1 : METADATA_RELATIVE_2) + "_name", "", "java.lang.String");
//		setMetaData(prefix + (number == 1 ? METADATA_RELATIVE_1 : METADATA_RELATIVE_2) + "_relation", "", "java.lang.String");
//		setMetaData(prefix + (number == 1 ? METADATA_RELATIVE_1 : METADATA_RELATIVE_2) + "_relation", "", "java.lang.String");
//		setMetaData(prefix + (number == 1 ? METADATA_RELATIVE_1 : METADATA_RELATIVE_2) + "_workPhone", "", "java.lang.String");
//		setMetaData(prefix + (number == 1 ? METADATA_RELATIVE_1 : METADATA_RELATIVE_2) + "_workPhone", "", "java.lang.String");
//		setMetaData(prefix + (number == 1 ? METADATA_RELATIVE_1 : METADATA_RELATIVE_2) + "_workPhone", "", "java.lang.String");
		store();
	}

	@Override
	public void storeForbiddenRelative(String name, String personalID, String details) {
		setMetaData(METADATA_FORBIDDEN_RELATIVE + "_name", name, "java.lang.String");
		setMetaData(METADATA_FORBIDDEN_RELATIVE + "_personalID", personalID, "java.lang.String");
		setMetaData(METADATA_FORBIDDEN_RELATIVE + "_details", details, "java.lang.String");
		store();
	}

	@Override
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

	@Override
	public Boolean hasGrowthDeviation() {
		return hasGrowthDeviation("");
	}

	@Override
	public Boolean hasGrowthDeviation(String prefix) {
		String meta = getMetaData(prefix + METADATA_GROWTH_DEVIATION);
		if (meta != null && meta.length() > 0) {
			return new Boolean(meta);
		}
		return null;
	}

	@Override
	public void setHasGrowthDeviation(Boolean hasGrowthDeviation) {
		setHasGrowthDeviation("", hasGrowthDeviation);
	}

	@Override
	public void setHasGrowthDeviation(String prefix, Boolean hasGrowthDeviation) {
		if (hasGrowthDeviation != null) {
			setMetaData(prefix + METADATA_GROWTH_DEVIATION, hasGrowthDeviation.toString());
		}
		else {
			removeMetaData(prefix + METADATA_GROWTH_DEVIATION);
		}
	}

	@Override
	public String getGrowthDeviationDetails() {
		return getGrowthDeviationDetails("");
	}

	@Override
	public String getGrowthDeviationDetails(String prefix) {
		return getMetaData(prefix + METADATA_GROWTH_DEVIATION_DETAILS);
	}

	@Override
	public void setGrowthDeviationDetails(String details) {
		setGrowthDeviationDetails("", details);
	}

	@Override
	public void setGrowthDeviationDetails(String prefix, String details) {
		if (details != null && details.length() > 0) {
			setMetaData(prefix + METADATA_GROWTH_DEVIATION_DETAILS, details);
		}
		else {
			removeMetaData(prefix + METADATA_GROWTH_DEVIATION_DETAILS);
		}
	}

	@Override
	public Boolean hasRegularMedicationRequirement() {
		return hasRegularMedicationRequirement("");
	}

	@Override
	public Boolean hasRegularMedicationRequirement(String prefix) {
		String meta = getMetaData(prefix + METADATA_REGULAR_MEDICATION);
		if (meta != null && meta.length() > 0) {
			return new Boolean(meta);
		}
		return null;
	}

	@Override
	public void setHasRegularMedicationRequirement(Boolean medication) {
		setHasRegularMedicationRequirement("", medication);
	}

	@Override
	public void setHasRegularMedicationRequirement(String prefix, Boolean medication) {
		if (medication != null) {
			setMetaData(prefix + METADATA_REGULAR_MEDICATION, medication.toString());
		}
		else {
			removeMetaData(prefix + METADATA_REGULAR_MEDICATION);
		}
	}

	@Override
	public String getDisabilitiesDescription() {
		return getDisabilitiesDescription("");
	}

	@Override
	public String getDisabilitiesDescription(String prefix) {
		return getMetaData(prefix + METADATA_BRIEF_DISABILITIES_DESCRIPTION);
	}

	@Override
	public void setDisabilitiesDescription(String details) {
		setDisabilitiesDescription("", details);
	}

	@Override
	public void setDisabilitiesDescription(String prefix, String details) {
		if (details != null && details.length() > 0) {
			setMetaData(prefix + METADATA_BRIEF_DISABILITIES_DESCRIPTION, details);
		}
		else {
			removeMetaData(prefix + METADATA_BRIEF_DISABILITIES_DESCRIPTION);
		}
	}

	@Override
	public String getRegularMedicationDetails() {
		return getRegularMedicationDetails("");
	}

	@Override
	public String getRegularMedicationDetails(String prefix) {
		return getMetaData(prefix + METADATA_REGULAR_MEDICATION_DETAILS);
	}

	@Override
	public void setRegularMedicationDetails(String details) {
		setRegularMedicationDetails("", details);
	}

	@Override
	public void setRegularMedicationDetails(String prefix, String details) {
		if (details != null && details.length() > 0) {
			setMetaData(prefix + METADATA_REGULAR_MEDICATION_DETAILS, details);
		}
		else {
			removeMetaData(prefix + METADATA_REGULAR_MEDICATION_DETAILS);
		}
	}

	@Override
	public Boolean hasAllergies() {
		return hasAllergies("");
	}

	@Override
	public Boolean hasAllergies(String prefix) {
		String meta = getMetaData(prefix + METADATA_ALLERGIES);
		if (meta != null && meta.length() > 0) {
			return new Boolean(meta);
		}
		return null;
	}

	@Override
	public void setHasAllergies(Boolean hasAllergies) {
		setHasAllergies("", hasAllergies);
	}

	@Override
	public void setHasAllergies(String prefix, Boolean hasAllergies) {
		if (hasAllergies != null) {
			setMetaData(prefix + METADATA_ALLERGIES, hasAllergies.toString());
		}
		else {
			removeMetaData(prefix + METADATA_ALLERGIES);
		}
	}

	@Override
	public String getAllergiesDetails() {
		return getAllergiesDetails("");
	}

	@Override
	public String getAllergiesDetails(String prefix) {
		return getMetaData(prefix + METADATA_ALLERGIES_DETAILS);
	}

	@Override
	public void setAllergiesDetails(String details) {
		setAllergiesDetails("", details);
	}

	@Override
	public void setAllergiesDetails(String prefix, String details) {
		if (details != null && details.length() > 0) {
			setMetaData(prefix + METADATA_ALLERGIES_DETAILS, details);
		}
		else {
			removeMetaData(prefix + METADATA_ALLERGIES_DETAILS);
		}
	}

	@Override
	public boolean hasMultiLanguageHome() {
		String meta = getMetaData(METADATA_MULTI_LANGUAGE_HOME);
		if (meta != null) {
			return new Boolean(meta).booleanValue();
		}
		return false;
	}

	@Override
	public void setHasMultiLanguageHome(boolean hasMultiLanguageHome) {
		setMetaData(METADATA_MULTI_LANGUAGE_HOME, String.valueOf(hasMultiLanguageHome), "java.lang.Boolean");
	}

	@Override
	public String getLanguage() {
		return getMetaData(METADATA_LANGUAGE);
	}

	@Override
	public void setLanguage(String language) {
		if (language != null && language.length() > 0) {
			setMetaData(METADATA_LANGUAGE, language, "java.lang.String");
		}
		else {
			removeMetaData(METADATA_LANGUAGE);
		}
	}

	@Override
	public String getOtherInformation() {
		return getOtherInformation("");
	}

	@Override
	public String getOtherInformation(String prefix) {
		return getMetaData(prefix + METADATA_OTHER_INFORMATION);
	}

	@Override
	public void setOtherInformation(String otherInformation) {
		setOtherInformation("", otherInformation);
	}

	@Override
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
			Collection<Custodian> custodians = getCustodians();
			Iterator<Custodian> iter = custodians.iterator();
			while (iter.hasNext()) {
				Custodian custodian = iter.next();
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
			return IBOLookup.getServiceInstance(getIWMainApplication().getIWApplicationContext(), FamilyLogic.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	@Override
	public Integer ejbFindUserForUserGroup(Group group) throws FinderException {
		return super.ejbFindUserForUserGroup(group);
	}
}
