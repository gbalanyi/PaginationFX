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

import java.sql.Connection;
import java.util.List;
import java.util.Objects;

import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLTemplates;

import lombok.Getter;

/**
 * @author Gábor Balanyi
 */
@Getter
public class QDSLJDBCDataSource<MT> extends AbstractQDSLJDBCDataSource<MT> {

	private Connection connection;

	public QDSLJDBCDataSource(Connection connection) {
		this.connection = Objects.requireNonNull(connection);
	}

	public QDSLJDBCDataSource(Connection connection, SQLTemplates sqlTemplates) {

		this(connection);
		this.setSqlTemplates(Objects.requireNonNull(sqlTemplates));

	}

	@Override
	public List<MT> executeQuery() {

		SQLQuery<MT> query = this.buildQuery();
		return query.clone(connection).fetch();

	}

	@Override
	public long executeCountQuery() {

		SQLQuery<Long> query = this.buildCountQuery();
		return query.clone(connection).fetchOne();

	}

}
