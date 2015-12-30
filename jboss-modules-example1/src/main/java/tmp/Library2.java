package tmp;
import tmp.Library3;

public class Library2 {
  public void bar() {
    Library3 lib3 = new Library3();
    lib3.printVersion();    // Should print "This is version 1."
  }
}