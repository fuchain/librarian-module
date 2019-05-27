package repository;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import entities.User;
@Repository
public interface IUserRepository extends CrudRepository<User, String> {

}
