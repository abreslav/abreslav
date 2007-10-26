package runtime.pascal.interpreter;

import org.junit.Test;
import static org.junit.Assert.*;

import pascal.types.*;
import runtime.SimpleEvaluatorContext;
import runtime.tree.IVisitHandler;
import runtime.tree.expressions.*;
import runtime.tree.statements.*;
import core.*;

public class SampleTest {

	private static final class Unit {
		Instance a;
		Instance b;
		Instance c;
		
		@SuppressWarnings("static-access")
		public Integer get_a() {
			return IntegerType.INTEGER.F_THIS.readValue(a);
		}

		@SuppressWarnings("static-access")
		public Integer get_b() {
			return IntegerType.INTEGER.F_THIS.readValue(b);
		}

		@SuppressWarnings("static-access")
		public Double get_c() {
			return RealType.REAL.F_THIS.readValue(c);
		}
	}

	private static final class UnitType extends ObjectType<Unit> {
		Instance.DataField<Unit> field = new Instance.DataField<Unit>();
	
		@Override
		protected IField[] doGetFields() {
			return new IField[] {
				new FieldDescriptor(IntegerType.INTEGER, "a", this) {
				
					public Instance readValue(Instance host) {
						return field.readValue(host).a;
					}
				
					public void writeValue(Instance host, Instance value) {
						field.readValue(host).a = value;
					}
				},
				new FieldDescriptor(IntegerType.INTEGER, "b", this) {
				
					public Instance readValue(Instance host) {
						return field.readValue(host).b;
					}
				
					public void writeValue(Instance host, Instance value) {
						field.readValue(host).b = value;
					}
				},
				new FieldDescriptor(RealType.REAL, "c", this) {
				
					public Instance readValue(Instance host) {
						return field.readValue(host).c;
					}
				
					public void writeValue(Instance host, Instance value) {
						field.readValue(host).c = value;
					}
				} 
			};
		}
	
		@Override
		protected IMethod[] doGetMethods() {
			return null;
		}
	
		public Instance getDefaultValue() {
			return null;
		}
	}
	
