package com.swed.fuelcounter.persistence;


/**
@Component
public class AmountRepositoryImpl {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private AmountRepository repository;

    @SuppressWarnings("unused")
    public List<Amount> sumAmountByDate() {
        String hql = "SELECT NEW Amount(Sum(price*volume) as amount, date) FROM FuelRecord GROUP BY date";
        TypedQuery<Amount> query = entityManager.createQuery(hql, Amount.class);
        //query.setParameter("id", id);
        return query.getResultList();
    }

    @SuppressWarnings("unused")
    public List<Amount> sumAmountByMonthAndDriver(int driverId) {
        String hql = "SELECT NEW Amount(Sum(price*volume) as amount, date) FROM FuelRecord WHERE driverId = :driverId GROUP BY date";
        TypedQuery<Amount> query = entityManager.createQuery(hql, Amount.class);
        query.setParameter("driverId", driverId);
        return query.getResultList();
    }

}

 **/