package ttr;

import java.security.AccessControlException;
import java.security.Permission;
import java.util.Hashtable;

/**
 * This is a SecurityManager that grants all kinds of permissions, but
 * logs and outputs any non-standard permissions granted.
 */
public class LiberalSecurityManager extends SecurityManager {
    @SuppressWarnings("unchecked")
	private Hashtable grantedPermissions;

    @SuppressWarnings("unchecked")
	public LiberalSecurityManager() {
		grantedPermissions = new Hashtable();
    }

	/**
	 * Override checkPermission to grant all kind of permissions:
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void checkPermission(Permission perm) {
        try {
            super.checkPermission(perm);
        } catch(AccessControlException ace) {
            if(grantedPermissions.get(perm) == null) {
                //System.out.println("LiberalSecurityManager granted permission: "+perm);
                grantedPermissions.put(perm, perm);
            }
        }
    }
}
