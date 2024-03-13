package dev.ime.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import dev.ime.tool.RegexPattern;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
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
@Table( name = "address" )
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Generated
public class Address {

	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column( name = "address_id" )
	private Long id;
	
	@Column( nullable = false, length = 100 )
	@NotNull
	@NotBlank
	@Size( min = 1, max = 100 )
	@Pattern( regexp = RegexPattern.LOCATION_FULL )
	private String location;
	
	@Column( nullable = false, length = 50 )
	@NotNull
	@NotBlank
	@Size( min = 1, max = 50 )
	@Pattern( regexp = RegexPattern.NAME_FULL )
	private String city;
	
	@Column( nullable = false, length = 50 )
	@NotNull
	@NotBlank
	@Size( min = 1, max = 50 )
	@Pattern( regexp = RegexPattern.NAME_FULL )
	private String country;
	
	@Column( nullable = false, length = 100 )
	@NotNull
	@NotBlank
	@Size( min = 6, max = 100 )
	@Email
	private String email;	

	@CreationTimestamp
	@Column( name = "creation_timestamp" )
	private LocalDateTime creationTimestamp;
	
	@UpdateTimestamp
	@Column( name = "update_timestamp" )
	private LocalDateTime updateTimestamp;
	
	@ManyToOne
	@JoinColumn(name = "customer_id", nullable= false )
	@NotNull
	@ToString.Exclude
	private Customer customer;

	@Override
	public int hashCode() {
		return Objects.hash(city, country, creationTimestamp, email, id, location, updateTimestamp);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Address other = (Address) obj;
		return Objects.equals(city, other.city) && Objects.equals(country, other.country)
				&& Objects.equals(creationTimestamp, other.creationTimestamp) && Objects.equals(email, other.email)
				&& Objects.equals(id, other.id) && Objects.equals(location, other.location)
				&& Objects.equals(updateTimestamp, other.updateTimestamp);
	}

	
}
