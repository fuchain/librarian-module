package entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="tbl_employee")
public class Employee {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	
	 
	private String employeeName;
	@ManyToOne(cascade=CascadeType.ALL, targetEntity=Department.class)
    @JoinColumn(name="dep_id")
	private Department Department;
	
	
	
	
	public long getId() {
		return id;
	}	
	public void setId(long id) {
		this.id = id;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public Department getDepartement() {
		return Department;
	}
	public void setDepartement(Department departement) {
		this.Department = departement;
	}
	
	
	
	
	
}
