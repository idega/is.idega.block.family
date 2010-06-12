package is.idega.block.family.business;

import java.util.Locale;
import java.util.Map;

/**
 * Spring bean to provide services from {@link FamilyLogic}
 * 
 * @author <a href="mailto:valdas@idega.com">Valdas Žemaitis</a>
 * @version $Revision: 1.0 $
 *
 * Last modified: $Date: 2009.09.29 16:25:26 $ by: $Author: valdas $
 */
public interface FamilyHelper {
	
	public static final String BEAN_IDENTIFIER = "familyLogicServicesProvider";
	
	public  Map<Locale, Map<String, String>> getTeenagesOfCurrentUser();

	public String getSpouseName();
	
	public String getSpousePersonalId();
	
	public String getSpouseTelephone();
	
	public String getSpouseMobile();
	
	public String getSpouseEmail();
}