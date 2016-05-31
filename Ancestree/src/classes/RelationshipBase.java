package classes;
public abstract class RelationshipBase {
	private Person relatedTo;
	public Person getRelatedTo() {
		return relatedTo;
	}
	public void setRelatedTo(Person relatedTo) {
		this.relatedTo = relatedTo;
	}
	public RelationshipBase(Person relatedTo){
		this.relatedTo=relatedTo;
	}
}
