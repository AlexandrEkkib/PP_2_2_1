package hiber.dao;

import hiber.model.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class UserDaoImp implements UserDao {


    private SessionFactory sessionFactory;
    @Autowired
    public UserDaoImp(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void add(User user) {
        sessionFactory.getCurrentSession().save(user);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> listUsers() {
        TypedQuery<User> query = sessionFactory.getCurrentSession()
                .createQuery("select user from User user LEFT JOIN FETCH user.car", User.class);
        return query.getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public User getUserByModelSeriesCar(String model, int series) {
        String hql = "select user from User user JOIN FETCH user.car c WHERE c.model = :model AND c.series = :series";
        TypedQuery<User> query = sessionFactory.getCurrentSession().createQuery(hql, User.class);
        query.setParameter("model", model).setParameter("series", series);

        List<User> results = query.getResultList();

        return results.isEmpty() ? null : results.get(0);
    }

}
