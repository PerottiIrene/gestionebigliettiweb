package it.prova.gestionebigliettiweb.dao;

import java.util.List;

import javax.persistence.EntityManager;

import it.prova.gestionebigliettiweb.model.Biglietto;

public class BigliettoDAOImpl implements BigliettoDAO {

	private EntityManager entityManager;

	@Override
	public List<Biglietto> list() throws Exception {
		return entityManager.createQuery("from Biglietto", Biglietto.class).getResultList();
	}

	@Override
	public Biglietto findOne(Long id) throws Exception {
		return entityManager.find(Biglietto.class, id);
	}

	@Override
	public void update(Biglietto input) throws Exception {
		if (input == null) {
			throw new Exception("Problema valore in input");
		}
		entityManager.merge(input);

	}

	@Override
	public void insert(Biglietto input) throws Exception {
		if (input == null) {
			throw new Exception("Problema valore in input");
		}
		entityManager.persist(input);
	}

	@Override
	public void delete(Biglietto input) throws Exception {
		if (input == null) {
			throw new Exception("Problema valore in input");
		}
		entityManager.remove(entityManager.merge(input));

	}

	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;

	}

	@Override
	public List<Biglietto> findByExample(Biglietto input) throws Exception {

		if (input == null)
			throw new Exception("Valore di input non ammesso.");

		String query = "from Biglietto where 1=1 ";
		if (input.getProvenienza() != null && !input.getProvenienza().isEmpty()) {
			query += " and provenienza like '" + input.getProvenienza() + "%' ";
		}

		if (input.getDestinazione() != null && !input.getDestinazione().isEmpty()) {
			query += " and destinazione like '" + input.getDestinazione() + "%' ";
		}

		if (input.getPrezzo() != null && input.getPrezzo() > 0) {
			query += " and prezzo = '" + input.getPrezzo() + "' ";
		}

		if (input.getData() != null) {
			query += " and data='" + input.getData().toInstant() + "' ";
		}

		return entityManager.createQuery(query, Biglietto.class).getResultList();
	}

}
