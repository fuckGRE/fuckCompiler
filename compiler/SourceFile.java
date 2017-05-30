package compiler;

import java.io.File;

/**
 * Created by yangj on 2017/5/29.
 */
public class SourceFile implements LoadArg {
    static final String SOURCE = ".cb";

    private final String originalName;
    private String currentName;

    SourceFile(String name) {
        this.originalName = name;
        this.currentName = name;
    }

    public boolean isSourceFile() {
        return true;
    }

    public String toString() {
        return currentName;
    }

    String path() {
        return currentName;
    }

    String getCurrentName() {
        return currentName;
    }

    void setCurrentName(String name) {
        this.currentName = name;
    }

    private String baseName(String path) {
        return new File(path).getName();
    }

}
