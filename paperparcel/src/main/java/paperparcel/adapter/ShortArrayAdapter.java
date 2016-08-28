/*
 * Copyright (C) 2016 Bradley Campbell.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package paperparcel.adapter;

import android.os.Parcel;
import android.support.annotation.NonNull;
import paperparcel.TypeAdapter;

/** Default {@link TypeAdapter} for {@code short[]} types */
public final class ShortArrayAdapter extends AbstractAdapter<short[]> {
  public static final ShortArrayAdapter INSTANCE = new ShortArrayAdapter();

  @NonNull @Override protected short[] read(@NonNull Parcel source) {
    int size = source.readInt();
    short[] value = new short[size];
    for (int i = 0; i < size; i++) {
      value[i] = (short) source.readInt();
    }
    return value;
  }

  @Override protected void write(@NonNull short[] value, @NonNull Parcel dest, int flags) {
    dest.writeInt(value.length);
    for (short s : value) {
      dest.writeInt((int) s);
    }
  }

  private ShortArrayAdapter() {}
}
