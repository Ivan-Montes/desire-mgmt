package dev.ime.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import dev.ime.tool.RegexPattern;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table( name = "customers" )
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Generated
public class Customer {

	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column( name = "customer_id")
	private Long id;
	
	
	@Column( name = "company_name", nullable = false, length = 100 )
	@NotNull
	@NotBlank
	@Size( min = 1, max = 100 )
	@Pattern( regexp = RegexPattern.DESCRIPTION_FULL )
	private String companyName;
	
	
	@Column( name = "contact_name", nullable = false, length = 50 )
	@NotNull
	@NotBlank
	@Size( min = 1, max = 50 )
	@Pattern( regexp = RegexPattern.NAME_FULL )
	private String contactName;
	

	@CreationTimestamp
	@Column( name = "creation_timestamp" )
	private LocalDateTime creationTimestamp;	
	
	@UpdateTimestamp
	@Column( name = "update_timestamp" )
	private LocalDateTime updateTimestamp;	

	@OneToMany( mappedBy = "customer" )	
	@ToString.Exclude
	private List<Address>addresses = new ArrayList<>();

	@Override
	public int hashCode() {
		return Objects.hash(companyName, contactName, creationTimestamp, id, updateTimestamp);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		return Objects.equals(companyName, other.companyName) && Objects.equals(contactName, other.contactName)
				&& Objects.equals(creationTimestamp, other.creationTimestamp) && Objects.equals(id, other.id)
				&& Objects.equals(updateTimestamp, other.updateTimestamp);
	}
	
	
}
