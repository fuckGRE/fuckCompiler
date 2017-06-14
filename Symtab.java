import java.util.ArrayList;

/**
 * Created by yangj on 2017/6/11.
 */
public class Symtab {
    public static ArrayList<VariableType> globalVarTab = new ArrayList<>();//全局变量+常量表
    public static ArrayList<FunctionType> funcTab = new ArrayList<>();//函数表

    public static int getGlobalVarNum() {
        return globalVarTab.size();
    }

    public static int getFuncNum() {
        return funcTab.size();
    }

    public static VariableType get(ArrayList<VariableType> varTab, int i){
        return varTab.get(i);
    }

    public static FunctionType getFunc(int i){
        return funcTab.get(i);
    }

    public static FunctionType lastFunc() {
        return funcTab.get(funcTab.size() - 1);
    }

    public static VariableType lastVar(ArrayList<VariableType> varTab) {
        return varTab.get(varTab.size() - 1);
    }

    public static int is_func_declare(String funcName) { //检查函数是否声明
        for (int i = 0; i < getFuncNum(); i++) {
            if (funcName.equals(funcTab.get(i).name))
                return i;
        }
        return -1;
    }

    public static int is_globalVar_declare(String varName) { //检查全局变量/常量是否声明
        for (int i = 0; i < getGlobalVarNum(); i++) {
            if (varName.equals(globalVarTab.get(i).name))
                return i;
        }
        return -1;
    }

    public static void display_tab(ArrayList<VariableType> tab, int num) { //打印参数表和变量表
        for (int i = 0; i < num; i++) {
            if (tab.get(i).isArray) {
                LoveC.pr_sym.print("   name: " + tab.get(i).name + " type: " + tab.get(i).type + " value: " + tab.get(i).value
                        + " isArray: " + tab.get(i).isArray + " length: " + tab.get(i).length
                        + " isStatic: " + tab.get(i).isStatic + " isConst: " + tab.get(i).isConst);
                if (tab.get(i).point_level > 1)
                    LoveC.pr_sym.print(" isPointer: " + tab.get(i).isPointer + " pointerLevel: " + (tab.get(i).point_level - 1));
                LoveC.pr_sym.print("\n");
            } else {
                LoveC.pr_sym.print("   name: " + tab.get(i).name + " type: " + tab.get(i).type + " value " + tab.get(i).value
                        + " isStatic: " + tab.get(i).isStatic + " isConst: " + tab.get(i).isConst);
                if (tab.get(i).isPointer)
                    LoveC.pr_sym.print(" isPointer: " + tab.get(i).isPointer + " pointerLevel: " + tab.get(i).point_level);
                LoveC.pr_sym.print("\n");
            }
        }
    }

    public static void display_global_var_tab() throws Exception { //打印全局变量表
        LoveC.pr_sym.print("GLOBAL_VARIABLE {\n");
        display_tab(globalVarTab, getGlobalVarNum());
        LoveC.pr_sym.print("\n}\n");
    }

    public static void display_func_tab() throws Exception { //打印函数表
        for (int i = 2; i < getFuncNum(); i++) {
            LoveC.pr_sym.print("FUNCTION {\n name: " + funcTab.get(i).name + " returnType: " + funcTab.get(i).returnType
                    + " returnValue: " + funcTab.get(i).returnValue.name + " \n params: {\n");
            funcTab.get(i).display_param_tab();
            LoveC.pr_sym.print(" }\n variables: {\n");
            funcTab.get(i).display_var_tab();
            LoveC.pr_sym.print(" }\n}\n");
        }
    }

    public static void addSysFunc() { //预先加入系统函数
        FunctionType tempFunc = new FunctionType();
        tempFunc.name = "print";
        tempFunc.returnType = "void";
        tempFunc.isStatic = false;
        funcTab.add(tempFunc);
        tempFunc = new FunctionType();
        tempFunc.name = "input";
        tempFunc.returnType = "void";
        tempFunc.isStatic = false;
        funcTab.add(tempFunc);
    }

    public static int checkAssignType(String lhs, String rhs) {
        if (lhs.equals("int")) {
            if(rhs.equals("int"))
                return 0;
            if (rhs.equals("float") || rhs.equals("char") || rhs.equals("double"))
                return 1;
            else
                return 2;

        }
        if (lhs.equals("float")) {
            if(rhs.equals("float"))
                return 0;
            if (rhs.equals("int") || rhs.equals("double"))
                return 1;
            else
                return 2;
        }
        if(lhs.equals("double")){
            if(rhs.equals("double"))
                return 0;
            if(rhs.equals("int") || rhs.equals("double"))
                return 1;
            else
                return 2;
        }
        if(lhs.equals("char")){
            if(rhs.equals("char"))
                return 0;
            if(rhs.equals("int"))
                return 1;
            else
                return 2;
        }
        return 0;
    }

    public static boolean checkCalculateType(String lhs, String rhs){
        if((lhs.equals("int") || lhs.equals("char"))&&(rhs.equals("int")||rhs.equals("char")))
            return true;
        else
            return false;
    }

    public static int checkCallType(VariableType lhs, VariableType rhs){
        if(lhs.isPointer){
            if(!rhs.isPointer || lhs.point_level != rhs.point_level)
                return 2;
            if(lhs.isArray && rhs.isArray){
                if(lhs.length != rhs.length)
                    return 2;
            }

        }
        return checkAssignType(lhs.type, rhs.type);
    }

}
