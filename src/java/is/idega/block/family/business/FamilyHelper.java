package is.idega.block.family.business;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.idega.builder.bean.AdvancedProperty;
import com.idega.core.localisation.data.ICLanguage;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.Country;
import com.idega.core.location.data.PostalCode;
import com.idega.core.user.data.User;
import com.idega.util.CoreConstants;

/**
 * Spring bean to provide services from {@link FamilyLogic}
 *
 * @author <a href="mailto:valdas@idega.com">Valdas Žemaitis</a>
 * @version $Revision: 1.0 $
 *
 *          Last modified: $Date: 2009.09.29 16:25:26 $ by: $Author: valdas $
 */
public interface FamilyHelper {

	public static final String BEAN_IDENTIFIER = "familyLogicServicesProvider";

	/**
	 *
	 * @return 	{@link Map} of ({@link User#getID()},
	 * 			{@link User#getName()}) with {@link CoreConstants.EMPTY}, Select..." choice,
	 * 			{@link Map} with {@link CoreConstants.EMPTY}, Select..."
	 * 			choice, if records were not found or not read,
	 * 			{@link Collections#emptyMap()} if failed.
	 */
	public Map<Locale, Map<String, String>> getTeenagersOfCurrentUser();

	public String getSpouseName();

	public String getSpousePersonalId();

	public String getSpouseTelephone();

	public String getSpouseMobile();

	public String getSpouseEmail();

	/**
	 * <p>
	 * Method for getting name of {@link User}.
	 * </p>
	 *
	 * @param userId
	 *            Id of user {@link com.idega.user.data.User#getId()}.
	 * @return {@link User#getName()}, {@link CoreConstants#EMPTY} if does not
	 *         exist.
	 */
	public String getName(String userId);

	/**
	 * <p>
	 * Method for getting social security code {@link User}.
	 * </p>
	 *
	 * @param userId
	 *            Id of user {@link com.idega.user.data.User#getId()}.
	 * @return {@link User#getPersonalID()}, {@link CoreConstants#EMPTY} if does
	 *         not exist.
	 */
	public String getSocialSecurityCode(String userId);

	/**
	 * <p>
	 * Method for getting main address of {@link User}. Address is made of room
	 * number, house number, street, city or village, region, country.
	 * </p>
	 *
	 * @param userId
	 *            Id of user {@link com.idega.user.data.User#getId()}.
	 * @return Address of {@link User}, {@link CoreConstants#EMPTY} if
	 *         does not exist.
	 */
	public String getAddress(String userId);

	/**
	 * <p>
	 * Method for getting main address of {@link User}. {@link Address} is made of room
	 * number, house number, street, city or village, region, country and so on...
	 * </p>
	 *
	 * @param userId
	 *            Id of user {@link com.idega.user.data.User#getId()}.
	 * @return {@link Address} of {@link User}, <code>null</code> if
	 *         does not exist.
	 */
	public Address getAddressByUserId(String userId);

	/**
	 * <p>Gets name of city {@link User} live in.</p>
	 * @param userId {@link User#getPrimaryKey()}.
	 * @return {@link Address#getCity()} or <code>null</code> on failure.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	public String getCity(String userId);

	/**
	 * <p>
	 * Method for getting postal code of child of {@link User}.
	 * </p>
	 *
	 * @param userId
	 *            Id of user {@link com.idega.user.data.User#getId()}.
	 * @return {@link PostalCode#getPostalCode} of user,
	 *         {@link CoreConstants#EMPTY} if do not exist.
	 */
	public String getPostalCode(String userId);

	/**
	 * <p>
	 * Method for getting main phone number of {@link User}.
	 * </p>
	 *
	 * @param userId
	 *            Id of user {@link com.idega.user.data.User#getId()}.
	 * @return {@link com.idega.user.data.User#getUsersHomePhone().getNumber()},
	 *         {@link CoreConstants#EMPTY} if do not exist.
	 */
	public String getHomePhoneNumber(String userId);

