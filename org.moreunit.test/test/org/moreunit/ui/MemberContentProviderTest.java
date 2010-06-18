package org.moreunit.ui;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.junit.Before;
import org.junit.Test;

public class MemberContentProviderTest
{

    private Set<IType> types;
    private Set<IMethod> methods;

    @Before
    public void setUp()
    {
        types = new LinkedHashSet<IType>();
        methods = new LinkedHashSet<IMethod>();
    }

    @Test
    public void testGetElementsWhenNoTypeNorMethodIsProvided()
    {
        Object[] elements = new MemberContentProvider(types, methods).getElements(null);
        assertEquals(0, elements.length);
    }

    @Test
    public void testGetElementsWhenNoTypeHasMethods()
    {
        types.add(mockType("type2"));
        types.add(mockType("type3"));
        types.add(mockType("type1"));

        MemberContentProvider contentProvider = new MemberContentProvider(types, methods);

        Object[] elements = contentProvider.getElements(null);
        assertEquals(3, elements.length);
        assertEquals("type1", ((IType) elements[0]).getElementName());
        assertEquals("type2", ((IType) elements[1]).getElementName());
        assertEquals("type3", ((IType) elements[2]).getElementName());
    }

    @Test
    public void testGetElementsWhenEveryTypeHasMethods()
    {
        IType type2 = mockType("type2");
        types.add(type2);

        IType type1 = mockType("type1");
        types.add(type1);

        methods.add(mockMethod(type1, "method1B"));
        methods.add(mockMethod(type2, "method2A"));
        methods.add(mockMethod(type1, "method1A"));

        MemberContentProvider contentProvider = new MemberContentProvider(types, methods);

        Object[] elements = contentProvider.getElements(null);
        assertEquals(2, elements.length);
        assertEquals("type1", ((IType) elements[0]).getElementName());
        assertEquals("type2", ((IType) elements[1]).getElementName());

        Object[] children1 = contentProvider.getChildren(elements[0]);
        assertEquals(2, children1.length);
        assertEquals("method1A", ((IMethod) children1[0]).getElementName());
        assertEquals("method1B", ((IMethod) children1[1]).getElementName());

        Object[] children2 = contentProvider.getChildren(elements[1]);
        assertEquals(1, children2.length);
        assertEquals("method2A", ((IMethod) children2[0]).getElementName());
    }

    @Test
    public void testGetElementsWithSomeMethodTypesNotInGivenTypeSet()
    {
        IType type1 = mockType("type1");
        types.add(type1);

        IType type2 = mockType("type2"); // not added to type set
        methods.add(mockMethod(type2, "method2A"));

        MemberContentProvider contentProvider = new MemberContentProvider(types, methods);

        Object[] elements = contentProvider.getElements(null);
        assertEquals(2, elements.length);
        assertEquals("type1", ((IType) elements[0]).getElementName());
        assertEquals("type2", ((IType) elements[1]).getElementName());

        Object[] children2 = contentProvider.getChildren(elements[1]);
        assertEquals(1, children2.length);
        assertEquals("method2A", ((IMethod) children2[0]).getElementName());
    }

    @Test
    public void testGetElementsWhithBothTypesWithoutMethodsAndTypesHavingMethods()
    {
        IType type3 = mockType("type3");
        IType type1 = mockType("type1");

        methods.add(mockMethod(type1, "method1B"));
        methods.add(mockMethod(type3, "method3A"));
        methods.add(mockMethod(type1, "method1A"));

        IType type2 = mockType("type2");
        types.add(type2);

        MemberContentProvider contentProvider = new MemberContentProvider(types, methods);

        Object[] elements = contentProvider.getElements(null);
        assertEquals(3, elements.length);
        assertEquals("type2", ((IType) elements[0]).getElementName());
        assertEquals("type1", ((IType) elements[1]).getElementName());
        assertEquals("type3", ((IType) elements[2]).getElementName());

        Object[] children1 = contentProvider.getChildren(elements[1]);
        assertEquals(2, children1.length);
        assertEquals("method1A", ((IMethod) children1[0]).getElementName());
        assertEquals("method1B", ((IMethod) children1[1]).getElementName());

        Object[] children3 = contentProvider.getChildren(elements[2]);
        assertEquals(1, children3.length);
        assertEquals("method3A", ((IMethod) children3[0]).getElementName());
    }

    private IType mockType(String typeName)
    {
        IType mock = createNiceMock(IType.class);
        expect(mock.getElementName()).andStubReturn(typeName);
        replay(mock);
        return mock;
    }

    private IMethod mockMethod(IType declaringType, String methodName)
    {
        IMethod mock = createNiceMock(IMethod.class);
        expect(mock.getElementName()).andStubReturn(methodName);
        expect(mock.getDeclaringType()).andStubReturn(declaringType);
        replay(mock);
        return mock;
    }
}
