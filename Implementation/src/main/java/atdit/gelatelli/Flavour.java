package atdit.gelatelli;

public class Flavour implements Comparable<Flavour> {

    private String flavourName;
    private double contributionMargin;
    private int sort;

    public Flavour(String flavourName, double contributionMargin) {
        this.flavourName = flavourName;
        this.contributionMargin = contributionMargin;
        sort = 0;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    // Getter and setter for flavourName
    public String getFlavourName() {
        return flavourName;
    }

    public void setFlavourName(String flavourName) {
        this.flavourName = flavourName;
    }

    // Getter and setter for contributionMargin
    public double getContributionMargin() {
        return contributionMargin;
    }

    public void setContributionMargin(double contributionMargin) {
        this.contributionMargin = contributionMargin;
    }

    public void increaseSort() {
        sort++;
    }

    public void resetSort() {
        sort = 0;
    }

    @Override
    public int compareTo(Flavour other) {
        int sortComparison = Integer.compare(other.sort, this.sort);

        // If 'sort' attributes are equal, compare by the 'contributionMargin' attribute
        if (sortComparison == 0) {
            return Double.compare(other.contributionMargin, this.contributionMargin);
        }

        return sortComparison;
    }
}