	/**
	 * <p>
	 * Method for getting cell phone number of {@link User}.
	 * </p>
	 *
	 * @param userId
	 *            Id of user {@link com.idega.user.data.User#getId()}.
	 * @return {@link com.idega.user.data.User#getUsersMobilePhone()
	 *         .getNumber()}, {@link CoreConstants#EMPTY} if do not exist.
	 */
	public String getCellPhoneNumber(String userId);

	/**
	 * <p>
	 * Method for getting work phone number of {@link User}.
	 * </p>
	 *
	 * @param userId
	 *            Id of user {@link com.idega.user.data.User#getId()}.
	 * @return {@link com.idega.user.data.User#getUsersWorkPhone().getNumber()},
	 *         {@link CoreConstants#EMPTY} if do not exist.
	 */
	public String getWorkPhoneNumber(String userId);

	/**
	 * <p>
	 * Method for getting e-mail address of {@link User}.
	 * </p>
	 *
	 * @param userId
	 *            Id of user {@link com.idega.user.data.User#getId()}.
	 * @return {@link com.idega.user.data.User#getUsersEmail()
	 *         #getEmailAddress()}, {@link CoreConstants#EMPTY} if do not exist.
	 */
	public String getEMailAddress(String userId);

	/**
	 * <p>
	 * Method for getting relations from {@link FamilyConstants}.
	 * </p>
	 *
	 * @return	Relation {@link Map} from {@link FamilyConstants},
	 * 			{@link Map} with {@link CoreConstants.EMPTY}, Select..."
	 * 			choice, if records were not found or read.
	 * 			{@link Collections#emptyMap()} if failed.
	 */
	public Map<Locale, Map<String, String>> getRelationNames();

	/**
	 * <p>
	 * Method, for getting relation type from {@link FamilyConstants}.
	 * </p>
	 *
	 * @param childId
	 *            Child, smaller brother or other user
	 *            {@link com.idega.user.data.User#getId()}.
	 * @param relatedUserId
	 *            Parent, sibling etc. {@link com.idega.user.data.User#getId()}.
	 * @return Relation from {@link FamilyConstants}, -1 if not found.
	 */
	public String getRelation(String childId, String relatedUserId);

	/**
	 * <p>
	 * Method for getting all languages exits in database.
	 * </p>
	 *
	 * @return 	{@link Map} of ({@link ICLanguage#getIsoAbbreviation()},
	 * 			{@link ICLanguage#getName()}).
	 * 			{@link Map} with {@link CoreConstants.EMPTY}, Select..."
	 * 			choice, if records were not found or not read,
	 * 			{@link Collections#emptyMap()} if failed.
	 */
	public Map<Locale, Map<String, String>> getLanguages();

	/**
	 * <p>
	 * Method for getting {@link ICLanguage#getIsoAbbreviation()}
	 * of {@link User#getLanguages()}.
	 * </p>
	 *
	 * @param numberOfLanguage
	 *            Language number in {@link User#getLanguages()}.
	 * @param userId
	 *            Id of user {@link com.idega.user.data.User#getId()}.
	 * @return 	Language from {@link User#getLanguages()},
	 * 			{@link CoreConstants#EMPTY} if does not exist.
	 */
	public String getLanguage(int numberOfLanguage, String userId);

	/**
	 * <p>
	 * </p>
	 *
	 * @param userId
	 *            Id of user {@link com.idega.user.data.User#getId()}.
	 * @return {@link ICLanguage} from {@link User#getPreferredLocale} or first
	 *         {@link ICLanguage} from
	 *         {@link FamilyHelper#getLanguage(int, String)}.
	 *         {@link CoreConstants#EMPTY}, if no preferred {@link Locale} or
	 *         {@link ICLanguage} was found.
	 */
	public String getMotherLanguage(String userId);

