package com.nagarro.interview.api.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import com.nagarro.interview.api.model.ViewStatementRequest;
import com.nagarro.interview.api.model.ViewStatementResponse;
import com.nagarro.interview.db.entities.Account;
import com.nagarro.interview.db.entities.Statement;

@Component
public class FetchStatmentsService {
	private Session session;
	@PersistenceContext
	EntityManager factory;

	public ViewStatementResponse fetch(ViewStatementRequest request) {
		boolean isPeriodDetermined = request.getFromDate() != null;
		session = factory.unwrap(Session.class);
		Optional<Account> account = session
				.createQuery("SELECT b FROM Account b where b.accountNumber=" + request.getAccountId(), Account.class)
				.stream().findFirst();
		if (!account.isPresent())
			return new ViewStatementResponse(null, null, "ERROR No Account Data Found");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		List<Statement> stats;
		if (isPeriodDetermined)
			stats = session
					.createQuery("SELECT b FROM Statement b where b.accountId=" + account.get().getId() ,
							Statement.class)
					.stream()
					.filter(st -> LocalDate.parse(st.getDate(), formatter).compareTo(request.getFromDate()) >= 0
							&& LocalDate.parse(st.getDate(), formatter).compareTo(request.getToDate()) <= 0
							&& ( request.getToAmount()==null?true:
								(st.getAmount().compareTo(request.getFromAmount())>=0 && st.getAmount().compareTo(request.getToAmount())<=0))
					).collect(Collectors.toList());
		else {
			LocalDate beforePeriod = LocalDate.now().minusMonths(3);
			stats = session
					.createQuery("SELECT b FROM Statement b where b.accountId=" + account.get().getId() ,
							Statement.class)
					.stream().filter(st -> LocalDate.parse(st.getDate(), formatter).compareTo(beforePeriod) >= 0
					&& ( request.getToAmount()==null?true:
							(st.getAmount().compareTo(request.getFromAmount())>=0 && st.getAmount().compareTo(request.getToAmount())<=0))
				
							)
					.collect(Collectors.toList());
		}
		return new ViewStatementResponse(stats, account.get(), "Success");

	}

	

}
