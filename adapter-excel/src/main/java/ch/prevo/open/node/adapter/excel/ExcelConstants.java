package ch.prevo.open.node.adapter.excel;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public final class ExcelConstants {

    public static final int OASI_COLUMN_INDEX = 0;
    public static final int DATE_COLUMN_INDEX = 1;
    public static final int RETIREMENT_FUND_UID_COLUMN_INDEX = 2;
    public static final int REFERENCE_COLUMN_INDEX = 3;
    public static final int NAME_COLUMN_INDEX = 4;
    public static final int ADDITIONAL_NAME_COLUMN_INDEX = 5;
    public static final int STREET_COLUMN_INDEX = 6;
    public static final int POSTAL_CODE_COLUMN_INDEX = 7;
    public static final int CITY_COLUMN_INDEX = 8;
    public static final int IBAN_COLUMN_INDEX = 9;

    public static final String TERMINATION_INFORMATION_LABEL = "Austrittsinformation";
    public static final String COMMENCEMENT_INFORMATION_LABEL = "Eintrittsinformation";

    public static final String OASI_LABEL = "AHV-Nummer";
    public static final String RETIREMENT_FUND_UID_LABEL = "UID der VE";
    public static final String RETIREMENT_FUND_NAME_LABEL = "Name der VE";

    public static final String REFERENCE_LABEL = "Referenz";
    public static final String OWN_RETIREMENT_FUND_UID_LABEL = "UID der eigenen VE";
    public static final String OWN_REFERENCE_LABEL = "Eigene Referenz";
    public static final String DATE_LABEL = "Datum";
    public static final String COMMENCEMENT_DATE_LABEL = "Eintritt";
    public static final String TERMINATION_DATE_LABEL = "Austritt";

    public static final String NEW_RETIREMENT_FUND_UID_LABEL = "UID der neuen VE";
    public static final String NEW_RETIREMENT_FUND_NAME_LABEL = "Name der neuen VE";
    public static final String ADDITIONAL_NAME_LABEL = "Zusatzname";
    public static final String STREET_LABEL = "Strasse / Postfach";
    public static final String POSTAL_CODE_LABEL = "PLZ";
    public static final String CITY_LABEL = "Ort";
    public static final String IBAN_LABEL = "IBAN";
    public static final String NEW_RETIREMENT_FUND_REFERENCE_LABEL = "Referenznr. der neuen VE";

    public static final String OLD_RETIREMENT_FUND_UID_LABEL = "UID der ehemaligen VE";

    private ExcelConstants() {}

    public static final String COMMENCEMENTS_LABEL = "Eintritte";
    public static final String TERMINATION_LABEL = "Austritte";

    public static Date convert(LocalDate date) {
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
