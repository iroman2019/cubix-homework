package hu.cubix.hr.iroman.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import hu.cubix.hr.iroman.model.Request;
import hu.cubix.hr.iroman.model.RequestStatus;

public interface RequestRepository extends JpaRepository<Request, Long>, JpaSpecificationExecutor<Request> {

	@Query("SELECT r FROM Request r LEFT JOIN FETCH r.requester LEFT JOIN FETCH r.approver LEFT JOIN FETCH r.requester.position LEFT JOIN FETCH r.approver.position")
	Page<Request> findAllWithEmployees(Pageable pageable);

	Page<Request> findAll(Pageable pageable);

	@Query("SELECT r FROM Request r LEFT JOIN FETCH r.requester LEFT JOIN FETCH r.approver WHERE r.id=?1")
	Request findByIdWithEmployees(Long id);

	@Query("SELECT r.requestSatus FROM Request r WHERE r.id=?1")
	RequestStatus findRequestSatusById(long id);

}
