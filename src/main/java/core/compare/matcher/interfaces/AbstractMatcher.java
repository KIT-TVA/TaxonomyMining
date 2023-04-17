package main.java.core.compare.matcher.interfaces;


public abstract class AbstractMatcher implements Matcher {
    private String matcherName;
    private String matcherDescription;

    public AbstractMatcher(String matcherName, String matcherDescription) {
        setMatcherName(matcherName);
        setMatcherDescription(matcherDescription);
    }

    @Override
    public String getMatcherName() {
        return this.matcherName;
    }

    @Override
    public String getMatcherDescription() {
        return this.matcherDescription;
    }

    public void setMatcherName(String matcherName) {
        this.matcherName = matcherName;
    }

    public void setMatcherDescription(String matcherDescription) {
        this.matcherDescription = matcherDescription;
    }

    @Override
    public String toString() {
        return matcherName;
    }

}
