package is.idega.block.family.business;


import is.idega.block.family.data.Child;
import is.idega.block.family.data.Custodian;
import is.idega.block.family.data.FamilyData;
import is.idega.block.family.data.FamilyMember;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.business.IBOService;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;

public interface FamilyLogic extends IBOService {

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getChild
	 */
	public Child getChild(User child) throws RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getCustodian
	 */
	public Custodian getCustodian(User custodian) throws RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getChildrenFor
	 */
	public Collection getChildrenFor(User user) throws NoChildrenFound, RemoteException;
	
	public Collection<User> getChildrenForUserUnderAge(User user, int age) throws NoChildrenFound, RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getChildrenInCustodyOf
	 */
	public Collection<User> getChildrenInCustodyOf(User user) throws NoChildrenFound, RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getSiblingsFor
	 */
	public Collection getSiblingsFor(User user) throws NoSiblingFound, RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getSpouseFor
	 */
	public User getSpouseFor(User user) throws NoSpouseFound, RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getCohabitantFor
	 */
	public User getCohabitantFor(User user) throws NoCohabitantFound, RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getCustodiansFor
	 */
	public Collection getCustodiansFor(User user, boolean returnParentsIfNotFound) throws NoCustodianFound, RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getCustodiansFor
	 */
	public Collection getCustodiansFor(User user) throws NoCustodianFound, RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getParentsFor
	 */
	public Collection getParentsFor(User user) throws NoParentFound, RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#hasPersonGotChildren
	 */
	public boolean hasPersonGotChildren(User person) throws RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#hasPersonGotSpouse
	 */
	public boolean hasPersonGotSpouse(User person) throws RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#hasPersonGotCohabitant
	 */
	public boolean hasPersonGotCohabitant(User person) throws RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#hasPersonGotSiblings
	 */
	public boolean hasPersonGotSiblings(User person) throws RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#isChildOf
	 */
	public boolean isChildOf(User childToCheck, User parent) throws RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#isChildInCustodyOf
	 */
	public boolean isChildInCustodyOf(User childToCheck, User parent) throws RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#isRelatedTo
	 */
	public boolean isRelatedTo(User user, User userToCheck) throws RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#isParentOf
	 */
	public boolean isParentOf(User parentToCheck, User child) throws RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#isCustodianOf
	 */
	public boolean isCustodianOf(User custodianToCheck, User child) throws RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#isSpouseOf
	 */
	public boolean isSpouseOf(User personToCheck, User relatedPerson) throws RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#isCohabitantOf
	 */
	public boolean isCohabitantOf(User personToCheck, User relatedPerson) throws RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#isSiblingOf
	 */
	public boolean isSiblingOf(User personToCheck, User relatedPerson) throws RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#setAsChildFor
	 */
	public void setAsChildFor(User personToSet, User parent) throws CreateException, RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#setAsParentFor
	 */
	public void setAsParentFor(User parent, User child) throws CreateException, RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#setAsCustodianFor
	 */
	public void setAsCustodianFor(User custodian, User child) throws CreateException, RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#setAsSpouseFor
	 */
	public void setAsSpouseFor(User personToSet, User relatedPerson) throws CreateException, RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#setAsCohabitantFor
	 */
	public void setAsCohabitantFor(User personToSet, User relatedPerson) throws CreateException, RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#setAsSiblingFor
	 */
	public void setAsSiblingFor(User personToSet, User relatedPerson) throws CreateException, RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#removeAsChildFor
	 */
	public void removeAsChildFor(User personToSet, User parent) throws RemoveException, RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#removeAsParentFor
	 */
	public void removeAsParentFor(User parent, User child) throws RemoveException, RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#removeAsCustodianFor
	 */
	public void removeAsCustodianFor(User custodian, User child) throws RemoveException, RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#removeAsSpouseFor
	 */
	public void removeAsSpouseFor(User personToSet, User relatedPerson) throws RemoveException, RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#removeAsCohabitantFor
	 */
	public void removeAsCohabitantFor(User personToSet, User relatedPerson) throws RemoveException, RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#removeAsSiblingFor
	 */
	public void removeAsSiblingFor(User personToSet, User relatedPerson) throws RemoveException, RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#removeAsChildFor
	 */
	public void removeAsChildFor(User personToSet, User parent, User performer) throws RemoveException, RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#removeAsParentFor
	 */
	public void removeAsParentFor(User parent, User child, User performer) throws RemoveException, RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#removeAsCustodianFor
	 */
	public void removeAsCustodianFor(User custodian, User child, User performer) throws RemoveException, RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#removeAsSpouseFor
	 */
	public void removeAsSpouseFor(User personToSet, User relatedPerson, User performer) throws RemoveException, RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#removeAsCohabitantFor
	 */
	public void removeAsCohabitantFor(User personToSet, User relatedPerson, User performer) throws RemoveException, RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#removeAsSiblingFor
	 */
	public void removeAsSiblingFor(User personToSet, User relatedPerson, User performer) throws RemoveException, RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getChildRelationType
	 */
	public String getChildRelationType() throws RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getParentRelationType
	 */
	public String getParentRelationType() throws RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getSiblingRelationType
	 */
	public String getSiblingRelationType() throws RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getSpouseRelationType
	 */
	public String getSpouseRelationType() throws RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getCohabitantRelationType
	 */
	public String getCohabitantRelationType() throws RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getCustodianRelationType
	 */
	public String getCustodianRelationType() throws RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getUserBusiness
	 */
	public UserBusiness getUserBusiness() throws RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#registerAsDeceased
	 */
	public void registerAsDeceased(User user, Date deceasedDate) throws RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#registerAsDeceased
	 */
	public void registerAsDeceased(User user, Date deceasedDate, User performer) throws RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#removeAllFamilyRelationsForUser
	 */
	public void removeAllFamilyRelationsForUser(User user) throws RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#removeAllFamilyRelationsForUser
	 */
	public void removeAllFamilyRelationsForUser(User user, User performer) throws RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#setFamilyForUser
	 */
	public void setFamilyForUser(String familyiNr, User user) throws CreateException, RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#updateFamilyForUser
	 */
	public void updateFamilyForUser(String familyNr, User user) throws CreateException, RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getFamilyMember
	 */
	public FamilyMember getFamilyMember(User user) throws FinderException, RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getFamily
	 */
	public FamilyData getFamily(String familyNr) throws FinderException, RemoteException;
	
	/**
	 * Gets localized relation name from relation type 
	 * @param iwrb
	 * @param type
	 * @return
	 */
	public String getRelationName(IWResourceBundle iwrb, String type);
	
	/**
	 * @param user User to set relation to
	 * @param relatedUser user that will be set related to user
	 * @param relationType 
	 */
	public void setRelation(User user, User relatedUser, String relationType) throws CreateException, RemoteException;
	
	/** Removes relation from users
	 * @param user
	 * @param relatedUser
	 * @param relationType 
	 */
	public void removeRelation(User user, User relatedUser, String relationType)  throws RemoveException, RemoteException;
	
	/** Gets users that are related to user
	 * @param user
	 * @param relatedUser
	 * @param relationType 
	 * @return
	 */
	public Collection<User> getRelatedUsers(User user, String relationType) throws RemoteException, FinderException;
}