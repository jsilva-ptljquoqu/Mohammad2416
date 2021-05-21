package com.momir.contactmgr;

import com.momir.contactmgr.model.Contact;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.util.List;


/**
 * Created by Mohammad.Mirzakhani on 5/13/17.
 */
public class Application {

    //Hold reusable refrence to SessionFactory (Since we need only one)
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();

    }

    public static void main(String[] args) {

        Contact contact = new Contact.ContactBuilder("Mohammad", "Mirzakhani")
                .withEmail("mohammad.mirzakhany@gmail.com.com")
                .withPhone(000000000L)
                .build();

        System.out.println(contact);

        //Insert to db
        int id = save(contact);

        //select a list of contacts
        for (Contact c : fetchAllContacts()) {
            System.out.println(c);
        }
        //OR
//        fetchAllContacts().stream().ForEach(System.out::println);


        //Update
        Contact c = findContactById(id);
        c.setFirstName("Ali");
        update(c);

        System.out.println("----Update-----");
        for (Contact newC : fetchAllContacts()) {
            System.out.println(newC);
        }

        System.out.println("%n%n Delete %n%n ");

        Contact contactWillBeDelete = findContactById(id);
        delete(contactWillBeDelete);

        System.out.println("----Deleted-----");
        for (Contact newCc : fetchAllContacts()) {
            System.out.println(newCc);
        }
    }


    private static int save(Contact contact) {

        //Open a session
        Session session = sessionFactory.openSession();

        //Begin a transaction
        session.beginTransaction();

        //Use the session to save the contact
        int id = (int) session.save(contact);

        //Commit the transaction
        session.getTransaction().commit();

        //close the session
        session.close();

        return id;
    }


    @SuppressWarnings("unchecked")
    private static List<Contact> fetchAllContacts() {
        //Open session
        Session session = sessionFactory.openSession();
        //Create Criteria
        Criteria criteria = session.createCriteria(Contact.class);
        List<Contact> contacts = criteria.list();
        //close the session
        session.close();

        return contacts;
    }

    private static Contact findContactById(int id) {
        Session session = sessionFactory.openSession();

        //Retrieve the persistent object (or null if not found)
        Contact contact = session.get(Contact.class, id);

        session.close();

        return contact;
    }

    private static void update(Contact contact) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.update(contact);

        session.getTransaction().commit();
        session.close();
    }

    private static void delete(Contact contact){
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.delete(contact);

        session.getTransaction().commit();
        session.close();
    }


}
