package is.idega.block.family.data;


import is.idega.block.family.business.NoCustodianFound;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.idega.data.IDOEntity;
import com.idega.user.data.User;

public interface Child extends IDOEntity, User {


	/**
	 *
	 * @return all siblings for this {@link Child} or
	 * {@link Collections#emptyList()} on failure;
	 */
	public Collection<User> getSiblings();

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#getCustodians
	 */
	public Collection<Custodian> getCustodians() throws NoCustodianFound;

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#getMother
	 */
	public Custodian getMother();

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#getFather
	 */
	public Custodian getFather();

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#getCustodian
	 */
	public Custodian getCustodian(String relation);

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#setRelation
	 */
	public void setRelation(Custodian custodian, String relation);

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#getRelation
	 */
	public String getRelation(Custodian custodian);

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#getExtraCustodian
	 */
	public Custodian getExtraCustodian();

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#setExtraCustodian
	 */
	public void setExtraCustodian(Custodian custodian);

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#setExtraCustodian
	 */
	public void setExtraCustodian(Custodian custodian, String relation);

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#getRelatives
	 */
	public List<Relative> getRelatives();

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#getRelatives
	 */
	public List<Relative> getRelatives(String prefix);

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#storeMainRelative
	 */
	public void storeMainRelative(String prefix, String name, String relation, String homePhone, String workPhone, String mobilePhone, String email);

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#getMainRelative
	 */
	public Relative getMainRelative(String prefix);

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#storeRelative
	 */
	public void storeRelative(String name, String relation, int number, String homePhone, String workPhone, String mobilePhone, String email);

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#storeRelative
	 */
	public void storeRelative(String personalID, String name, String relation, int number, String homePhone, String workPhone, String mobilePhone, String email);

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#storeRelative
	 */
	public void storeRelative(String prefix, String personalID, String name, String relation, int number, String homePhone, String workPhone, String mobilePhone, String email);

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#removeExtraCustodian
	 */
	public void removeExtraCustodian();

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#removeRelative
	 */
	public void removeRelative(int number);

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#removeRelative
	 */
	public void removeRelative(String prefix, int number);

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#storeForbiddenRelative
	 */
	public void storeForbiddenRelative(String name, String personalID, String details);

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#getForbiddenRelative
	 */
	public Relative getForbiddenRelative();

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#hasGrowthDeviation
	 */
	public Boolean hasGrowthDeviation();

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#hasGrowthDeviation
	 */
	public Boolean hasGrowthDeviation(String prefix);

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#setHasGrowthDeviation
	 */
	public void setHasGrowthDeviation(Boolean hasGrowthDeviation);

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#setHasGrowthDeviation
	 */
	public void setHasGrowthDeviation(String prefix, Boolean hasGrowthDeviation);

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#getGrowthDeviationDetails
	 */
	public String getGrowthDeviationDetails();

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#getGrowthDeviationDetails
	 */
	public String getGrowthDeviationDetails(String prefix);

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#setGrowthDeviationDetails
	 */
	public void setGrowthDeviationDetails(String details);

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#setGrowthDeviationDetails
	 */
	public void setGrowthDeviationDetails(String prefix, String details);

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#hasRegularMedicationRequirement
	 */
	public Boolean hasRegularMedicationRequirement();

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#hasRegularMedicationRequirement
	 */
	public Boolean hasRegularMedicationRequirement(String prefix);

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#setHasRegularMedicationRequirement
	 */
	public void setHasRegularMedicationRequirement(Boolean medication);

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#setHasRegularMedicationRequirement
	 */
	public void setHasRegularMedicationRequirement(String prefix, Boolean medication);

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#getDisabilitiesDescription
	 */
	public String getDisabilitiesDescription();

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#getDisabilitiesDescription
	 */
	public String getDisabilitiesDescription(String prefix);

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#setDisabilitiesDescription
	 */
	public void setDisabilitiesDescription(String details);

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#setDisabilitiesDescription
	 */
	public void setDisabilitiesDescription(String prefix, String details);

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#getRegularMedicationDetails
	 */
	public String getRegularMedicationDetails();

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#getRegularMedicationDetails
	 */
	public String getRegularMedicationDetails(String prefix);

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#setRegularMedicationDetails
	 */
	public void setRegularMedicationDetails(String details);

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#setRegularMedicationDetails
	 */
	public void setRegularMedicationDetails(String prefix, String details);

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#hasAllergies
	 */
	public Boolean hasAllergies();

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#hasAllergies
	 */
	public Boolean hasAllergies(String prefix);

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#setHasAllergies
	 */
	public void setHasAllergies(Boolean hasAllergies);

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#setHasAllergies
	 */
	public void setHasAllergies(String prefix, Boolean hasAllergies);

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#getAllergiesDetails
	 */
	public String getAllergiesDetails();

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#getAllergiesDetails
	 */
	public String getAllergiesDetails(String prefix);

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#setAllergiesDetails
	 */
	public void setAllergiesDetails(String details);

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#setAllergiesDetails
	 */
	public void setAllergiesDetails(String prefix, String details);

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#hasMultiLanguageHome
	 */
	public boolean hasMultiLanguageHome();

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#setHasMultiLanguageHome
	 */
	public void setHasMultiLanguageHome(boolean hasMultiLanguageHome);

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#getLanguage
	 */
	public String getLanguage();

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#setLanguage
	 */
	public void setLanguage(String language);

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#getOtherInformation
	 */
	public String getOtherInformation();

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#getOtherInformation
	 */
	public String getOtherInformation(String prefix);

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#setOtherInformation
	 */
	public void setOtherInformation(String otherInformation);

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#setOtherInformation
	 */
	public void setOtherInformation(String prefix, String otherInformation);
}