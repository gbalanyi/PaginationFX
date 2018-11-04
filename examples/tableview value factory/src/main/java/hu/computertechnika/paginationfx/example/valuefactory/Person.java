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
package hu.computertechnika.paginationfx.example.valuefactory;

import java.sql.Date;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * @author Gábor Balanyi
 */
public class Person {

	private ObjectProperty<Long> id = new SimpleObjectProperty<>(this, "id");

	private ObjectProperty<String> firstName = new SimpleObjectProperty<>(this, "firstName");

	private ObjectProperty<String> lastName = new SimpleObjectProperty<>(this, "lastName");

	private ObjectProperty<Integer> age = new SimpleObjectProperty<>(this, "age");

	private ObjectProperty<String> gender = new SimpleObjectProperty<>(this, "gender");

	private ObjectProperty<Date> dateOfBirth = new SimpleObjectProperty<>(this, "dateOfBirth");

	public ObjectProperty<Long> idProperty() {
		return id;
	}

	public Long getId() {
		return id.get();
	}

	public void setId(Long id) {
		this.id.set(id);
	}

	public ObjectProperty<String> firstNameProperty() {
		return firstName;
	}

	public String getFirstName() {
		return firstName.get();
	}

	public void setFirstName(String firstName) {
		this.firstName.set(firstName);
	}

	public ObjectProperty<String> lastNameProperty() {
		return lastName;
	}

	public String getLastName() {
		return lastName.get();
	}

	public void setLastName(String lastName) {
		this.lastName.set(lastName);
	}

	public ObjectProperty<Integer> ageProperty() {
		return age;
	}

	public Integer getAge() {
		return age.get();
	}

	public void setAge(Integer age) {
		this.age.set(age);
	}

	public ObjectProperty<String> genderProperty() {
		return gender;
	}

	public String getGender() {
		return gender.get();
	}

	public void setGender(String gender) {
		this.gender.set(gender);
	}

	public ObjectProperty<Date> dateOfBirthProperty() {
		return dateOfBirth;
	}

	public Date getDateOfBirth() {
		return dateOfBirth.get();
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth.set(dateOfBirth);
	}

}
