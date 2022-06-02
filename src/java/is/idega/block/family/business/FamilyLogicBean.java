package is.idega.block.family.business;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.user.business.UserBusiness;
import com.idega.user.business.UserStatusBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.data.GroupRelation;
import com.idega.user.data.GroupRelationHome;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;
import com.idega.util.CoreConstants;
import com.idega.util.IWTimestamp;
import com.idega.util.ListUtil;

import is.idega.block.family.data.Child;
import is.idega.block.family.data.ChildHome;
import is.idega.block.family.data.Custodian;
import is.idega.block.family.data.CustodianHome;
import is.idega.block.family.data.FamilyData;
import is.idega.block.family.data.FamilyMember;
import is.idega.block.family.data.FamilyMemberHome;

/**
 * Title: idegaWeb Member User Subsystem Description: idegaWeb Member User
 * Subsystem is the base system for Membership management Copyright: Copyright
 * (c) 2002 Company: idega
 *
 * @author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */

public class FamilyLogicBean extends IBOServiceBean implements FamilyLogic {

	private static final long serialVersionUID = -8696169731242119560L;

	private static final String RELATION_TYPE_GROUP_PARENT = CoreConstants.FAM_RELATION_PARENT;
	private static final String RELATION_TYPE_GROUP_CUSTODIAN = CoreConstants.FAM_RELATION_CUSTODIAN;
	private static final String RELATION_TYPE_GROUP_CHILD = CoreConstants.FAM_RELATION_CHILD;
	private static final String RELATION_TYPE_GROUP_SPOUSE = CoreConstants.FAM_RELATION_SPOUSE;
	private static final String RELATION_TYPE_GROUP_SIBLING = CoreConstants.FAM_RELATION_SIBLING;
	private static final String RELATION_TYPE_GROUP_COHABITANT = CoreConstants.FAM_RELATION_COHABITANT;

	protected FamilyMemberHome getFamilyMemberHome() {
		try {
			return (FamilyMemberHome) this.getIDOHome(FamilyMember.class);
		}
		catch (RemoteException e) {
			throw new EJBException(e.getMessage());
		}
	}

	protected CustodianHome getCustodianHome() {
		try {
			return (CustodianHome) this.getIDOHome(Custodian.class);
		}
		catch (RemoteException e) {
			throw new EJBException(e.getMessage());
		}
	}

	protected ChildHome getChildHome() {
		try {
			return (ChildHome) this.getIDOHome(Child.class);
		}
		catch (RemoteException e) {
			throw new EJBException(e.getMessage());
		}
	}

	protected GroupHome getGroupHome() {
		try {
			return (GroupHome) this.getIDOHome(Group.class);
		}
		catch (RemoteException e) {
			throw new EJBException(e.getMessage());
		}
	}

	protected GroupRelationHome getGroupRelationHome() {
		try {
			return (GroupRelationHome) this.getIDOHome(GroupRelation.class);
		}
		catch (RemoteException e) {
			throw new EJBException(e.getMessage());
		}
	}

	protected Collection<User> convertGroupCollectionToUserCollection(Collection<Group> coll, String relationType) {
		if (ListUtil.isEmpty(coll)) {
			return Collections.emptyList();
		}

		Collection<User> newColl = new ArrayList<>();
		for (Group group: coll) {
			newColl.add(convertGroupToUser(group, relationType));
		}

		return newColl;
	}

	protected User convertGroupToUser(Group group, String relationType) {
		try {
			if (relationType.equals(RELATION_TYPE_GROUP_CUSTODIAN) || relationType.equals(RELATION_TYPE_GROUP_PARENT) || relationType.equals(RELATION_TYPE_GROUP_COHABITANT) || relationType.equals(RELATION_TYPE_GROUP_SPOUSE)) {
				return castUserGroupToCustodian(group);
			}
			else if (relationType.equals(RELATION_TYPE_GROUP_CHILD) || relationType.equals(RELATION_TYPE_GROUP_SIBLING)) {
				return castUserGroupToChild(group);
			}
			else {
				return getUserBusiness().castUserGroupToUser(group);
			}
		}
		catch (Exception e) {
			throw new EJBException("Group " + group + " (" + group.getPrimaryKey().toString() + ")  is not a UserGroup");
		}
	}

	protected Custodian castUserGroupToCustodian(Group userGroup) throws EJBException {
		try {
			if (userGroup instanceof User) {
				User user = (User) userGroup;
				if (user instanceof Custodian) {
					return (Custodian) user;
				}
				else {
					return getCustodianHome().findByPrimaryKey(user.getPrimaryKey());
				}
			}
			else {
				return this.getCustodianHome().findUserForUserGroup(userGroup);
			}
		}
		catch (Exception e) {
			throw new IBORuntimeException(e);
		}
	}

	protected Child castUserGroupToChild(Group userGroup) throws EJBException {
		try {
			if (userGroup instanceof User) {
				User user = (User) userGroup;
				if (user instanceof Child) {
					return (Child) user;
				}
				else {
					return getChildHome().findByPrimaryKey(user.getPrimaryKey());
				}
			}
			else {
				return this.getChildHome().findUserForUserGroup(userGroup);
			}
		}
		catch (Exception e) {
			throw new IBORuntimeException(e);
		}
	}

