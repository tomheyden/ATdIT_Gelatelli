package atdit.gelatelli.models;

import java.time.LocalDate;
import java.util.Objects;

import atdit.gelatelli.controllers.ProductionController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a icecream flavour.
 */
public class Flavour implements Comparable<Flavour> {

    private String flavourName;
    private double contributionMargin;
    private int sort;
    private LocalDate earliestBBD;
    private static final Logger logger = LoggerFactory.getLogger(ProductionController.class);

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
        logger.debug("New Flavour instance created with name: {} and contribution margin: {}", flavourName, contributionMargin);
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
        logger.debug("New Flavour instance created with name: {}, contribution margin: {}, sort value: {}, and earliest best-before date: {}",
                flavourName, contributionMargin, sort, earliestBBD);

    }

    /**
     * Gets the sort value of the flavour.
     *
     * @return the sort value of the flavour
     */
    public int getSort() {
        logger.debug("Retrieving the sort value of the Flavour instance");
        return sort;
    }

    /**
     * Sets the sort value of the flavour.
     *
     * @param sort the new sort value of the flavour
     */
    public void setSort(int sort) {
        this.sort = sort;
        logger.debug("Setting the sort value of the Flavour instance to {}", sort);
    }

    /**
     * Gets the name of the flavour.
     *
     * @return the name of the flavour
     */
    public String getFlavourName() {
        logger.debug("Retrieving the name of the Flavour instance");
        return flavourName;
    }

    /**
     * Sets the name of the flavour.
     *
     * @param flavourName the new name of the flavour
     */
    public void setFlavourName(String flavourName) {
        this.flavourName = flavourName;
        logger.debug("Setting the name of the Flavour instance to {}", flavourName);
    }

    /**
     * Gets the contribution margin of the flavour.
     *
     * @return the contribution margin of the flavour
     */
    public double getContributionMargin() {
        logger.debug("Retrieving the contribution margin of the Flavour instance");
        return contributionMargin;
    }

    /**
     * Sets the contribution margin of the flavour.
     *
     * @param contributionMargin the new contribution margin of the flavour
     */
    public void setContributionMargin(double contributionMargin) {
        this.contributionMargin = contributionMargin;
        logger.debug("Setting the contribution margin of the Flavour instance to {}", contributionMargin);
    }

    /**
     * Gets the earliest best-before date of the flavour.
     *
     * @return the earliest best-before date of the flavour
     */
    public LocalDate getEarliestBBD() {
        logger.debug("Retrieving the earliest best before date of the Flavour instance");
        return earliestBBD;
    }

    /**
     * Sets the earliest best-before date of the flavour.
     *
     * @param earliestBBD the new earliest best-before date of the flavour
     */
    public void setEarliestBBD(LocalDate earliestBBD) {
        this.earliestBBD = earliestBBD;
        logger.debug("Setting the earliest best before date of the Flavour instance to {}", earliestBBD);
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
                logger.debug("Comparing Flavour objects by earliestBBD: {} vs {}", this.earliestBBD, other.getEarliestBBD());
                return bbdComparison;
            }
        }
        // If 'earliestBBD' attributes are equal, compare by the 'sort' attribute
        int sortComparison = Integer.compare(other.sort, this.sort);

        // If 'sort' attributes are equal, compare by the 'contributionMargin' attribute
        if (sortComparison == 0) {
            logger.debug("Comparing flavours by contributionMargin: {} vs. {}: {}", this, other, sortComparison);
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
        logger.debug("Calling Flavour.equals()");

        if (this == obj) {
            logger.debug("The objects are the same.");
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            logger.debug("The objects are not the same class.");
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
        logger.debug("Calling Flavour.hashCode()");
        return Objects.hash(flavourName, contributionMargin, sort);
    }
}
