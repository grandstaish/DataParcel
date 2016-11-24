package paperparcel.internal;

import paperparcel.TypeAdapter;
import paperparcel.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class ShortArrayAdapterTests {
  @Test public void shortArraysAreCorrectlyParcelled() {
    TypeAdapter<short[]> adapter = StaticAdapters.SHORT_ARRAY_ADAPTER;
    short[] expected = new short[] { 42 };
    short[] result = TestUtils.writeThenRead(adapter, expected);
    assertThat(result).isEqualTo(expected);
  }
}
