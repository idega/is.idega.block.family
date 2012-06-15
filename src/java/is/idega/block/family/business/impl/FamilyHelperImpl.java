package is.idega.block.family.business.impl;

import is.idega.block.family.business.FamilyConstants;
import is.idega.block.family.business.FamilyHelper;
import is.idega.block.family.business.FamilyLogic;
import is.idega.block.family.business.FamilyRelationsHelper;
import is.idega.block.family.business.NoChildrenFound;
import is.idega.block.family.business.NoParentFound;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;

import javax.ejb.EJBException;
import javax.ejb.FinderException;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.idega.builder.bean.AdvancedProperty;
import com.idega.core.business.DefaultSpringBean;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.localisation.data.ICLanguage;
import com.idega.core.localisation.data.ICLanguageHome;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.Country;
import com.idega.core.location.data.CountryHome;
import com.idega.core.location.data.PostalCode;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;
import com.idega.util.ArrayUtil;
import com.idega.util.CoreConstants;
import com.idega.util.ListUtil;
import com.idega.util.StringUtil;
import com.idega.util.expression.ELUtil;

/**
 * Implementation for {@link FamilyHelper}
 *
 * @author <a href="mailto:valdas@idega.com">Valdas Žemaitis</a>
 * @version $Revision: 1.0 $
 *
 *          Last modified: $Date: 2009.09.30 18:43:56 $ by: $Author: valdas $
 */
@Scope(BeanDefinition.SCOPE_SINGLETON)
@Service(FamilyHelper.BEAN_IDENTIFIER)
public class FamilyHelperImpl extends DefaultSpringBean implements FamilyHelper {

	/*
	 * (non-Javadoc)
	 * @see is.idega.block.family.business.FamilyHelper#getTeenagesOfCurrentUser()
	 */
	@Override
	public Map<Locale, Map<String, String>> getTeenagersOfCurrentUser() {
		IWResourceBundle iwrb = getResourceBundle(getBundle(FamilyConstants.IW_BUNDLE_IDENTIFIER));
		if (iwrb == null)
			return Collections.emptyMap();

		Map<Locale, Map<String, String>> teenagers = new HashMap<Locale, Map<String, String>>();

		Map<String, String> childrenInfo = new LinkedHashMap<String, String>();

		childrenInfo.put(CoreConstants.EMPTY, iwrb.getLocalizedString("select_your_child", "Select your child"));

		Locale locale = getCurrentLocale();
		teenagers.put(locale, childrenInfo);

		User currentUser = getCurrentUser();
		if (currentUser == null) {
			getLogger().info("User must be logged in!");
			return teenagers;
		}

		FamilyLogic familyLogic = getServiceInstance(FamilyLogic.class);
		if (familyLogic == null)
			return teenagers;

		int maxAge = Integer.valueOf(getApplication().getSettings().getProperty("max_music_student_age", String.valueOf(16)));
		Collection<User> teenagersOfCurrentUser = null;
		try {
			teenagersOfCurrentUser = familyLogic.getChildrenForUserUnderAge(currentUser, maxAge);
		} catch (NoChildrenFound e) {
			getLogger().info(currentUser + " doesn't have children younger than " + maxAge + " years");
			return teenagers;
		} catch (RemoteException e) {
			getLogger().log(Level.WARNING, "Some error occurred while getting teenagers for: " + currentUser, e);
			return teenagers;
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Some error occurred while getting teenagers for: " + currentUser, e);
			return teenagers;
		}

		if (ListUtil.isEmpty(teenagersOfCurrentUser)) {
			getLogger().warning(currentUser + " doesn't have children younger than " + maxAge + " years");
			return teenagers;
		}

		List<AdvancedProperty> children = new ArrayList<AdvancedProperty>();
		for (User teenage : teenagersOfCurrentUser)
			children.add(new AdvancedProperty(teenage.getId(), teenage.getName()));
		doSortValues(children, childrenInfo, locale);

		return teenagers;
	}

