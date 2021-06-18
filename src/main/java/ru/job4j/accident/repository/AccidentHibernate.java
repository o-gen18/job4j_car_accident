package ru.job4j.accident.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;

import java.util.List;
import java.util.function.Function;

@Repository
public class AccidentHibernate {
    private final SessionFactory sf;

    public AccidentHibernate(SessionFactory sf) {
        this.sf = sf;
        List<Rule> rules = List.of(
                Rule.of("1 Driving passenger cars"),
                Rule.of("2 Driving a bus"),
                Rule.of("3 Parking rule")
        );
        List<AccidentType> types = List.of(
                AccidentType.of("1 Vehicle and vehicle"),
                AccidentType.of("2 Vehicle and human"),
                AccidentType.of("3 Vehicle and bicycle")
        );
        Accident accident = Accident.of("Bus crashed on the crossroad",
                "Yesterday on the central crossroad a bus full of passengers fell over. No injured.",
                "Sadovaya 50",
                types.get(1));
        accident.addRule(rules.get(1));
        accident.addRule(rules.get(0));

        rules.forEach(this::save);
        types.forEach(this::save);
        this.saveAccident(accident);
    }

    private <T> T doTransaction(final Function<Session, T> command) {
        final Session session = sf.openSession();
        final Transaction tr = session.beginTransaction();
        try {
            T rsl = command.apply(session);
            tr.commit();
            return rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    private <T> T save(T model) {
        return doTransaction(session -> {
            session.save(model);
            return model;
        });
    }

    private Accident create(Accident accident) {
        return this.doTransaction(session -> {
            session.save(accident);
            return accident;
        });
    }

    private Accident update(Accident accident) {
        return this.doTransaction(session -> {
            session.update(accident);
            return accident;
        });
    }

    public Accident saveAccident(Accident accident) {
        if (accident.getId() == 0) {
            return create(accident);
        } else {
            return update(accident);
        }
    }

    public List<AccidentType> getTypes() {
        return doTransaction(session -> session
                .createQuery("FROM AccidentType", AccidentType.class)
                .list());
    }

    public List<Rule> getRules() {
        return doTransaction(session -> session
                .createQuery("FROM Rule", Rule.class)
                .list());
    }

    public List<Accident> getAccidents() {
        return doTransaction(session -> session
                .createQuery("SELECT DISTINCT ac FROM Accident ac " +
                        "JOIN FETCH ac.type " +
                        "JOIN FETCH ac.rules", Accident.class)
                .list());
    }

    public Accident getAccidentById(int id) {
        return doTransaction(session -> session
                .createQuery("SELECT DISTINCT ac FROM Accident ac " +
                        "JOIN FETCH ac.type " +
                        "JOIN FETCH ac.rules WHERE ac.id = :ID", Accident.class)
                .setParameter("ID", id)
                .uniqueResult());
    }

    public Accident findAndSetRules(String[] ids, Accident accident) {
        if (ids != null) {
            for (String id : ids) {
                accident.addRule(Rule.of(Integer.parseInt(id), null));
            }
        }
        return accident;
    }
}
