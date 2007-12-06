package is.idega.block.family.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHomeImpl;

public class FamilyLogicHomeImpl extends IBOHomeImpl implements FamilyLogicHome {

	public Class getBeanInterfaceClass() {
		return FamilyLogic.class;
	}

	public FamilyLogic create() throws CreateException {
		return (FamilyLogic) super.createIBO();
	}
}