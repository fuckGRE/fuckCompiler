package compiler;

import exception.OptionParseError;
import parser.LibraryLoader;
import type.TypeTable;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by yangj on 2017/5/29.
 */
public class Options {
    static Options parse(String[] args) {
        Options opts = new Options();
        opts.parseArgs(args);
        return opts;
    }

    private CompilerType compilerType;
    private LibraryLoader libraryLoader = new LibraryLoader();
    private boolean debug = false;
<<<<<<< HEAD
    private List<LoadArg> loadArgList;
=======
    private List<LoadOpt> loadOptList;
>>>>>>> master
    private List<SourceFile> sourceFileList;

    CompilerType compilerType() {
        return compilerType;
    }

    boolean isDebug() {
        return debug;
    }

    LibraryLoader libraryLoader() {
        return libraryLoader;
    }

    List<SourceFile> sourceFile() {
        return sourceFileList;
    }

    TypeTable typeTable() {
        return TypeTable.ilp32();
    }
<<<<<<< HEAD

    List<String> loadArgs() {
        List<String> res = new ArrayList<String>();
        for (LoadArg arg : loadArgList) {
            res.add(arg.toString());
        }
        return res;
    }

=======
    
>>>>>>> master

    void parseArgs(String[] inputArgs) {
        sourceFileList = new ArrayList<>();
        ListIterator<String> args = Arrays.asList(inputArgs).listIterator();
<<<<<<< HEAD
        loadArgList = new ArrayList<>();
=======
        loadOptList = new ArrayList<>();
>>>>>>> master
        while (args.hasNext()) {
            String arg = args.next();
            if (arg.equals("--")) {
                break;
            } else if (arg.startsWith("-")) {
                if (CompilerType.isTypeOption(arg)) {
                    if (compilerType != null) {
                        parseError(compilerType.toOption() + " option and "
                                + arg + " option is exclusive");
                    }
                    compilerType = compilerType.fromOption(arg);
                } else if (arg.startsWith("-I")) {
                    libraryLoader.addLoadPath(getOptArg(arg, args));
                } else if (arg.startsWith("-L")) {
                    addLoadArg("-L" + getOptArg(arg, args));
<<<<<<< HEAD
                } else {
=======
                } else if (arg.startsWith("--help")) {
                    printOptions(System.out);
                }
                else {
>>>>>>> master
                    parseError("Unknown option: " + arg);
                }
            } else {
                sourceFileList.add(new SourceFile(arg));
            }
        }
    }

    private void addLoadArg(String arg) {
<<<<<<< HEAD
        loadArgList.add(new LoadOpt(arg));
=======
        loadOptList.add(new LoadOpt(arg));
>>>>>>> master
    }

    private String getOptArg(String opt, ListIterator<String> args) {
        String path = opt.substring(2);
        if (path.length() != 0) {
            return path;
        } else {
            return nextArg(opt, args);
        }
    }

    private String nextArg(String opt, ListIterator<String> args) {
        if (!args.hasNext()) {
            parseError("missing argument for " + opt);
        }
        return args.next();
    }

    private void parseError(String msg) {
        throw new OptionParseError(msg);
    }

    void printOptions(PrintStream out) {
        out.println("cbc [options] file...");
        out.println("Options:");
        out.println("  --check-syntax   Checks syntax and quit.");
        out.println("  --show-tokens    Shows tokens and quit.");
        // --show-stmt is a hidden option.
        // --show-expr is a hidden option.
        out.println("  --show-ast       Shows AST and quit.");
        out.println("  --show-semantic  Shows AST after semantic checks and quit.");
        // --show-reference is a hidden option.
        out.println("  --show-ir        Shows IR and quit.");
        out.println("  --help           Prints this message and quit.");
    }
}


