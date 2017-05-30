package compiler;

import ast.AST;
import exception.*;
import ir.IR;
import parser.Parser;
import type.TypeTable;
import utils.ErrorHandler;

import java.io.File;
import java.util.List;

/**
 * Created by yangj on 2017/5/29.
 */
public class Compiler {
    static final public String ProjectName = "C_Compiler";

    static public void main(String[] args) {
        new Compiler(ProjectName).command(args);
    }

    private final ErrorHandler errorHandler;

    public Compiler(String projectName) {
        this.errorHandler = new ErrorHandler(projectName);
    }


    public void command(String[] args) {
        Options opts = parseOptions(args);
        if (opts.compilerType() == CompilerType.CheckSyntax) {
            System.exit(checkSyntax(opts) ? 0 : 1);
        }
        try {
            List<SourceFile> sourceFileList = opts.sourceFile();
            build(sourceFileList, opts);
            System.exit(0);
        } catch (CompileException ex) {
            errorHandler.error(ex.getMessage());
            System.exit(1);
        }
    }

    private Options parseOptions(String[] args) {
        try {
            return Options.parse(args);
        } catch (OptionParseError err) {
            errorHandler.error(err.getMessage());
            errorHandler.error("Try \"cbc --help\" for commond info");
            System.exit(1);
            return null;
        }
    }

    private boolean checkSyntax(Options opts) {
        boolean ok = true;
        for (SourceFile src : opts.sourceFile()) {
            if (isValidSyntax(src.path(), opts)) {
                System.out.println(src.path() + ": OK in Syntax Check");
            } else {
                System.out.println(src.path() + ": Error in Syntax Check");
                ok = false;
            }
        }
        return ok;
    }

    private boolean isValidSyntax(String path, Options opts) {
        try {
            parseFile(path, opts);
            return true;
        } catch (SyntaxException ex) {
            return false;
        } catch (FileException ex) {
            errorHandler.error(ex.getMessage());
            return false;
        }
    }

    public AST parseFile(String path, Options opts) throws SyntaxException, FileException {
        return Parser.parseFile(new File(path), opts.libraryLoader(), errorHandler, opts.isDebug());
    }

    public void build(List<SourceFile> sourceFileList, Options opts) throws CompileException {
        for (SourceFile src : sourceFileList) {
            compile(src.path(), opts);
        }
    }

    public void compile(String srcPath, Options opts) throws CompileException {
        AST ast = parseFile(srcPath, opts);
        if (showAST(ast, opts.compilerType()))
            return;
        TypeTable typeTable = opts.typeTable();
        AST sem = semanticAnalyze(ast, typeTable, opts);
    }


    public AST semanticAnalyze(AST ast, TypeTable types, Options opts) throws SemanticException {
        new LocalResolver(errorHandler).resolve(ast);
        new TypeResolver(types, errorHandler).resolve(ast);
        types.semanticCheck(errorHandler);
        if (opts.compilerType() == CompilerType.ShowReference) {
            ast.dump();
            return ast;
        }
        new DereferenceChecker(types, errorHandler).check(ast);
        new TypeChecker(types, errorHandler).check(ast);
        return ast;
    }

    private boolean showAST(AST ast, CompilerType type) {
        switch (type) {
            case ShowTokens:
                ast.dumpTokens(System.out);
                return true;
            case ShowAST:
                ast.dump();
                return true;
            default:
                return false;
        }
    }

    private boolean dumpIR(IR ir, CompilerType type) {
        if (type == CompilerType.ShowIR) {
            ir.dump();
            return true;
        } else {
            return false;
        }
    }

    private void errorExit(String msg) {
        errorHandler.error(msg);
        System.exit(1);
    }

}
