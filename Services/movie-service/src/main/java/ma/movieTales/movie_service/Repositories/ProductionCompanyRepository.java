package ma.movieTales.movie_service.Repositories;

import ma.movieTales.movie_service.Entity.ProductionCompany;
import org.hibernate.sql.ast.tree.expression.JdbcParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductionCompanyRepository extends JpaRepository<ProductionCompany , Long> {

    @Query("SELECT p FROM ProductionCompany p WHERE p.name = :name")
    Optional<ProductionCompany> getProdCompanyByName(@Param("name") String name);

}
