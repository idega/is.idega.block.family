/*
 * $Id: FamilyLogicHomeImpl.java,v 1.5 2006/03/30 07:27:06 laddi Exp $
 * Created on Mar 29, 2006
 *
 * Copyright (C) 2006 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.block.family.business;




import com.idega.business.IBOHomeImpl;


/**
 * <p>
 * TODO laddi Describe Type FamilyLogicHomeImpl
 * </p>
 *  Last modified: $Date: 2006/03/30 07:27:06 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.5 $
 */
public class FamilyLogicHomeImpl extends IBOHomeImpl implements FamilyLogicHome {

	protected Class getBeanInterfaceClass() {
		return FamilyLogic.class;
	}

	public FamilyLogic create() throws javax.ejb.CreateException {
		return (FamilyLogic) super.createIBO();
	}

}
