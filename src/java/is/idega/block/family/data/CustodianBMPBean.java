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
import java.sql.Date;
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
import com.idega.util.IWTimestamp;


public class CustodianBMPBean extends UserBMPBean implements User, Custodian {

	private static final long serialVersionUID = 3875912690930541866L;

	private static final String METADATA_HAS_STUDIES = "has_studies";
	private static final String METADATA_STUDIES = "studies";
	private static final String METADATA_STUDY_START = "study_start";
	private static final String METADATA_STUDY_END = "study_end";
	private static final String METADATA_NATIONALITY = "nationality";
	private static final String METADATA_MARITAL_STATUS = "marital_status";

	@Override
	public Collection<User> getChildrenInCustody() throws NoChildrenFound {
		try {
			return getFamilyLogic().getChildrenInCustodyOf(this);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	@Override
	public boolean isCustodianOf(Child child) {
		try {
			return getFamilyLogic().isCustodianOf(this, child);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	@Override
	public User getSpouse() throws NoSpouseFound {
		try {
			return getFamilyLogic().getSpouseFor(this);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	@Override
	public User getCohabitant() throws NoCohabitantFound {
		try {
			return getFamilyLogic().getCohabitantFor(this);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	@Override
	public Phone getHomePhone() throws NoPhoneFoundException {
		try {
			return getUserBusiness().getUsersHomePhone(this);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	@Override
	public Phone getWorkPhone() throws NoPhoneFoundException {
		try {
			return getUserBusiness().getUsersWorkPhone(this);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	@Override
	public Phone getMobilePhone() throws NoPhoneFoundException {
		try {
			return getUserBusiness().getUsersMobilePhone(this);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	@Override
	public Email getEmail() throws NoEmailFoundException {
		try {
			return getUserBusiness().getUsersMainEmail(this);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	@Override
	public void setHomePhone(String homePhone) {
		try {
			getUserBusiness().updateUserHomePhone(this, homePhone);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	@Override
	public void setWorkPhone(String workPhone) {
		try {
			getUserBusiness().updateUserWorkPhone(this, workPhone);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	@Override
	public void setMobilePhone(String mobilePhone) {
		try {
			getUserBusiness().updateUserMobilePhone(this, mobilePhone);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	@Override
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

	@Override
	public boolean hasStudies() {
		String meta = getMetaData(METADATA_HAS_STUDIES);
		if (meta != null) {
			return new Boolean(meta).booleanValue();
		}
		return false;
	}

	@Override
	public void setHasStudies(boolean hasStudies) {
		setMetaData(METADATA_HAS_STUDIES, String.valueOf(hasStudies), "java.lang.Boolean");
	}

	@Override
	public String getStudies() {
		return getMetaData(METADATA_STUDIES);
	}

	@Override
	public void setStudies(String studies) {
		if (studies != null && studies.length() > 0) {
			setMetaData(METADATA_STUDIES, studies, "java.lang.String");
		}
		else {
			removeMetaData(METADATA_STUDIES);
		}
	}

	@Override
	public Date getStudyStart() {
		String meta = getMetaData(METADATA_STUDY_START);
		if (meta != null) {
			return new IWTimestamp(meta).getDate();
		}
		return null;
	}

	@Override
	public void setStudyStart(Date date) {
		if (date != null) {
			setMetaData(METADATA_STUDY_START, date.toString());
		}
		else {
			removeMetaData(METADATA_STUDY_START);
		}
	}

	@Override
	public Date getStudyEnd() {
		String meta = getMetaData(METADATA_STUDY_END);
		if (meta != null) {
			return new IWTimestamp(meta).getDate();
		}
		return null;
	}

	@Override
	public void setStudyEnd(Date date) {
		if (date != null) {
			setMetaData(METADATA_STUDY_END, date.toString());
		}
		else {
			removeMetaData(METADATA_STUDY_END);
		}
	}

	@Override
	public String getNationality() {
		return getMetaData(METADATA_NATIONALITY);
	}

	@Override
	public void setNationality(String nationality) {
		if (nationality != null && nationality.length() > 0) {
			setMetaData(METADATA_NATIONALITY, nationality, "java.lang.String");
		}
		else {
			removeMetaData(METADATA_NATIONALITY);
		}
	}

	@Override
	public String getMaritalStatus() {
		return getMetaData(METADATA_MARITAL_STATUS);
	}

	@Override
	public void setMaritalStatus(String maritalStatus) {
		if (maritalStatus != null && maritalStatus.length() > 0) {
			setMetaData(METADATA_MARITAL_STATUS, maritalStatus, "java.lang.String");
		}
		else {
			removeMetaData(METADATA_MARITAL_STATUS);
		}
	}

	private UserBusiness getUserBusiness() {
		try {
			return IBOLookup.getServiceInstance(getIWMainApplication().getIWApplicationContext(), UserBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
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