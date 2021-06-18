package ru.job4j.accident.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

/**
 * Class for saving and retrieving objects from database. Based upon JdbcTemplate.
 */
//@Repository
public class AccidentJdbcTemplate {
    private final JdbcTemplate jdbc;

    /**
     * Row mapper used for compiling Accident object based on data stored in Result Set.
     */
    private RowMapper<Accident> accidentRowMapper = (rs, row) -> {
        Accident accident = new Accident();
        accident.setId(rs.getInt("id"));
        accident.setName(rs.getString("name"));
        accident.setText(rs.getString("text"));
        accident.setAddress(rs.getString("address"));
        accident.setType(AccidentType.of(rs.getInt("accident_type_id"), null));
        return accident;
    };

    private RowMapper<AccidentType> accidentTypeRowMapper =
            (rs, row) -> AccidentType.of(rs.getInt("id"), rs.getString("name"));

    private RowMapper<Rule> ruleRowMapper =
            (rs, row) -> Rule.of(rs.getInt("id"), rs.getString("name"));

    public AccidentJdbcTemplate(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        List<Rule> rules = List.of(
                Rule.of("Driving passenger cars"),
                Rule.of("Driving a bus"),
                Rule.of("Parking rule")
        );
        List<AccidentType> types = List.of(
                AccidentType.of("Vehicle and vehicle"),
                AccidentType.of("Vehicle and human"),
                AccidentType.of("Vehicle and bicycle")
        );
        Accident accident = Accident.of("Bus crashed on the crossroad",
                "Yesterday on the central crossroad a bus full of passengers fell over. No injured.",
                "Sadovaya 50",
                types.get(1));
        accident.addRule(rules.get(1));
        accident.addRule(rules.get(0));

        rules.forEach(this::saveAccidentRule);
        types.forEach(this::saveAccidentType);
        this.saveAccident(accident);
    }

    public Accident getAccidentById(int id) {
        Accident accident = jdbc.queryForObject("SELECT * FROM accident WHERE id = ?", accidentRowMapper, id);
        if (accident == null) {
            return null;
        }
        accident.setType(getTypeById(accident.getType().getId()));
        List<Rule> rules = getRulesLinkedWithAccident(accident);
        rules.forEach(accident::addRule);
        return accident;
    }

    /**
     * Puts an accident to the storage.
     * @param accident concrete accident object.
     * @return accident object with unique generated id.
     */
    public Accident saveAccident(Accident accident) {
        if (accident.getId() == 0) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbc.update((connection) -> {
                PreparedStatement ps = connection.prepareStatement("INSERT INTO accident (name, text, address, accident_type_id) values (?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, accident.getName());
                ps.setString(2, accident.getText());
                ps.setString(3, accident.getAddress());
                ps.setInt(4, accident.getType().getId());
                return ps;
            }, keyHolder);
            accident.setId((Integer) keyHolder.getKeys().get("id"));
            for (Rule rule : accident.getRules()) {
                jdbc.update("INSERT INTO accident_rule (accident_id, rule_id) VALUES (?, ?)", accident.getId(), rule.getId());
            }
        } else {
            jdbc.update("UPDATE accident SET name = ?, text = ?, address = ?, accident_type_id = ? WHERE id = ?",
                    accident.getName(),
                    accident.getText(),
                    accident.getAddress(),
                    accident.getType().getId(),
                    accident.getId());
            jdbc.update("DELETE FROM accident_rule WHERE accident_id = ?", accident.getId());
            for (Rule rule : accident.getRules()) {
                jdbc.update("INSERT INTO accident_rule VALUES (?, ?)", accident.getId(), rule.getId());
            }
        }
        return accident;
    }

    public AccidentType findAccidentTypeByName(String name) {
        return jdbc.queryForObject(
                "SELECT * FROM accident_type WHERE name = ?", accidentTypeRowMapper, name);
    }

    public Rule findRuleByName(String name) {
        return jdbc.queryForObject(
                "SELECT * FROM rule WHERE name = ?", ruleRowMapper, name);
    }

    /**
     * Fetches a rule object from the database by its id.
     * @param id Id of a rule to find.
     * @return Rule object that was found in the database.
     */
    public Rule findRuleById(int id) {
        return jdbc.queryForObject("SELECT * FROM rule WHERE id = ?", ruleRowMapper, id);
    }

    /**
     * Removes an accident object from the storage by id.
     * @param accident accident object to be removed.
     * @return the number of rows affected.
     */
    public int removeAccident(Accident accident) {
        return jdbc.update("DELETE FROM accident WHERE id = ?", accident.getId());
    }

    public List<Accident> getAccidents() {
        List<Accident> list = jdbc.query("SELECT * FROM accident", accidentRowMapper);
        for (Accident accident : list) {
            accident.setType(getTypeById(accident.getType().getId()));

            List<Rule> rules = getRulesLinkedWithAccident(accident);
            rules.forEach(accident::addRule);
        }
        return list;
    }

    public List<Rule> getRulesLinkedWithAccident(Accident accident) {
        return jdbc.query(
                "SELECT * FROM accident_rule INNER JOIN rule ON accident_rule.rule_id = rule.id WHERE accident_rule.accident_id = ?",
                ruleRowMapper, accident.getId());
    }

    public AccidentType getTypeById(int id) {
        return jdbc.queryForObject("SELECT * FROM accident_type WHERE id = ?", accidentTypeRowMapper, id);
    }

    public AccidentType saveAccidentType(AccidentType type) {
        if (type.getId() == 0) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbc.update(connection -> {
                        PreparedStatement ps = connection.prepareStatement("INSERT INTO accident_type (name) VALUES (?)",
                                Statement.RETURN_GENERATED_KEYS);
                        ps.setString(1, type.getName());
                        return ps;
                    }, keyHolder);
            type.setId((Integer) keyHolder.getKeys().get("id"));
        } else {
            jdbc.update("UPDATE accident_type SET name = ? WHERE id = ?", type.getName(), type.getId());
        }
        return type;
    }

    public List<AccidentType> getTypes() {
        return jdbc.query("SELECT * FROM accident_type", accidentTypeRowMapper);
    }

    /**
     * Finds the type of a given accident in the database by type-id and
     * sets its name.
     * @param accident Accident object having accident type inner object with only known id.
     * @return Accident object with set accident type.
     */
    public Accident findAndSetType(Accident accident) {
        AccidentType type = accident.getType();
        type.setName(getTypeById(type.getId()).getName());
        return accident;
    }

    public Rule saveAccidentRule(Rule rule) {
        if (rule.getId() == 0) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbc.update(connection -> {
                        PreparedStatement ps = connection.prepareStatement("INSERT INTO rule (name) values (?)",
                                Statement.RETURN_GENERATED_KEYS);
                        ps.setString(1, rule.getName());
                        return ps;
                    }, keyHolder);
            rule.setId((Integer) keyHolder.getKeys().get("id"));
        } else {
            jdbc.update("UPDATE rule SET name = ? WHERE id = ?", rule.getName(), rule.getId());
        }
        return rule;
    }

    public List<Rule> getRules() {
        return jdbc.query("SELECT * FROM rule", ruleRowMapper);
    }

    public Accident findAndSetRules(String[] ids, Accident accident) {
        if (ids != null) {
            for (String id: ids) {
                accident.addRule(findRuleById(Integer.parseInt(id)));
            }
        }
        return accident;
    }
}