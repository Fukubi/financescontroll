package com.learning.financescontroll.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.learning.financescontroll.enumerators.TipoEnum;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_entry")
@Data
@NoArgsConstructor
public class EntryEntity implements Serializable {
	
	private static final long serialVersionUID = 924519538656969502L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "tipo")
	private TipoEnum tipo;
	
	@Column(name = "data")
	private Date data;
	
	@Column(name = "valor")
	private int valor;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "categoria_id")
	private CategoryEntity categoria;
}
