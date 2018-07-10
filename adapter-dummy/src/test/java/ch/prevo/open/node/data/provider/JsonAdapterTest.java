package ch.prevo.open.node.data.provider;

import ch.prevo.open.data.api.JobEnd;
import ch.prevo.open.data.api.JobStart;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JsonAdapter.class)
public class JsonAdapterTest {

    @Inject
    private JsonAdapter jsonAdapter;

    @Test
    public void getJobStartInformation() {
        List<JobStart> jobEndInformation = jsonAdapter.getJobStarts();
        assertThat(jobEndInformation).hasSize(3);
    }

    @Test
    public void getJobEndInformation() {
        List<JobEnd> jobEndInformation = jsonAdapter.getJobEnds();
        assertThat(jobEndInformation).hasSize(3);
    }
}