/*
 * $Id: FamilyLogic.java,v 1.1 2004/08/27 16:15:24 joakim Exp $
 * Created on 27.8.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.block.family.business;

import is.idega.idegaweb.member.business.NoChildrenFound;
import is.idega.idegaweb.member.business.NoCohabitantFound;
import is.idega.idegaweb.member.business.NoCustodianFound;
import is.idega.idegaweb.member.business.NoParentFound;
import is.idega.idegaweb.member.business.NoSiblingFound;
import is.idega.idegaweb.member.business.NoSpouseFound;
import java.rmi.RemoteException;
import java.util.*;
import javax.ejb.*;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.*;


/**
 * 
 *  Last modified: $Date: 2004/08/27 16:15:24 $ by $Author: joakim $
 * 
 * @author <a href="mailto:Joakim@idega.com">Joakim</a>
 * @version $Revision: 1.1 $
 */
public interface FamilyLogic {

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getChildrenFor
	 */
	public Collection getChildrenFor(User user) throws NoChildrenFound, RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getChildrenInCustodyOf
	 */
	public Collection getChildrenInCustodyOf(User user) throws NoChildrenFound, RemoteException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getSiblingsFor
	 */
	public Collection getSiblingsFor(User user) throws NoSiblingFound, RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getSpouseFor
	 */
	public User getSpouseFor(User user) throws NoSpouseFound, java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getCohabitantFor
	 */
	public User getCohabitantFor(User user) throws NoCohabitantFound, java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getCustodiansFor
	 */
	public Collection getCustodiansFor(User user, boolean returnParentsIfNotFound) throws NoCustodianFound,
			RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getCustodiansFor
	 */
	public Collection getCustodiansFor(User user) throws NoCustodianFound, RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getParentsFor
	 */
	public Collection getParentsFor(User user) throws NoParentFound, RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#hasPersonGotChildren
	 */
	public boolean hasPersonGotChildren(User person) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#hasPersonGotSpouse
	 */
	public boolean hasPersonGotSpouse(User person) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#hasPersonGotCohabitant
	 */
	public boolean hasPersonGotCohabitant(User person) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#hasPersonGotSiblings
	 */
	public boolean hasPersonGotSiblings(User person) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#isChildOf
	 */
	public boolean isChildOf(User childToCheck, User parent) throws RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#isChildInCustodyOf
	 */
	public boolean isChildInCustodyOf(User childToCheck, User parent) throws RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#isParentOf
	 */
	public boolean isParentOf(User parentToCheck, User child) throws RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#isCustodianOf
	 */
	public boolean isCustodianOf(User custodianToCheck, User child) throws RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#isSpouseOf
	 */
	public boolean isSpouseOf(User personToCheck, User relatedPerson) throws RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#isCohabitantOf
	 */
	public boolean isCohabitantOf(User personToCheck, User relatedPerson) throws RemoteException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#isSiblingOf
	 */
	public boolean isSiblingOf(User personToCheck, User relatedPerson) throws RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#setAsChildFor
	 */
	public void setAsChildFor(User personToSet, User parent) throws CreateException, RemoteException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#setAsParentFor
	 */
	public void setAsParentFor(User parent, User child) throws CreateException, RemoteException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#setAsCustodianFor
	 */
	public void setAsCustodianFor(User custodian, User child) throws CreateException, RemoteException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#setAsSpouseFor
	 */
	public void setAsSpouseFor(User personToSet, User relatedPerson) throws CreateException, RemoteException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#setAsCohabitantFor
	 */
	public void setAsCohabitantFor(User personToSet, User relatedPerson) throws CreateException, RemoteException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#setAsSiblingFor
	 */
	public void setAsSiblingFor(User personToSet, User relatedPerson) throws CreateException, RemoteException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#removeAsChildFor
	 */
	public void removeAsChildFor(User personToSet, User parent) throws RemoveException, RemoteException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#removeAsParentFor
	 */
	public void removeAsParentFor(User parent, User child) throws RemoveException, RemoteException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#removeAsCustodianFor
	 */
	public void removeAsCustodianFor(User custodian, User child) throws RemoveException, RemoteException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#removeAsSpouseFor
	 */
	public void removeAsSpouseFor(User personToSet, User relatedPerson) throws RemoveException, RemoteException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#removeAsCohabitantFor
	 */
	public void removeAsCohabitantFor(User personToSet, User relatedPerson) throws RemoveException, RemoteException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#removeAsSiblingFor
	 */
	public void removeAsSiblingFor(User personToSet, User relatedPerson) throws RemoveException, RemoteException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getChildRelationType
	 */
	public String getChildRelationType() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getParentRelationType
	 */
	public String getParentRelationType() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getSiblingRelationType
	 */
	public String getSiblingRelationType() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getSpouseRelationType
	 */
	public String getSpouseRelationType() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getCohabitantRelationType
	 */
	public String getCohabitantRelationType() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getCustodianRelationType
	 */
	public String getCustodianRelationType() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getUserBusiness
	 */
	public UserBusiness getUserBusiness() throws RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#registerAsDeceased
	 */
	public void registerAsDeceased(User user, Date deceasedDate) throws RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#removeAllFamilyRelationsForUser
	 */
	public void removeAllFamilyRelationsForUser(User user) throws RemoteException, java.rmi.RemoteException;
}
