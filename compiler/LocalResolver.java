package compiler;

import ast.*;
import entity.*;
import exception.SemanticException;
import utils.ErrorHandler;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by yangj on 2017/5/29.
 */
public class LocalResolver extends CompilerVisitor {
    private final LinkedList<Scope> scopeStack;
    private final ConstantTable constantTable;
    private final ErrorHandler errorHandler;

    public LocalResolver(ErrorHandler eh) {
        this.errorHandler = eh;
        this.scopeStack = new LinkedList<Scope>();
        this.constantTable = new ConstantTable();
    }

    private void resolve(StmtNode s) {
        s.accept(this);
    }

    private void resolve(ExprNode e) {
        e.accept(this);
    }

    public void resolve(AST ast) throws SemanticException {
        ToplevelScope toplevelScope = new ToplevelScope();
        scopeStack.add(toplevelScope);

        for (Entity decla : ast.declarations()) {
            toplevelScope.declareEntity(decla);
        }
<<<<<<< HEAD
        for (Entity defin : ast.definitions()) {
            toplevelScope.declareEntity(defin);
=======

        for (Entity defin : ast.definitions()) {
            toplevelScope.defineEntity(defin);
>>>>>>> master
        }

        resolveGlobalVarInitializers(ast.definedVariables());
        resolveConstantValues(ast.constants());
        resolveFunctions(ast.definedFunctions());

        toplevelScope.checkReferences(errorHandler);
        if (errorHandler.errorOccured()) {
            throw new SemanticException("Compile error");
        }

        ast.setScope(toplevelScope);
        ast.setConstantTable(constantTable);
    }

    private void resolveGlobalVarInitializers(List<DefinedVariable> globalVars) {
        for (DefinedVariable gvar : globalVars) {
            resolve(gvar.initializer());
        }
    }

    private void resolveConstantValues(List<Constant> constants) {
        for (Constant c : constants) {
            resolve(c.value());
        }
    }

    public void resolveFunctions(List<DefinedFunction> functions) {
        for (DefinedFunction func : functions) {
            pushScope(func.parameters());
            resolve(func.body());
            func.setScope(popScope());
        }
    }

    public Void visit(BlockNode node) {
        pushScope(node.variables());
        super.visit(node);
        node.setScope(popScope());
        return null;
    }

    private void pushScope(List<? extends DefinedVariable> variables) {
        LocalScope scope = new LocalScope(currentScope());
        for (DefinedVariable var : variables) {
            if (scope.isDefinedLocally(var.name())) {
                error(var.location(), "duplicated variable in scope: " + var.name());
            } else {
                scope.defineVariable(var);
            }
        }
        scopeStack.addLast(scope);
    }

    private LocalScope popScope() {
        return (LocalScope) scopeStack.removeLast();
    }

    private Scope currentScope() {
        return scopeStack.getLast();
    }

    public Void visit(StringLiteralNode node) {
        node.setEntry(constantTable.intern(node.value()));
        return null;
    }

    public Void visit(VariableNode node) {
        try {
            Entity entity = currentScope().get(node.name());
            entity.refered();
            node.setEntity(entity);
        } catch (SemanticException ex) {
            error(node, ex.getMessage());
        }
        return null;
    }

    private void error(Node node, String msg) {
        errorHandler.error(node.location(), msg);
    }

    private void error(Location loc, String msg) {
        errorHandler.error(loc, msg);
    }

}
