/**TypeRef表示类型名称
Type表示类型定义
举例如：
struct point{int x; int y}是类型的定义
struct point是类型的名称
*/
package net.loveruby.cflat.type;
import net.loveruby.cflat.ast.Location;

public abstract class TypeRef {
    protected Location location;

    public TypeRef(Location loc) {
        this.location = loc;
    }

    public Location location() {
        return location;
    }

    public int hashCode() {
        return toString().hashCode();
    }
}
