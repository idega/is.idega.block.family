package is.idega.block.family.business.impl;

import is.idega.block.family.business.FamilyLogic;
import is.idega.block.family.business.FamilyRelationsHelper;
import is.idega.block.family.business.NoCohabitantFound;
import is.idega.block.family.business.NoSpouseFound;

import java.util.Collection;
import java.util.logging.Level;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.idega.core.business.DefaultSpringBean;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.EmailType;
import com.idega.core.contact.data.EmailTypeBMPBean;
import com.idega.core.contact.data.Phone;
import com.idega.core.contact.data.PhoneType;
import com.idega.user.data.User;
import com.idega.util.ListUtil;
import com.idega.util.StringUtil;

@Scope("session")
@Service(FamilyRelationsHelperImpl.BEAN_IDENTIFIER)
public class FamilyRelationsHelperImpl extends DefaultSpringBean implements FamilyRelationsHelper {

	static final String BEAN_IDENTIFIER = "familyRelationsHelper";

	private User currentUser;
	private User spouseOrCohabitant;

	private User getUser() {
		if (currentUser == null) {
			currentUser = getOldUser(getCurrentUser());
		}
		return currentUser;
	}

	private User getSpouseOrCohabitant() {
		if (spouseOrCohabitant == null) {
			FamilyLogic familyLogic = getServiceInstance(FamilyLogic.class);
			try {
				spouseOrCohabitant = familyLogic.getSpouseFor(getUser());
			} catch (NoSpouseFound e) {
			} catch (Exception e) {
				getLogger().log(Level.WARNING, "Error getting a spouse for current user: " + getUser(), e);
			}
			if (spouseOrCohabitant == null) {
				try {
					spouseOrCohabitant = familyLogic.getCohabitantFor(getUser());
				} catch (NoCohabitantFound e) {
				} catch (Exception e) {
					getLogger().log(Level.WARNING, "Error getting a cohabitant for current user: " + getUser(), e);
				}
			}
		}
		return spouseOrCohabitant;
	}

	@Override
	@SuppressWarnings("unchecked")
	public String getSpouseEmail() {
		User spouse = getSpouseOrCohabitant();
		if (spouse == null) {
			return null;
		}

		Collection<Email> emails = spouse.getEmails();
		if (ListUtil.isEmpty(emails)) {
			return null;
		}

		String email = null;
		EmailType mailType = null;
		for (Email mail: emails) {
			mailType = mail.getEmailType();
			if (mailType != null && EmailTypeBMPBean.MAIN_EMAIL.equals(mailType.getUniqueName())) {
				email = mail.getEmailAddress();
			}
		}

		if (StringUtil.isEmpty(email)) {
			email = emails.iterator().next().getEmailAddress();
		}

		return email;
	}

	@Override
	public String getSpouseName() {
		User spouse = getSpouseOrCohabitant();
		if (spouse == null) {
			return null;
		}

		return spouse.getName();
	}

	@Override
	public String getSpousePersonalId() {
		User spouse = getSpouseOrCohabitant();
		if (spouse == null) {
			return null;
		}

		return spouse.getPersonalID();
	}

	@Override
	public String getSpouseMobile() {
		return getPhoneByType(PhoneType.MOBILE_PHONE_ID);
	}

	@Override
	public String getSpouseTelephone() {
		return getPhoneByType(PhoneType.HOME_PHONE_ID);
	}

	@SuppressWarnings("unchecked")
	private String getPhoneByType(int phoneType) {
		User spouse = getSpouseOrCohabitant();
		if (spouse == null) {
			return null;
		}

		Collection<Phone> phones = null;
		try {
			phones = spouse.getPhones(String.valueOf(phoneType));
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error getting phones for " + spouse + " by type: " + phoneType);
		}

		return ListUtil.isEmpty(phones) ? null : phones.iterator().next().getNumber();
	}
}