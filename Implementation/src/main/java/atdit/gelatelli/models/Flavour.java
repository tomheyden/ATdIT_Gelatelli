package atdit.gelatelli.models;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a icecream flavour.
 */
public class Flavour implements Comparable<Flavour> {

    private String flavourName;
    private double contributionMargin;
    private int sort;
    private LocalDate earliestBBD;

    /**
     * Constructs a new flavour with the given name and contribution margin.
     *
     * @param flavourName        the name of the flavour
     * @param contributionMargin the contribution margin of the flavour
     */
    public Flavour(String flavourName, double contributionMargin) {
        this.flavourName = flavourName;
        this.contributionMargin = contributionMargin;
        sort = 0;
        //earliestBBD = LocalDate.of(0000,01,01);
    }

    /**
     * Constructs a new flavour with the given name, contribution margin, sort value, and earliest best-before date.
     *
     * @param flavourName        the name of the flavour
     * @param contributionMargin the contribution margin of the flavour
     * @param sort               the sort value of the flavour
     * @param earliestBBD        the earliest best-before date of the flavour
     */
    public Flavour(String flavourName, double contributionMargin, int sort, LocalDate earliestBBD) {
        this.flavourName = flavourName;
        this.contributionMargin = contributionMargin;
        this.sort = sort;
        this.earliestBBD = earliestBBD;
    }

    /**
     * Gets the sort value of the flavour.
     *
     * @return the sort value of the flavour
     */
    public int getSort() {
        return sort;
    }

    /**
     * Sets the sort value of the flavour.
     *
     * @param sort the new sort value of the flavour
     */
    public void setSort(int sort) {
        this.sort = sort;
    }

    /**
     * Gets the name of the flavour.
     *
     * @return the name of the flavour
     */
    public String getFlavourName() {
        return flavourName;
    }

    /**
     * Sets the name of the flavour.
     *
     * @param flavourName the new name of the flavour
     */
    public void setFlavourName(String flavourName) {
        this.flavourName = flavourName;
    }

    /**
     * Gets the contribution margin of the flavour.
     *
     * @return the contribution margin of the flavour
     */
    public double getContributionMargin() {
        return contributionMargin;
    }

    /**
     * Sets the contribution margin of the flavour.
     *
     * @param contributionMargin the new contribution margin of the flavour
     */
    public void setContributionMargin(double contributionMargin) {
        this.contributionMargin = contributionMargin;
    }

    /**
     * Gets the earliest best-before date of the flavour.
     *
     * @return the earliest best-before date of the flavour
     */
    public LocalDate getEarliestBBD() {
        return earliestBBD;
    }

    /**
     * Sets the earliest best-before date of the flavour.
     *
     * @param earliestBBD the new earliest best-before date of the flavour
     */
    public void setEarliestBBD(LocalDate earliestBBD) {
        this.earliestBBD = earliestBBD;
    }

    /**
     * Increases the sort value of the flavour by 1.
     */
    public void increaseSort() {
        sort++;
    }

    /**
     * Resets the sort value of the flavour to 0.
     */
    public void resetSort() {
        sort = 0;
    }

    /**
     * Compares this Flavour to another Flavour based on the earliestBBD attribute, then the sort attribute,
     * and finally the contributionMargin attribute. Returns a negative integer, zero, or a positive integer
     * as this Flavour is less than, equal to, or greater than the specified Flavour.
     *
     * @param other the Flavour to be compared.
     * @return a negative integer, zero, or a positive integer as this Flavour is less than, equal to, or greater than the specified Flavour.
     */
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

    /**
     * Indicates whether some other object is "equal to" this one. Two Flavours are considered equal
     * if they have the same flavourName, contributionMargin, and sort attributes.
     *
     * @param obj the reference object with which to compare.
     * @return true    if this object is the same as the obj argument; false otherwise
     */
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

    /**
     * Returns a hash code value for the Flavour object.
     *
     * @return a hash code value for the Flavour object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(flavourName, contributionMargin, sort);
    }
}
