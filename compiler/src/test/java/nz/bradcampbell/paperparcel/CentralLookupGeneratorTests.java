package nz.bradcampbell.paperparcel;

import static com.google.common.truth.Truth.assertThat;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;
import static com.google.testing.compile.JavaSourcesSubjectFactory.javaSources;
import static java.util.Arrays.asList;
import static nz.bradcampbell.paperparcel.CentralLookupGenerator.findLowestCommonPackageName;

import com.google.common.base.Joiner;
import com.google.common.truth.Truth;
import com.google.testing.compile.JavaFileObjects;

import javax.tools.JavaFileObject;

import org.junit.Test;

public class CentralLookupGeneratorTests {
  @Test public void singleParcelable() throws Exception {
    JavaFileObject source = JavaFileObjects.forSourceString("test.Test", Joiner.on('\n').join(
        "package test;",
        "import nz.bradcampbell.paperparcel.PaperParcel;",
        "@PaperParcel",
        "public final class Test {",
        "private final boolean child;",
        "public Test(boolean child) {",
        "this.child = child;",
        "}",
        "public boolean getChild() {",
        "return this.child;",
        "}",
        "}"
    ));

    JavaFileObject centralLookup = JavaFileObjects.forSourceString("test.PaperParcels", Joiner.on("\n").join(
        "package test;",
        "import android.os.Parcelable;",
        "import java.lang.Class;",
        "import java.util.LinkedHashMap;",
        "import java.util.Map;",
        "public final class PaperParcels {",
        "private static final Map<Class, Delegator> FROM_ORIGINAL = new LinkedHashMap<>();",
        "private static final Map<Class, Delegator> FROM_PARCELABLE = new LinkedHashMap<>();",
        "static {",
        "Delegator<Test, TestParcel> delegator0 = new Delegator<Test, TestParcel>() {",
        "@Override public Test unwrap(TestParcel wrapper) {",
        "return wrapper.getContents();",
        "}",
        "@Override public TestParcel wrap(Test object) {",
        "return TestParcel.wrap(object);",
        "}",
        "};",
        "FROM_ORIGINAL.put(Test.class, delegator0);",
        "FROM_PARCELABLE.put(TestParcel.class, delegator0);",
        "}", // End of static block
        "public static <ORIG, PARCELABLE extends Parcelable> PARCELABLE wrap(ORIG originalObj) {",
        "Class<?> type = originalObj.getClass();",
        "Delegator<ORIG, PARCELABLE> delegator = FROM_ORIGINAL.get(type);",
        "return delegator.wrap(originalObj);",
        "}",
        "",
        "public static <ORIG, PARCELABLE extends Parcelable> ORIG unwrap(PARCELABLE parcelableObj) {",
        "Class<?> type = parcelableObj.getClass();",
        "Delegator<ORIG, PARCELABLE> delegator = FROM_PARCELABLE.get(type);",
        "return delegator.unwrap(parcelableObj);",
        "}",
        "interface Delegator<ORIG, PARCELABLE extends Parcelable> {",
        "ORIG unwrap(PARCELABLE parcelableObj);",
        "PARCELABLE wrap(ORIG originalObj);",
        "}", // End of Delegator interface
        "}" // End of PaperParcels class
        ));
    Truth.assertAbout(javaSource())
        .that(source)
        .processedWith(new PaperParcelProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(getParcelFile("test", "Test"), centralLookup);
  }

  @Test public void multipleParcelable() throws Exception {
    JavaFileObject source = JavaFileObjects.forSourceString("test.Test", Joiner.on('\n').join(
        "package test;",
        "import nz.bradcampbell.paperparcel.PaperParcel;",
        "@PaperParcel",
        "public final class Test {",
        "private final boolean child;",
        "public Test(boolean child) {",
        "this.child = child;",
        "}",
        "public boolean getChild() {",
        "return this.child;",
        "}",
        "}"
    ));

    JavaFileObject source2 = JavaFileObjects.forSourceString("test.bar.Test2", Joiner.on('\n').join(
        "package test.bar;",
        "import nz.bradcampbell.paperparcel.PaperParcel;",
        "@PaperParcel",
        "public final class Test2 {",
        "private final boolean child;",
        "public Test2(boolean child) {",
        "this.child = child;",
        "}",
        "public boolean getChild() {",
        "return this.child;",
        "}",
        "}"
    ));
    JavaFileObject centralLookup = JavaFileObjects.forSourceString("test.PaperParcels", Joiner.on("\n").join(
        "package test;",
        "import android.os.Parcelable;",
        "import java.lang.Class;",
        "import java.util.LinkedHashMap;",
        "import java.util.Map;",
        "import test.bar.Test2;",
        "import test.bar.Test2Parcel;",
        "public final class PaperParcels {",
        "private static final Map<Class, Delegator> FROM_ORIGINAL = new LinkedHashMap<>();",
        "private static final Map<Class, Delegator> FROM_PARCELABLE = new LinkedHashMap<>();",
        "static {",
        "Delegator<Test2, Test2Parcel> delegator0 = new Delegator<Test2, Test2Parcel>() {",
        "@Override public Test2 unwrap(Test2Parcel wrapper) {",
        "return wrapper.getContents();",
        "}",
        "@Override public Test2Parcel wrap(Test2 object) {",
        "return Test2Parcel.wrap(object);",
        "}",
        "};",
        "FROM_ORIGINAL.put(Test2.class, delegator0);",
        "FROM_PARCELABLE.put(Test2Parcel.class, delegator0);",
        "Delegator<Test, TestParcel> delegator1 = new Delegator<Test, TestParcel>() {",
        "@Override public Test unwrap(TestParcel wrapper) {",
        "return wrapper.getContents();",
        "}",
        "@Override public TestParcel wrap(Test object) {",
        "return TestParcel.wrap(object);",
        "}",
        "};",
        "FROM_ORIGINAL.put(Test.class, delegator1);",
        "FROM_PARCELABLE.put(TestParcel.class, delegator1);",
        "}", // End of static block
        "public static <ORIG, PARCELABLE extends Parcelable> PARCELABLE wrap(ORIG originalObj) {",
        "Class<?> type = originalObj.getClass();",
        "Delegator<ORIG, PARCELABLE> delegator = FROM_ORIGINAL.get(type);",
        "return delegator.wrap(originalObj);",
        "}",
        "",
        "public static <ORIG, PARCELABLE extends Parcelable> ORIG unwrap(PARCELABLE parcelableObj) {",
        "Class<?> type = parcelableObj.getClass();",
        "Delegator<ORIG, PARCELABLE> delegator = FROM_PARCELABLE.get(type);",
        "return delegator.unwrap(parcelableObj);",
        "}",
        "interface Delegator<ORIG, PARCELABLE extends Parcelable> {",
        "ORIG unwrap(PARCELABLE parcelableObj);",
        "PARCELABLE wrap(ORIG originalObj);",
        "}", // End of Delegator interface
        "}" // End of PaperParcels class
    ));
    Truth.assertAbout(javaSources())
        .that(asList(source, source2))
        .processedWith(new PaperParcelProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(getParcelFile("test", "Test"), getParcelFile("test.bar", "Test2"), centralLookup);
  }

  private static JavaFileObject getParcelFile(String packageName, String original) {
    String parcel = original + "Parcel";
    return JavaFileObjects.forSourceString(packageName + "." + original + "Parcel", Joiner.on('\n').join(
          "package " + packageName + ";",
          "import android.os.Parcel;",
          "import android.os.Parcelable;",
          "import java.lang.Override;",
          "public final class " + parcel + " implements Parcelable {",
          "public static final Parcelable.Creator<" + parcel + "> CREATOR = new Parcelable.Creator<" + parcel + ">() {",
          "@Override public " + parcel + " createFromParcel(Parcel in) {",
          "return new " + parcel + "(in);",
          "}",
          "@Override public " + parcel + "[] newArray(int size) {",
          "return new " + parcel + "[size];",
          "}",
          "};",
          "private final " + original + " data;",
          "private " + parcel + "(" + original + " data) {",
          "this.data = data;",
          "}",
          "private " + parcel + "(Parcel in) {",
          "boolean child = in.readInt() == 1;",
          "this.data = new " + original + "(child);",
          "}",
          "public static final " + parcel + " wrap(" + original + " data) {",
          "return new " + parcel + "(data);",
          "}",
          "public " + original + " getContents() {",
          "return data;",
          "}",
          "@Override public int describeContents() {",
          "return 0;",
          "}",
          "@Override public void writeToParcel(Parcel dest, int flags) {",
          "boolean child = data.getChild();",
          "dest.writeInt(child ? 1 : 0);",
          "}",
          "}"
      ));
  }

  @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
  @Test public void commonPackageName() throws Exception {
    assertThat(findLowestCommonPackageName(asList("foo"))).isEqualTo("foo");
    assertThat(findLowestCommonPackageName(asList("foo", "foo.bar"))).isEqualTo("foo");
    assertThat(findLowestCommonPackageName(asList("foo.bar", "foo"))).isEqualTo("foo");
    assertThat(findLowestCommonPackageName(asList("foo.bar", "foo.baz"))).isEqualTo("foo");
    assertThat(findLowestCommonPackageName(asList("foo.bar", "foo.baz", "foo.troll"))).isEqualTo("foo");
  }
}