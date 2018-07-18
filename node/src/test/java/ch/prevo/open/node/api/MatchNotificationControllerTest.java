package ch.prevo.open.node.api;

import ch.prevo.open.encrypted.model.MatchForCommencement;
import ch.prevo.open.encrypted.services.Cryptography;
import ch.prevo.open.node.NodeApplication;
import ch.prevo.open.node.config.NodeConfiguration;
import ch.prevo.open.node.data.provider.MockProviderFactory;
import ch.prevo.open.node.services.MatchNotificationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.serviceloader.ServiceListFactoryBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.Collections;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {NodeApplication.class, MatchNotificationControllerTest.Config.class})
@WebAppConfiguration
public class MatchNotificationControllerTest extends RestBaseTest {

    @TestConfiguration
    static class Config {
        @Inject
        private NodeConfiguration nodeConfiguration;

        @Bean
        public MatchNotificationService matchNotificationService() throws Exception {
            final ServiceListFactoryBean factory = Mockito.mock(ServiceListFactoryBean.class);
            given(factory.getObject()).willReturn(Collections.singletonList(new MockProviderFactory()));
            return new MatchNotificationService(factory, nodeConfiguration);
        }
    }

    @Test
    public void sendCommencementNotificationToPreviousRetirementFund() throws Exception {
        // given
        MatchForCommencement commencementMatchNotification = new MatchForCommencement();
        commencementMatchNotification.setEncryptedOasiNumber(Cryptography.digestOasiNumber("756.1234.5678.97"));
        commencementMatchNotification.setRetirementFundUid("CHE-109.740.084");
        commencementMatchNotification.setPreviousRetirementFundUid("CHE-109.537.488");
        commencementMatchNotification.setCommencementDate(LocalDate.of(2018, 7, 1));
        commencementMatchNotification.setTerminationDate(LocalDate.of(2018, 6, 30));

        // when
        mockMvc.perform(post("/commencement-match-notification")
                .content(this.convertToJson(commencementMatchNotification))
                .contentType(contentType))

                //then

                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.encryptedDataBase64", notNullValue(String.class)))
                .andExpect(jsonPath("$.encryptedSymmetricKeyBase64", notNullValue(String.class)));
    }
}
