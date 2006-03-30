/*
 * $Id$
 * Created on Mar 30, 2006
 *
 * Copyright (C) 2006 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.block.family.data;

import is.idega.block.family.business.NoChildrenFound;
import is.idega.block.family.business.NoCohabitantFound;
import is.idega.block.family.business.NoSpouseFound;

import java.util.Collection;


import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.data.IDOEntity;
import com.idega.user.business.NoEmailFoundException;
import com.idega.user.business.NoPhoneFoundException;
import com.idega.user.data.User;


/**
 * <p>
 * TODO laddi Describe Type Custodian
 * </p>
 *  Last modified: $Date$ by $Author$
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision$
 */
public interface Custodian extends IDOEntity, User {

	/**
	 * @see is.idega.block.family.data.CustodianBMPBean#getChildrenInCustody
	 */
	public Collection getChildrenInCustody() throws NoChildrenFound;

	/**
	 * @see is.idega.block.family.data.CustodianBMPBean#isCustodianOf
	 */
	public boolean isCustodianOf(Child child);

	/**
	 * @see is.idega.block.family.data.CustodianBMPBean#getSpouse
	 */
	public User getSpouse() throws NoSpouseFound;

	/**
	 * @see is.idega.block.family.data.CustodianBMPBean#getCohabitant
	 */
	public User getCohabitant() throws NoCohabitantFound;

	/**
	 * @see is.idega.block.family.data.CustodianBMPBean#getHomePhone
	 */
	public Phone getHomePhone() throws NoPhoneFoundException;

	/**
	 * @see is.idega.block.family.data.CustodianBMPBean#getWorkPhone
	 */
	public Phone getWorkPhone() throws NoPhoneFoundException;

	/**
	 * @see is.idega.block.family.data.CustodianBMPBean#getMobilePhone
	 */
	public Phone getMobilePhone() throws NoPhoneFoundException;

	/**
	 * @see is.idega.block.family.data.CustodianBMPBean#getEmail
	 */
	public Email getEmail() throws NoEmailFoundException;

	/**
	 * @see is.idega.block.family.data.CustodianBMPBean#setHomePhone
	 */
	public void setHomePhone(String homePhone);

	/**
	 * @see is.idega.block.family.data.CustodianBMPBean#setWorkPhone
	 */
	public void setWorkPhone(String workPhone);

	/**
	 * @see is.idega.block.family.data.CustodianBMPBean#setMobilePhone
	 */
	public void setMobilePhone(String mobilePhone);

	/**
	 * @see is.idega.block.family.data.CustodianBMPBean#setEmail
	 */
	public void setEmail(String email);

}
