package edu.harvard.iq.dataverse.engine.command.impl;

import edu.harvard.iq.dataverse.Dataset;
import edu.harvard.iq.dataverse.DataverseUser;
import edu.harvard.iq.dataverse.engine.Permission;
import edu.harvard.iq.dataverse.engine.command.AbstractCommand;
import edu.harvard.iq.dataverse.engine.command.CommandContext;
import edu.harvard.iq.dataverse.engine.command.RequiredPermissions;
import edu.harvard.iq.dataverse.engine.command.exception.CommandException;
import java.util.Objects;

/**
 * Creates a {@link Dataset} in the passed {@link CommandContext}.
 * @author michael
 */
@RequiredPermissions(Permission.UndoableEdit)
public class CreateDatasetCommand extends AbstractCommand<Dataset> {
	
	private final Dataset theDataset;

	public CreateDatasetCommand(Dataset theDataset, DataverseUser user ) {
		super( user, theDataset.getOwner() );
		this.theDataset = theDataset;
	}
	
	@Override
	public Dataset execute(CommandContext ctxt) throws CommandException {
		return ctxt.datasets().CreateDatasetCommand(theDataset);
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 97 * hash + Objects.hashCode(this.theDataset);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if ( ! (obj instanceof CreateDatasetCommand) ) {
			return false;
		}
		final CreateDatasetCommand other = (CreateDatasetCommand) obj;
		return Objects.equals(this.theDataset, other.theDataset);
	}
	
	@Override
	public String toString() {
		return "[DatasetCreate dataset:" + theDataset.getId() + "]";
	}
}