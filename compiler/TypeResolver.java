package compiler;

import ast.*;
import entity.*;
import type.CompositeType;
import type.Type;
import type.TypeTable;
import utils.ErrorHandler;

import java.util.List;

/**
 * Created by yangj on 2017/5/29.
 */
public class TypeResolver extends CompilerVisitor
        implements EntityVisitor<Void>, DeclarationVisitor<Void> {
    private final TypeTable typeTable;
    private final ErrorHandler errorHandler;

    public TypeResolver(TypeTable typeTable, ErrorHandler errorHandler) {
        this.typeTable = typeTable;
        this.errorHandler = errorHandler;
    }

    public void resolve(AST ast) {
        defineTypes(ast.types());
        for (TypeDefinition t : ast.types()) {
            t.accept(this);
        }
        for (Entity e : ast.entities()) {
            e.accept(this);
        }
    }

    private void defineTypes(List<TypeDefinition> typeDefinitionList) {
        for (TypeDefinition def : typeDefinitionList) {
            if (typeTable.isDefined(def.typeRef())) {
                error(def, "duplicated type definition: " + def.typeRef());
            } else {
                typeTable.put(def.typeRef(), def.definingType());
            }
        }
    }

    private void bindType(TypeNode node) {
        if (node.isResolved())
            return;
        node.setType(typeTable.get(node.typeRef()));
    }

    public Void visit(StructNode node) {
        resolveCompositeType(node);
        return null;
    }

    public Void visit(UnionNode node) {
        resolveCompositeType(node);
        return null;
    }

    public void resolveCompositeType(CompositeTypeDefinition definition) {
        CompositeType ct = (CompositeType) typeTable.get(definition.typeNode().typeRef());
        if (ct == null) {
            throw new Error("cannot intern sturct/union: " + definition.name());
        }
        for (Slot s : ct.members()) {
            bindType(s.typeNode());
        }
    }

<<<<<<< HEAD
    public Void visit(TypedefNode typedefNode) {
        bindType(typedefNode.typeNode());
        bindType(typedefNode.realTypeNode());
        return null;
    }

=======
>>>>>>> master
    public Void visit(DefinedVariable variable) {
        bindType(variable.typeNode());
        if (variable.hasInitializer()) {
            visitExpr(variable.initializer());
        }
        return null;
    }

    public Void visit(UndefinedVariable variable) {
        bindType(variable.typeNode());
        return null;
    }

    public Void visit(Constant c) {
        bindType(c.typeNode());
        visitExpr(c.value());
        return null;
    }

    public Void visit(DefinedFunction func) {
        resolveFunctionHeader(func);
        visitStmt(func.body());
        return null;
    }

    public Void visit(UndefinedFunction func) {
        resolveFunctionHeader(func);
        return null;
    }

    private void resolveFunctionHeader(Function func) {
        bindType(func.typeNode());
        for (Parameter param : func.parameters()) {
            // arrays must be converted to pointers in a function parameter.
            Type t = typeTable.getParamType(param.typeNode().typeRef());
            param.typeNode().setType(t);
        }
    }
    // #@@}

    //
    // Expressions
    //

    public Void visit(BlockNode node) {
        for (DefinedVariable var : node.variables()) {
            var.accept(this);
        }
        visitStmts(node.stmts());
        return null;
    }

    public Void visit(CastNode node) {
        bindType(node.typeNode());
        super.visit(node);
        return null;
    }

    public Void visit(SizeofExprNode node) {
        bindType(node.typeNode());
        super.visit(node);
        return null;
    }

    public Void visit(SizeofTypeNode node) {
        bindType(node.operandTypeNode());
        bindType(node.typeNode());
        super.visit(node);
        return null;
    }

    public Void visit(IntegerLiteralNode node) {
        bindType(node.typeNode());
        return null;
    }

    public Void visit(StringLiteralNode node) {
        bindType(node.typeNode());
        return null;
    }

    private void error(Node node, String msg) {
        errorHandler.error(node.location(), msg);
    }


}
