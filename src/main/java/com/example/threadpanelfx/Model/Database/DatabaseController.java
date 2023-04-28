package com.example.threadpanelfx.Model.Database;

import java.util.List;

public class DatabaseController {
    private final IEntryDAO m_entryDao = new EntryDAO();

    /*public static void main(String[] args)
    {
        // тест

        DatabaseController controller = new DatabaseController();

        Entry entry = new Entry("testPlayer1", 2);
        controller.saveOrUpdateEntry(entry);
        var res = controller.findAllEntries();
        System.out.println(res);
    }*/

    public DatabaseController() {
    }

    public Entry findEntry(String playerName) {
        return m_entryDao.findByName(playerName);
    }

    public void saveEntry(Entry entry) {
        m_entryDao.save(entry);
    }

    public void deleteEntry(Entry entry) {
        m_entryDao.delete(entry);
    }

    public void updateEntry(Entry entry) {
        m_entryDao.update(entry);
    }

    public List<Entry> findAllEntries() {
        return m_entryDao.findAll();
    }

    public void saveOrUpdateEntry(Entry entry)
    {
        if (findEntry(entry.GetPlayerName()) != null)
            updateEntry(entry);
        else
            saveEntry(entry);
    }
}
