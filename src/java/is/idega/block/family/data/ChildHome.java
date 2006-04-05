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



import javax.ejb.FinderException;

import com.idega.data.IDOHome;
import com.idega.user.data.Group;


/**
 * <p>
 * TODO laddi Describe Type ChildHome
 * </p>
 *  Last modified: $Date$ by $Author$
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision$
 */
public interface ChildHome extends IDOHome {

	public Child create() throws javax.ejb.CreateException;

	public Child findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see is.idega.block.family.data.ChildBMPBean#ejbFindUserForUserGroup
	 */
	public Child findUserForUserGroup(Group group) throws FinderException;

}
