package compiler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangj on 2017/5/29.
 */

enum CompilerType {
    CheckSyntax("--check-syntax"),
    ShowTokens("--show-tokens"),
    ShowAST("--show-ast"),
<<<<<<< HEAD
    ShowStmt("--show-stmt"),
    ShowExpr("--show-expr"),
    ShowSemantic("--show-semantic"),
    ShowReference("--show-reference"),
    ShowIR("--show-ir"),
    Compile("-C");
=======
    ShowSemantic("--show-semantic"),
    ShowReference("--show-reference"),
    ShowIR("--show-ir"),
    ShowHelp("--help");
>>>>>>> master

    static private Map<String, CompilerType> types;

    static {
<<<<<<< HEAD
        types = new HashMap<String, CompilerType>();
        types.put("--check-syntax", CheckSyntax);
        types.put("--show-tokens", ShowTokens);
        types.put("--show-ast", ShowAST);
        types.put("--show-stmt", ShowStmt);
        types.put("--show-expr", ShowExpr);
        types.put("--show-semantic", ShowSemantic);
        types.put("--show-reference", ShowReference);
        types.put("--show-ir", ShowIR);
=======
        types = new HashMap<>();
        types.put("--check-syntax", CheckSyntax);
        types.put("--show-tokens", ShowTokens);
        types.put("--show-ast", ShowAST);
        types.put("--show-semantic", ShowSemantic);
        types.put("--show-reference", ShowReference);
        types.put("--show-ir", ShowIR);
        types.put("--help", ShowHelp);
>>>>>>> master
    }

    static public boolean isTypeOption(String opt) {
        return types.containsKey(opt);
    }

    static public CompilerType fromOption(String opt) {
        CompilerType t = types.get(opt);
        if (t == null) {
            throw new Error("Unknown compile type: " + opt);
        }
        return t;
    }

    private final String option;

    CompilerType(String opt) {
        this.option = opt;
    }

    public String toOption() {
        return option;
    }


}
