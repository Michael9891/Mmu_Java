package hit.controller;

import hit.model.Model;
import hit.model.MMUModel;
import hit.view.MMUView;
import hit.view.View;

public class MMUController implements Controller {
	private Model model;
	private View view;
	private boolean fileIsFound;

	public MMUController(Model model, View view) {
		this.model = model;
		this.view = view;
		((MMUView) view).setController(this);
	}
	//get commands from local file
	public void inst(){
		((MMUView) view).setConfiguration(((MMUModel) model).getCommands());          //model transfer command from file
		((MMUView) view).setRamCapacity(((MMUModel) model).getRamCapacity());
	}

	//get command from server
	public void instRemote() {
		String[] s = null;
		s = ((MMUView) view).getDetails();
		fileIsFound = ((MMUModel) model).fileIsFound(s); // get data from user
															// and search file
															// in server
		if (!fileIsFound) // if file not found ask the user if he want try again
			((MMUView) view).openDialog();
		else {
			((MMUView) view).setConfiguration(((MMUModel) model).getCommands()); // model
																					// transfer
																					// command
																					// from
																					// file
			((MMUView) view).setRamCapacity(((MMUModel) model).getRamCapacity());
		}

	}
}
