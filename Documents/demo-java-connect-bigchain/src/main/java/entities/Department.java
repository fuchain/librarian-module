package entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="tbl_Department")
public class Department {

	
	 @Id
	 @GeneratedValue(strategy = GenerationType.AUTO)
	 @Column(name="dep_id")
	 private Long id;	
	private String deparmentName;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "Department")
	private List<Employee> employess;
	
	
	
	public List<Employee> getEmployess() {
		return employess;
	}
	public void setEmployess(List<Employee> employess) {
		this.employess = employess;
	}
	public Long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public String getDeparmentName() {
		return deparmentName;
	}
	public void setDeparmentName(String deparmentName) {
		this.deparmentName = deparmentName;
	}
	
	
	
	
}
