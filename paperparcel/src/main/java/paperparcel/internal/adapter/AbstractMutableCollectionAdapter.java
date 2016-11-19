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

package paperparcel.internal.adapter;

import android.os.Parcel;
import android.support.annotation.NonNull;
import java.util.Collection;
import paperparcel.AbstractAdapter;
import paperparcel.TypeAdapter;

/**
 * A convenience base class for making it easy to write {@link TypeAdapter}s to handle classes
 * that implement the {@link Collection} interface. The {@link Collection} type must support the
 * {@link Collection#add(Object)} method.
 *
 * @param <C> The Collection type to handle
 * @param <T> The Collection's item type (can be generic)
 */
abstract class AbstractMutableCollectionAdapter<C extends Collection<T>, T>
    extends AbstractAdapter<C> {
  private final TypeAdapter<T> itemAdapter;

  AbstractMutableCollectionAdapter(TypeAdapter<T> itemAdapter) {
    this.itemAdapter = itemAdapter;
  }

  @NonNull @Override protected C read(@NonNull Parcel source) {
    int size = source.readInt();
    C collection = newCollection(size);
    for (int i = 0; i < size; i++) {
      collection.add(itemAdapter.readFromParcel(source));
    }
    return collection;
  }

  @Override protected void write(@NonNull C value, @NonNull Parcel dest, int flags) {
    dest.writeInt(value.size());
    for (T item : value) {
      itemAdapter.writeToParcel(item, dest, flags);
    }
  }

  /** Creates a new instance of the {@link Collection} of the given {@code size} */
  protected abstract C newCollection(int size);
}
