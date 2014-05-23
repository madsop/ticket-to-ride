package ttr.nettverk;

import java.security.AccessControlException;
import java.security.Permission;
import java.util.Hashtable;

/**
 * This is a SecurityManager that grants all kinds of permissions, but
 * logs and outputs any non-standard permissions granted.
 */
class LiberalSecurityManager extends SecurityManager {
    private final Hashtable<Permission, Permission> grantedPermissions;

    public LiberalSecurityManager() {
		grantedPermissions = new Hashtable<Permission, Permission>();
    }

	/**
	 * Override checkPermission to grant all kind of permissions:
	 */
	@Override
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
