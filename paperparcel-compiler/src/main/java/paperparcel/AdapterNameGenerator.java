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

package paperparcel;

import com.google.common.base.CaseFormat;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.WildcardTypeName;
import java.util.Map;

/**
 * Generates a human readable field name to use for an adapter instance in a class. Adapters
 * are added as constant fields, so generated names are formatted with uppercase characters and
 * words are separated by underscores as per java convention.
 */
final class AdapterNameGenerator {
  private final Map<TypeName, String> typeToNames = Maps.newLinkedHashMap();
  private final UniqueNameSet adapterNames = new UniqueNameSet("_");

  /**
   * Creates a name based on the given {@link TypeName}. Names are constants, so will use
   * {@link CaseFormat#UPPER_UNDERSCORE} formatting.
   */
  String getName(TypeName typeName) {
    String name = typeToNames.get(typeName);
    if (name != null) {
      return name;
    }
    name = getNameInternal(typeName);
    name = CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, name);
    name = adapterNames.getUniqueName(name);
    typeToNames.put(typeName, name);
    return name;
  }

  private String getNameInternal(TypeName typeName) {
    String adapterName = null;
    if (typeName instanceof WildcardTypeName) {
      WildcardTypeName wildcardTypeName = (WildcardTypeName) typeName;
      String upperBoundsPart = "";
      String lowerBoundsPart = "";
      for (TypeName upperBound : wildcardTypeName.upperBounds) {
        upperBoundsPart += getNameInternal(upperBound);
      }
      for (TypeName lowerBound : wildcardTypeName.lowerBounds) {
        lowerBoundsPart += getNameInternal(lowerBound);
      }
      adapterName = upperBoundsPart + lowerBoundsPart;
    }
    if (typeName instanceof ArrayTypeName) {
      ArrayTypeName arrayTypeName = (ArrayTypeName) typeName;
      adapterName = getNameInternal(arrayTypeName.componentType) + "Array";
    }
    if (typeName instanceof ParameterizedTypeName) {
      ParameterizedTypeName parameterizedTypeName = (ParameterizedTypeName) typeName;
      String paramPart = "";
      for (TypeName param : parameterizedTypeName.typeArguments) {
        paramPart += getNameInternal(param);
      }
      adapterName = paramPart + parameterizedTypeName.rawType.simpleName();
    }
    if (typeName instanceof ClassName) {
      ClassName className = (ClassName) typeName;
      adapterName = Joiner.on("_").join(className.simpleNames());
    }
    if (typeName.isPrimitive()) {
      adapterName = typeName.toString();
    }
    if (adapterName == null) {
      throw new AssertionError();
    }
    return adapterName;
  }
}
