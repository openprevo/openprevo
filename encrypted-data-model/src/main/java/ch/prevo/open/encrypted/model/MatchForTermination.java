package ch.prevo.open.encrypted.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDate;

/**
 * Matching notification sent to an OpenPrevo Node.
 */
public class MatchForTermination extends MatchNotification {

    private EncryptedData transferInformation;


    public MatchForTermination() {
    }

    public MatchForTermination(String encryptedOasiNumber, String previousRetirementFundUid, String newRetirementFundUid, LocalDate commencementDate, LocalDate terminationDate, EncryptedData transferInformation) {
        super(encryptedOasiNumber, previousRetirementFundUid, newRetirementFundUid, commencementDate, terminationDate);
        this.transferInformation = transferInformation;
    }

    public EncryptedData getTransferInformation() {
        return transferInformation;
    }

    public void setTransferInformation(EncryptedData transferInformation) {
        this.transferInformation = transferInformation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        MatchForTermination that = (MatchForTermination) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(transferInformation, that.transferInformation)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(transferInformation)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .appendSuper(super.toString())
                .append("transferInformation", transferInformation)
                .toString();
    }
}