	@Override
	public String getSpouseEmail() {
		try {
			return getApplicantServices().getSpouseEmail();
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error getting spouse email", e);
		}
		return CoreConstants.EMPTY;
	}

	@Override
	public String getSpouseMobile() {
		try {
			return getApplicantServices().getSpouseMobile();
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error getting spouse mobile", e);
		}
		return CoreConstants.EMPTY;
	}

	@Override
	public String getSpouseName() {
		try {
			return getApplicantServices().getSpouseName();
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error getting a name of spouse", e);
		}
		return CoreConstants.EMPTY;
	}

	@Override
	public String getSpousePersonalId() {
		try {
			return getApplicantServices().getSpousePersonalId();
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error getting spouse personal id",
					e);
		}
		return CoreConstants.EMPTY;
	}

	@Override
	public String getSpouseTelephone() {
		try {
			return getApplicantServices().getSpouseTelephone();
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error getting spouse telephone", e);
		}
		return CoreConstants.EMPTY;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see is.idega.block.family.business.FamilyHelper#getNameOfChild(
	 * java.lang.String)
	 */
	@Override
	public String getName(String userId) {
		User user = getUser(userId);

		if (user == null) {
			return CoreConstants.EMPTY;
		}

		return user.getName();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see is.idega.block.family.business.FamilyHelper#getSocialCodeOfChild(
	 * java.lang.String)
	 */
	@Override
	public String getSocialSecurityCode(String userId) {
		User user = getUser(userId);

		if (user == null) {
			return CoreConstants.EMPTY;
		}

		return user.getPersonalID();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see is.idega.block.family.business.FamilyHelper#getMainAddressOfChild(
	 * java.lang.String)
	 */
	@Override
	public String getAddress(String userId) {
		Address address = getAddressByUserId(userId);

		if (address == null) {
			return CoreConstants.EMPTY;
		}

		String tmpString = null;
		String addressString = null;

		tmpString = address.getName();
		if (tmpString != null) {
			addressString = tmpString + CoreConstants.COMMA + CoreConstants.SPACE;
			tmpString = null;
		}

		PostalCode postalCode = address.getPostalCode();
		if (postalCode != null) {
			tmpString = postalCode.getPostalCode();
		}

		if (tmpString != null) {
			addressString = addressString + tmpString + CoreConstants.SPACE;
		}

		return addressString;
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.block.family.business.FamilyHelper#getAddressByUserId(java.lang.String)
	 */
	@Override
	public Address getAddressByUserId(String userId) {
		User user = getUser(userId);

		if (user == null) {
			return null;
		}

		Address address = null;

		try {
			address = user.getUsersMainAddress();
		} catch (EJBException e) {
			getLogger().log(Level.WARNING, "Adress not found.");
			return null;
		} catch (RemoteException e) {
			getLogger().log(Level.WARNING, "Adress not found.", e);
			return null;
		}

		return address;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see is.idega.block.family.business.FamilyHelper#getPostalCodeOfChild(
	 * java.lang.String)
	 */
	@Override
	public String getPostalCode(String userId) {
		User user = getUser(userId);

		if (user == null) {
			return CoreConstants.EMPTY;
		}

		Address address = null;

		try {
			address = user.getUsersMainAddress();
		} catch (EJBException e) {
			getLogger().log(Level.WARNING, "Postal code not found");
			return CoreConstants.EMPTY;
		} catch (RemoteException e) {
			getLogger().log(Level.WARNING, "Postal code not found", e);
			return CoreConstants.EMPTY;
		}

		if (address == null) {
			return CoreConstants.EMPTY;
		}

		PostalCode postalCode = address.getPostalCode();

		if (postalCode == null) {
			return CoreConstants.EMPTY;
		}

		return postalCode.getPostalCode();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see is.idega.block.family.business.FamilyHelper#getHomeTelephoneOfChild(
	 * java.lang.String)
	 */
	@Override
	public String getHomePhoneNumber(String userId) {
		User user = getUser(userId);

		if (user == null) {
			return CoreConstants.EMPTY;
		}

		try {
			Phone phone = user.getUsersHomePhone();

			if (phone == null) {
				return CoreConstants.EMPTY;
			}

			return phone.getNumber();

		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Phone number not found.", e);
			return CoreConstants.EMPTY;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see is.idega.block.family.business.FamilyHelper#
	 * getMobileTelephoneOfChild(java.lang.String)
	 */
	@Override
	public String getCellPhoneNumber(String userId) {
		User user = getUser(userId);

		if (user == null) {
			return CoreConstants.EMPTY;
		}

		try {
			Phone phone = user.getUsersMobilePhone();

			if (phone == null) {
				return CoreConstants.EMPTY;
			}

			return phone.getNumber();
		} catch (EJBException e) {
			getLogger().log(Level.WARNING, "Phone number not found.");
			return CoreConstants.EMPTY;
		} catch (RemoteException e) {
			getLogger().log(Level.WARNING, "Phone number not found.", e);
			return CoreConstants.EMPTY;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * is.idega.block.family.business.FamilyHelper#getWorkPhoneNumber(java.lang
	 * .String)
	 */
	@Override
	public String getWorkPhoneNumber(String userId) {
		User user = getUser(userId);

		if (user == null) {
			return CoreConstants.EMPTY;
		}

		try {
			Phone phone = user.getUsersWorkPhone();

			if (phone == null) {
				return CoreConstants.EMPTY;
			}

			return phone.getNumber();
		} catch (EJBException e) {
			getLogger().log(Level.WARNING, "Phone number not found.");
			return CoreConstants.EMPTY;
		} catch (RemoteException e) {
			getLogger().log(Level.WARNING, "Phone number not found.", e);
			return CoreConstants.EMPTY;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see is.idega.block.family.business.FamilyHelper#getEMailOfChild(
	 * java.lang.String)
	 */
	@Override
	public String getEMailAddress(String userId) {
		User user = getUser(userId);

		if (user == null) {
			return CoreConstants.EMPTY;
		}

		try {
			Email email = user.getUsersEmail();

			if (email == null) {
				return CoreConstants.EMPTY;
			}

			return email.getEmailAddress();
		} catch (EJBException e) {
			getLogger().log(Level.WARNING, "e-mail address not found.");
			return CoreConstants.EMPTY;
		} catch (RemoteException e) {
			getLogger().log(Level.WARNING, "e-mail address not found.", e);
			return CoreConstants.EMPTY;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see is.idega.block.family.business.FamilyHelper#
	 * getLanguageSelectionMapOfCurrentUser()
	 */
	@Override
	public Map<Locale, Map<String, String>> getLanguages() {
		IWResourceBundle iwrb = getResourceBundle(getBundle(FamilyConstants.IW_BUNDLE_IDENTIFIER));
		if (iwrb == null)
			return Collections.emptyMap();

		Map<String, String> languagesMap = new LinkedHashMap<String, String>();
		Map<Locale, Map<String, String>> languageSelection = new HashMap<Locale, Map<String, String>>();

		languagesMap.put(CoreConstants.EMPTY, iwrb.getLocalizedString("select_your_language", "Select your language"));
		Locale currentLocale = getCurrentLocale();
		languageSelection.put(currentLocale, languagesMap);

		Locale[] locales = Locale.getAvailableLocales();
		if (ArrayUtil.isEmpty(locales))
			return languageSelection;

		List<AdvancedProperty> languages = new ArrayList<AdvancedProperty>();
		for (Locale locale: locales)
			languages.add(new AdvancedProperty(locale.getLanguage(), locale.getDisplayLanguage(locale)));
		doSortValues(languages, languagesMap, currentLocale);

		return languageSelection;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see is.idega.block.family.business.FamilyHelper#
	 * getLanguageOfCurrentUser(int)
	 */
	@Override
	public String getLanguage(int numberOfLanguage, String userId) {
		User user = getUser(userId);

		if (user == null) {
			return CoreConstants.EMPTY;
		}

		List<ICLanguage> languageList = null;
		try {
			languageList = new ArrayList<ICLanguage>(user.getLanguages());
		} catch (IDORelationshipException e) {
			return CoreConstants.EMPTY;
		}

		if (ListUtil.isEmpty(languageList))
			return CoreConstants.EMPTY;

		ICLanguage icl = languageList.get(numberOfLanguage);
		if (icl == null)
			return CoreConstants.EMPTY;

		return icl.getIsoAbbreviation();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see is.idega.block.family.business.FamilyHelper#
	 * getMotherLanguageSelectionMapOfCurrentUser()
	 */
	@Override
	public String getMotherLanguage(String userId) {
		User user = getUser(userId);
		if (user == null)
			return CoreConstants.EMPTY;

		String prefferedLocale = user.getPreferredLocale();

		if (StringUtil.isEmpty(prefferedLocale))
			return getLanguage(0, userId);

		Locale locale = ICLocaleBusiness.getLocaleFromLocaleString(prefferedLocale);
		if (locale == null)
			return CoreConstants.EMPTY;

		ICLanguage icl = null;
		try {
			ICLanguageHome icLanguageHome = (ICLanguageHome) IDOLookup.getHome(ICLanguage.class);
			icl = icLanguageHome.findByISOAbbreviation(locale.getLanguage());
		} catch (IDOLookupException e) {
			getLogger().log(Level.WARNING, "Unable to get languages service.");
			return CoreConstants.EMPTY;
		} catch (FinderException e) {
			getLogger().log(Level.WARNING, "Language not found.");
			return CoreConstants.EMPTY;
		}

		if (icl == null)
			return CoreConstants.EMPTY;

		return icl.getIsoAbbreviation();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see is.idega.block.family.business.FamilyHelper#
	 * getSecondLanguageOfCurrentUser()
	 */
	@Override
	public String getSecondLanguage(String userId) {
		return getLanguage(1, userId);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see is.idega.block.family.business.FamilyHelper#
	 * getThirdLanguageOfCurrentUser()
	 */
	@Override
	public String getThirdLanguage(String userId) {
		return getLanguage(2, userId);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see is.idega.block.family.business.FamilyHelper#
	 * getFourthLanguageOfCurrentUser()
	 */
	@Override
	public String getFourthLanguage(String userId) {
		return getLanguage(3, userId);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see is.idega.block.family.business.FamilyHelper#
	 * getCountrySelectionMapOfCurrentUser()
	 */
	@Override
	public Map<Locale, Map<String, String>> getCountries() {
		IWResourceBundle iwrb = getResourceBundle(getBundle(FamilyConstants.IW_BUNDLE_IDENTIFIER));

		if (iwrb == null)
			return Collections.emptyMap();

		Map<String, String> countryMap = new LinkedHashMap<String, String>();

		countryMap.put(CoreConstants.EMPTY, iwrb.getLocalizedString("select_your_country", "Select your country"));

		Map<Locale, Map<String, String>> countrySelection = new HashMap<Locale, Map<String, String>>();
		Locale currentLocale = getCurrentLocale();
		countrySelection.put(currentLocale, countryMap);

		Locale[] locales = Locale.getAvailableLocales();
		if (ArrayUtil.isEmpty(locales))
			return countrySelection;

		List<AdvancedProperty> countries = new ArrayList<AdvancedProperty>();
		for (Locale locale: locales) {
			String country = locale.getCountry();
			if (StringUtil.isEmpty(country))
				continue;

			countries.add(new AdvancedProperty(country, locale.getDisplayCountry(locale)));
		}
		doSortValues(countries, countryMap, currentLocale);

		return countrySelection;
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.block.family.business.FamilyHelper#getCountriesList()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection<Country> getCountriesList() {
		Collection<Country> countryCollection = null;
		try {
			CountryHome countryHome = (CountryHome) IDOLookup.getHome(Country.class);
			countryCollection = countryHome.findAll();
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Unable to get list of countries.", e);
			return null;
		}

		return countryCollection;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * is.idega.block.family.business.FamilyHelper#getCountry(java.lang.String)
	 */
	@Override
	public Country getCountry(String userId) {
		try {
			Address address = getAddressByUserId(userId);

			if (address == null) {
				return null;
			}

			return address.getCountry();
		} catch (EJBException e) {
			getLogger().log(Level.WARNING, "Unable to get country of user");
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.block.family.business.FamilyHelper#getCountryName(java.lang.String)
	 */
	@Override
	public String getCountryName(String userId) {
		Country country = getCountry(userId);
		if (country == null) {
			return CoreConstants.EMPTY;
		}

		return country.getName();
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.block.family.business.FamilyHelper#getCountryISOgetIsoAbbreviation(java.lang.String)
	 */
	@Override
	public String getCountryISOgetIsoAbbreviation(String userId) {
		Country country = getCountry(userId);
		if (country == null) {
			return CoreConstants.EMPTY;
		}

		return country.getIsoAbbreviation();
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.block.family.business.FamilyHelper#getCity(java.lang.String)
	 */
	@Override
	public String getCity(String userId) {
		Address address = getAddressByUserId(userId);

		if (address == null) {
			return CoreConstants.EMPTY;
		}

		return address.getCity();
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.block.family.business.FamilyHelper#getRelation(java.lang.String, java.lang.String)
	 */
	@Override
	public String getRelation(String childId, String relatedUserId) {
		User user = getUser(childId);
		User userRelation = getUser(relatedUserId);

		if (user == null || userRelation == null) {
			return CoreConstants.EMPTY;
		}

		FamilyLogic familyLogic = getServiceInstance(FamilyLogic.class);

		if (familyLogic == null) {
			return CoreConstants.EMPTY;
		}

		com.idega.user.data.Gender gender = userRelation.getGender();

		try {
			if (familyLogic.isChildOf(user, userRelation)) {
				if (gender == null) {
					return CoreConstants.EMPTY;
				} else if (gender.isMaleGender()) {
					return FamilyConstants.RELATION_FATHER;
				} else if (gender.isFemaleGender()) {
					return FamilyConstants.RELATION_MOTHER;
				}
			}
		} catch (RemoteException e) {
			getLogger().log(Level.WARNING, "Relation not found.", e);
			return CoreConstants.EMPTY;
		}

		try {
			if (familyLogic.isCustodianOf(userRelation, user)) {
				if (gender == null) {
					return FamilyConstants.RELATION_GUARDIAN;
				} else if (gender.isMaleGender()) {
					return FamilyConstants.RELATION_STEPFATHER;
				} else if (gender.isFemaleGender()) {
					return FamilyConstants.RELATION_STEPMOTHER;
				}
			}
		} catch (RemoteException e) {
			getLogger().log(Level.WARNING, "Relation not found.", e);
			return CoreConstants.EMPTY;
		}

		try {
			if (familyLogic.isSiblingOf(userRelation, user)) {
				return FamilyConstants.RELATION_SIBLING;
			}
		} catch (RemoteException e) {
			getLogger().log(Level.WARNING, "Relation not found.", e);
			return CoreConstants.EMPTY;
		}

		return CoreConstants.EMPTY;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see is.idega.block.family.business.FamilyHelper#
	 * getRelationOfCurrentUser()
	 */
	@Override
	public Map<Locale, Map<String, String>> getRelationNames() {
		IWResourceBundle iwrb = getResourceBundle(getBundle(FamilyConstants.IW_BUNDLE_IDENTIFIER));

		if (iwrb == null)
			return Collections.emptyMap();

		Map<String, String> availableRelations = new LinkedHashMap<String, String>();
		availableRelations.put(CoreConstants.EMPTY, iwrb.getLocalizedString("select_your_relation",	"Select your relation"));

		List<AdvancedProperty> relations = new ArrayList<AdvancedProperty>();
		relations.add(new AdvancedProperty(FamilyConstants.RELATION_MOTHER, iwrb.getLocalizedString("mother", "Mother")));
		relations.add(new AdvancedProperty(FamilyConstants.RELATION_FATHER, iwrb.getLocalizedString("father", "Father")));
		relations.add(new AdvancedProperty(FamilyConstants.RELATION_STEPMOTHER, iwrb.getLocalizedString("stepmother", "Stepmother")));
		relations.add(new AdvancedProperty(FamilyConstants.RELATION_STEPFATHER, iwrb.getLocalizedString("stepfather", "Stepfather")));
		relations.add(new AdvancedProperty(FamilyConstants.RELATION_GRANDMOTHER, iwrb.getLocalizedString("grandmother",	"Grandmother")));
		relations.add(new AdvancedProperty(FamilyConstants.RELATION_GRANDFATHER, iwrb.getLocalizedString("grandfather",	"Grandfather")));
		relations.add(new AdvancedProperty(FamilyConstants.RELATION_SIBLING, iwrb.getLocalizedString("sibling",	"Sibling")));
		relations.add(new AdvancedProperty(FamilyConstants.RELATION_GUARDIAN, iwrb.getLocalizedString("guardian", "Guardian")));
		relations.add(new AdvancedProperty(FamilyConstants.RELATION_OTHER, iwrb.getLocalizedString("other", "Other")));

		Map<Locale, Map<String, String>> relationsWithLocale = new HashMap<Locale, Map<String,String>>();
		Locale locale = getCurrentLocale();
		relationsWithLocale.put(locale, availableRelations);

		doSortValues(relations, availableRelations, locale);

		return relationsWithLocale;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * is.idega.block.family.business.FamilyHelper#getMaritalStatusOfCurrentUser
	 * ()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String getMaritalStatus(String userId) {
		IWResourceBundle iwrb = getResourceBundle(getBundle(FamilyConstants.IW_BUNDLE_IDENTIFIER));

		if (iwrb == null) {
			return CoreConstants.EMPTY;
		}

		FamilyLogic familyLogic = getServiceInstance(FamilyLogic.class);
		User user = getUser(userId);
		boolean childHasOtherParent = false;

		if (familyLogic == null) {
			return CoreConstants.EMPTY;
		}

		if (user == null) {
			return CoreConstants.EMPTY;
		}

		Collection<User> childrenOfCurrentUser = null;

		Collection<User> parentsOfChild = null;

		try {
			if (familyLogic.hasPersonGotSpouse(user)) {
				return iwrb.getLocalizedString(FamilyConstants.MARITAL_STATUS_MARRIED, "Married");
			}

			if (familyLogic.hasPersonGotCohabitant(user)) {
				return iwrb.getLocalizedString(FamilyConstants.MARITAL_STATUS_COHABITANT, "Cohabitant");
			}

			childrenOfCurrentUser = familyLogic.getChildrenFor(user);

			if (ListUtil.isEmpty(childrenOfCurrentUser)) {
				return iwrb.getLocalizedString(FamilyConstants.MARITAL_STATUS_SINGLE, "Single");
			}

			List<User> childList = new ArrayList<User>(childrenOfCurrentUser);
			parentsOfChild = familyLogic.getParentsFor(childList.get(0));
		} catch (RemoteException e) {
			getLogger().log(Level.WARNING, "Unable to get info about spouse.",
					e);
			return CoreConstants.EMPTY;
		} catch (NoParentFound e) {
			getLogger().log(Level.SEVERE, "Something went very wrong.", e);
			return CoreConstants.EMPTY;
		} catch (NoChildrenFound e) {
			return iwrb.getLocalizedString(FamilyConstants.MARITAL_STATUS_SINGLE, "Single");
		}

		for (User parent : parentsOfChild) {
			if (!parent.equals(user)) {
				childHasOtherParent = true;
			}
		}

		if (childHasOtherParent) {
			return iwrb.getLocalizedString(FamilyConstants.MARITAL_STATUS_DIVORCED, "Divorced");
		} else {
			return iwrb.getLocalizedString(FamilyConstants.MARITAL_STATUS_SINGLE, "Single");
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see is.idega.block.family.business.FamilyHelper#getCurrentParent()
	 */
	@Override
	public String getCurrentParent(String childId) {
		if (isWrongId(childId)) {
			return CoreConstants.EMPTY;
		}

		User currentUser = getCurrentUser();

		if (currentUser == null) {
			currentUser = getCurrentUser();
		}

		if (currentUser == null) {
			return CoreConstants.EMPTY;
		}

		Collection<User> parents = getParents(childId);

		for (User parent : parents) {
			if (currentUser.equals(parent)) {
				return currentUser.getId();
			}
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * is.idega.block.family.business.FamilyHelper#getAnotherParent(java.lang
	 * .String)
	 */
	@Override
	public String getAnotherParent(String childId) {
		User user = getUser(childId);

		if (user == null) {
			return CoreConstants.EMPTY;
		}

		Collection<User> parents = getParents(childId);

		if (parents == null) {
			return CoreConstants.EMPTY;
		}

		User currentParent = getUser(getCurrentParent(childId));

		if (currentParent == null) {
			return CoreConstants.EMPTY;
		}

		for (User parent : parents) {
			if (!currentParent.equals(parent)) {
				return parent.getId();
			}
		}

		return CoreConstants.EMPTY;
	}

	private FamilyRelationsHelper getApplicantServices() {
		FamilyRelationsHelper applicantServices = null;

		try {
			applicantServices = ELUtil.getInstance().getBean(
					FamilyRelationsHelperImpl.BEAN_IDENTIFIER);
		} catch (Exception e) {
			getLogger().log(Level.WARNING,
					"Error getting bean " + FamilyRelationsHelper.class, e);
		}
		return applicantServices;
	}

	/**
	 * <p>Parents of user.</p>
	 * @param userId some {@link User#getPrimaryKey()}, who's parents yout want to get.
	 * @return parents in {@link User} form.
	 */
	@SuppressWarnings("unchecked")
	private Collection<User> getParents(String userId) {
		FamilyLogic familyLogic = getServiceInstance(FamilyLogic.class);
		User user = getUser(userId);
		Collection<User> parents = null;

		if (user == null) {
			return null;
		}

		try {
			parents = familyLogic.getParentsFor(user);
			return parents;
		} catch (NoParentFound e) {
			getLogger().log(Level.WARNING, "Parent not found");
			return null;
		} catch (RemoteException e) {
			getLogger().log(Level.WARNING, "Unable to get parents of user.", e);
			return null;
		}

	}

	/**
	 * @return {@link com.idega.user.business.UserBusinessBean}
	 */
	private UserBusiness getUserBusiness() {
		return getServiceInstance(UserBusiness.class);
	}

	/**
	 * @param id {@link String} of {@link User#getPrimaryKey()}
	 * @return {@link User} by id.
	 */
	private User getUser(String id) {
		if (isWrongId(id)) {
			return null;
		}

		try {
			return getUserBusiness().getUser(Integer.valueOf(id));
		} catch (RemoteException e) {
			getLogger().log(Level.WARNING, "Unable to get user by ID: " + id);
			return null;
		} catch (NumberFormatException e) {
			getLogger().log(Level.WARNING, "No such user. ID: " + id);
			return null;
		}
	}

	/**
	 * <p>Helping method, checks for wrong id.</p>
	 * @param id of something.
	 * @return <code>true</code> if id does not contain {, }, CoreConstants#EMPTY, -1.
	 * <code>false</code> otherwise.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	private boolean isWrongId(String id){
		if (StringUtil.isEmpty(id)) {
			return Boolean.TRUE;
		}

		if (id.contains(CoreConstants.CURLY_BRACKET_LEFT)
				|| id.contains(CoreConstants.CURLY_BRACKET_RIGHT)) {
			return Boolean.TRUE;
		}

		if (id.equals("-1")) {
			return Boolean.TRUE;
		}

		return Boolean.FALSE;
	}
}
