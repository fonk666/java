package tmp;
import tmp.Library3;

public class Library1 {
  public void foo() {
    Library3 lib3 = new Library3();
    lib3.printVersion();    // Should print "This is version 2." if the correct version of Library3 is loaded.
  }
}