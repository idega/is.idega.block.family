/*
 * $Id$
 * Created on Apr 5, 2006
 *
 * Copyright (C) 2006 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.block.family.data;

import is.idega.block.family.business.NoCustodianFound;
import is.idega.block.family.business.NoSiblingFound;

import java.util.Collection;
import java.util.List;


import com.idega.data.IDOEntity;
import com.idega.user.data.User;


/**
 * <p>
 * TODO laddi Describe Type Child
 * </p>
 *  Last modified: $Date$ by $Author$
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision$
 */
public interface Child extends IDOEntity, User {

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#getSiblings
	 */
	public Collection getSiblings() throws NoSiblingFound;

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#getCustodians
	 */
	public Collection getCustodians() throws NoCustodianFound;

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
	public List getRelatives();

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#storeRelative
	 */
	public void storeRelative(String name, String relation, int number, String homePhone, String workPhone, String mobilePhone, String email);

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
	 * @see is.idega.block.family.data.ChildBMPBean#setHasGrowthDeviation
	 */
	public void setHasGrowthDeviation(Boolean hasGrowthDeviation);

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#getGrowthDeviationDetails
	 */
	public String getGrowthDeviationDetails();

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#setGrowthDeviationDetails
	 */
	public void setGrowthDeviationDetails(String details);

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#hasAllergies
	 */
	public Boolean hasAllergies();

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#setHasAllergies
	 */
	public void setHasAllergies(Boolean hasAllergies);

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#getAllergiesDetails
	 */
	public String getAllergiesDetails();

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#setAllergiesDetails
	 */
	public void setAllergiesDetails(String details);

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
	 * @see is.idega.block.family.data.ChildBMPBean#setOtherInformation
	 */
	public void setOtherInformation(String otherInformation);

}
