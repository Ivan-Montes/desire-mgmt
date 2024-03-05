package dev.ime.entity;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table( name = "order_details")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Generated
public class OrderDetail {

	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column( name = "order_details_id")
	private Long id;
	
	@Column( nullable = false)
	@NotNull
	@Min(1)
	@Max(9999)
	private Integer quantity;
	
	@Column( nullable = false)
	@NotNull
	@Min(0)
	@Max(100)
	private Double discount;
	
	@Column( name = "product_id", nullable = false )
	@NotNull
	@Min(1)
	@Max(9999)
	private Long productId;

	@CreationTimestamp
	@Column( name = "creation_timestamp" )
	private LocalDateTime creationTimestamp;
	
	@UpdateTimestamp
	@Column( name = "update_timestamp" )
	private LocalDateTime updateTimestamp;

	@ManyToOne
	@JoinColumn( name = "order_id", nullable = false )
	@NotNull
	@ToString.Exclude
	private Order order;


	@Override
	public int hashCode() {
		return Objects.hash(creationTimestamp, discount, id, productId, quantity, updateTimestamp);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderDetail other = (OrderDetail) obj;
		return Objects.equals(creationTimestamp, other.creationTimestamp) && Objects.equals(discount, other.discount)
				&& Objects.equals(id, other.id) && Objects.equals(productId, other.productId)
				&& Objects.equals(quantity, other.quantity) && Objects.equals(updateTimestamp, other.updateTimestamp);
	}
	
}
