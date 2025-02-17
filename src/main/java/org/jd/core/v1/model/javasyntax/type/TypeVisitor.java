/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.model.javasyntax.type;

public interface TypeVisitor {
    void visit(ArrayTypeArguments type);
    void visit(DiamondTypeArgument type);
    void visit(WildcardExtendsTypeArgument type);
    void visit(PrimitiveType type);
    void visit(ObjectType type);
    void visit(InnerObjectType type);
    void visit(Types types);
    void visit(TypeBounds type);
    void visit(TypeParameter type);
    void visit(TypeParameterWithTypeBounds type);
    void visit(TypeParameters types);
    void visit(WildcardSuperTypeArgument type);
    void visit(GenericType type);
    void visit(WildcardTypeArgument type);
}
