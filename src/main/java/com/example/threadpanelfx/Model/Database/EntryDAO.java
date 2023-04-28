package com.example.threadpanelfx.Model.Database;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class EntryDAO implements IEntryDAO {
    @Override
    public Entry findByName(String playerName) {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(Entry.class, playerName);
    }

    @Override
    public void save(Entry entry) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(entry);
        tx1.commit();
        session.close();
    }

    @Override
    public void update(Entry entry) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(entry);
        tx1.commit();
        session.close();
    }

    @Override
    public void delete(Entry entry) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(entry);
        tx1.commit();
        session.close();
    }

    @Override
    public List<Entry> findAll() {
        List<Entry> entries = (List<Entry>) HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("From Entry").list();
        return entries;
    }
}
