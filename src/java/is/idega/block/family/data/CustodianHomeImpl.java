/*
 * $Id$
 * Created on Apr 1, 2006
 *
 * Copyright (C) 2006 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.block.family.data;



import javax.ejb.FinderException;

import com.idega.data.IDOFactory;
import com.idega.user.data.Group;


/**
 * <p>
 * TODO laddi Describe Type CustodianHomeImpl
 * </p>
 *  Last modified: $Date$ by $Author$
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision$
 */
public class CustodianHomeImpl extends IDOFactory implements CustodianHome {

	protected Class getEntityInterfaceClass() {
		return Custodian.class;
	}

	public Custodian create() throws javax.ejb.CreateException {
		return (Custodian) super.createIDO();
	}

	public Custodian findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (Custodian) super.findByPrimaryKeyIDO(pk);
	}

	public Custodian findUserForUserGroup(Group group) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((CustodianBMPBean) entity).ejbFindUserForUserGroup(group);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

}
