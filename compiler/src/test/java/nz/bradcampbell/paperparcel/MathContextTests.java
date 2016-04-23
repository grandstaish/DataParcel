package nz.bradcampbell.paperparcel;

import com.google.common.base.Joiner;
import com.google.testing.compile.JavaFileObjects;
import javax.tools.JavaFileObject;
import org.junit.Test;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

public class MathContextTests {

  @Test public void mathContextTest() throws Exception {
    JavaFileObject source =
        JavaFileObjects.forSourceString("test.Test", Joiner.on('\n').join(
            "package test;",
            "import java.math.MathContext;",
            "import nz.bradcampbell.paperparcel.PaperParcel;",
            "@PaperParcel",
            "public final class Test {",
            "  private final MathContext child;",
            "  public Test(MathContext child) {",
            "    this.child = child;",
            "  }",
            "  public MathContext getChild() {",
            "    return this.child;",
            "  }",
            "}"
        ));

    JavaFileObject expectedSource =
        JavaFileObjects.forSourceString("test/Test$$Wrapper", Joiner.on('\n').join(
            "package test;",
            "import android.os.Parcel;",
            "import android.os.Parcelable;",
            "import java.lang.Enum;",
            "import java.lang.Override;",
            "import java.math.MathContext;",
            "import java.math.RoundingMode;",
            "import nz.bradcampbell.paperparcel.internal.ParcelableWrapper;",
            "public final class Test$$Wrapper implements ParcelableWrapper<Test> {",
            "  public static final Parcelable.Creator<Test$$Wrapper> CREATOR = ",
            "      new Parcelable.Creator<Test$$Wrapper>() {",
            "    @Override public Test$$Wrapper createFromParcel(Parcel in) {",
            "      MathContext outChild = null;",
            "      if (in.readInt() == 0) {",
            "        RoundingMode outChildRoundingMode = null;",
            "        if (in.readInt() == 0) {",
            "          outChildRoundingMode = Enum.valueOf(RoundingMode.class, in.readString());",
            "        }",
            "        outChild = new MathContext(in.readInt(), outChildRoundingMode);",
            "      }",
            "      Test data = new Test(outChild);",
            "      return new Test$$Wrapper(data);",
            "    }",
            "    @Override public Test$$Wrapper[] newArray(int size) {",
            "      return new Test$$Wrapper[size];",
            "    }",
            "  };",
            "  private final Test data;",
            "  public Test$$Wrapper(Test data) {",
            "    this.data = data;",
            "  }",
            "  @Override public Test get() {",
            "    return this.data;",
            "  }",
            "  @Override public int describeContents() {",
            "    return 0;",
            "  }",
            "  @Override public void writeToParcel(Parcel dest, int flags) {",
            "    MathContext child = this.data.getChild();",
            "    if (child == null) {",
            "      dest.writeInt(1);",
            "    } else {",
            "      dest.writeInt(0);",
            "      if (child.getRoundingMode() == null) {",
            "        dest.writeInt(1);",
            "      } else {",
            "        dest.writeInt(0);",
            "        dest.writeString(child.getRoundingMode().name());",
            "      }",
            "      dest.writeInt(child.getPrecision());",
            "    }",
            "  }",
            "}"
        ));

    assertAbout(javaSource()).that(source)
        .processedWith(new PaperParcelProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(expectedSource);
  }
}