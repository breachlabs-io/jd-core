/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.model.javasyntax.type;

public class ObjectType implements Type {
    public static final ObjectType TYPE_BOOLEAN           = new ObjectType("java/lang/Boolean", "java.lang.Boolean", "Boolean");
    public static final ObjectType TYPE_BYTE              = new ObjectType("java/lang/Byte", "java.lang.Byte", "Byte");
    public static final ObjectType TYPE_CHARACTER         = new ObjectType("java/lang/Character", "java.lang.Character", "Character");
    public static final ObjectType TYPE_CLASS             = new ObjectType("java/lang/Class", "java.lang.Class", "Class");
    public static final ObjectType TYPE_DOUBLE            = new ObjectType("java/lang/Double", "java.lang.Double", "Double");
    public static final ObjectType TYPE_EXCEPTION         = new ObjectType("java/lang/Exception", "java.lang.Exception", "Exception");
    public static final ObjectType TYPE_FLOAT             = new ObjectType("java/lang/Float", "java.lang.Float", "Float");
    public static final ObjectType TYPE_INTEGER           = new ObjectType("java/lang/Integer", "java.lang.Integer", "Integer");
    public static final ObjectType TYPE_LONG              = new ObjectType("java/lang/Long", "java.lang.Long", "Long");
    public static final ObjectType TYPE_MATH              = new ObjectType("java/lang/Math", "java.lang.Math", "Math");
    public static final ObjectType TYPE_OBJECT            = new ObjectType("java/lang/Object", "java.lang.Object", "Object");
    public static final ObjectType TYPE_RUNTIME_EXCEPTION = new ObjectType("java/lang/RuntimeException", "java.lang.RuntimeException", "RuntimeException");
    public static final ObjectType TYPE_SHORT             = new ObjectType("java/lang/Short", "java.lang.Short", "Short");
    public static final ObjectType TYPE_STRING            = new ObjectType("java/lang/String", "java.lang.String", "String");
    public static final ObjectType TYPE_STRING_BUFFER     = new ObjectType("java/lang/StringBuffer", "java.lang.StringBuffer", "StringBuffer");
    public static final ObjectType TYPE_STRING_BUILDER    = new ObjectType("java/lang/StringBuilder", "java.lang.StringBuilder", "StringBuilder");
    public static final ObjectType TYPE_SYSTEM            = new ObjectType("java/lang/System", "java.lang.System", "System");
    public static final ObjectType TYPE_THREAD            = new ObjectType("java/lang/Thread", "java.lang.Thread", "Thread");
    public static final ObjectType TYPE_THROWABLE         = new ObjectType("java/lang/Throwable", "java.lang.Throwable", "Throwable");

    public static final ObjectType TYPE_PRIMITIVE_BOOLEAN = new ObjectType("Z");
    public static final ObjectType TYPE_PRIMITIVE_BYTE    = new ObjectType("B");
    public static final ObjectType TYPE_PRIMITIVE_CHAR    = new ObjectType("C");
    public static final ObjectType TYPE_PRIMITIVE_DOUBLE  = new ObjectType("D");
    public static final ObjectType TYPE_PRIMITIVE_FLOAT   = new ObjectType("F");
    public static final ObjectType TYPE_PRIMITIVE_INT     = new ObjectType("I");
    public static final ObjectType TYPE_PRIMITIVE_LONG    = new ObjectType("J");
    public static final ObjectType TYPE_PRIMITIVE_SHORT   = new ObjectType("S");
    public static final ObjectType TYPE_PRIMITIVE_VOID    = new ObjectType("V");

    public static final ObjectType TYPE_UNDEFINED_OBJECT = new ObjectType("java/lang/Object", "java.lang.Object", "Object") {
        @Override public String toString() { return "UndefinedObjectType"; }
    };

    protected String internalName;
    protected String qualifiedName;
    protected String name;

    protected BaseTypeArgument typeArguments;
    protected int dimension;
    protected String descriptor;

    public ObjectType(String internalName, String qualifiedName, String name) {
        this(internalName, qualifiedName, name, null, 0);
    }

    public ObjectType(String internalName, String qualifiedName, String name, int dimension) {
        this(internalName, qualifiedName, name, null, dimension);
    }

    public ObjectType(String internalName, String qualifiedName, String name, BaseTypeArgument typeArguments) {
        this(internalName, qualifiedName, name, typeArguments, 0);
    }

