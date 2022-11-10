package compare.comparator.interfaces;

public interface ResultElement<Type> {
	
	
	public Comparator<Type> getUsedComparator();

	public float getSimilarity();
	public void setSimilarity(float similarity);
}
