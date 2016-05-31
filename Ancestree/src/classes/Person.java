package classes;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Person {
	private String firstName;
	private String lastName;
	private Date birthDate;
	private boolean isMale;
	private SpouseRelationship spouseof;
	private List<ParentRelationship> parentof;
	private ChildRelationship father;
	private ChildRelationship mother;
	
	public Person(String firstName, String lastName){
		this.firstName=firstName;
		this.lastName=lastName;
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

	public ChildRelationship getMother() {
		return mother;
	}

	public void setMother(ChildRelationship mother) {
		this.mother = mother;
	}
	
	public void setSpousePerson(Person spousePerson){
		spouseof=new SpouseRelationship(spousePerson);
	}
	
	public Person getSpousePerseon(){
		return spouseof.getRelatedTo();
	}
	
	public void setChildPerson(Person childPerson){
		if(parentof==null){
			parentof=new ArrayList<ParentRelationship>();
		}
		parentof.add(new ParentRelationship(childPerson));
	}
	
	public void setFatherPerson(Person fatherPerson){
		father=new ChildRelationship(fatherPerson);
	}
	
	public Person getFatherPerson(){
		return father.getRelatedTo();
	}
	
	public void setMotherPerson(Person motherPerson){
		mother=new ChildRelationship(motherPerson);
	}
	
	public Person getMotherPerson(){
		return mother.getRelatedTo();
	}
	
}
