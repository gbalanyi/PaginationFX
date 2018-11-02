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
package hu.computertechnika.paginationfx.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Gábor Balanyi
 */
@Getter
public abstract class AbstractSQLDataSource<S, F, W, O, Q, MT> {

	private List<S> selectExpressions = new ArrayList<>();

	private F fromExpression;

	private List<W> whereExpressions = new ArrayList<>();

	private List<O> orderExpressions = new ArrayList<>();

	@Setter
	private long offset;

	@Setter
	private long maxResults;

	public void addSelectExpression(S s) {
		this.getSelectExpressions().add(Objects.requireNonNull(s));
	}

	public void removeSelectExpression(S s) {
		this.getSelectExpressions().remove(s);
	}

	public void setFromExpression(F f) {
		this.fromExpression = Objects.requireNonNull(f);
	}

	public void addWhereExpression(W w) {
		this.getWhereExpressions().add(Objects.requireNonNull(w));
	}

	public boolean removeWhereExpression(W w) {
		return this.getWhereExpressions().remove(w);
	}

	public void addOrderExpression(O o) {
		this.getOrderExpressions().add(Objects.requireNonNull(o));
	}

	public boolean removeOrderExpression(O o) {
		return this.getOrderExpressions().remove(o);
	}

	protected abstract Q buildQuery();

	protected abstract Q buildCountQuery();

	public abstract List<MT> executeQuery();

	public abstract long executeCountQuery();

}
