package is.idega.block.family.business.impl;

import is.idega.block.family.business.FamilyHelper;
import is.idega.block.family.business.FamilyLogic;
import is.idega.block.family.business.NoChildrenFound;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.idega.core.business.DefaultSpringBean;
import com.idega.user.data.User;
import com.idega.util.ListUtil;

/**
 * Implementation for {@link FamilyHelper}
 * 
 * @author <a href="mailto:valdas@idega.com">Valdas Žemaitis</a>
 * @version $Revision: 1.0 $
 *
 * Last modified: $Date: 2009.09.30 18:43:56 $ by: $Author: valdas $
 */
@Scope(BeanDefinition.SCOPE_SINGLETON)
@Service(FamilyHelper.BEAN_IDENTIFIER)
public class FamilyHelperImpl extends DefaultSpringBean implements FamilyHelper {

	private static final Logger LOGGER = Logger.getLogger(FamilyHelperImpl.class.getName());
	
	public Map<Locale, Map<String, String>> getTeenagesOfCurrentUser() {
		Map<Locale, Map<String, String>> teenagers = new HashMap<Locale, Map<String,String>>();
		
		User currentUser = getCurrentUser();
		if (currentUser == null) {
			LOGGER.info("User must be logged in!");
			return teenagers;
		}
		
		FamilyLogic familyLogic = getServiceInstance(FamilyLogic.class);
		if (familyLogic == null) {
			return teenagers;
		}
		
		Collection<User> teenagersOfCurrentUser = null;
		try {
			teenagersOfCurrentUser = familyLogic.getChildrenForUserUnderAge(currentUser, 16);
		} catch (NoChildrenFound e) {
			LOGGER.info(currentUser + " doesn't have children");
		} catch (RemoteException e) {
			LOGGER.log(Level.WARNING, "Some error occurred while getting teenagers for: " + currentUser, e);
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Some error occurred while getting teenagers for: " + currentUser, e);
		}
		if (ListUtil.isEmpty(teenagersOfCurrentUser)) {
			return teenagers;
		}
		
		Locale locale = getCurrentLocale();
		Map<String, String> childrenInfo = new HashMap<String, String>();
		teenagers.put(locale, childrenInfo);
		
		for (User teenage: teenagersOfCurrentUser) {
			childrenInfo.put(teenage.getId(), teenage.getName());
		}
		
		return teenagers;
	}
	
}