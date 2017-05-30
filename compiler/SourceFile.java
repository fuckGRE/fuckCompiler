package compiler;

import java.io.File;

/**
 * Created by yangj on 2017/5/29.
 */
public class SourceFile {

    private String currentName;

    SourceFile(String name) {
        this.currentName = name;
    }

    public String toString() {
        return currentName;
    }

    String path() {
        return currentName;
    }
}
