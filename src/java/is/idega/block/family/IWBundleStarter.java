package is.idega.block.family;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;
import com.idega.repository.data.ImplementorRepository;
import com.idega.user.data.GroupRelationType;
import com.idega.user.data.GroupRelationTypeHome;
import com.idega.user.presentation.LinkToFamilyLogic;
import com.idega.util.CoreConstants;

import is.idega.block.family.business.LinkToFamilyLogicImpl;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Jun 10, 2004
 */
public class IWBundleStarter implements IWBundleStartable {

	@Override
	public void start(IWBundle starterBundle) {
		insertStartData();
		ImplementorRepository repository = ImplementorRepository.getInstance();
		repository.addImplementor(LinkToFamilyLogic.class, LinkToFamilyLogicImpl.class);
	}

	@Override
	public void stop(IWBundle starterBundle) {
		// nothing to do
	}

	protected void insertStartData() {
		/*
		 * @todo Move to user plugin system
		 **/
		insertGroupRelationType(CoreConstants.GROUP_RELATION_FAMILY);
		insertGroupRelationType(CoreConstants.FAM_RELATION_CHILD);
		insertGroupRelationType(CoreConstants.FAM_RELATION_PARENT);
		insertGroupRelationType(CoreConstants.FAM_RELATION_SPOUSE);
		insertGroupRelationType(CoreConstants.FAM_RELATION_COHABITANT);
	}

	private void insertGroupRelationType(String groupRelationType) {
		/**
		 * @todo Move this to a more appropriate place
		 **/
		try {
			GroupRelationTypeHome grtHome = (GroupRelationTypeHome) com.idega.data.IDOLookup.getHome(GroupRelationType.class);
			GroupRelationType grType;
			try {
				grType = grtHome.findByPrimaryKey(groupRelationType);
			}
			catch (FinderException fe) {
				try {
					grType = grtHome.create();
					grType.setType(groupRelationType);
					grType.store();
//					sendStartMessage("Registered Group relation type: '" + groupRelationType + "'");
				}
				catch (CreateException ce) {
					ce.printStackTrace();
				}
			}
		}
		catch (IDOLookupException ile) {
			ile.printStackTrace();
		}
	}

}
