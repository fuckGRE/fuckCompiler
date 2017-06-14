/**
 * Created by yangj on 2017/6/11.
 */
public class VariableType {
    String name; //名字
    String type; //类型
    String value; //值
    int length; //数组长度
    int size;  //单个字符大小（数组、指针）
    int point_level; //指针级数
    boolean isStatic; //是否为静态
    boolean isArray; //是否为数组
    boolean isPointer; //是否为指针
    boolean isGlobal; //是否是全局变量
    boolean isConst; //是否是常量


    public boolean isIntorChar()
    {
        if(type.equals("int") || type.equals("char"))
            return true;
        return false;
    }
}
