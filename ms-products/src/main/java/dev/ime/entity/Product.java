package dev.ime.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import dev.ime.tool.RegexPattern;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
@Table( name = "products" )
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Product {
	
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column( name = "product_id" )
	private Long id;
	
	@Column( nullable = false, unique = true, length = 50 )
	@NotNull
	@NotBlank
	@Size( min = 1, max = 50 )
	@Pattern( regexp = RegexPattern.NAME_FULL )
	private String name;
	
	@Column( nullable = false, name = "unit_price" )
	@NotNull
	@Min(0)
	@Max(99999)	
	private Double unitPrice;
	
	@Column( nullable = false, name = "unit_in_stock" )
	@NotNull
	@Min(0)
	@Max(99999)
	private Integer unitInStock;
	
	@Column( nullable = false )
	@NotNull
	private Boolean discontinued;

	@NotNull
	@ManyToOne
	@JoinColumn( name = "category_id", nullable= false )
	@ToString.Exclude
	private Category category;	

	@CreationTimestamp
	@Column( name = "creation_timestamp" )
	private LocalDateTime creationTimestamp;
	
	@UpdateTimestamp
	@Column( name = "update_timestamp" )
	private LocalDateTime updateTimestamp;

	@Override
	public int hashCode() {
		return Objects.hash(creationTimestamp, discontinued, id, name, unitInStock, unitPrice, updateTimestamp);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		return Objects.equals(creationTimestamp, other.creationTimestamp)
				&& Objects.equals(discontinued, other.discontinued) && Objects.equals(id, other.id)
				&& Objects.equals(name, other.name) && Objects.equals(unitInStock, other.unitInStock)
				&& Objects.equals(unitPrice, other.unitPrice) && Objects.equals(updateTimestamp, other.updateTimestamp);
	}
	
}
