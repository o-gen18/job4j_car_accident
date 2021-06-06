package ru.job4j.accident.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class AccidentMem {
    private Map<Integer, Accident> accidents = new ConcurrentHashMap<>();
    private AtomicInteger index = new AtomicInteger(0);

    private AtomicInteger typeIndex = new AtomicInteger(0);

    private Map<Integer, AccidentType> types = new ConcurrentHashMap<>();

    {
        saveAccidentType(AccidentType.of("Vehicle and human"));
        saveAccidentType(AccidentType.of("Vehicle and vehicle"));
        saveAccidentType(AccidentType.of("Vehicle and bicycle"));
    }

    public Accident getAccidentById(int id) {
        return accidents.get(id);
    }

    /**
     * Puts an accident to the storage.
     * @param accident concrete accident object.
     * @return Previous accident object associated with the given id or null if there is no
     * association.
     */
    public Accident saveAccident(Accident accident) {
        if (accident.getId() == 0) {
            accident.setId(index.incrementAndGet());
        }
        return accidents.put(accident.getId(), accident);
    }

    /**
     * Removes an accident object from the storage by id.
     * @param accident accident object to be removed.
     * @return removed object or null if there was no such object.
     */
    public Accident removeAccident(Accident accident) {
        return accidents.remove(accident.getId());
    }

    public Map<Integer, Accident> getAccidents() {
        return accidents;
    }

    public AccidentType getTypeById(int id) {
        return types.get(id);
    }

    public AccidentType saveAccidentType(AccidentType type) {
        if (type.getId() == 0) {
            type.setId(typeIndex.incrementAndGet());
        }
        return types.put(type.getId(), type);
    }

    public Map<Integer, AccidentType> getTypes() {
        return types;
    }
}