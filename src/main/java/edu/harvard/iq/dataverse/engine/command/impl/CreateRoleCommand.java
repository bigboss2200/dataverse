package edu.harvard.iq.dataverse.engine.command.impl;

import edu.harvard.iq.dataverse.Dataverse;
import edu.harvard.iq.dataverse.authorization.DataverseRole;
import edu.harvard.iq.dataverse.authorization.Permission;
import edu.harvard.iq.dataverse.authorization.users.User;
import edu.harvard.iq.dataverse.engine.command.AbstractCommand;
import edu.harvard.iq.dataverse.engine.command.CommandContext;
import edu.harvard.iq.dataverse.engine.command.RequiredPermissions;
import edu.harvard.iq.dataverse.engine.command.exception.CommandException;

/**
 * Create a new role in a dataverse.
 * @author michael
 */
@RequiredPermissions( Permission.ManageDataversePermissions )
public class CreateRoleCommand extends AbstractCommand<DataverseRole> {
	
	private final DataverseRole created;
	private final Dataverse dv;
	
	public CreateRoleCommand(DataverseRole aRole, User aUser, Dataverse anAffectedDataverse) {
		super(aUser, anAffectedDataverse);
		created = aRole;
		dv = anAffectedDataverse;
	}

	@Override
	public DataverseRole execute(CommandContext ctxt) throws CommandException {
		dv.addRole(created);
		return ctxt.roles().save(created);
	}
	
}
