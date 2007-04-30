package is.idega.block.family.data;


import com.idega.user.data.Group;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class ChildHomeImpl extends IDOFactory implements ChildHome {

	public Class getEntityInterfaceClass() {
		return Child.class;
	}

	public Child create() throws CreateException {
		return (Child) super.createIDO();
	}

	public Child findByPrimaryKey(Object pk) throws FinderException {
		return (Child) super.findByPrimaryKeyIDO(pk);
	}

	public Child findUserForUserGroup(Group group) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((ChildBMPBean) entity).ejbFindUserForUserGroup(group);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}
}