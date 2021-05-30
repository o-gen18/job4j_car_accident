package ru.job4j.accident.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.accident.model.Accident;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class AccidentMem {
    private Map<Integer, Accident> accidents = new ConcurrentHashMap<>();
    private AtomicInteger index = new AtomicInteger(0);

    /**
     * Adds an accident to the storage.
     * @param accident concrete accident object.
     * @return unique generated index of an accident.
     */
    public Accident addAccident(Accident accident) {
        accident.setId(index.incrementAndGet());
        accidents.put(accident.getId(), accident);
        return accident;
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
}