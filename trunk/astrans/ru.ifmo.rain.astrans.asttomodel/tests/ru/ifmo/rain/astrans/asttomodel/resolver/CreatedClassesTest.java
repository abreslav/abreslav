package ru.ifmo.rain.astrans.asttomodel.resolver;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import astrans.AstransFactory;
import astrans.CreateClass;
import astrans.CreatedEClass;
import astransast.AstransastFactory;
import astransast.QualifiedName;

public class CreatedClassesTest {

	private static final String NAME = "class";
	private CreatedClasses classes;
	private CreateClass createClass;
	private QualifiedName name;

	@Before
	public void setUp() throws Exception {
		classes = new CreatedClasses();
		createClass = AstransFactory.eINSTANCE.createCreateClass();
		createClass.setName(NAME);
		classes.add(createClass);
		
		name = AstransastFactory.eINSTANCE.createQualifiedName();
		name.setName(NAME);
	}

	@Test
	public final void testAdd() {
		assertSame(createClass, classes.getEClassifierReference(name).getCreate());
	}

	@Test
	public void testGetEClassifierReference() {
		CreatedEClass ref1 = classes.getEClassifierReference(name);
		CreatedEClass ref2 = classes.getEClassifierReference(name);
		assertTrue(ref1 != ref2);
		assertSame(ref1.getCreate(), ref2.getCreate());
	}
}