    public ObjectType(String internalName, String qualifiedName, String name, BaseTypeArgument typeArguments, int dimension) {
        this.internalName = internalName;
        this.qualifiedName = qualifiedName;
        this.name = name;
        this.typeArguments = typeArguments;
        this.dimension = dimension;
        this.descriptor = createDescriptor("L" + internalName + ';', dimension);

        assert (internalName != null) && !internalName.endsWith(";");
    }

    public ObjectType(String primitiveDescriptor) {
        this(primitiveDescriptor, 0);
    }

    public ObjectType(String primitiveDescriptor, int dimension) {
        this.internalName = primitiveDescriptor;
        this.qualifiedName = this.name = PrimitiveType.getPrimitiveType(primitiveDescriptor.charAt(0)).getName();
        this.dimension = dimension;
        this.descriptor = createDescriptor(primitiveDescriptor, dimension);
    }

    protected static String createDescriptor(String descriptor, int dimension) {
        switch (dimension) {
            case 0:
                return descriptor;
            case 1:
                return "[" + descriptor;
            case 2:
                return "[[" + descriptor;
            default:
                return new String(new char[dimension]).replaceAll("\0", "[") + descriptor;
        }
    }

    public ObjectType(ObjectType ot) {
        this.internalName = ot.getInternalName();
        this.qualifiedName = ot.getQualifiedName();
        this.name = ot.getName();
        this.typeArguments = ot.getTypeArguments();
        this.dimension = ot.getDimension();
        this.descriptor = ot.getDescriptor();
    }

    public String getInternalName() {
        return internalName;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    @Override
    public String getName() {
        return name;
    }

    public BaseTypeArgument getTypeArguments() {
        return typeArguments;
    }

    @Override
    public String getDescriptor() {
        return descriptor;
    }

    @Override
    public int getDimension() {
        return dimension;
    }

    @Override
    public Type createType(int dimension) {
        assert dimension >= 0 : "ObjectType.createType(dim) : create type with zero or negative dimension";

        if (this.dimension == dimension) {
            return this;
        } else if (descriptor.charAt(descriptor.length()-1) != ';') {
            // Array of primitive types
            if (dimension == 0) {
                return PrimitiveType.getPrimitiveType(descriptor.charAt(this.dimension));
            } else {
                return new ObjectType(internalName, dimension);
            }
        } else {
            // Object type or array of object types
            return new ObjectType(internalName, qualifiedName, name, typeArguments, dimension);
        }
    }

    public ObjectType createType(BaseTypeArgument typeArguments) {
        return new ObjectType(internalName, qualifiedName, name, typeArguments, dimension);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObjectType that = (ObjectType) o;

        if (dimension != that.dimension) return false;
        if (!internalName.equals(that.internalName)) return false;

        if ("java/lang/Class".equals(internalName)) {
            boolean wildcard1 = (typeArguments == null) || (typeArguments.getClass() == WildcardTypeArgument.class);
            boolean wildcard2 = (that.typeArguments == null) || (that.typeArguments.getClass() == WildcardTypeArgument.class);

            if (wildcard1 && wildcard2) {
                return true;
            }
        }

        return typeArguments != null ? typeArguments.equals(that.typeArguments) : that.typeArguments == null;
    }

    @Override
    public int hashCode() {
        int result = internalName.hashCode();
        result = 31 * result + (typeArguments != null ? typeArguments.hashCode() : 0);
        result = 31 * result + dimension;
        return result;
    }

    @Override
    public void accept(TypeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean isTypeArgumentAssignableFrom(BaseTypeArgument typeArgument) {
        if (typeArgument.getClass() != ObjectType.class) {
            return false;
        }

        ObjectType ot = (ObjectType)typeArgument;

        if ((dimension != ot.getDimension()) || !internalName.equals(ot.getInternalName())) {
            return false;
        }

        if (ot.getTypeArguments() == null) {
            return (typeArgument == null);
        } else if (typeArgument == null) {
            return false;
        } else {
            return typeArguments.isTypeArgumentAssignableFrom(ot.getTypeArguments());
        }
    }

    @Override
    public boolean isObject() {
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ObjectType{");

        sb.append(internalName);

        if (typeArguments != null) {
            sb.append('<').append(typeArguments).append('>');
        }

        if (dimension > 0) {
            sb.append(", dimension=").append(dimension);
        }

        return sb.append('}').toString();
    }
}
