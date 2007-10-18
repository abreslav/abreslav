package core;

public abstract class FieldDescriptor {

	private final IType fieldType;
	private final IType declaringType;
	private final String name;
	
	public FieldDescriptor(IType fieldType, String name, IType declaringType) {
		this.fieldType = fieldType;
		this.name = name;
		this.declaringType = declaringType;
	}

	public IType getFieldType() {
		return fieldType;
	}

	public IType getDeclaringType() {
		return declaringType;
	}
	
	public String getName() {
		return name;
	}
	
	public abstract IFunction getReadFunction();
	public abstract IFunction getWriteFunction();
}