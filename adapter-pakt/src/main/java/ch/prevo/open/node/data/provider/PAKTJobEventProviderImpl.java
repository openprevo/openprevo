package ch.prevo.open.node.data.provider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import ch.prevo.open.data.api.Address;
import ch.prevo.open.data.api.CapitalTransferInformation;
import ch.prevo.open.data.api.JobEnd;
import ch.prevo.open.data.api.JobInfo;
import ch.prevo.open.data.api.JobStart;
import ch.prevo.pakt.PartnerVermittlungRepository;
import ch.prevo.pakt.TozsPtverm;
import ch.prevo.pakt.zd.utils.CdMeld;

@Service
public class PAKTJobEventProviderImpl implements JobEndProvider, JobStartProvider {

	@Inject
	private PartnerVermittlungRepository repository;

	@Override
	public List<JobEnd> getJobEnds() {
		final List<JobEnd> jobEnds = new ArrayList<>();

		repository.findAll().forEach(ptVerm -> {
			if (CdMeld.DADURCHF.getCode() == ptVerm.getCdmeld()) {
				jobEnds.add(buildJobEnd(ptVerm));
			}
		});
		return jobEnds;
	}

	private JobEnd buildJobEnd(TozsPtverm ptVerm) {
		// TODO fulfill missing properties
		return new JobEnd(Integer.toString(ptVerm.getId().getId()), buildJobInfo(ptVerm));
	}

	private JobInfo buildJobInfo(TozsPtverm ptVerm) {
		JobInfo jobInfo = new JobInfo();
		jobInfo.setOasiNumber(ptVerm.getAhv());
		jobInfo.setRetirementFundUid(getRetirementFundId(ptVerm));
		jobInfo.setInternalPersonId(ptVerm.getIdgeschaeftpol());
		jobInfo.setInternalReferenz(ptVerm.getNameve());
		// TODO fulfill missing properties
		return jobInfo;

	}

	private String getRetirementFundId(TozsPtverm ptVerm) {
		// TODO retrieve uid
		return Short.toString(ptVerm.getCdstf());
	}

	@Override
	public List<JobStart> getJobStarts() {
		final List<JobStart> jobStarts = new ArrayList<>();

		repository.findAll().forEach(ptVerm -> {
			if (CdMeld.NEUEINTRERF.getCode() == ptVerm.getCdmeld()) {
				jobStarts.add(buildJobStart(ptVerm));
			}
		});
		return jobStarts;
	}

	private JobStart buildJobStart(TozsPtverm ptVerm) {
		// TODO fulfill missing properties
		return new JobStart(Integer.toString(ptVerm.getId().getId()), buildJobInfo(ptVerm), buildCapitalTransferInformation(ptVerm));
	}

	private CapitalTransferInformation buildCapitalTransferInformation(TozsPtverm ptVerm) {
		// TODO Auto-generated method stub
		CapitalTransferInformation capitalTransferInfo = new CapitalTransferInformation(ptVerm.getNameve(), ptVerm.getTxtiban());
		capitalTransferInfo.setAddress(buuildAddress(ptVerm));
		return capitalTransferInfo;
	}

	private Address buuildAddress(TozsPtverm ptVerm) {
		// TODO Auto-generated method stub
		Address address = new Address();
		return address;
	}
}