	/**
	 * @param userId
	 *            Id of user {@link com.idega.user.data.User#getId()}.
	 * @see is.idega.block.family.business.FamilyHelper#getLanguage(int, String)
	 * @return Language from {@link FamilyHelper#getLanguage(int, String)} with
	 *         number 1.
	 */
	public String getSecondLanguage(String userId);

	/**
	 * @param userId
	 *            Id of user {@link com.idega.user.data.User#getId()}.
	 * @see is.idega.block.family.business.FamilyHelper#getLanguage(int, String)
	 * @return Language from {@link FamilyHelper#getLanguage(int, String)} with
	 *         number 2.
	 */
	public String getThirdLanguage(String userId);

	/**
	 * @param userId
	 *            Id of user {@link com.idega.user.data.User#getId()}.
	 * @see is.idega.block.family.business.FamilyHelper#getLanguage(int, String)
	 * @return Language from {@link FamilyHelper#getLanguage(int, String)}. with
	 *         number 3.
	 */
	public String getFourthLanguage(String userId);

	/**
	 * <p>
	 * Find a country, where user lives in.
	 * </p>
	 *
	 * @param userId
	 *            Id of user {@link com.idega.user.data.User#getId()}.
	 * @return {@link Country} or <code>null</code> on failure.
	 */
	public Country getCountry(String userId);

	/**
	 * <p>
	 * Find a country, where user lives in.
	 * </p>
	 *
	 * @param userId
	 *            Id of user {@link com.idega.user.data.User#getId()}.
	 * @return {@link Country#getName()} or
	 *         {@link CoreConstants#EMPTY} if country does not found.
	 */
	public String getCountryName(String userId);

	/**
	 * <p>
	 * Find a country, where user lives in.
	 * </p>
	 *
	 * @param userId
	 *            Id of user {@link com.idega.user.data.User#getId()}.
	 * @return {@link Country#getIsoAbbreviation()} or
	 *         {@link CoreConstants#EMPTY} if country does not found.
	 */
	public String getCountryISOgetIsoAbbreviation(String userId);

	/**
	 * <p>
	 * Method for getting all countries, which exits in database.
	 * </p>
	 *
	 * @return 	{@link Map} of ({@link Country#getIsoAbbreviation()},
	 * 			{@link Country#getName()}).
	 * 			{@link Map} with {@link CoreConstants.EMPTY}, Select..."
	 * 			choice, if records were not found or not read,
	 * 			{@link Collections#emptyMap()} if failed.
	 */
	public Map<Locale, Map<String, String>> getCountries();

	/**
	 * <p>
	 * Method for getting all countries, which exits in database.
	 * </p>
	 *
	 * @return {@link Collection} of {@link Country}, null if failed.
	 */
	public Collection<Country> getCountriesList();

	/**
	 * <p>
	 * Method for getting marital status.
	 * </p>
	 *
	 * @param userId
	 *            Id of user {@link com.idega.user.data.User#getId()}.
	 * @return Possible marital statuses are detailed at {@link FamilyConstants}
	 *         or {@link CoreConstants#EMPTY} if not found.
	 */
	public String getMaritalStatus(String userId);

	/**
	 * <p>
	 * Method for getting id of parent, who is connected now or is parent.
	 * </p>
	 *
	 * @param childId
	 *            Id of child user {@link com.idega.user.data.User#getId()}.
	 * @return Id of father, mother or somebody, who is the parent of child:
	 *         {@link com.idega.user.data.User#getId()}.
	 */
	public String getCurrentParent(String childId);

	/**
	 * <p>
	 * Method for getting id of another parent. Not the one, who is connected
	 * now, or already been chosen.
	 * </p>
	 *
	 * @param childId
	 *            Id of child user {@link com.idega.user.data.User#getId()}.
	 * @return Id of father, mother or somebody, who is the parent of child:
	 *         {@link com.idega.user.data.User#getId()}.
	 */
	public String getAnotherParent(String childId, String currentParentPersonalId);

	public List<AdvancedProperty> getTeenagersForParentByPersonalId(String personalId);

}
