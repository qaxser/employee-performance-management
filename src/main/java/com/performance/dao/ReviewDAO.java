package com.performance.dao;

import com.performance.model.Review;
import com.performance.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class ReviewDAO {
    
    // Method called by EmployeeBean.getLatestReview() and ReviewHistoryBean.loadEmployeeAndReviews()
    public List<Review> getReviewsByEmployeeId(Long employeeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Review> query = session.createQuery(
                "FROM Review r WHERE r.employee.id = :employeeId ORDER BY r.reviewDate DESC", 
                Review.class
            );
            query.setParameter("employeeId", employeeId);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public void save(Review review) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(review);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to save review", e);
        }
    }
    
    public Review findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Review.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // Legacy method - keeping for backwards compatibility
    public List<Review> findByEmployeeId(Long employeeId) {
        return getReviewsByEmployeeId(employeeId);
    }
    
    public Review getLatestReviewForEmployee(Long employeeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Review> query = session.createQuery(
                "FROM Review r WHERE r.employee.id = :employeeId ORDER BY r.reviewDate DESC", 
                Review.class
            );
            query.setParameter("employeeId", employeeId);
            query.setMaxResults(1);
            List<Review> results = query.list();
            return results.isEmpty() ? null : results.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // Method to get the second latest review for comparison (for notifications)
    public Review getSecondLatestReviewForEmployee(Long employeeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Review> query = session.createQuery(
                "FROM Review r WHERE r.employee.id = :employeeId ORDER BY r.reviewDate DESC", 
                Review.class
            );
            query.setParameter("employeeId", employeeId);
            query.setMaxResults(2);
            List<Review> results = query.list();
            return results.size() >= 2 ? results.get(1) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public List<Review> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Review> query = session.createQuery(
                "FROM Review r ORDER BY r.reviewDate DESC", 
                Review.class
            );
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public void update(Review review) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(review);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to update review", e);
        }
    }
    
    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Review review = session.get(Review.class, id);
            if (review != null) {
                session.delete(review);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to delete review", e);
        }
    }
}