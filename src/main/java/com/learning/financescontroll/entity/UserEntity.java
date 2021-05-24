package com.learning.financescontroll.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_user")
@Data
@NoArgsConstructor
public class UserEntity implements Serializable {

	private static final long serialVersionUID = -1743186815042319536L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id")
	private Long id;

	@Column(name = "nome")
	private String nome;
	
	@Column(name = "role")
	private String role = "ROLE_CUSTOMER";

	@Column(name = "credenciais")
	private CredentialEntity credenciais = new CredentialEntity();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "aula_id")
	@Column(name = "entries")
	private List<EntryEntity> entries;

}
