package org.example.eshopnew.product;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class ProductService {

    private final ProductRepository repository;
    private final ProductSpecificationMapper specificationMapper;

    public ProductService(ProductRepository repository, ProductSpecificationMapper specificationMapper) {
        this.repository = repository;
        this.specificationMapper = specificationMapper;
    }

    @Transactional(readOnly = true)
    public Page<Product> findAll(ProductFilter filter, Pageable pageable) {
        return repository.findAll(specificationMapper.toSpecification(filter), pageable);
    }

    @Transactional(readOnly = true)
    public Product findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found: " + id));
    }

    public Product create(Product product) {
        return repository.save(product);
    }

    public Product update(UUID id, Product product) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Product not found: " + id);
        }
        product.setId(id);
        return repository.save(product);
    }

    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Product not found: " + id);
        }
        repository.deleteById(id);
    }
}