	@Test
	public void testSample() throws Exception {
		final SimpleEvaluatorContext context = new SimpleEvaluatorContext();
		final PascalInterpreter interpreter = new PascalInterpreter(context);
	
		// Creates unit type
		final UnitType unitType = new UnitType();
		final Unit unit = new Unit();
		final Instance unitInstance = unitType.createInstance(unit);
		final int unitId = context.addInstance(unitInstance);
	
		// Puts constants to the context 
		int id_1 = context.addInstance(IntegerType.INTEGER.createInstance(1));
		int id_0 = context.addInstance(IntegerType.INTEGER.createInstance(0));
		
		// Creates counters pool
		final VisitCounters visitCounters = new VisitCounters();
	
		// Constructs runtime tree
		Block body = RuntimeTreeNodeFactory.INSTANCE.createBlock(
			RuntimeTreeNodeFactory.addAfterHandler(
				RuntimeTreeNodeFactory.INSTANCE.createAssignment(
					RuntimeTreeNodeFactory.INSTANCE.createFieldAccess(
						new InstanceAccess(unitId),
						unitType.lookupField("a")
					),
					RuntimeTreeNodeFactory.INSTANCE.createInstanceAccess(id_1)
				), 
				new IVisitHandler() {
					public void run() {
					{{
					  	assertEquals(1, unit.get_a());
					  }}
					}
				}
			),
			RuntimeTreeNodeFactory.addAfterHandler(
				RuntimeTreeNodeFactory.INSTANCE.createIf(
					RuntimeTreeNodeFactory.INSTANCE.createFunctionCall(
						RuntimeTreeNodeFactory.INSTANCE.createFieldAccess(
							new InstanceAccess(unitId),
							unitType.lookupField("a")
						),
						OrdinalType.GT,
						RuntimeTreeNodeFactory.INSTANCE.createInstanceAccess(id_0)
					),
					RuntimeTreeNodeFactory.addAfterHandler(
						RuntimeTreeNodeFactory.INSTANCE.createAssignment(
							RuntimeTreeNodeFactory.INSTANCE.createFieldAccess(
								new InstanceAccess(unitId),
								unitType.lookupField("a")
							),
							RuntimeTreeNodeFactory.INSTANCE.createFunctionCall(
								RuntimeTreeNodeFactory.INSTANCE.createFieldAccess(
									new InstanceAccess(unitId),
									unitType.lookupField("a")
								),
								IntegerType.ADD,
								RuntimeTreeNodeFactory.INSTANCE.createInstanceAccess(id_1)
							)
						), 
						visitCounters.addCounter(1, "some")
					),
					null
				), 
				visitCounters.addCounter(1, "if")
			),
			RuntimeTreeNodeFactory.addAfterHandler(
				RuntimeTreeNodeFactory.INSTANCE.createWhile(
					RuntimeTreeNodeFactory.INSTANCE.createFunctionCall(
						RuntimeTreeNodeFactory.INSTANCE.createFieldAccess(
							new InstanceAccess(unitId),
							unitType.lookupField("a")
						),
						OrdinalType.GT,
						RuntimeTreeNodeFactory.INSTANCE.createInstanceAccess(id_0)
					),
					RuntimeTreeNodeFactory.INSTANCE.createAssignment(
						RuntimeTreeNodeFactory.INSTANCE.createFieldAccess(
							new InstanceAccess(unitId),
							unitType.lookupField("a")
						),
						RuntimeTreeNodeFactory.INSTANCE.createFunctionCall(
							RuntimeTreeNodeFactory.INSTANCE.createFieldAccess(
								new InstanceAccess(unitId),
								unitType.lookupField("a")
							),
							IntegerType.SUB,
							RuntimeTreeNodeFactory.INSTANCE.createInstanceAccess(id_1)
						)
					)
				), 
				visitCounters.addCounter(1, null)
			),
			RuntimeTreeNodeFactory.INSTANCE.createIf(
				RuntimeTreeNodeFactory.INSTANCE.createFunctionCall(
					RuntimeTreeNodeFactory.INSTANCE.createFieldAccess(
						new InstanceAccess(unitId),
						unitType.lookupField("a")
					),
					OrdinalType.GT,
					RuntimeTreeNodeFactory.INSTANCE.createInstanceAccess(id_0)
				),
				RuntimeTreeNodeFactory.INSTANCE.createAssignment(
					RuntimeTreeNodeFactory.INSTANCE.createFieldAccess(
						new InstanceAccess(unitId),
						unitType.lookupField("a")
					),
					RuntimeTreeNodeFactory.INSTANCE.createFunctionCall(
						RuntimeTreeNodeFactory.INSTANCE.createFieldAccess(
							new InstanceAccess(unitId),
							unitType.lookupField("a")
						),
						IntegerType.ADD,
						RuntimeTreeNodeFactory.INSTANCE.createInstanceAccess(id_1)
					)
				),
				RuntimeTreeNodeFactory.INSTANCE.createAssignment(
					RuntimeTreeNodeFactory.INSTANCE.createFieldAccess(
						new InstanceAccess(unitId),
						unitType.lookupField("a")
					),
					RuntimeTreeNodeFactory.INSTANCE.createFunctionCall(
						RuntimeTreeNodeFactory.INSTANCE.createFieldAccess(
							new InstanceAccess(unitId),
							unitType.lookupField("a")
						),
						IntegerType.ADD,
						RuntimeTreeNodeFactory.INSTANCE.createFunctionCall(
							RuntimeTreeNodeFactory.INSTANCE.createInstanceAccess(id_1),
							IntegerType.NEG
						)
					)
				)
			)
		);
	
		// Executes the constructed tree
		interpreter.execute(body);
		
		// Asserts on counter values
		visitCounters.assertCounters();
	}
}