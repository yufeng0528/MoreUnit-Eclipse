package org.moreunit.elements;


import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.moreunit.log.LogHandler;
import org.moreunit.preferences.Preferences;
import org.moreunit.util.MagicNumbers;

/**
 * JavaProjectFacade offers easy access to {@link IJavaProject}
 * 
 * @author vera
 * 30.01.2006 20:30:54
 */
public class JavaProjectFacade {
	
	private IJavaProject javaProject;
	
	public JavaProjectFacade(IJavaProject javaProject) {
		this.javaProject = javaProject;
	}

	public IPackageFragmentRoot getJUnitSourceFolder() {
		try {
			IPackageFragmentRoot[] packageFragmentRoots = javaProject.getPackageFragmentRoots();
			for(int i=0; i<packageFragmentRoots.length; i++) {
				IPackageFragmentRoot packageFragmentRoot = packageFragmentRoots[i];
				String junitFolder = Preferences.instance().getJunitDirectoryFromPreferences();
				if(packageFragmentRoot instanceof IPackageFragmentRoot && packageFragmentRoot.getElementName().equals(junitFolder))
					return packageFragmentRoot;
			}
		} catch (JavaModelException exc) {
			LogHandler.getInstance().handleExceptionLog(exc);
		}
		
		return null;
	}
	
	public IType[] getTestCasesFromJavaProject() throws JavaModelException {
		IType testCaseType = javaProject.findType("junit.framework.TestCase");
		if(testCaseType == null)
			return null;

		ITypeHierarchy hierarchy= testCaseType.newTypeHierarchy(javaProject, new NullProgressMonitor());
		IType[] testCaseListe = hierarchy.getAllSubtypes(testCaseType);
		return testCaseListe;
	}
	
	public void deleteTestCaseMarkers() {
		try {
			javaProject.getProject().deleteMarkers(MagicNumbers.TEST_CASE_MARKER, true, IResource.DEPTH_INFINITE);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	public void addTestCaseMarkers() {
		if(!javaProject.isOpen())
			return;
		
		try {
			IType[] testCaseListe = (new JavaProjectFacade(javaProject)).getTestCasesFromJavaProject();

			if(testCaseListe == null)
				return;
			
			for(int i=0; i<testCaseListe.length; i++) {
				IType testCase = testCaseListe[i];
				ICompilationUnit compilationUnit = testCase.getCompilationUnit();
				if(compilationUnit != null)
					(new TestCaseTypeFacade(compilationUnit)).createMarkerForTestedClass();
				else
					LogHandler.getInstance().handleInfoLog("JavaProjectFacade.addTestCaseMarkers(): CompilatioUnit is null "+testCase.getFullyQualifiedName());
			}
		} catch (JavaModelException e) {
			LogHandler.getInstance().handleExceptionLog(e);
		} catch (CoreException e) {
			LogHandler.getInstance().handleExceptionLog(e);
		} catch (NullPointerException e) {
			LogHandler.getInstance().handleExceptionLog(e);
		}
	}	
	
}

// $Log: not supported by cvs2svn $
// Revision 1.1  2006/06/22 20:22:29  gianasista
// package rename
//
// Revision 1.1  2006/06/19 20:08:48  gianasista
// CVS Refactoring
//
// Revision 1.7  2006/05/23 19:38:33  gianasista
// Splitted JavaFileFacade into two classes
//
// Revision 1.6  2006/05/21 10:58:07  gianasista
// moved prefs to Preferences class
//
// Revision 1.5  2006/05/12 17:52:38  gianasista
// added comments
//
// Revision 1.4  2006/04/14 17:14:22  gianasista
// Refactoring Support with dialog
//
// Revision 1.3  2006/02/22 21:30:21  gianasista
// Bugfix: Statement null-save
//
// Revision 1.2  2006/01/31 19:05:54  gianasista
// Refactored MarkerTools and added methods to corresponding facade classes.
//
// Revision 1.1  2006/01/30 21:12:32  gianasista
// Further Refactorings (moved methods from singleton classes like PluginTools to facade classes)
//