	protected Group convertUserToGroup(User user) {
		try {
			return user.getUserGroup();
		}
		catch (Exception e) {
			throw new EJBException("User " + user + " has no a UserGroup");
		}
	}

	@Override
	public Child getChild(User child) {
		if (child instanceof Child) {
			return (Child) child;
		}

		if (child == null) {
			getLogger().warning("Provided user object (expected child as user) is null!");
			return null;
		}

		try {
			return getChildHome().findByPrimaryKey(child.getPrimaryKey());
		} catch (FinderException fe) {
			fe.printStackTrace();
		}

		return null;
	}

	@Override
	public Custodian getCustodian(User custodian) {
		if (custodian instanceof Custodian) {
			return (Custodian) custodian;
		}
		else {
			try {
				return getCustodianHome().findByPrimaryKey(custodian.getPrimaryKey());
			}
			catch (FinderException fe) {
				fe.printStackTrace();
			}
		}

		return null;
	}

	/**
	 * @return A Collection of User objects (children) who are children of this
	 *         user. Returns an empty Collection if no children found.
	 * @throws NoChildrenFound
	 *           if no children are found
	 */
	@Override
	public Collection<User> getChildrenFor(User user) throws NoChildrenFound {
		if (user == null) {
			getLogger().warning("User is not provided");
			return Collections.emptyList();
		}

		String userName = null;
		try {
			userName = user.getName();

			Collection<Group> coll = user.getRelatedBy(RELATION_TYPE_GROUP_PARENT);

			if (coll == null || coll.isEmpty()) {
				throw new NoChildrenFound(userName);
			}
			return convertGroupCollectionToUserCollection(coll, RELATION_TYPE_GROUP_PARENT);
		}
		catch (FinderException e) {
			throw new NoChildrenFound(userName);
		}
	}

