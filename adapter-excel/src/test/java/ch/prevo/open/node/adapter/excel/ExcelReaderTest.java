package ch.prevo.open.node.adapter.excel;

import ch.prevo.open.data.api.JobEnd;
import ch.prevo.open.data.api.JobInfo;
import ch.prevo.open.data.api.JobStart;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ExcelReaderTest {

    @Test
    public void readEmploymentTerminations() throws Exception {
        List<JobEnd> jobEnds = getExcelReader().getJobEnds();
        assertEquals(2, jobEnds.size());

        JobInfo firstJobInfo = jobEnds.get(0).getJobInfo();
        assertEquals("7566374437536", firstJobInfo.getOasiNumber());
        assertEquals(LocalDate.of(2018, 1, 1), firstJobInfo.getDate());
        assertEquals("CHE-109.740.084", firstJobInfo.getRetirementFundUid());
    }

    @Test
    public void readEmploymentStarts_blabla_betterNaming() throws Exception {
        List<JobStart> jobStarts = getExcelReader().getJobStarts();

        assertEquals(2, jobStarts.size());

        JobInfo secondJobInfo = jobStarts.get(1).getJobInfo();
        assertEquals("7568152139908", secondJobInfo.getOasiNumber());
        assertEquals(LocalDate.of(2018, 8, 16), secondJobInfo.getDate());
        assertEquals("CHE-223.471.073", secondJobInfo.getRetirementFundUid());
    }

    private ExcelReader getExcelReader() throws Exception {
        return new ExcelReader("/retirement-fund-test-data_de.xlsx");
    }

}