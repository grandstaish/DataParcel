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

package nz.bradcampbell.java7example;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Date;

import paperparcel.PaperParcel;

@PaperParcel
public final class State implements Parcelable {
  public static Parcelable.Creator<State> CREATOR = PaperParcelState.CREATOR;

  private final int count;

  // Non-private variable can be assigned and read directly; it doesn't need a corresponding
  // constructor argument/setter method and an accessor method.
  Date modificationDate;

  // Excluded from the parcelling/unparcelling process
  private transient long somethingToExclude = 1000L;

  public State(int count) {
    this.count = count;
  }

  public int getCount() {
    return count;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    PaperParcelState.writeToParcel(this, dest, flags);
  }
}
