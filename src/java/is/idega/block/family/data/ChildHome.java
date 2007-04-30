package is.idega.block.family.data;


import com.idega.user.data.Group;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;

public interface ChildHome extends IDOHome {

	public Child create() throws CreateException;

	public Child findByPrimaryKey(Object pk) throws FinderException;

	public Child findUserForUserGroup(Group group) throws FinderException;
}