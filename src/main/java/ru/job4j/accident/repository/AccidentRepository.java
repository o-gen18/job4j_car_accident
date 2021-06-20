package ru.job4j.accident.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;

import java.util.List;
import java.util.Optional;

public interface AccidentRepository extends CrudRepository<Accident, Integer> {

    @Override
    @Query("SELECT DISTINCT ac FROM Accident ac JOIN FETCH ac.type FULL JOIN FETCH ac.rules")
    Iterable<Accident> findAll();

    @Override
    @Query("SELECT DISTINCT ac FROM Accident ac JOIN FETCH ac.type FULL JOIN FETCH ac.rules WHERE ac.id = :ID")
    Optional<Accident> findById(@Param("ID") Integer var1);

    @Query("FROM AccidentType")
    List<AccidentType> getTypes();

    @Query("FROM Rule")
    List<Rule> getRules();

    @Query("SELECT r FROM Rule r WHERE r.id = :ID")
    Rule findRuleById(@Param("ID") int id);

    @Query
    default Accident findAndSetRules(String[] ids, Accident accident) {
        if (ids != null) {
            for (String id : ids) {
                accident.addRule(this.findRuleById(Integer.parseInt(id)));
            }
        }
        return accident;
    }
}
