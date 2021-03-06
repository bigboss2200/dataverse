package edu.harvard.iq.dataverse.authorization.groups;

import edu.harvard.iq.dataverse.authorization.RoleAssignee;
import edu.harvard.iq.dataverse.authorization.users.User;
import edu.harvard.iq.dataverse.authorization.groups.impl.PersistedGroup;


/**
 * An object that contains unbounded number of {@link RoleAssignee}s (e.g Users, other groups).
 * Implementors might want to look at {@link PersistedGroup} for a more convenient implementation.
 * 
 * @author michael
 */
public interface Group extends RoleAssignee {
    
    public static final String IDENTIFIER_PREFIX = "&";
    
    /**
     * A unique identifier of this group within a Dataverse system installation.
     * @return unique id of this group
     * @see #getName() 
     */
    public String getAlias();
    
    /**
     * @return Name of the group (for display purposes)
     * @see #getAlias() 
     */
    public String getName();
    
    /**
     * @return Description of this group
     */
    public String getDescription();
    
    /**
     * Tests to see whether the {@code aUser} is a part of {@code this} group, based on the user itself and the request.
     * Some groups may determine membership based on request properties, such as IP address.
     * 
     * @param aUser The user whose inclusion we test 
     * @return {@code true} iff {@code anAssignee} is in this group; {@code false} otherwise.
     */
    public boolean contains(  User aUser );
    
    public boolean isEditable();
    
    /**
     * References the object that created the group, and may also edit it.
     * @return the creator of this group.
     */
    public GroupProvider getGroupProvider();
    
}
