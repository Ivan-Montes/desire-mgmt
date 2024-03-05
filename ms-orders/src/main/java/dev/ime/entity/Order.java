package dev.ime.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table( name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Generated
public class Order {

	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column( name = "order_id")
	private Long id;
	
	@Column( nullable = false, name = "customer_id")
	@NotNull
	@Min(0)
	@Max(9999)
	private Long customerId;
	
	@Column( nullable = false, name = "order_date")
	@NotNull
	private LocalDate orderDate;
	
	@CreationTimestamp
	@Column( name = "creation_timestamp" )
	private LocalDateTime creationTimestamp;	
	
	@UpdateTimestamp
	@Column( name = "update_timestamp" )
	private LocalDateTime updateTimestamp;	

	@OneToMany( mappedBy = "order" )	
	@ToString.Exclude
	private Set<OrderDetail>orderDetails = new HashSet<>();

	@Override
	public int hashCode() {
		return Objects.hash(creationTimestamp, customerId, id, orderDate, updateTimestamp);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		return Objects.equals(creationTimestamp, other.creationTimestamp)
				&& Objects.equals(customerId, other.customerId) && Objects.equals(id, other.id)
				&& Objects.equals(orderDate, other.orderDate) && Objects.equals(updateTimestamp, other.updateTimestamp);
	}
	
	
}
