package compiler;

import ast.*;
import entity.DefinedFunction;
import entity.DefinedVariable;
import exception.SemanticError;
import exception.SemanticException;
import type.CompositeType;
import type.Type;
import type.TypeTable;
import utils.ErrorHandler;

/**
 * Created by yangj on 2017/5/29.
 */
public class DereferenceChecker extends CompilerVisitor {
    private final TypeTable typeTable;
    private final ErrorHandler errorHandler;

    public DereferenceChecker(TypeTable typeTable, ErrorHandler errorHandler) {
        this.typeTable = typeTable;
        this.errorHandler = errorHandler;
    }

    public void check(AST ast) throws SemanticException {
        for (DefinedVariable var : ast.definedVariables()) {
            checkToplevelVariable(var);
        }
        for (DefinedFunction func : ast.definedFunctions()) {
            check(func.body());
        }
        if (errorHandler.errorOccured()) {
            throw new SemanticException("Compile failed");
        }
    }

    private void checkToplevelVariable(DefinedVariable var) {
        checkVariable(var);
        if (var.hasInitializer()) {
            //TODO: what is initializer
            checkConstant(var.initializer());
        }
    }

    private void checkConstant(ExprNode node) {
        if (!node.isConstant()) {
            errorHandler.error(node.location(), "not a constant");
        }
    }

    private void check(StmtNode node) {
        node.accept(this);
    }

    private void check(ExprNode node) {
        node.accept(this);
    }

    public Void visit(BlockNode node) {
        for (DefinedVariable var : node.variables()) {
            checkVariable(var);
        }
        for (StmtNode stmt : node.stmts()) {
            try {
                check(stmt);
            } catch (SemanticError err) {
                ;
            }
        }
        return null;
    }

    private void checkVariable(DefinedVariable var) {
        if (var.hasInitializer()) {
            try {
                check(var.initializer());
            } catch (SemanticError err) {
                ;
            }
        }
    }

    public Void visit(AssignNode node) {
        super.visit(node);
        checkAssignment(node);
        return null;
    }

    public Void visit(OpAssignNode node) {
        super.visit(node);
        checkAssignment(node);
        return null;
    }

    private void checkAssignment(AbstractAssignNode node) {
        if (!node.lhs().isAssignable()) {
            semanticError(node.location(), "invalid lhs expression");
        }
    }

    public Void visit(PrefixOpNode node) {
        super.visit(node);
        if (!node.expr().isAssignable()) {
            semanticError(node.expr().location(), "cannot increment/decrement");
        }
        return null;
    }

    public Void visit(SuffixOpNode node) {
        super.visit(node);
        if (!node.expr().isAssignable()) {
            semanticError(node.expr().location(), "cannot increment/decrement");
        }
        return null;
    }

    public Void visit(FuncallNode node) {
        super.visit(node);
        if (!node.expr().isCallable()) {
            semanticError(node.location(), "calling object is not a function");
        }
        return null;
    }

    public Void visit(ArefNode node) {
        super.visit(node);
        if (!node.expr().isPointer()) {
            semanticError(node.location(), "indexing non-arry/pointer expression");
        }
        handleImplicitAddress(node);
        return null;
    }

    public Void visit(MemberNode node) {
        super.visit(node);
        checkMemberRef(node.location(), node.expr().type(), node.member());
        handleImplicitAddress(node);
        return null;
    }

    public Void visit(PtrMemberNode node) {
        super.visit(node);
        if (!node.expr().isPointer()) {
            undereferableError(node.location());
        }
        checkMemberRef(node.location(), node.dereferedType(), node.member());
        handleImplicitAddress(node);
        return null;
    }

    private void checkMemberRef(Location loc, Type t, String s) {
        if (!t.isCompositeType()) {
            semanticError(loc, "accessing member '" + s + "' for " +
                    "non-struct/union: " + t);
        }
        CompositeType type = t.getCompositeType();
        if (!type.hasMember(s)) {
            semanticError(loc, type.toString()
                    + "does not have member: " + s);
        }
    }

    public Void visit(DereferenceNode node) {
        super.visit(node);
        if (!node.expr().isPointer()) {
            undereferableError(node.location());
        }
        handleImplicitAddress(node);
        return null;
    }

    public Void visit(AddressNode node) {
        super.visit(node);
        if (!node.expr().isLvalue()) {
            semanticError(node.location(), "invalid expression for &");
        }
        Type baseType = node.expr().type();
        if (!node.expr().isLoadable()) {
            node.setType(baseType);
        } else {
            node.setType(typeTable.pointerTo(baseType));
        }
        return null;
    }

    public Void visit(VariableNode node) {
        super.visit(node);
        if (node.entity().isConstant()) {
            checkConstant(node.entity().value());
        }
        handleImplicitAddress(node);
        return null;
    }

    public Void visit(CastNode node) {
        super.visit(node);
        if (node.type().isArray()) {
            semanticError(node.location(), "cast specifies array type");
        }
        return null;
    }

    private void handleImplicitAddress(LHSNode node) {
        if (!node.isLoadable()) {
            Type t = node.type();
            if (t.isArray()) {
                node.setType(typeTable.pointerTo(t.baseType()));
            } else {
                node.setType(typeTable.pointerTo(t));
            }
        }
    }

    private void undereferableError(Location loc) {
        semanticError(loc, "dereferencing non-pointer expression");
    }

    private void semanticError(Location loc, String msg) {
        errorHandler.error(loc, msg);
        throw new SemanticError("invalid expression");
    }


}
