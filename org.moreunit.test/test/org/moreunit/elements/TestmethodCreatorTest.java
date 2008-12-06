package org.moreunit.elements;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.moreunit.SimpleProjectTestCase;
import org.moreunit.WorkspaceHelper;
import org.moreunit.preferences.PreferenceConstants;

/**
 * @author vera
 *
 * 02.08.2007 07:37:24
 */
public class TestmethodCreatorTest extends SimpleProjectTestCase {

	public void testCreateFirstTestMethodJUnit3() throws CoreException {
		IType cutType = WorkspaceHelper.createJavaClass(sourcesPackage, "Hello");
		IType testcaseType = WorkspaceHelper.createJavaClass(testPackage, "HelloTest");
		IMethod getNumberOneMethod = WorkspaceHelper.createMethodInJavaType(cutType, "public int getNumberOne()", "return 1");

		TestmethodCreator testmethodCreator = new TestmethodCreator(cutType.getCompilationUnit(), PreferenceConstants.TEST_TYPE_VALUE_JUNIT_3);
		IMethod createTestMethod = testmethodCreator.createTestMethod(getNumberOneMethod);
		
		assertEquals("testGetNumberOne", createTestMethod.getElementName());
		assertFalse(createTestMethod.getSource().startsWith("@Test"));
		
		IMethod[] methods = testcaseType.getMethods();
		assertEquals(1, methods.length);
		assertEquals(createTestMethod, methods[0]);
	}
	
	public void testCreateFirstTestMethodJUnit4() throws CoreException {
		IType cutType = WorkspaceHelper.createJavaClass(sourcesPackage, "Hello");
		IType testcaseType = WorkspaceHelper.createJavaClass(testPackage, "HelloTest");
		IMethod getNumberOneMethod = WorkspaceHelper.createMethodInJavaType(cutType, "public int getNumberOne()", "return 1");

		TestmethodCreator testmethodCreator = new TestmethodCreator(cutType.getCompilationUnit(), PreferenceConstants.TEST_TYPE_VALUE_JUNIT_4);
		IMethod createTestMethod = testmethodCreator.createTestMethod(getNumberOneMethod);
		
		assertEquals("testGetNumberOne", createTestMethod.getElementName());
		assertTrue(createTestMethod.getSource().startsWith("@Test"));
		
		IMethod[] methods = testcaseType.getMethods();
		assertEquals(1, methods.length);
		assertEquals(createTestMethod, methods[0]);
	}

	public void testCreateSecondTestMethodJUnit3() throws CoreException {
		IType cutType = WorkspaceHelper.createJavaClass(sourcesPackage, "Hello");
		IType testcaseType = WorkspaceHelper.createJavaClass(testPackage, "HelloTest");
		WorkspaceHelper.createMethodInJavaType(cutType, "public int getNumberOne()", "return 1");
		IMethod testMethod = WorkspaceHelper.createMethodInJavaType(testcaseType, "public void testGetNumberOne()", "");
		
		TestmethodCreator testmethodCreator = new TestmethodCreator(testcaseType.getCompilationUnit(), PreferenceConstants.TEST_TYPE_VALUE_JUNIT_3);
		IMethod createTestMethod = testmethodCreator.createTestMethod(testMethod);
		assertEquals("testGetNumberOneSuffix", createTestMethod.getElementName());
		assertFalse(createTestMethod.getSource().startsWith("@Test"));
		
		IMethod[] methods = testcaseType.getMethods();
		assertEquals(2, methods.length);
	}
	
	public void testCreateSecondTestMethodJUnit4() throws CoreException {
		IType cutType = WorkspaceHelper.createJavaClass(sourcesPackage, "Hello");
		IType testcaseType = WorkspaceHelper.createJavaClass(testPackage, "HelloTest");
		WorkspaceHelper.createMethodInJavaType(cutType, "public int getNumberOne()", "return 1");
		IMethod testMethod = WorkspaceHelper.createMethodInJavaType(testcaseType, "public void testGetNumberOne()", "");
		
		TestmethodCreator testmethodCreator = new TestmethodCreator(testcaseType.getCompilationUnit(), PreferenceConstants.TEST_TYPE_VALUE_JUNIT_4);
		IMethod createTestMethod = testmethodCreator.createTestMethod(testMethod);
		assertEquals("testGetNumberOneSuffix", createTestMethod.getElementName());
		assertTrue(createTestMethod.getSource().startsWith("@Test"));
		
		IMethod[] methods = testcaseType.getMethods();
		assertEquals(2, methods.length);		
	}

	public void testGetTestMethodName() 
	{
		assertEquals("testName", TestmethodCreator.getTestMethodName("name"));
		assertEquals("testName", TestmethodCreator.getTestMethodName("Name"));
	}
}