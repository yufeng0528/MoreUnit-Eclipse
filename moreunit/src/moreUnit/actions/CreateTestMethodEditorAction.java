package moreUnit.actions;

import moreUnit.handler.MoreUnitActionHandler;
import moreUnit.log.LogHandler;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

public class CreateTestMethodEditorAction implements IEditorActionDelegate {
	
	IEditorPart editorPart;

	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		editorPart = targetEditor;
	}

	public void run(IAction action) {
		LogHandler.getInstance().handleInfoLog("CreateTestMethodEditorAction.run()");
		MoreUnitActionHandler.getInstance().executeCreateTestMethodAction(editorPart);
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}
}

// $Log: not supported by cvs2svn $
// Revision 1.2  2006/01/19 21:39:44  gianasista
// Added CVS-commit-logging to all java-files
//