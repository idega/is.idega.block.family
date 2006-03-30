/*
 * $Id$
 * Created on Mar 29, 2006
 *
 * Copyright (C) 2006 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.block.family.data;

import is.idega.block.family.business.FamilyLogic;
import is.idega.block.family.business.NoChildrenFound;
import is.idega.block.family.business.NoCohabitantFound;
import is.idega.block.family.business.NoSpouseFound;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.user.business.NoEmailFoundException;
import com.idega.user.business.NoPhoneFoundException;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.user.data.UserBMPBean;


public class CustodianBMPBean extends UserBMPBean implements User, Custodian {

	public Collection getChildrenInCustody() throws NoChildrenFound {
		try {
			return getFamilyLogic().getChildrenInCustodyOf(this);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	public boolean isCustodianOf(Child child) {
		try {
			return getFamilyLogic().isCustodianOf(this, child);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	public User getSpouse() throws NoSpouseFound {
		try {
			return getFamilyLogic().getSpouseFor(this);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	public User getCohabitant() throws NoCohabitantFound {
		try {
			return getFamilyLogic().getCohabitantFor(this);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	public Phone getHomePhone() throws NoPhoneFoundException {
		try {
			return getUserBusiness().getUsersHomePhone(this);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	public Phone getWorkPhone() throws NoPhoneFoundException {
		try {
			return getUserBusiness().getUsersWorkPhone(this);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	public Phone getMobilePhone() throws NoPhoneFoundException {
		try {
			return getUserBusiness().getUsersMobilePhone(this);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	public Email getEmail() throws NoEmailFoundException {
		try {
			return getUserBusiness().getUsersMainEmail(this);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	public void setHomePhone(String homePhone) {
		try {
			getUserBusiness().updateUserHomePhone(this, homePhone);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	public void setWorkPhone(String workPhone) {
		try {
			getUserBusiness().updateUserWorkPhone(this, workPhone);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	public void setMobilePhone(String mobilePhone) {
		try {
			getUserBusiness().updateUserMobilePhone(this, mobilePhone);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	public void setEmail(String email) {
		try {
			getUserBusiness().updateUserMail(this, email);
		}
		catch (CreateException ce) {
			log(ce);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	private UserBusiness getUserBusiness() {
		try {
			return (UserBusiness) IBOLookup.getServiceInstance(getIWMainApplication().getIWApplicationContext(), UserBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
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