	/**
	 * @return A Collection of User objects (children) who in his custody. Returns
	 *         an empty Collection if no children found.
	 * @throws NoChildrenFound
	 *           if no children are found
	 */
	@Override
	public Collection<User> getChildrenInCustodyOf(User user) throws NoChildrenFound {
		if (user == null) {
			getLogger().warning("User is not provided");
			return Collections.emptyList();
		}

		String userName = null;
		try {
			userName = user.getName();

			String relation = RELATION_TYPE_GROUP_CUSTODIAN;
			Collection<Group> coll = user.getRelatedBy(relation);

			if (ListUtil.isEmpty(coll)) {
				relation = RELATION_TYPE_GROUP_PARENT;
				coll = user.getRelatedBy(relation);
			}
			if (coll == null || coll.isEmpty()) {
				throw new NoChildrenFound(userName);
			}
			return convertGroupCollectionToUserCollection(coll, relation);
		}
		catch (FinderException e) {
			throw new NoChildrenFound(userName);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.block.family.business.FamilyLogic#getSiblingsFor(com.idega.user.data.User)
	 */
	@Override
	public Collection<User> getSiblingsFor(User user) {
		if (user == null) {
			return Collections.emptyList();
		}

		Collection<Group> coll = null;
		try {
			coll = user.getRelatedBy(RELATION_TYPE_GROUP_SIBLING);
		} catch (FinderException e) {
			getLogger().log(Level.WARNING, "Failed to get siblings for: " + user.getName());
		}

		if (ListUtil.isEmpty(coll)) {
			return Collections.emptyList();
		}

		return convertGroupCollectionToUserCollection(coll,
				RELATION_TYPE_GROUP_SIBLING);
	}

	/**
	 * @return User object for the spouse of a user.
	 * @throws NoSpouseFound
	 *           if no spouse is found
	 */
	@Override
	public User getSpouseFor(User user) throws NoSpouseFound {
		String userName = null;
		try {
			userName = user.getName();
			Collection<Group> coll = user.getRelatedBy(RELATION_TYPE_GROUP_SPOUSE);
			Iterator<Group> spouseIter = coll.iterator();
			if (spouseIter.hasNext()) {
				Group group = spouseIter.next();
				return convertGroupToUser(group, RELATION_TYPE_GROUP_SPOUSE);
			}
			else {
				throw new NoSpouseFound(userName);
			}
		}
		catch (Exception e) {
			throw new NoSpouseFound(userName);
		}
	}

	/**
	 * @return User object for the spouse of a user.
	 * @throws NoSpouseFound
	 *           if no spouse is found
	 */
	@Override
	public User getCohabitantFor(User user) throws NoCohabitantFound {
		String userName = null;
		try {
			userName = user.getName();
			Collection<Group> coll = user.getRelatedBy(RELATION_TYPE_GROUP_COHABITANT);
			Group group = coll.iterator().next();
			return convertGroupToUser(group, RELATION_TYPE_GROUP_COHABITANT);
		}
		catch (Exception e) {
			throw new NoCohabitantFound(userName);
		}
	}

	@Override
	public Collection<User> getCustodiansFor(com.idega.user.data.bean.User user, boolean returnParentsIfNotFound) throws NoCustodianFound {
		return getCustodiansFor(user.getId(), user.getName(), returnParentsIfNotFound);
	}

	/**
	 * @return A Collection of User object who are custodians of a user. If no
	 *         custodian is found it will return the parents of that user. Returns
	 *         an empty Collection if no custodians or parents are found.
	 * @throws NoCustodianFound
	 *           if no custodians are found
	 */
	@Override
	public Collection<User> getCustodiansFor(User user, boolean returnParentsIfNotFound) throws NoCustodianFound {
		if (user == null) {
			return Collections.emptyList();
		}

		return getCustodiansFor(Integer.valueOf(user.getId()), user.getName(), returnParentsIfNotFound);
	}

	private Collection<User> getCustodiansFor(Integer id, String name, boolean returnParentsIfNotFound) throws NoCustodianFound {
		try {
			GroupHome groupHome = getGroupHome();
			Collection<Group> coll = groupHome.getReverseRelatedBy(id, RELATION_TYPE_GROUP_CUSTODIAN);
			if (coll == null || coll.isEmpty()) {
				if (returnParentsIfNotFound) {
					try {
						return getParentsFor(id, name);
					} catch (NoParentFound ex) {
						throw new NoCustodianFound(name);
					}
				} else {
					throw new NoCustodianFound(name);
				}
			}
			return convertGroupCollectionToUserCollection(coll, RELATION_TYPE_GROUP_CUSTODIAN);
		} catch (FinderException e) {
			throw new NoCustodianFound(name);
		}
	}

	@Override
	public Collection<User> getCustodiansFor(User user) throws NoCustodianFound {
		return getCustodiansFor(user, true);
	}

	/**
	 * @return A Collection of User object who are parents of a user. Returns an
	 *         empty Collection if no parents are found.
	 * @throws NoCustodianFound
	 *           if no custodians are found
	 */
	@Override
	public Collection<User> getParentsFor(User user) throws NoParentFound {
		if (user == null) {
			return Collections.emptyList();
		}
		return getParentsFor(Integer.valueOf(user.getId()), user.getName());
	}

	private Collection<User> getParentsFor(Integer id, String name) throws NoParentFound {
		try {
			GroupHome groupHome = getGroupHome();
			Collection<Group> coll = groupHome.getReverseRelatedBy(id, RELATION_TYPE_GROUP_PARENT);
			if (coll == null || coll.isEmpty()) {
				throw new NoParentFound(name);
			}
			return convertGroupCollectionToUserCollection(coll, RELATION_TYPE_GROUP_PARENT);
		}
		catch (FinderException e) {
			throw new NoParentFound(name);
		}
	}

	@Override
	public boolean hasPersonGotChildren(User person) {
		/**
		 * @todo Implement better
		 */
		try {
			this.getChildrenFor(person);
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean hasPersonGotSpouse(User person) {
		/**
		 * @todo Implement better
		 */
		try {
			this.getSpouseFor(person);
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean hasPersonGotCohabitant(User person) {
		/**
		 * @todo Implement better
		 */
		try {
			this.getCohabitantFor(person);
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean hasPersonGotSiblings(User person) {
		/**
		 * @todo Implement better
		 */
		try {
			this.getSiblingsFor(person);
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

	/**
	 * @returns True if the childToCheck is a child of parent else false
	 */
	@Override
	public boolean isChildOf(User childToCheck, User parent) {
		try {
			Collection<User> coll = getChildrenFor(parent);
			return coll.contains(childToCheck);
		}
		catch (NoChildrenFound ex) {
		}
		return false;
	}

	/**
	 * @returns True if the childToCheck is a child in custody of parent else
	 *          false
	 */
	@Override
	public boolean isChildInCustodyOf(User childToCheck, User parent) {
		try {
			Collection<User> coll = getChildrenInCustodyOf(parent);
			return coll.contains(childToCheck);
		}
		catch (NoChildrenFound ex) {
		}
		return false;
	}

	@Override
	public boolean isRelatedTo(User user, User userToCheck) {
		if (isParentOf(user, userToCheck)) {
			return true;
		}
		else if (isChildOf(user, userToCheck)) {
			return true;
		}
		else if (isSpouseOf(user, userToCheck)) {
			return true;
		}
		else if (isSiblingOf(user, userToCheck)) {
			return true;
		}
		return false;
	}

	/**
	 * @return True if the parentToCheck is the parent of childToCheck else false
	 */
	@Override
	public boolean isParentOf(User parentToCheck, User child) {
		try {
			Collection<User> coll = getParentsFor(child);
			return coll.contains(parentToCheck);
		}
		catch (NoParentFound ex) {
			return false;
		}
	}

	/**
	 * @return True if the custodianToCheck is the custodian of childToCheck else
	 *         false
	 */
	@Override
	public boolean isCustodianOf(User custodianToCheck, User child) {
		try {
			Collection<User> coll = getChildrenInCustodyOf(custodianToCheck);

			return coll.contains(child);
		}
		catch (NoChildrenFound ex) {
			return false;
		}
	}

	/**
	 * @return True if the personToCheck is a spouse of relatedPerson else false
	 */
	@Override
	public boolean isSpouseOf(User personToCheck, User relatedPerson) {
		if (this.hasPersonGotSpouse(personToCheck)) {
			try {
				User spouse = this.getSpouseFor(personToCheck);
				if (spouse.equals(relatedPerson)) {
					return true;
				}
			}
			catch (NoSpouseFound nsf) {
			}
		}
		return false;
	}

	/**
	 * @return True if the personToCheck is a spouse of relatedPerson else false
	 */
	@Override
	public boolean isCohabitantOf(User personToCheck, User relatedPerson) {
		if (this.hasPersonGotCohabitant(personToCheck)) {
			try {
				User cohabitant = this.getCohabitantFor(personToCheck);
				if (cohabitant.equals(relatedPerson)) {
					return true;
				}
			}
			catch (NoCohabitantFound nsf) {
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.block.family.business.FamilyLogic#isSiblingOf(com.idega.user.data.User, com.idega.user.data.User)
	 */
	@Override
	public boolean isSiblingOf(User personToCheck, User relatedPerson) {
		Collection<User> coll = getSiblingsFor(relatedPerson);
		if (ListUtil.isEmpty(coll)) {
			return Boolean.FALSE;
		}

		return coll.contains(personToCheck);
	}

	@Override
	public void setAsChildFor(User personToSet, User parent) throws CreateException {
		if (!this.isChildOf(personToSet, parent)) {
			personToSet.addUniqueRelation(convertUserToGroup(parent), RELATION_TYPE_GROUP_CHILD);
			parent.addUniqueRelation(convertUserToGroup(personToSet), RELATION_TYPE_GROUP_PARENT);
		}
	}

	@Override
	public void setAsParentFor(User parent, User child) throws CreateException {
		if (!this.isParentOf(parent, child)) {
			child.addUniqueRelation(convertUserToGroup(parent), RELATION_TYPE_GROUP_CHILD);
			parent.addUniqueRelation(convertUserToGroup(child), RELATION_TYPE_GROUP_PARENT);
		}
	}

	@Override
	public void setAsCustodianFor(User custodian, User child) throws CreateException {
		if (!this.isCustodianOf(custodian, child)) {
			child.addUniqueRelation(convertUserToGroup(custodian), RELATION_TYPE_GROUP_CHILD);
			custodian.addUniqueRelation(convertUserToGroup(child), RELATION_TYPE_GROUP_CUSTODIAN);
		}
	}

	@Override
	public void setAsSpouseFor(User personToSet, User relatedPerson) throws CreateException {
		if (!this.isSpouseOf(personToSet, relatedPerson)) {
			personToSet.addUniqueRelation(convertUserToGroup(relatedPerson), RELATION_TYPE_GROUP_SPOUSE);
			relatedPerson.addRelation(convertUserToGroup(personToSet), RELATION_TYPE_GROUP_SPOUSE);
		}
	}

	@Override
	public void setAsCohabitantFor(User personToSet, User relatedPerson) throws CreateException {
		if (!this.isCohabitantOf(personToSet, relatedPerson)) {
			personToSet.addUniqueRelation(convertUserToGroup(relatedPerson), RELATION_TYPE_GROUP_COHABITANT);
			relatedPerson.addRelation(convertUserToGroup(personToSet), RELATION_TYPE_GROUP_COHABITANT);
		}
	}

	@Override
	public void setAsSiblingFor(User personToSet, User relatedPerson) throws CreateException {
		if (!this.isSiblingOf(personToSet, relatedPerson)) {
			personToSet.addUniqueRelation(convertUserToGroup(relatedPerson), RELATION_TYPE_GROUP_SIBLING);
			relatedPerson.addRelation(convertUserToGroup(personToSet), RELATION_TYPE_GROUP_SIBLING);
			/*
			 * Changed from addUnique 2 addRelation so that relation would be created
			 * relatedPerson.addUniqueRelation(convertUserToGroup(personToSet),this.RELATION_TYPE_GROUP_SIBLING);
			 */
		}
	}

	@Override
	public void removeAsChildFor(User personToSet, User parent) throws RemoveException {
		personToSet.removeRelation(convertUserToGroup(parent), RELATION_TYPE_GROUP_CHILD);
		parent.removeRelation(convertUserToGroup(personToSet), RELATION_TYPE_GROUP_PARENT);
	}

	@Override
	public void removeAsParentFor(User parent, User child) throws RemoveException {
		child.removeRelation(convertUserToGroup(parent), RELATION_TYPE_GROUP_CHILD);
		parent.removeRelation(convertUserToGroup(child), RELATION_TYPE_GROUP_PARENT);
	}

	@Override
	public void removeAsCustodianFor(User custodian, User child) throws RemoveException {
		child.removeRelation(convertUserToGroup(custodian), RELATION_TYPE_GROUP_CHILD);
		custodian.removeRelation(convertUserToGroup(child), RELATION_TYPE_GROUP_CUSTODIAN);
	}

	@Override
	public void removeAsSpouseFor(User personToSet, User relatedPerson) throws RemoveException {
		personToSet.removeRelation(convertUserToGroup(relatedPerson), RELATION_TYPE_GROUP_SPOUSE);
		relatedPerson.removeRelation(convertUserToGroup(personToSet), RELATION_TYPE_GROUP_SPOUSE);
	}

	@Override
	public void removeAsCohabitantFor(User personToSet, User relatedPerson) throws RemoveException {
		personToSet.removeRelation(convertUserToGroup(relatedPerson), RELATION_TYPE_GROUP_COHABITANT);
		relatedPerson.removeRelation(convertUserToGroup(personToSet), RELATION_TYPE_GROUP_COHABITANT);
	}

	@Override
	public void removeAsSiblingFor(User personToSet, User relatedPerson) throws RemoveException {
		personToSet.removeRelation(convertUserToGroup(relatedPerson), RELATION_TYPE_GROUP_SIBLING);
		relatedPerson.removeRelation(convertUserToGroup(personToSet), RELATION_TYPE_GROUP_SIBLING);
	}

	@Override
	public void removeAsChildFor(User personToSet, User parent, User performer) throws RemoveException {
		personToSet.removeRelation(convertUserToGroup(parent), RELATION_TYPE_GROUP_CHILD, performer);
		parent.removeRelation(convertUserToGroup(personToSet), RELATION_TYPE_GROUP_PARENT, performer);
	}

	@Override
	public void removeAsParentFor(User parent, User child, User performer) throws RemoveException {
		child.removeRelation(convertUserToGroup(parent), RELATION_TYPE_GROUP_CHILD, performer);
		parent.removeRelation(convertUserToGroup(child), RELATION_TYPE_GROUP_PARENT, performer);
	}

	@Override
	public void removeAsCustodianFor(User custodian, User child, User performer) throws RemoveException {
		child.removeRelation(convertUserToGroup(custodian), RELATION_TYPE_GROUP_CHILD, performer);
		custodian.removeRelation(convertUserToGroup(child), RELATION_TYPE_GROUP_CUSTODIAN, performer);
	}

	@Override
	public void removeAsSpouseFor(User personToSet, User relatedPerson, User performer) throws RemoveException {
		personToSet.removeRelation(convertUserToGroup(relatedPerson), RELATION_TYPE_GROUP_SPOUSE, performer);
		relatedPerson.removeRelation(convertUserToGroup(personToSet), RELATION_TYPE_GROUP_SPOUSE, performer);
	}

	@Override
	public void removeAsCohabitantFor(User personToSet, User relatedPerson, User performer) throws RemoveException {
		personToSet.removeRelation(convertUserToGroup(relatedPerson), RELATION_TYPE_GROUP_COHABITANT, performer);
		relatedPerson.removeRelation(convertUserToGroup(personToSet), RELATION_TYPE_GROUP_COHABITANT, performer);
	}

	@Override
	public void removeAsSiblingFor(User personToSet, User relatedPerson, User performer) throws RemoveException {
		personToSet.removeRelation(convertUserToGroup(relatedPerson), RELATION_TYPE_GROUP_SIBLING, performer);
		relatedPerson.removeRelation(convertUserToGroup(personToSet), RELATION_TYPE_GROUP_SIBLING, performer);
	}

	/**
	 * Returns the RELATION_TYPE_GROUP_CHILD.
	 *
	 * @return String
	 */
	@Override
	public String getChildRelationType() {
		return RELATION_TYPE_GROUP_CHILD;
	}

	/**
	 * Returns the RELATION_TYPE_GROUP_PARENT.
	 *
	 * @return String
	 */
	@Override
	public String getParentRelationType() {
		return RELATION_TYPE_GROUP_PARENT;
	}

	/**
	 * Returns the RELATION_TYPE_GROUP_SIBLING.
	 *
	 * @return String
	 */
	@Override
	public String getSiblingRelationType() {
		return RELATION_TYPE_GROUP_SIBLING;
	}

	/**
	 * Returns the RELATION_TYPE_GROUP_SPOUSE.
	 *
	 * @return String
	 */
	@Override
	public String getSpouseRelationType() {
		return RELATION_TYPE_GROUP_SPOUSE;
	}

	/**
	 * Returns the RELATION_TYPE_GROUP_COHABITANT.
	 *
	 * @return String
	 */
	@Override
	public String getCohabitantRelationType() {
		return RELATION_TYPE_GROUP_COHABITANT;
	}

	/**
	 * Returns the RELATION_TYPE_GROUP_CUSTODIAN.
	 *
	 * @return String
	 */
	@Override
	public String getCustodianRelationType() {
		return RELATION_TYPE_GROUP_CUSTODIAN;
	}

	@Override
	public UserBusiness getUserBusiness() {
		try {
			return this.getServiceInstance(UserBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	/**
	 * @deprecated use registerAsDeceased(User user, Date deceasedDate, User
	 *             performer)
	 */
	@Override
	@Deprecated
	public void registerAsDeceased(User user, Date deceasedDate) {
		try {
			removeAllFamilyRelationsForUser(user);
			UserStatusBusiness userStatusService = getServiceInstance(UserStatusBusiness.class);
			userStatusService.setUserAsDeceased((Integer) user.getPrimaryKey(), deceasedDate);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	@Override
	public void registerAsDeceased(User user, Date deceasedDate, User performer) {
		try {
			removeAllFamilyRelationsForUser(user, performer);
			UserStatusBusiness userStatusService = getServiceInstance(UserStatusBusiness.class);
			userStatusService.setUserAsDeceased(user, deceasedDate);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	/**
	 * @deprecated use removeAllFamilyRelationsForUser(User user, User performer)
	 */
	@Override
	@Deprecated
	public void removeAllFamilyRelationsForUser(User user) {
		try {
			Collection<User> children = getChildrenFor(user);
			if (children != null) {
				Iterator<User> kids = children.iterator();
				while (kids.hasNext()) {
					User child = kids.next();
					removeAsChildFor(child, user);
				}
			}

		}
		catch (RemoveException ex) {
			ex.printStackTrace();
		}
		catch (NoChildrenFound x) {
		}

		try {
			Collection<User> children = getChildrenInCustodyOf(user);
			if (children != null) {
				Iterator<User> kids = children.iterator();
				while (kids.hasNext()) {
					User child = kids.next();
					removeAsCustodianFor(user, child);
				}
			}

		}
		catch (RemoveException ex) {
			ex.printStackTrace();
		}
		catch (NoChildrenFound x) {
		}

		try {
			User spouse = getSpouseFor(user);
			if (spouse != null) {
				removeAsSpouseFor(spouse, user);
			}

		}
		catch (RemoveException ex) {
			ex.printStackTrace();
		}
		catch (NoSpouseFound x) {
		}

		try {
			Collection<User> parents = getParentsFor(user);
			if (parents != null) {
				Iterator<User> ents = parents.iterator();
				while (ents.hasNext()) {
					User ent = ents.next();
					removeAsParentFor(ent, user);
				}
			}

		}
		catch (RemoveException ex) {
			ex.printStackTrace();
		}
		catch (NoParentFound x) {
		}

		try {
			Collection<User> custodians = getCustodiansFor(user);
			if (custodians != null) {
				Iterator<User> ents = custodians.iterator();
				while (ents.hasNext()) {
					User ent = ents.next();
					removeAsCustodianFor(ent, user);
				}
			}

		}
		catch (RemoveException ex) {
			ex.printStackTrace();
		}
		catch (NoCustodianFound x) {
		}

		try {
			Collection<User> siblings = getSiblingsFor(user);
			if (siblings != null) {
				Iterator<User> sibling = siblings.iterator();
				while (sibling.hasNext()) {
					User sibl = sibling.next();
					removeAsSiblingFor(sibl, user);
				}
			}

		}
		catch (RemoveException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void removeAllFamilyRelationsForUser(User user, User performer) {
		try {
			Collection<User> children = getChildrenFor(user);
			if (children != null) {
				Iterator<User> kids = children.iterator();
				while (kids.hasNext()) {
					User child = kids.next();
					removeAsChildFor(child, user, performer);
				}
			}

		}
		catch (RemoveException ex) {
			ex.printStackTrace();
		}
		catch (NoChildrenFound x) {
		}

		try {
			Collection<User> children = getChildrenInCustodyOf(user);
			if (children != null) {
				Iterator<User> kids = children.iterator();
				while (kids.hasNext()) {
					User child = kids.next();
					removeAsCustodianFor(user, child, performer);
				}
			}

		}
		catch (RemoveException ex) {
			ex.printStackTrace();
		}
		catch (NoChildrenFound x) {
		}

		try {
			User spouse = getSpouseFor(user);
			if (spouse != null) {
				removeAsSpouseFor(spouse, user, performer);
			}

		}
		catch (RemoveException ex) {
			ex.printStackTrace();
		}
		catch (NoSpouseFound x) {
		}

		try {
			Collection<User> parents = getParentsFor(user);
			if (parents != null) {
				Iterator<User> ents = parents.iterator();
				while (ents.hasNext()) {
					User ent = ents.next();
					removeAsParentFor(ent, user, performer);
				}
			}

		}
		catch (RemoveException ex) {
			ex.printStackTrace();
		}
		catch (NoParentFound x) {
		}

		try {
			Collection<User> custodians = getCustodiansFor(user);
			if (custodians != null) {
				Iterator<User> ents = custodians.iterator();
				while (ents.hasNext()) {
					User ent = ents.next();
					removeAsCustodianFor(ent, user, performer);
				}
			}

		}
		catch (RemoveException ex) {
			ex.printStackTrace();
		}
		catch (NoCustodianFound x) {
		}

		try {
			Collection<User> siblings = getSiblingsFor(user);
			if (siblings != null) {
				Iterator<User> sibling = siblings.iterator();
				while (sibling.hasNext()) {
					User sibl = sibling.next();
					removeAsSiblingFor(sibl, user, performer);
				}
			}

		}
		catch (RemoveException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void setFamilyForUser(String familyiNr, User user) throws CreateException {
		FamilyMemberHome familyMemberHome = getFamilyMemberHome();
		FamilyMember familyMember = familyMemberHome.create();
		familyMember.setFamilyNr(familyiNr);
		familyMember.setUser(user);
		familyMember.store();
	}

	@Override
	public void updateFamilyForUser(String familyNr, User user) throws CreateException {
		FamilyMemberHome familyMemberHome = getFamilyMemberHome();
		FamilyMember fMember = null;
		try {
			fMember = familyMemberHome.findForUser(user);
			fMember.setFamilyNr(familyNr);
			fMember.store();
		}
		catch (FinderException f) {
			setFamilyForUser(familyNr, user);
		}
	}

	@Override
	public FamilyMember getFamilyMember(User user) throws FinderException {
		return getFamilyMemberHome().findForUser(user);
	}

	@Override
	public FamilyData getFamily(String familyNr) throws FinderException {
		FamilyData familyData = new FamilyData();
		FamilyMemberHome familyMemberHome = getFamilyMemberHome();
		Iterator<FamilyMember> iter = familyMemberHome.findAllByFamilyNR(familyNr).iterator();
		while (iter.hasNext()) {
			FamilyMember familyMember = iter.next();
			switch (familyMember.getRole()) {
				case FamilyMember.FATHER: {
					familyData.setHusband(familyMember.getUser());
					break;
				}
				case FamilyMember.MOTHER: {
					familyData.setWife(familyMember.getUser());
					break;
				}
				case FamilyMember.CHILD: {
					familyData.addChild(familyMember.getUser());
					break;
				}
				default: {
					System.out.println("Member of family does not have a role! " + familyMember.getUser().getName());
				}
			}
		}
		return familyData;
	}

	@Override
	public Collection<User> getChildrenForUserUnderAge(User user, int age) throws NoChildrenFound, RemoteException {
		Collection<User> allChildren = getChildrenFor(user);
		if (ListUtil.isEmpty(allChildren)) {
			return null;
		}

		IWTimestamp dateBeforeXYears = new IWTimestamp(System.currentTimeMillis());
		dateBeforeXYears.setYear(dateBeforeXYears.getYear() - age);

		Collection<User> childrenUnderAge = new ArrayList<>();
		for (Object object: allChildren) {
			if (object instanceof Custodian) {
				try {
					object = ((UserHome) IDOLookup.getHome(User.class)).findByPrimaryKey(((Custodian) object).getId());
				} catch (Exception e) {
					getLogger().log(Level.WARNING, "Error getting user", e);
				}
			}

			if (object instanceof User) {
				User child = (User) object;

				Date dateOfBirth = child.getDateOfBirth();
				if (dateOfBirth == null) {
					continue;
				}

				IWTimestamp childBirthDay = new IWTimestamp(dateOfBirth);
				if (childBirthDay.isLaterThanOrEquals(dateBeforeXYears)) {
					childrenUnderAge.add(child);
				}
			}
		}

		return childrenUnderAge;
	}

	@Override
	public String getRelationName(IWResourceBundle iwrb, String type) {
		if(type.equals(RELATION_TYPE_GROUP_PARENT)) {
			return iwrb.getLocalizedString("parent", "Parent");
		}
		if(type.equals(RELATION_TYPE_GROUP_CUSTODIAN)) {
			return iwrb.getLocalizedString("custodian", "Custodian");
		}
		if(type.equals(RELATION_TYPE_GROUP_CHILD)) {
			return iwrb.getLocalizedString("child", "Child");
		}
		if(type.equals(RELATION_TYPE_GROUP_SPOUSE)) {
			return iwrb.getLocalizedString("spouse", "Spouse");
		}
		if(type.equals(RELATION_TYPE_GROUP_SIBLING)) {
			return iwrb.getLocalizedString("sibling", "Sibling");
		}
		if(type.equals(RELATION_TYPE_GROUP_COHABITANT)) {
			return iwrb.getLocalizedString("cohabitant", "Cohabitant");
		}
		return CoreConstants.EMPTY;
	}

	@Override
	public void setRelation(User user, User relatedUser, String relationType)
			throws CreateException, RemoteException {
		if(relationType.equals(getChildRelationType())){
			setAsChildFor(relatedUser, user);
		}else if(relationType.equals(getParentRelationType())){
			setAsParentFor(relatedUser, user);
		}else if(relationType.equals(getCustodianRelationType())){
			setAsCustodianFor(relatedUser, user);
		}else if(relationType.equals(getSiblingRelationType())){
			setAsSiblingFor(user, relatedUser);
		}else if(relationType.equals(getSpouseRelationType())){
			setAsSpouseFor(user, relatedUser);
		}else if(relationType.equals(getCohabitantRelationType())){
			setAsCohabitantFor(user, relatedUser);
		}
	}

	@Override
	public void removeRelation(User user, User relatedUser, String relationType)
			throws RemoveException, RemoteException {
		if(relationType.equals(getChildRelationType())){
			removeAsChildFor(relatedUser, user);
		}else if(relationType.equals(getParentRelationType())){
			removeAsParentFor(relatedUser, user);
		}else if(relationType.equals(getCustodianRelationType())){
			removeAsCustodianFor(relatedUser, user);
		}else if(relationType.equals(getSiblingRelationType())){
			removeAsSiblingFor(user, relatedUser);
		}else if(relationType.equals(getSpouseRelationType())){
			removeAsSpouseFor(user, relatedUser);
		}else if(relationType.equals(getCohabitantRelationType())){
			removeAsCohabitantFor(user, relatedUser);
		}
	}

	@Override
	public Collection<User> getRelatedUsers(User user, String relationType)
			throws RemoteException, FinderException {
		if(relationType.equals(getChildRelationType())){
			return getChildrenFor(user);
		}else if(relationType.equals(getParentRelationType())){
			return getParentsFor(user);
		}else if(relationType.equals(getCustodianRelationType())){
			return getCustodiansFor(user,false);
		}else if(relationType.equals(getSiblingRelationType())){
			return getSiblingsFor(user);
		}else if(relationType.equals(getSpouseRelationType())){
			User spouse = getSpouseFor(user);
			ArrayList<User> spouses = new ArrayList<>();
			spouses.add(spouse);
			return spouses;
		}else if(relationType.equals(getCohabitantRelationType())){
			User cohabitant = getCohabitantFor(user);
			ArrayList<User> cohabitants = new ArrayList<>();
			cohabitants.add(cohabitant);
			return cohabitants;
		}
		return Collections.emptyList();
	}

	@Override
	public Collection<User> getRelatedUsers(User user){
		return user.getRelatedUsers(getAllRelations());
	}

	private Collection<String> getAllRelations(){
		return Arrays.asList(
				RELATION_TYPE_GROUP_PARENT,
				RELATION_TYPE_GROUP_CUSTODIAN,
				RELATION_TYPE_GROUP_CHILD,
				RELATION_TYPE_GROUP_SPOUSE,
				RELATION_TYPE_GROUP_SIBLING,
				RELATION_TYPE_GROUP_COHABITANT
		);
	}

	@Override
	public Collection<Custodian> getConvertedUsersAsCustodians(Collection<User> custodians) {
		if (ListUtil.isEmpty(custodians)) {
			return Collections.emptyList();
		}

		Collection<Custodian> results = new ArrayList<>();
		for (User custodian: custodians) {
			Custodian result = castUserGroupToCustodian(custodian);
			if (result != null) {
				results.add(result);
			}
		}
		return results;
	}

	@Override
	public void store(Custodian custodian, Child child, boolean storeMaritalStatus, String relation, String maritalStatus) {
		try {
			Thread saver = new Thread(new Runnable() {

				@Override
				public void run() {
					if (storeMaritalStatus) {
						custodian.setMaritalStatus(maritalStatus);
					}
					custodian.store();

					if (custodian.isCustodianOf(child)) {
						try {
							child.storeRelation(custodian, relation);
						} catch (Throwable e) {
							getLogger().log(Level.WARNING, "Error storing child " + child + " after relation '" + relation + "' with " + custodian + " added");
						}
					} else {
						child.setExtraCustodian(custodian, relation);
						try {
							child.store();
						} catch (Throwable e) {
							getLogger().log(Level.WARNING, "Error storing child " + child + " after extra custodian (" + custodian + ") added");
						}
					}
				}

			});
			if (getIWMainApplication().getSettings().getBoolean("family.update_un_synchron", false)) {
				saver.start();
			} else {
				saver.run();
			}
		} catch (Throwable e) {
			getLogger().log(Level.WARNING, "Error storing custodian (" + custodian + ") and child " + child, e);
		}
	}

	@Override
	public void storeRelative(Child child, String personalID, String name, String relation, int number, String homePhone, String workPhone, String mobilePhone, String email) {
		if (child == null) {
			getLogger().warning("Child is not provided");
			return;
		}

		try {
			child.storeRelative(personalID, name, relation, number, homePhone, workPhone, mobilePhone, email);
		} catch (Throwable e) {
			getLogger().log(Level.WARNING, "Error storing relative (name: '" + name + "', personal ID: '" + personalID + "', number: " + number + ") for child " + child, e);
		}
	}

	@Override
	public void removeRelative(Child child, int number) {
		if (child == null) {
			getLogger().warning("Child is not provided");
			return;
		}

		try {
			child.removeRelative(number);
		} catch (Throwable e) {
			getLogger().log(Level.WARNING, "Error removing relative (number: " + number + ") for child " + child, e);
		}
	}

}