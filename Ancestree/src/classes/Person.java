package classes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Person implements Comparable<Person> {
	private String firstName;
	private String lastName;
	private Date birthDate;
	private Date deathDate;
	private boolean isMale;
	private SpouseRelationship spouseof;
	private List<ParentRelationship> parentof;
	private ChildRelationship father;
	private int index;

	public Person(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getFullName() {
		return lastName + " " + firstName;
	}

	public void setSpousePerson(Person spousePerson) {
		spouseof = new SpouseRelationship(spousePerson);
	}

	public Person getSpousePerseon() {
		if(spouseof==null) return null;
		return spouseof.getRelatedTo();
	}
	
	public List<Person> getChildPersons(){
		if(parentof==null) return new ArrayList<Person>();
		List<Person> output=new ArrayList<Person>();
		for(ParentRelationship child:parentof){
			output.add(child.getRelatedTo());
		}
		return output;
	}
	
	public void deleteChild(Person person){
		for(int i=0;i<parentof.size();++i){
			if(parentof.get(i).getRelatedTo().equals(person)){
				parentof.remove(i);
				return;
			}
		}
	}

	public void setChildPerson(Person childPerson) {
		if (parentof == null) {
			parentof = new ArrayList<ParentRelationship>();
		}
		parentof.add(new ParentRelationship(childPerson));
	}

	public void setFatherPerson(Person fatherPerson) {
		father = new ChildRelationship(fatherPerson);
	}

	public Person getFatherPerson() {
		if(father==null) return null;
		return father.getRelatedTo();
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Date getDeathDate() {
		return deathDate;
	}

	public void setDeathDate(Date deathDate) {
		this.deathDate = deathDate;
	}

	public boolean isMale() {
		return isMale;
	}

	public void setMale(boolean isMale) {
		this.isMale = isMale;
	}

	public SpouseRelationship getSpouseof() {
		return spouseof;
	}

	public void setSpouseof(SpouseRelationship spouseof) {
		this.spouseof = spouseof;
	}

	public List<ParentRelationship> getParentof() {
		return parentof;
	}

	public void setParentof(List<ParentRelationship> parentof) {
		this.parentof = parentof;
	}

	public ChildRelationship getFather() {
		return father;
	}

	public void setFather(ChildRelationship father) {
		this.father = father;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public int compareTo(Person another) {
		// TODO Auto-generated method stub
		return Integer.compare(this.index, another.index);
	}

}
