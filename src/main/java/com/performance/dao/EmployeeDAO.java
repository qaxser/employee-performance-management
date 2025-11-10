package com.performance.dao;

import com.performance.model.Employee;
import com.performance.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class EmployeeDAO {
    
    // Method called by EmployeeBean.saveEmployee()
    public void saveEmployee(Employee employee) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(employee);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to save employee", e);
        }
    }
    
    // Method called by EmployeeBean.loadEmployees()
    public List<Employee> getAllEmployees() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Employee> query = session.createQuery("FROM Employee e ORDER BY e.name", Employee.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // Method called by ReviewHistoryBean.loadEmployeeAndReviews()
    public Employee getEmployeeById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Employee.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // Legacy method - keeping for backwards compatibility
    public void save(Employee employee) {
        saveEmployee(employee);
    }
    
    // Legacy method - keeping for backwards compatibility
    public Employee findById(Long id) {
        return getEmployeeById(id);
    }
    
    // Legacy method - keeping for backwards compatibility
    public List<Employee> findAll() {
        return getAllEmployees();
    }
    
    public List<Employee> searchEmployees(String searchTerm, String department) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            StringBuilder hql = new StringBuilder("FROM Employee e WHERE 1=1");
            
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                hql.append(" AND LOWER(e.name) LIKE :searchTerm");
            }
            
            if (department != null && !department.trim().isEmpty() && !department.equals("All")) {
                hql.append(" AND e.department = :department");
            }
            
            hql.append(" ORDER BY e.name");
            
            Query<Employee> query = session.createQuery(hql.toString(), Employee.class);
            
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                query.setParameter("searchTerm", "%" + searchTerm.toLowerCase() + "%");
            }
            
            if (department != null && !department.trim().isEmpty() && !department.equals("All")) {
                query.setParameter("department", department);
            }
            
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public void update(Employee employee) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(employee);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to update employee", e);
        }
    }
    
    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Employee employee = session.get(Employee.class, id);
            if (employee != null) {
                session.delete(employee);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to delete employee", e);
        }
    }
    
    public List<String> getAllDepartments() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<String> query = session.createQuery(
                "SELECT DISTINCT e.department FROM Employee e ORDER BY e.department", 
                String.class
            );
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}