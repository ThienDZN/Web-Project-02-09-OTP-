package vn.iotstar.dao.impl;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import vn.iotstar.config.JpaConfig;
import vn.iotstar.dao.IProductDao;
import vn.iotstar.entity.Product;

public class ProductDao implements IProductDao {
    @Override
    public void insert(Product product) {
        EntityManager entityManager = JpaConfig.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(product);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void update(Product product) {
        EntityManager entityManager = JpaConfig.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(product);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void delete(Long productId) throws Exception {
        EntityManager entityManager = JpaConfig.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Product product = entityManager.find(Product.class, productId);
            if (product == null) {
                throw new Exception("No catalog entry was found for id = " + productId);
            }
            entityManager.remove(product);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Product findById(Long productId) {
        EntityManager entityManager = JpaConfig.getEntityManager();
        try {
            TypedQuery<Product> query = entityManager.createQuery(
                    "SELECT p FROM Product p JOIN FETCH p.category WHERE p.productId = :id", Product.class);
            query.setParameter("id", productId);
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Product> findAll() {
        EntityManager entityManager = JpaConfig.getEntityManager();
        try {
            return entityManager.createNamedQuery("Product.findAll", Product.class).getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Product> findLatestActive(int limit) {
        EntityManager entityManager = JpaConfig.getEntityManager();
        try {
            TypedQuery<Product> query = entityManager.createQuery(
                    "SELECT p FROM Product p JOIN FETCH p.category ORDER BY p.productId DESC", Product.class);
            query.setMaxResults(limit);
            return query.getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Product> findActive(int page, int pageSize) {
        EntityManager entityManager = JpaConfig.getEntityManager();
        try {
            TypedQuery<Product> query = entityManager.createQuery(
                    "SELECT p FROM Product p JOIN FETCH p.category ORDER BY p.productId DESC", Product.class);
            query.setFirstResult(Math.max(0, (page - 1) * pageSize));
            query.setMaxResults(pageSize);
            return query.getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public int countActive() {
        EntityManager entityManager = JpaConfig.getEntityManager();
        try {
            Query query = entityManager.createQuery("SELECT COUNT(p) FROM Product p");
            return ((Long) query.getSingleResult()).intValue();
        } finally {
            entityManager.close();
        }
    }
}
