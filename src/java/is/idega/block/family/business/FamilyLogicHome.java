package is.idega.block.family.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHome;
import java.rmi.RemoteException;

public interface FamilyLogicHome extends IBOHome {

	public FamilyLogic create() throws CreateException, RemoteException;
}