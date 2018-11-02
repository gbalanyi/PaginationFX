/**
 * Copyright 2018 PaginationFX
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software 
 * and associated documentation files (the "Software"), to deal in the Software without 
 * restriction, including without limitation the rights to use, copy, modify, merge, publish, 
 * distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the 
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or 
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR 
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR 
 * ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE 
 * SOFTWARE. 
*/
package hu.computertechnika.paginationfx.filter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Gábor Balanyi
 */
@Getter
@Setter
public abstract class AbstractFilter<T, P> {

	private FilterType filterType = FilterType.NONE;

	private T[] filterValues;

	public abstract FilterType[] getSupportedFilterTypes();

	public abstract P createPredicate();

	public void setFilterType(FilterType filterType) {

		Objects.requireNonNull(filterType);
		if (!Arrays.asList(this.getSupportedFilterTypes()).contains(filterType)) {
			throw new IllegalArgumentException("Unsupported filter type: " + filterType.name());
		}
		this.filterType = filterType;

	}

	@SuppressWarnings("unchecked")
	public Class<T> getFilterValueType() {

		Type genericType = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		if (genericType instanceof Class<?>) {
			return (Class<T>) genericType;
		}
		if (genericType instanceof ParameterizedType) {
			return (Class<T>) ((ParameterizedType) genericType).getRawType();
		}
		if ("T".equals(genericType.getTypeName()) && genericType instanceof TypeVariable<?>) {

			TypeVariable<?> typeVariable = (TypeVariable<?>) genericType;
			if (typeVariable.getBounds() != null && typeVariable.getBounds().length > 0) {

				Type boundedType = typeVariable.getBounds()[0];
				if (boundedType instanceof Class<?>) {
					return (Class<T>) boundedType;
				}
				if (boundedType instanceof ParameterizedType) {
					return (Class<T>) ((ParameterizedType) boundedType).getRawType();
				}

			}

		}

		return null;

	}

}
