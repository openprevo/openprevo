package ch.prevo.open.hub.nodes;

import ch.prevo.open.encrypted.model.CapitalTransferInformation;
import ch.prevo.open.encrypted.model.InsurantInformation;
import ch.prevo.open.encrypted.model.CommencementMatchNotification;
import ch.prevo.open.encrypted.model.TerminationMatchNotification;
import ch.prevo.open.hub.match.Match;
import ch.prevo.open.hub.match.MatcherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Service
public class NodeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NodeService.class);

    private final NodeRegistry nodeRegistry;

    private final MatcherService matcherService;

    private final RestTemplate restTemplate;

    @Inject
    public NodeService(RestTemplateBuilder restTemplateBuilder, NodeRegistry nodeRegistry, MatcherService matcherService) {
        this.restTemplate = restTemplateBuilder.build();
        this.nodeRegistry = nodeRegistry;
        this.matcherService = matcherService;
    }

    public Set<InsurantInformation> getCurrentExits() {
        Set<InsurantInformation> exits = new HashSet<>();
        for (NodeConfiguration nodeConfig : nodeRegistry.getCurrentNodes()) {
            List<InsurantInformation> pensionFundExits = lookupInsurantInformationList(nodeConfig.getJobExitsUrl());
            List<InsurantInformation> filteredInformation = filterInvalidAndAlreadyMatchedEntries(nodeConfig,
                    pensionFundExits,
                    matcherService::employmentCommencementNotMatched);
            exits.addAll(filteredInformation);
        }
        return exits;
    }

    public Set<InsurantInformation> getCurrentEntries() {
        Set<InsurantInformation> entries = new HashSet<>();
        for (NodeConfiguration nodeConfig : nodeRegistry.getCurrentNodes()) {
            List<InsurantInformation> pensionFundEntries = lookupInsurantInformationList(nodeConfig.getJobEntriesUrl());
            List<InsurantInformation> filteredInformation = filterInvalidAndAlreadyMatchedEntries(nodeConfig,
                    pensionFundEntries,
                    matcherService::employmentTerminationNotMatched);
            entries.addAll(filteredInformation);
        }
        return entries;
    }

    private List<InsurantInformation> filterInvalidAndAlreadyMatchedEntries(NodeConfiguration nodeConfiguration,
                                                                            List<InsurantInformation> insurantInformation,
                                                                            Predicate<InsurantInformation> notYetMatched) {
        List<InsurantInformation> invalidMatches = verifyInsurantInformationOnlyBelongsToThisNode(nodeConfiguration,
                insurantInformation);
        return insurantInformation.stream()
                .filter(((Predicate<InsurantInformation>) invalidMatches::contains).negate())
                .filter(notYetMatched)
                .collect(toList());
    }

    private List<InsurantInformation> lookupInsurantInformationList(String url) {
        try {
            InsurantInformation[] nodeExits = restTemplate.getForObject(url, InsurantInformation[].class);
            return nodeExits == null ? emptyList() : asList(nodeExits);
        } catch (Exception e) {
            LOGGER.error("Could not fetch data from URL {}", url, e);
        }
        return emptyList();
    }

    private List<InsurantInformation> verifyInsurantInformationOnlyBelongsToThisNode(NodeConfiguration nodeConfig,
                                                                                     List<InsurantInformation> pensionFundExits) {
        List<InsurantInformation> invalidInsurants = pensionFundExits.stream()
                .filter(insurant -> !nodeConfig.containsRetirementFundUid(insurant.getRetirementFundUid()))
                .collect(toList());

        if (invalidInsurants.size() > 0) {
            LOGGER.error("Invalid data received from node {} the following insurants have an invalid retirement fund",
                    nodeConfig, invalidInsurants);
        }
        return invalidInsurants;
    }

    public void notifyMatches(List<Match> matches) {
        List<NodeConfiguration> nodeConfigurations = nodeRegistry.getCurrentNodes();

        for (Match match : matches) {
            try {
                // notify new node
                final CapitalTransferInformation transferInformation = tryNotifyNewRetirementFundAboutMatch(findNodeToNotify(match.getNewRetirementFundUid(), nodeConfigurations), match);
                // notify previous node
                tryNotifyPreviousRetirementFundAboutTerminationMatch(findNodeToNotify(match.getPreviousRetirementFundUid(), nodeConfigurations), match, transferInformation);
            } catch (Exception e) {
                LOGGER.error("Unexpected error occurred while notifying match: {}", match, e);
            }
        }
    }

    private CapitalTransferInformation tryNotifyNewRetirementFundAboutMatch(NodeConfiguration nodeConfig, Match match) {
        try {
            TerminationMatchNotification matchNotification = new TerminationMatchNotification();
            matchNotification.setEncryptedOasiNumber(match.getEncryptedOasiNumber());
            matchNotification.setRetirementFundUid(match.getNewRetirementFundUid());
            matchNotification.setReferenceId("");
            matchNotification.setPreviousRetirementFundUid(match.getPreviousRetirementFundUid());
            matchNotification.setEntryDate(match.getEntryDate());
            return restTemplate.postForObject(nodeConfig.getCommencementMatchNotifyUrl(), matchNotification, CapitalTransferInformation.class);
        } catch (Exception e) {
            // TODO persist information that match needs to be notified later
            LOGGER.error("Could not send notification for match {} to URL {}", match, nodeConfig.getCommencementMatchNotifyUrl(), e);
            return null;
        }
    }

    private void tryNotifyPreviousRetirementFundAboutTerminationMatch(NodeConfiguration nodeConfig, Match match, CapitalTransferInformation transferInformation) {
        try {
            CommencementMatchNotification matchNotification = new CommencementMatchNotification();
            matchNotification.setEncryptedOasiNumber(match.getEncryptedOasiNumber());
            matchNotification.setRetirementFundUid(match.getPreviousRetirementFundUid());
            matchNotification.setNewRetirementFundUid(match.getNewRetirementFundUid());
            matchNotification.setEntryDate(match.getEntryDate());
            matchNotification.setExitDate(match.getExitDate());
            matchNotification.setTransferInformation(transferInformation);
            restTemplate.postForEntity(nodeConfig.getTerminationMatchNotifyUrl(), matchNotification, Void.class);
        } catch (Exception e) {
            // TODO persist information that match needs to be notified later
            LOGGER.error("Could not send notification for match {} to URL {}", match, nodeConfig.getTerminationMatchNotifyUrl(), e);
        }
    }

    private NodeConfiguration findNodeToNotify(String retirementFundUid, List<NodeConfiguration> nodeConfigurations) {

        return nodeConfigurations.stream()
                .filter(n -> n.containsRetirementFundUid(retirementFundUid)).findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Did not find a node for retirement fund UID: " + retirementFundUid));
    }
}
