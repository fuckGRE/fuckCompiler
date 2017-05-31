package net.loveruby.cflat.ast;
import java.io.PrintStream;

abstract public class Node implements Dumpable {
    public Node() {
    }

    //返回某节点所对应的语法在代码中的位置
    abstract public Location location();

    public void dump() {
        dump(System.out);
    }

    public void dump(PrintStream s) {
        dump(new Dumper(s));
    }

    public void dump(Dumper d) {
        d.printClass(this, location());
        _dump(d);
    }

    abstract protected void _dump(Dumper d);
}
