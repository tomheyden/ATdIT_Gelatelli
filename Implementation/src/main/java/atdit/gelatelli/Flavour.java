package atdit.gelatelli;

import java.time.LocalDate;
import java.util.Objects;

public class Flavour implements Comparable<Flavour> {

    private String flavourName;
    private double contributionMargin;
    private int sort;
    private LocalDate earliestBBD;

    public Flavour(String flavourName, double contributionMargin) {
        this.flavourName = flavourName;
        this.contributionMargin = contributionMargin;
        sort = 0;
        //earliestBBD = LocalDate.of(0000,01,01);
    }

    public Flavour(String flavourName, double contributionMargin, int sort, LocalDate earliestBBD) {
        this.flavourName = flavourName;
        this.contributionMargin = contributionMargin;
        this.sort = sort;
        this.earliestBBD = earliestBBD;
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

    public LocalDate getEarliestBBD() {
        return earliestBBD;
    }

    public void setEarliestBBD(LocalDate earliestBBD) {
        this.earliestBBD = earliestBBD;
    }

    public void increaseSort() {
        sort++;
    }

    public void resetSort() {
        sort = 0;
    }

    @Override
    public int compareTo(Flavour other) {
        // Compare by the 'earliestBBD' attribute first
        if (earliestBBD != null && other.getEarliestBBD() != null) {
            int bbdComparison = this.earliestBBD.compareTo(other.earliestBBD);
            if (bbdComparison != 0) {
                return bbdComparison;
            }
        }
        // If 'earliestBBD' attributes are equal, compare by the 'sort' attribute
        int sortComparison = Integer.compare(other.sort, this.sort);

        // If 'sort' attributes are equal, compare by the 'contributionMargin' attribute
        if (sortComparison == 0) {
            return Double.compare(other.contributionMargin, this.contributionMargin);
        }

        return sortComparison;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Flavour other = (Flavour) obj;
        return Objects.equals(flavourName, other.flavourName)
                && Double.compare(contributionMargin, other.contributionMargin) == 0
                && sort == other.sort;
    }

    @Override
    public int hashCode() {
        return Objects.hash(flavourName, contributionMargin, sort);
    }
}
