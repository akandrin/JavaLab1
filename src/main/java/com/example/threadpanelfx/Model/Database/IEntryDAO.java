package com.example.threadpanelfx.Model.Database;

import java.util.List;

public interface IEntryDAO {
    Entry findByName(String playerName);
    void save(Entry entry);
    void update(Entry entry);
    void delete(Entry user);
    List<Entry> findAll();
}
