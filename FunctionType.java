import java.util.ArrayList;

/**
 * Created by yangj on 2017/6/11.
 */

public class FunctionType { //函数
    String name; //名字
    public ArrayList<VariableType> paramTab;//参数表
    public ArrayList<VariableType> varTab;//变量表
    String returnType; //返回值类型
    VariableType returnValue; //返回值
    boolean isStatic; //是否为静态

    public FunctionType() {
        paramTab = new ArrayList<>();
        varTab = new ArrayList<>();
    }

    public int getParamNum() {
        return paramTab.size();
    }

    public int getVarNum() {
        return varTab.size();
    }

    public boolean is_var_redeclare(String varName) { //检查变量是否重声明
        int pos = is_var_declare_param(varName);
        if (pos != -1)
            return true;
        pos = is_var_declare_var(varName);
        if (pos != -1)
            return true;
        return false;
    }

    public int is_var_declare_param(String varName) { //检查参数是否声明
        for (int i = 0; i < getParamNum(); i++) {
            if (varName.equals(paramTab.get(i).name))
                return i;
        }
        return -1;
    }

    public int is_var_declare_var(String varName) { //检查变量是否在函数内声明
        for (int i = 0; i < getVarNum(); i++) {
            if (varName.equals(varTab.get(i).name))
                return i;
        }
        return -1;
    }

    public void display_param_tab() { //打印参数表
        Symtab.display_tab(paramTab, getParamNum());
    }

    public void display_var_tab() { //打印变量表
        Symtab.display_tab(varTab, getVarNum());
    }
}
