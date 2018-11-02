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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.computertechnika.paginationfx.filter.AbstractJDBCFilter;
import hu.computertechnika.paginationfx.filter.FilterType;
import hu.computertechnika.paginationfx.filter.TemporalSupport;
import hu.computertechnika.paginationfx.filter.TemporalType;
import lombok.Getter;

/**
 * @author Gábor Balanyi
 */
@Getter
public class JDBCDataSource<MT> extends AbstractJDBCDataSource<MT> {

	private static final Logger LOGGER = LoggerFactory.getLogger(JDBCDataSource.class);

	private Connection connection;

	public JDBCDataSource(Connection connection) {
		this.connection = Objects.requireNonNull(connection);
	}

	protected Connection prepareConnection() {
		return connection;
	}

	protected PreparedStatement prepareQueryStatment(Connection connection) throws SQLException {
		return connection.prepareStatement(this.buildQuery());
	}

	protected PreparedStatement prepareCountQueryStatment(Connection connection) throws SQLException {
		return connection.prepareStatement(this.buildCountQuery());
	}

	protected void setStatementParameters(PreparedStatement preparedStatement) throws SQLException {

		int paramCounter = 1;
		for (AbstractJDBCFilter<?> filter : this.getWhereExpressions()) {

			String predicate = filter.createPredicate();
			if (predicate == null) {
				continue;
			}

			if (filter.getFilterValues() == null) {
				continue;
			}

			int paramCount = this.countParams(predicate);
			if (paramCount < 1) {
				continue;
			}

			for (int i = 0; i < paramCount && i < filter.getFilterValues().length; i++) {

				if (filter.getFilterValues()[i] != null) {

					if (filter instanceof TemporalSupport) {

						TemporalSupport temporalSupport = (TemporalSupport) filter;
						if (TemporalType.DATE.equals(temporalSupport.getTemporalType())) {

							java.sql.Date date = null;
							if (filter.getFilterValues()[i] instanceof Date) {
								date = new java.sql.Date(((Date) filter.getFilterValues()[i]).getTime());
							} else if (filter.getFilterValues()[i] instanceof Calendar) {
								date = new java.sql.Date(((Calendar) filter.getFilterValues()[i]).getTimeInMillis());
							} else if (filter.getFilterValues()[i] instanceof LocalDate) {
								date = java.sql.Date.valueOf((LocalDate) filter.getFilterValues()[i]);
							}
							preparedStatement.setDate(paramCounter++, date);

						} else if (TemporalType.TIME.equals(temporalSupport.getTemporalType())) {

							java.sql.Time time = null;
							if (filter.getFilterValues()[i] instanceof Date) {
								time = new java.sql.Time(((Date) filter.getFilterValues()[i]).getTime());
							} else if (filter.getFilterValues()[i] instanceof Calendar) {
								time = new java.sql.Time(((Calendar) filter.getFilterValues()[i]).getTimeInMillis());
							} else if (filter.getFilterValues()[i] instanceof LocalTime) {
								time = java.sql.Time.valueOf((LocalTime) filter.getFilterValues()[i]);
							}
							preparedStatement.setTime(paramCounter++, time);

						} else if (TemporalType.TIMESTAMP.equals(temporalSupport.getTemporalType())) {

							java.sql.Timestamp timestamp = null;
							if (filter.getFilterValues()[i] instanceof Date) {
								timestamp = new java.sql.Timestamp(((Date) filter.getFilterValues()[i]).getTime());
							} else if (filter.getFilterValues()[i] instanceof Calendar) {
								timestamp = new java.sql.Timestamp(
										((Calendar) filter.getFilterValues()[i]).getTimeInMillis());
							} else if (filter.getFilterValues()[i] instanceof LocalDateTime) {
								timestamp = java.sql.Timestamp.valueOf((LocalDateTime) filter.getFilterValues()[i]);
							}
							preparedStatement.setTimestamp(paramCounter++, timestamp);

						}

					} else if (FilterType.CONTAINS.equals(filter.getFilterType())
							|| FilterType.NOT_CONTAINS.equals(filter.getFilterType())) {
						preparedStatement.setString(paramCounter++, "%" + filter.getFilterValues()[i].toString() + "%");
					} else if (FilterType.STARTS_WITH.equals(filter.getFilterType())
							|| FilterType.NOT_STARTS_WITH.equals(filter.getFilterType())) {
						preparedStatement.setString(paramCounter++, filter.getFilterValues()[i].toString() + "%");
					} else if (FilterType.ENDS_WITH.equals(filter.getFilterType())
							|| FilterType.NOT_ENDS_WITH.equals(filter.getFilterType())) {
						preparedStatement.setString(paramCounter++, "%" + filter.getFilterValues()[i].toString());
					} else {
						preparedStatement.setObject(paramCounter++, filter.getFilterValues()[i]);
					}

				} else {
					preparedStatement.setNull(paramCounter++, Types.NULL);
				}

			}

		}

	}

	private int countParams(String predicate) {

		int count = 0;
		int fromIndex = -1;
		while ((fromIndex = predicate.indexOf("?", fromIndex + 1)) != -1) {
			count++;
		}

		return count;

	}

	private void checkState() {

		if (this.getSelectExpressions().isEmpty()) {
			throw new IllegalStateException("The select expression is null");
		}
		if (this.getFromExpression() == null) {
			throw new IllegalStateException("The from expression is null");
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MT> executeQuery() {

		this.checkState();

		List<Object[]> result = new ArrayList<>();
		try {

			PreparedStatement preparedStatement = this.prepareQueryStatment(this.prepareConnection());
			this.setStatementParameters(preparedStatement);

			ResultSet resultSet = preparedStatement.executeQuery();
			int columnCounter = resultSet.getMetaData().getColumnCount();
			Object[] row;
			while (resultSet.next()) {

				row = new Object[columnCounter];
				for (int i = 0; i < columnCounter; i++) {
					row[i] = resultSet.getObject(i + 1);
				}
				result.add(row);

			}

			resultSet.close();
			preparedStatement.close();

		} catch (SQLException e) {

			LOGGER.error(e.getMessage());
			throw new RuntimeException(e);

		}

		return (List<MT>) result;

	}

	@Override
	public long executeCountQuery() {

		this.checkState();

		long rowNumber = 0;
		try {

			PreparedStatement preparedStatement = this.prepareCountQueryStatment(this.prepareConnection());
			this.setStatementParameters(preparedStatement);

			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			rowNumber = resultSet.getLong(1);

			resultSet.close();
			preparedStatement.close();

		} catch (SQLException e) {

			LOGGER.error(e.getMessage());
			throw new RuntimeException(e);

		}

		return rowNumber;

	}

}
