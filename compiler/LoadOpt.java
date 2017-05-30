package compiler;

/**
 * Created by yangj on 2017/5/29.
 */
class LoadOpt implements LoadArg {
    private final String arg;

    LoadOpt(String arg) {
        this.arg = arg;
    }

    public boolean isSourceFile() {
        return false;
    }

    public String toString() {
        return arg;
    }

}
