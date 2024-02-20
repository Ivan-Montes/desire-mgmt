package dev.ime.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import dev.ime.tool.RegexPattern;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import jakarta.validation.constraints.Pattern;

@Entity
@Table( name = "categories" )
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Category {

	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column( name = "category_id" )
	private Long id;
	
	@Column( nullable = false, length = 50 )
	@NotNull
	@NotBlank
	@Size( min = 1, max = 50 )
	@Pattern( regexp = RegexPattern.NAME_FULL )
	private String name;
	
	@Column( length = 100 )
	@Size( min = 0, max = 100 )
	@Pattern( regexp = RegexPattern.NAME_BASIC )
	private String description;
	
	@NotNull
	@OneToMany( mappedBy = "category",  cascade = CascadeType.ALL, orphanRemoval = true )
	@ToString.Exclude
	private Set<Product>products = new HashSet<>();

	@CreationTimestamp
	@Column( name = "creation_timestamp" )
	private LocalDateTime creationTimestamp;
	
	@UpdateTimestamp
	@Column( name = "update_timestamp" )
	private LocalDateTime updateTimestamp;

	@Override
	public int hashCode() {
		return Objects.hash(creationTimestamp, description, id, name, updateTimestamp);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Category other = (Category) obj;
		return Objects.equals(creationTimestamp, other.creationTimestamp)
				&& Objects.equals(description, other.description) && Objects.equals(id, other.id)
				&& Objects.equals(name, other.name) && Objects.equals(updateTimestamp, other.updateTimestamp);
	}
	
	
}
